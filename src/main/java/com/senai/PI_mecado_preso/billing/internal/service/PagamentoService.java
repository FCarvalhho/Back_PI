/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.billing.internal.service;

import com.senai.PI_mecado_preso.billing.api.PagamentoProcessadoEvent;
import com.senai.PI_mecado_preso.billing.api.PagamentoPublicaAPI;
import com.senai.PI_mecado_preso.billing.api.CobrancaRequestDTO;
import com.senai.PI_mecado_preso.billing.api.CobrancaResponseDTO;
import com.senai.PI_mecado_preso.billing.internal.entity.MetodoPagamento;
import com.senai.PI_mecado_preso.billing.internal.entity.Pagamento;
import com.senai.PI_mecado_preso.billing.internal.repository.PagamentoRepository;
import com.senai.PI_mecado_preso.billing.internal.strategy.EstrategiaPagamento;
import com.senai.PI_mecado_preso.billing.internal.strategy.FabricaEstrategiaPagamento;
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import com.senai.PI_mecado_preso.shared.exception.RecursoNaoEncontradoException;
import com.senai.PI_mecado_preso.shared.exception.RegraDeNegocioException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
class PagamentoService implements PagamentoPublicaAPI {

    private final PagamentoRepository pagamentoRepository;
    private final FabricaEstrategiaPagamento fabricaEstrategia;
    private final ApplicationEventPublisher eventPublisher;
    private final TransactionTemplate transactionTemplate;

    public PagamentoService(PagamentoRepository pagamentoRepository, FabricaEstrategiaPagamento fabricaEstrategia, ApplicationEventPublisher eventPublisher, TransactionTemplate transactionTemplate) {
        this.pagamentoRepository = pagamentoRepository;
        this.fabricaEstrategia = fabricaEstrategia;
        this.eventPublisher = eventPublisher;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CompletableFuture<ResultadoPadrao<CobrancaResponseDTO>> processarCobranca(CobrancaRequestDTO request) {

        MetodoPagamento metodo;
        try {
            metodo = MetodoPagamento.valueOf(request.metodoPagamento().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
           throw new RegraDeNegocioException("Método de pagamento '" + request.metodoPagamento() + "' não é suportado pelo sistema.");
        }

        Pagamento pagamentoInicial = new Pagamento(request.pedidoId(), request.valor(), metodo, request.parcelas());
        final Pagamento pagamento = pagamentoRepository.save(pagamentoInicial);

        EstrategiaPagamento estrategia = fabricaEstrategia.obterEstrategia(metodo)
                .orElseThrow(() -> new RegraDeNegocioException("Estratégia de gateway de pagamento não configurada para o método: " + metodo));

        // 🌟 FLUXO ASSÍNCRONO: PIX E BOLETO
        if (metodo == MetodoPagamento.PIX || metodo == MetodoPagamento.BOLETO) {
            return CompletableFuture.supplyAsync(() -> {
                ResultadoPadrao<String> dadosGerados = estrategia.processar(pagamento);

                String pixCodigo = (metodo == MetodoPagamento.PIX) ? dadosGerados.dado() : null;
                String boletoLinha = (metodo == MetodoPagamento.BOLETO) ? dadosGerados.dado() : null;

                // Simulação de Webhook/Liquidação assíncrona tardia
                CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(15000); // Cliente leva um tempo a pagar

                        // 🌟 Executa a liquidação dentro de um bloco transacional limpo na thread background
                        transactionTemplate.executeWithoutResult(status -> {
                            Pagamento p = pagamentoRepository.findById(pagamento.getId()).orElseThrow();
                            p.confirmar();
                            pagamentoRepository.save(p);

                            // Pix e Boleto sempre notificam via evento pois são 100% assíncronos
                            eventPublisher.publishEvent(new PagamentoProcessadoEvent(request.pedidoId(), "PAGO"));
                        });
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
        // Guardamos o estado do timeout de forma segura entre as threads
        final AtomicBoolean timeoutOcorreu = new AtomicBoolean(false);

        return CompletableFuture.supplyAsync(() -> {
                    ResultadoPadrao<String> resultadoGateway = estrategia.processar(pagamento);

                    // 🌟 Encapsulamos a lógica numa transação programática para o Spring Modulith mapear corretamente
                    return transactionTemplate.execute(status -> {
                        Pagamento pagamentoFinal = pagamentoRepository.findById(pagamento.getId())
                                .orElseThrow(() -> new RecursoNaoEncontradoException("Transação de pagamento não encontrada com o ID: " + pagamento.getId()));

                        String statusSugerido;
                        if (resultadoGateway.isValid() && "APROVADO".equals(resultadoGateway.dado())) {
                            pagamentoFinal.confirmar();
                            statusSugerido = "PAGO";
                        } else {
                            pagamentoFinal.falhar();
                            statusSugerido = "FALHADO";
                        }

                        pagamentoRepository.save(pagamentoFinal);

                        // 🔥 REQUISITO ESPECÍFICO: Só publica o evento se a thread principal já tiver estourado o timeout!
                        if (timeoutOcorreu.get()) {
                            eventPublisher.publishEvent(new PagamentoProcessadoEvent(request.pedidoId(), statusSugerido));
                        }

                        return ResultadoPadrao.success(new CobrancaResponseDTO(
                                true,
                                statusSugerido,
                                statusSugerido.equals("PAGO") ? "Cartão autorizado." : resultadoGateway.failureReason(),
                                null,
                                null
                        ));
                    });
                })
                .orTimeout(3, TimeUnit.SECONDS)
                .exceptionally(erro -> {
                    if (erro instanceof TimeoutException || erro.getCause() instanceof TimeoutException) {
                        // 🌟 O timeout aconteceu na thread HTTP principal! Ativamos a flag.
                        timeoutOcorreu.set(true);
                        return ResultadoPadrao.success(new CobrancaResponseDTO(
                                false,
                                "PENDENTE",
                                "Processamento em background ativado devido a lentidão temporária do adquirente.",
                                null,
                                null
                        ));
                    }
                    throw new RegraDeNegocioException("Erro de barramento ao tentar processar cartão de forma não-bloqueante.");
                });
    }
}