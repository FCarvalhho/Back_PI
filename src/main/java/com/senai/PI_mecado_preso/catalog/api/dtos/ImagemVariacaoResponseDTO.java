package com.senai.PI_mecado_preso.catalog.api.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record ImagemVariacaoResponseDTO(
        UUID id,
        String urlImagem,
        Integer ordem,
        LocalDateTime criadoEm
) {
}
