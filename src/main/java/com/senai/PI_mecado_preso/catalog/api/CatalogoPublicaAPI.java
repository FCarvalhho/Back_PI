package com.senai.PI_mecado_preso.catalog.api;

import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;

import java.math.BigDecimal;
import java.util.UUID;

public interface CatalogoPublicaAPI {

    /**
     * Verifica se existe saldo suficiente em estoque para uma variação específica.
     */
    ResultadoPadrao<Boolean> verificarEstoque(UUID variacaoId, Integer quantidade);

    /**
     * Deduz a quantidade do estoque de forma atômica.
     */
    ResultadoPadrao<?> baixarEstoque(UUID variacaoId, Integer quantidade);

}
