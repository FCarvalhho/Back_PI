package com.senai.PI_mecado_preso.catalog.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record ProdutoRequestDTO(
        @NotBlank(message = "O nome do produto é obrigatório")
        @Size(max = 255, message = "O nome do produto não pode ultrapassar 255 caracteres")
        String nome,

        @Size(max = 2000, message = "A descrição do produto não pode ultrapassar 2000 caracteres")
        String description,

        List<UUID> atributosIds
) {
}
