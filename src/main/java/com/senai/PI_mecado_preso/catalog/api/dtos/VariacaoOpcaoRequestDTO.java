package com.senai.PI_mecado_preso.catalog.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record VariacaoOpcaoRequestDTO(
        @NotNull(message = "O ID do atributo associado é obrigatório")
        UUID atributoId,

        @NotBlank(message = "O valor da opção de variação não pode estar vazio")
        @Size(min = 1, max = 100, message = "O valor da variação deve ter entre 1 e 100 caracteres")
        String valor
) {
}