/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 *
 * @author Cansei2
 */
public record FuncionarioRequestDTO(
        
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,
        
        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha,
        
        @NotBlank(message = "A matricula é obrigatório")
        String matricula,

        @NotEmpty(message = "É necessário informar pelo menos uma regra de acesso")
        Set<String>roles
) {}