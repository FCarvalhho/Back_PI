package com.senai.PI_mecado_preso.catalog.api;

import java.math.BigDecimal;
import java.util.UUID;

public record DetalheItemCatalogoDTO(
        UUID variacaoId,
        String nomeProduto,
        BigDecimal preco,
        Integer estoque
) {}
