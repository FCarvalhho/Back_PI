package com.senai.PI_mecado_preso.catalog.api;

import java.util.UUID;

public record PedidoVariacaoExibicaoDTO(
        UUID id,
        String nomeProduto,
        String sku,
        String detalhes
) {}
