package com.senai.PI_mecado_preso.sales.api.dtos;

public record CheckoutResponseDTO(
        PedidoCriadoResponseDTO pedido,
        boolean processadoSincronamente,
        String statusCobranca,
        String mensagem,
        String pixCopiaECola,
        String linhaDigitavel
) {}
