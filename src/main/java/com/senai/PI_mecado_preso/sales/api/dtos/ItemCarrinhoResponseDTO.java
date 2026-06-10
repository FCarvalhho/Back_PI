package com.senai.PI_mecado_preso.sales.api.dtos;

import java.util.UUID;

public record ItemCarrinhoResponseDTO(
        UUID id,
//        ProdutoVariacaoResponseSalesDTO produto,
        int quantidade
) {
}
