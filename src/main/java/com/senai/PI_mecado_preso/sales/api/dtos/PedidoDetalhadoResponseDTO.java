package com.senai.PI_mecado_preso.sales.api.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoDetalhadoResponseDTO(
        UUID id,
        PedidoClienteExibicaoDTO cliente, // Objeto enriquecido
        String status,
        BigDecimal valorTotal,
        LocalDateTime criadoEm,
        List<ItemPedidoDetalhadoResponseDTO> itens
) {}
