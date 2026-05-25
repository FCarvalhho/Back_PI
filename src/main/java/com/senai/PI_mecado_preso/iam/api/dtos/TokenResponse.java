/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.api.dtos;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author Cansei2
 */

public record TokenResponse(
        String token,
        UUID id,
        String nome,
        List<String> roles
) {}
