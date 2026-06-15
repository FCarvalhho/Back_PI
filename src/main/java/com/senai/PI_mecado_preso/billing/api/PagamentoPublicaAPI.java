package com.senai.PI_mecado_preso.billing.api;

import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;

import java.util.concurrent.CompletableFuture;

public interface PagamentoPublicaAPI {
    CompletableFuture<ResultadoPadrao<CobrancaResponseDTO>> processarCobranca(CobrancaRequestDTO request);
}
