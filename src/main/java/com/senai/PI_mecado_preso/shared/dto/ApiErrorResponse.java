package com.senai.PI_mecado_preso.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiErrorResponse(
        int status,
        String erro,
        String mensagem,
        OffsetDateTime timestamp,
        List<CampoErro> campos
) {
    public record CampoErro(String nome, String mensagem) {}
}
