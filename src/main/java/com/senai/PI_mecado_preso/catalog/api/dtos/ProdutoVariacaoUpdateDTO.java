package com.senai.PI_mecado_preso.catalog.api.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public record ProdutoVariacaoUpdateDTO(
        @Size(max = 100, message = "O SKU não pode ultrapassar 100 caracteres")
        String sku,

        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        BigDecimal preco,

        @Min(value = 0, message = "O estoque não pode ser negativo")
        Integer estoque,

        List<VariacaoOpcaoRequestDTO> opcoes,
        List<ImagemVariacaoRequestDTO> imagens
) {
}
