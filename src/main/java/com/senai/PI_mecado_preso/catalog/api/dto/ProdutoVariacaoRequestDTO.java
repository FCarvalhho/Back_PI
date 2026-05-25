package com.senai.PI_mecado_preso.catalog.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProdutoVariacaoRequestDTO(
    String sku,
    BigDecimal preco,
    Integer estoque,
    List<VariacaoOpcaoRequestDTO> opcoes,
    List<ImagemVariacaoRequestDTO> imagens
) {
}
