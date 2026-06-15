package com.senai.PI_mecado_preso.iam.api.dtos;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record FuncionarioResponseDTO(
        UUID id,
        String nome,
        String email,
        Boolean ativo,
        LocalDateTime criadoEm,
        Set<String> roles,
        String matricula
) {}
