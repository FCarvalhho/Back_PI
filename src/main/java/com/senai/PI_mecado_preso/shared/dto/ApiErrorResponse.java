/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.shared.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;
import java.util.List;
/**
 *
 * @author GabrielB
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY) // Só renderiza a lista de campos se ela não estiver vazia
public record ApiErrorResponse(
        int status,
        String erro,
        String mensagem,
        OffsetDateTime timestamp,
        List<CampoErro> campos
) {
    public record CampoErro(String nome, String mensagem) {}
}
