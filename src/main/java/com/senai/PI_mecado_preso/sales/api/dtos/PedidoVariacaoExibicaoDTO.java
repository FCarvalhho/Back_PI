package com.senai.PI_mecado_preso.sales.api.dtos;

import java.util.UUID;

public record PedidoVariacaoExibicaoDTO(
        UUID id,
        String nomeProduto, // Nome vindo do produto pai
        String sku,
        String detalhes // Ex: "Cor: Preto, Tamanho: G"
) {}
