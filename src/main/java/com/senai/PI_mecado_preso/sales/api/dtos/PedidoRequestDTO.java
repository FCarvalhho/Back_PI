package com.senai.PI_mecado_preso.sales.api.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record PedidoRequestDTO(
        @NotNull(message = "O clienteId é obrigatório")
        UUID clienteId,

        @NotBlank(message = "O método de pagamento é obrigatório")
        String metodoPagamento,

        @Min(value = 1, message = "O número mínimo de parcelas é 1")
        Integer parcelas,

        @NotEmpty(message = "O pedido deve conter pelo menos um item")
        @Valid
        List<ItemPedidoRequestDTO> itens
) {}
