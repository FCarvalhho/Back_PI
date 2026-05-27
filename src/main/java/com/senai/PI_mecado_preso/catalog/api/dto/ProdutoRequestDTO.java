package com.senai.PI_mecado_preso.catalog.api.dto;

import java.util.List;
import java.util.UUID;

public record ProdutoRequestDTO(
        String nome,
        String descricao,
        List<UUID> atributosIds
) {
}
