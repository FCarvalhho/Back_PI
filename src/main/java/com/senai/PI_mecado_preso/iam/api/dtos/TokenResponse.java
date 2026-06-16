package com.senai.PI_mecado_preso.iam.api.dtos;

import java.util.List;
import java.util.UUID;

public record TokenResponse(
        String token,
        UUID id,
        String nome,
        List<String> roles
) {}
