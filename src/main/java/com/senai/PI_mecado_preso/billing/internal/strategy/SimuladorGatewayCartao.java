package com.senai.PI_mecado_preso.billing.internal.strategy;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class SimuladorGatewayCartao {

    /**
     * Simula a autorização de uma transação de cartão com instabilidade controlada.
     */
    public boolean autorizarTransacao(BigDecimal valor, Integer parcelas) {
        try {
            // Sorteia um tempo de resposta de 1 a 4 segundos de forma aleatória.
            // Se cair em 4 segundos, simulará perfeitamente o estouro do seu Timeout de 3s do Tomcat!
            long tempoDeEspera = ThreadLocalRandom.current().nextLong(1000, 4500);
            Thread.sleep(tempoDeEspera);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Regra acadêmica mantida: Valores acima de R$ 5.000,00 simulam falta de limite
        if (valor.compareTo(new BigDecimal("5000.00")) > 0) {
            return false;
        }

        return true;
    }
}

