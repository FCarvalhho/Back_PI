/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.api.dtos;

import jakarta.validation.constraints.*;

import java.util.Set;

/**
 *
 * @author Cansei2
 */
public record FuncionarioRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 255, message = "O nome não pode exceder 255 caracteres")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 255, message = "O e-mail não pode exceder 255 caracteres")
        String email,

        @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
        String senha,

        @NotBlank(message = "A matrícula é obrigatória")
        @Size(max = 50, message = "A matrícula não pode exceder 50 caracteres")
        String matricula,

        @NotEmpty(message = "É necessário informar pelo menos uma regra de acesso")
        Set<@Pattern(regexp = "ROLE_(CLIENTE|ADMIN|ESTOQUE|ENTREGA|FATURAMENTO)",
                message = "Perfil de acesso inválido. Escolha entre: ROLE_ADMIN, ROLE_ESTOQUE, ROLE_ENTREGA ou ROLE_FATURAMENTO") String> roles
) {}