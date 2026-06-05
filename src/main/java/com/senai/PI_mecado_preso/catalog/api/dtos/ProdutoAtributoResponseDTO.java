package com.senai.PI_mecado_preso.catalog.api.dtos;

import java.util.UUID;

public record ProdutoAtributoResponseDTO(
        UUID id,
        UUID atributoId,
        String atributoNome
) {
}
