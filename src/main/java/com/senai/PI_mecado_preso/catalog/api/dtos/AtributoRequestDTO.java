package com.senai.PI_mecado_preso.catalog.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AtributoRequestDTO(
        @NotBlank(message = "O nome do atributo é obrigatório")
        @Size(min = 2, max = 100, message = "O nome do atributo deve ter entre 2 e 100 caracteres")
        String nome
) {
}
