package com.senai.PI_mecado_preso.billing.api;

import java.util.UUID;

public record PagamentoProcessadoEvent(
        UUID pedidoId,
        String statusSugerido
) {}
