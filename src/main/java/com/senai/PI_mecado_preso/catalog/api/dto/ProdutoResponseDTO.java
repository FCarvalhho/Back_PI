package com.senai.PI_mecado_preso.catalog.api.dto;

import com.senai.PI_mecado_preso.catalog.internal.entity.ProdutoAtributo;
import com.senai.PI_mecado_preso.catalog.internal.entity.ProdutoVariacao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ProdutoResponseDTO(
        UUID id,
        String nome,
        String descricao,
        boolean ativo,
        LocalDateTime criadoEm,
        List<ProdutoVariacaoIDsDTO> variacoes,
        List<ProdutoAtributoResponseDTO> atributos
) {
}
