package com.senai.PI_mecado_preso.catalog.api.dto;

import com.senai.PI_mecado_preso.catalog.internal.entity.Produto;
import com.senai.PI_mecado_preso.catalog.internal.entity.VariacaoOpcao;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProdutoVariacaoResponseDTO(
        UUID id,
        String sku,
        BigDecimal preco,
        Integer estoque,
        List<VariacaoOpcaoResponseDTO> opcoes,
        List<ImagemVariacaoResponseDTO> imagens

) {
}
