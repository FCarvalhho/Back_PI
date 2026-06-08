package com.senai.PI_mecado_preso.billing.api;

public record CobrancaResponseDTO(
        boolean processadoSincronamente,
        String statusSugerido,
        String mensagem,
        String pixCopiaECola,
        String linhaDigitavel
) {}
