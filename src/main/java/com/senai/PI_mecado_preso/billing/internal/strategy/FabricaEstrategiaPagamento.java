package com.senai.PI_mecado_preso.billing.internal.strategy;

import com.senai.PI_mecado_preso.billing.internal.entity.MetodoPagamento;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FabricaEstrategiaPagamento {

    private final Map<MetodoPagamento, EstrategiaPagamento> estrategias;

    public FabricaEstrategiaPagamento(List<EstrategiaPagamento> listaEstrategias) {
        this.estrategias = listaEstrategias.stream()
                .collect(Collectors.toMap(EstrategiaPagamento::getMetodo, Function.identity()));
    }

    public Optional<EstrategiaPagamento> obterEstrategia(MetodoPagamento metodo) {
        return Optional.ofNullable(estrategias.get(metodo));
    }
}
