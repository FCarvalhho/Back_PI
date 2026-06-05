package com.senai.PI_mecado_preso.sales.api.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ItemCarrinhoRequestDTO(
        @NotNull(message = "O ID da variação do produto é obrigatório")
        UUID variacaoId,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade mínima para adicionar ao carrinho é 1")
        Integer quantidade
) {}
