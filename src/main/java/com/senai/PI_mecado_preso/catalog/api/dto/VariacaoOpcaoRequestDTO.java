package com.senai.PI_mecado_preso.catalog.api.dto;

import java.util.UUID;

public record VariacaoOpcaoRequestDTO(
        UUID atributoId,
        String valor
) {
}
