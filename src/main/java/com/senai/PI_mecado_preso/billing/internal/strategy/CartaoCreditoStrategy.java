package com.senai.PI_mecado_preso.billing.internal.strategy;

import com.senai.PI_mecado_preso.billing.internal.entity.MetodoPagamento;
import com.senai.PI_mecado_preso.billing.internal.entity.Pagamento;
import com.senai.PI_mecado_preso.billing.internal.repository.PagamentoRepository;
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import org.springframework.stereotype.Component;

@Component
class CartaoCreditoStrategy implements EstrategiaPagamento {

    private final SimuladorGatewayCartao simuladorGateway;
    private final PagamentoRepository pagamentoRepository;

    public CartaoCreditoStrategy(SimuladorGatewayCartao simuladorGateway, PagamentoRepository pagamentoRepository) {
        this.simuladorGateway = simuladorGateway;
        this.pagamentoRepository = pagamentoRepository;
    }

    @Override
    public ResultadoPadrao<String> processar(Pagamento pagamento) {
        boolean aprovado = simuladorGateway.autorizarTransacao(pagamento.getValor(), pagamento.getParcelas());

        if (!aprovado) {
            pagamento.falhar();
            pagamentoRepository.save(pagamento);
            return ResultadoPadrao.failure("Cartão de crédito recusado por insuficiência de saldo/limite.");
        }

        pagamento.confirmar();
        pagamentoRepository.save(pagamento);
        return ResultadoPadrao.success("APROVADO");
    }

    @Override
    public MetodoPagamento getMetodo() {
        return MetodoPagamento.CREDITO_CARD;
    }
}
