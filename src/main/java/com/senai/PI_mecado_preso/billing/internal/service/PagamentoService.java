/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.billing.internal.service;

import com.senai.PI_mecado_preso.billing.api.PagamentoPublicaAPI;
import com.senai.PI_mecado_preso.billing.api.CobrancaRequestDTO;
import com.senai.PI_mecado_preso.billing.api.CobrancaResponseDTO;
import com.senai.PI_mecado_preso.billing.internal.entity.MetodoPagamento;
import com.senai.PI_mecado_preso.billing.internal.entity.Pagamento;
import com.senai.PI_mecado_preso.billing.internal.repository.PagamentoRepository;
import com.senai.PI_mecado_preso.billing.internal.strategy.EstrategiaPagamento;
import com.senai.PI_mecado_preso.billing.internal.strategy.FabricaEstrategiaPagamento;
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
class PagamentoService implements PagamentoPublicaAPI {

    private final PagamentoRepository pagamentoRepository;
    private final FabricaEstrategiaPagamento fabricaEstrategia;

    public PagamentoService(PagamentoRepository pagamentoRepository, FabricaEstrategiaPagamento fabricaEstrategia) {
        this.pagamentoRepository = pagamentoRepository;
        this.fabricaEstrategia = fabricaEstrategia;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CompletableFuture<ResultadoPadrao<CobrancaResponseDTO>> processarCobranca(CobrancaRequestDTO request) {

        MetodoPagamento metodo;
        try {
            metodo = MetodoPagamento.valueOf(request.metodoPagamento().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return CompletableFuture.completedFuture(ResultadoPadrao.failure("Método de pagamento não suportado."));
        }

        Pagamento pagamentoInicial = new Pagamento(request.pedidoId(), request.valor(), metodo, request.parcelas());
        final Pagamento pagamento = pagamentoRepository.save(pagamentoInicial);

        EstrategiaPagamento estrategia = fabricaEstrategia.obterEstrategia(metodo)
                .orElseThrow(() -> new IllegalStateException("Estratégia não cadastrada."));

        // 🌟 FLUXO ASSÍNCRONO: PIX E BOLETO
        if (metodo == MetodoPagamento.PIX || metodo == MetodoPagamento.BOLETO) {
            return CompletableFuture.supplyAsync(() -> {
                // Executa a geração síncrona dos textos (BR Code / Linha digitável)
                ResultadoPadrao<String> dadosGerados = estrategia.processar(pagamento);

                String pixCodigo = (metodo == MetodoPagamento.PIX) ? dadosGerados.dado() : null;
                String boletoLinha = (metodo == MetodoPagamento.BOLETO) ? dadosGerados.dado() : null;

                // Simula o tempo assíncrono em background (Webhook/espera pelo pagamento do usuário)
                CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });

                CobrancaResponseDTO response = new CobrancaResponseDTO(
                        false,
                        "PENDENTE",
                        "Documento de pagamento gerado com sucesso. Use o código para concluir a transação.",
                        pixCodigo,
                        boletoLinha
                );
                return ResultadoPadrao.success(response);
            });
        }

        // 🌟 FLUXO SÍNCRONO NÃO-BLOQUEANTE: CARTÕES (CRÉDITO/DÉBITO)
        // O supplyAsync empurra a execução pesada para o ForkJoinPool, liberando a thread do Tomcat imediatamente
        return CompletableFuture.supplyAsync(() -> {
                    ResultadoPadrao<String> resultadoGateway = estrategia.processar(pagamento);

                    // Abre uma nova transação local na thread de background para persistir o veredito rápido do banco
                    Pagamento pagamentoFinal = pagamentoRepository.findById(pagamento.getId())
                            .orElseThrow(() -> new IllegalStateException("Pagamento não encontrado: " + pagamento.getId()));

                    if (resultadoGateway.isValid() && "APROVADO".equals(resultadoGateway.dado())) {
                        pagamentoFinal.confirmar();
                        pagamentoRepository.save(pagamentoFinal);
                        return ResultadoPadrao.success(new CobrancaResponseDTO(true, "PAGO", "Cartão autorizado.", null, null));
                    } else {
                        pagamentoFinal.falhar();
                        pagamentoRepository.save(pagamentoFinal);
                        return ResultadoPadrao.success(new CobrancaResponseDTO(true, "FALHADO", resultadoGateway.failureReason(), null, null));
                    }
                })
                // Estabelece a janela de timeout de 3 segundos de forma reativa através do agendador global do Java
                .orTimeout(3, TimeUnit.SECONDS)
                // Intercepta e mitiga o estouro do tempo de rede sem afetar o servidor HTTP
                .exceptionally(erro -> {
                    // No pipeline assíncrono direto do CompletableFuture, o erro é jogado ou encapsulado como CompletionException/TimeoutException
                    if (erro instanceof TimeoutException || erro.getCause() instanceof TimeoutException) {
                        CobrancaResponseDTO fallbackResponse = new CobrancaResponseDTO(
                                false, // vira assíncrono como contingência
                                "PENDENTE",
                                "Processamento em background ativado devido a lentidão temporária do adquirente.",
                                null,
                                null
                        );
                        return ResultadoPadrao.success(fallbackResponse);
                    }

                    // Fallback para falhas físicas críticas de conexão ou falta de banco
                    return ResultadoPadrao.failure("Erro de barramento ao tentar processar cartão de forma não-bloqueante.");
                });
    }
}