package com.senai.PI_mecado_preso.iam.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 255, message = "O nome não pode exceder 255 caracteres")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 255, message = "O e-mail não pode exceder 255 caracteres")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$", message = "Telefone inválido. Use um formato válido com DDD (ex: (11) 99999-9999)")
        String telefone,

        @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
        String senha,

        @NotBlank(message = "O CPF é obrigatório")
        @Pattern(regexp = "(^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$)|(^\\d{11}$)", message = "CPF inválido. Use o formato 000.000.000-00 ou apenas números")
        String cpf
) {}