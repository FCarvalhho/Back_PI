package com.senai.PI_mecado_preso.billing.api;

import java.math.BigDecimal;
import java.util.UUID;

public record CobrancaRequestDTO(
        UUID pedidoId,
        BigDecimal valor,
        String metodoPagamento,
        Integer parcelas
) {}