package com.senai.PI_mecado_preso.sales.api.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemPedidoRequestDTO(
        @NotNull(message = "O variacaoId é obrigatório")
        UUID variacaoId,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade mínima deve ser 1")
        Integer quantidade,

        @NotNull(message = "O variacaoId é obrigatório")
        BigDecimal precoUnitario
) {}
