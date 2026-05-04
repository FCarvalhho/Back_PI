/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.billing.api.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author a
 */
public record PagamentoEvent(
        UUID id,
        UUID pedidoId,
        String status,
        LocalDateTime dataOcorrencia) {

}
