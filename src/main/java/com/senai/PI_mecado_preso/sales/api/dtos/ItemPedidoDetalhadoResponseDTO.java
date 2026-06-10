package com.senai.PI_mecado_preso.sales.api.dtos;

import com.senai.PI_mecado_preso.catalog.api.PedidoVariacaoExibicaoDTO;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemPedidoDetalhadoResponseDTO(
        UUID id,
        PedidoVariacaoExibicaoDTO variacao,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {}
