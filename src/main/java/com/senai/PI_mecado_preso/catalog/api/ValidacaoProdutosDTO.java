package com.senai.PI_mecado_preso.catalog.api;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record ValidacaoProdutosDTO(
        BigDecimal valorTotal,
        Map<UUID, ItemValidadoDTO> itens
) {
}