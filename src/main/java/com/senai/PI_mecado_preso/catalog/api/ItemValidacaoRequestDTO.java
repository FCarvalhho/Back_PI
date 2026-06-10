package com.senai.PI_mecado_preso.catalog.api;

import java.math.BigDecimal;

public record ItemValidacaoRequestDTO(
        Integer quantidade,
        BigDecimal precoVisualizado
) {
}
