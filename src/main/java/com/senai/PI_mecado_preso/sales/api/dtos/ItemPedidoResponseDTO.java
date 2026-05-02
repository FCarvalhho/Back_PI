/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.senai.PI_mecado_preso.sales.api.dtos;

import java.math.BigDecimal;
import java.util.UUID;

/**
 *
 * @author Cansei2
 */
public record ItemPedidoResponseDTO(
        UUID id,
        UUID variacaoId,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
        ) {

}
