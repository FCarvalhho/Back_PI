package com.senai.PI_mecado_preso.catalog.api;

import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;

import java.math.BigDecimal;
import java.util.UUID;

public interface CatalogoPublicaAPI {

    ResultadoPadrao<Boolean> verificarEstoque(UUID variacaoId, Integer quantidade);

    ResultadoPadrao<?> baixarEstoque(UUID variacaoId, Integer quantidade);

    ResultadoPadrao<BigDecimal> obterPreco(UUID variacaoId);
}
