package com.senai.PI_mecado_preso.billing.internal.strategy;

import com.senai.PI_mecado_preso.billing.internal.entity.MetodoPagamento;
import com.senai.PI_mecado_preso.billing.internal.entity.Pagamento;
import com.senai.PI_mecado_preso.billing.internal.repository.PagamentoRepository;
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
class PixStrategy implements EstrategiaPagamento {

    private final PagamentoRepository pagamentoRepository;

    public PixStrategy(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @Override
    public ResultadoPadrao<String> processar(Pagamento pagamento) {

        String chaveFicticiaPix = "00020101021126580014br.gov.bcb.pix0136" +
                UUID.randomUUID().toString().replace("-", "") +
                "5204000053039865405" + pagamento.getValor().toString() + "5802BR5913Mercado-Preso";


        return ResultadoPadrao.success(chaveFicticiaPix);
    }

    @Override
    public MetodoPagamento getMetodo() {
        return MetodoPagamento.PIX;
    }
}