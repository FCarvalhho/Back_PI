package com.senai.PI_mecado_preso.catalog.api.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record ProdutoVariacaoRequestDTO(
        @NotBlank(message = "O SKU da variação é obrigatório")
        @Size(max = 100, message = "O SKU não pode ultrapassar 100 caracteres")
        String sku,

        @NotNull(message = "O preço da variação é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        BigDecimal preco,

        @NotNull(message = "O estoque da variação é obrigatório")
        @Min(value = 0, message = "O estoque não pode ser negativo")
        Integer estoque,

        List<@NotNull VariacaoOpcaoRequestDTO> opcoes,
        List<@NotNull ImagemVariacaoRequestDTO> imagens
) {
}
