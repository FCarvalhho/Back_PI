package com.senai.PI_mecado_preso.sales.api.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemPedidoCriadoResponseDTO(
        UUID id,
        UUID variacaoId,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {}
