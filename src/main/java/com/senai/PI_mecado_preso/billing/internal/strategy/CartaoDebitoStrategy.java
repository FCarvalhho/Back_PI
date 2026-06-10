package com.senai.PI_mecado_preso.billing.internal.strategy;

import com.senai.PI_mecado_preso.billing.internal.entity.MetodoPagamento;
import com.senai.PI_mecado_preso.billing.internal.entity.Pagamento;
import com.senai.PI_mecado_preso.billing.internal.repository.PagamentoRepository;
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
class CartaoDebitoStrategy implements EstrategiaPagamento {

    private final SimuladorGatewayCartao simuladorGateway;
    private final PagamentoRepository pagamentoRepository;

    public CartaoDebitoStrategy(SimuladorGatewayCartao simuladorGateway, PagamentoRepository pagamentoRepository) {
        this.simuladorGateway = simuladorGateway;
        this.pagamentoRepository = pagamentoRepository;
    }

    @Override
    public ResultadoPadrao<String> processar(Pagamento pagamento) {
        boolean aprovado = simuladorGateway.autorizarTransacao(pagamento.getValor(), pagamento.getParcelas());

        if (!aprovado) {
            pagamento.falhar();
            pagamentoRepository.save(pagamento);
            return ResultadoPadrao.failure("Transação de débito rejeitada pelo banco emissor.");
        }

        pagamento.confirmar();
        pagamentoRepository.save(pagamento);
        return ResultadoPadrao.success("APROVADO");
    }

    @Override
    public MetodoPagamento getMetodo() {
        return MetodoPagamento.DEBITO_CARD;
    }
}

