package com.senai.PI_mecado_preso.sales.api.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record PedidoCriadoEvent(
        UUID pedidoId,
        UUID clienteId,
        BigDecimal valorTotal
) {}
