package com.senai.PI_mecado_preso.catalog.api.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ProdutoResponseDTO(
        UUID id,
        String nome,
        String descricao,
        boolean ativo,
        LocalDateTime criadoEm,
        List<ProdutoVariacaoResponseDTO> variacoes,
        List<ProdutoAtributoResponseDTO> atributos
) {
}
