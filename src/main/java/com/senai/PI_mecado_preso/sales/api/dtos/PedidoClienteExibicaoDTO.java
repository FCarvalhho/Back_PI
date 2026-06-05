package com.senai.PI_mecado_preso.sales.api.dtos;

import java.util.UUID;

public record PedidoClienteExibicaoDTO(
        UUID id,
        String nome,
        String cpf
) {}
