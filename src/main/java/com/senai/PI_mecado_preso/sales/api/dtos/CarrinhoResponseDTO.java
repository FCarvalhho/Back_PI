package com.senai.PI_mecado_preso.sales.api.dtos;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record CarrinhoResponseDTO(
        UUID id,
        LocalDateTime atualizadoEm,
        Set<ItemCarrinhoResponseDTO> itens
) {
}
