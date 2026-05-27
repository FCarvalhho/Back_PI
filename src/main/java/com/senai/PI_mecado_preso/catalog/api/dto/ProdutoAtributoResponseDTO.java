package com.senai.PI_mecado_preso.catalog.api.dto;

import java.util.UUID;

public record ProdutoAtributoResponseDTO(
        UUID id,
        UUID atributoId,
        String atributoNome
) {
}
