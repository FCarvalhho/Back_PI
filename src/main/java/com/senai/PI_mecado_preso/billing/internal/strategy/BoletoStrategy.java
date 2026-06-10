package com.senai.PI_mecado_preso.billing.internal.strategy;

import com.senai.PI_mecado_preso.billing.internal.entity.MetodoPagamento;
import com.senai.PI_mecado_preso.billing.internal.entity.Pagamento;

import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import org.springframework.stereotype.Component;
import java.util.concurrent.ThreadLocalRandom;

@Component
class BoletoStrategy implements EstrategiaPagamento {

    @Override
    public ResultadoPadrao<String> processar(Pagamento pagamento) {

        int banco = ThreadLocalRandom.current().nextInt(100, 999);
        String linhaDigitavelFicticia = String.format("%03d91.79001 01043.513184 91020.150008 7 %d0000%s",
                banco,
                ThreadLocalRandom.current().nextInt(1000, 9999),
                pagamento.getValor().toString().replace(".", ""));

        return ResultadoPadrao.success(linhaDigitavelFicticia);
    }

    @Override
    public MetodoPagamento getMetodo() {
        return MetodoPagamento.BOLETO;
    }
}
