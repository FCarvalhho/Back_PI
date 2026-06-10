package com.senai.PI_mecado_preso.sales.api.dtos;

import com.senai.PI_mecado_preso.iam.api.PedidoUsuarioDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoDetalhadoResponseDTO(
        UUID id,
        PedidoUsuarioDTO cliente,
        String status,
        BigDecimal valorTotal,
        LocalDateTime criadoEm,
        List<ItemPedidoDetalhadoResponseDTO> itens
) {}
