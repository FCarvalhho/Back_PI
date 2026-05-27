/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.api.dtos;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Cansei2
 */
public record FuncionarioResponseDTO(
        UUID id,
        String nome,
        String email,
        Boolean ativo,
        LocalDateTime criadoEm,
        Set<String> roles,
        String matricula
) {}
