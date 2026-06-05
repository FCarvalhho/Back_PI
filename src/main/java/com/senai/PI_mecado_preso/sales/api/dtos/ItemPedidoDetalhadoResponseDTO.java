package com.senai.PI_mecado_preso.sales.api.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemPedidoDetalhadoResponseDTO(
        UUID id,
        PedidoVariacaoExibicaoDTO variacao, // Objeto enriquecido
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {}
