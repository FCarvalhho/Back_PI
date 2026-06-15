package com.senai.PI_mecado_preso.billing.internal.strategy;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class SimuladorGatewayCartao {

    public boolean autorizarTransacao(BigDecimal valor, Integer parcelas) {
        try {
            long tempoDeEspera = ThreadLocalRandom.current().nextLong(1000, 4500);
            Thread.sleep(tempoDeEspera);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (valor.compareTo(new BigDecimal("5000.00")) > 0) {
            return false;
        }

        return true;
    }
}

