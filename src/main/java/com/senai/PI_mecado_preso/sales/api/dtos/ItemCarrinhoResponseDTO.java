package com.senai.PI_mecado_preso.sales.api.dtos;

import com.senai.PI_mecado_preso.catalog.api.DetalheItemCatalogoDTO;
import java.util.UUID;

public record ItemCarrinhoResponseDTO(
        UUID id,
        DetalheItemCatalogoDTO produto,
        int quantidade
) {
}
