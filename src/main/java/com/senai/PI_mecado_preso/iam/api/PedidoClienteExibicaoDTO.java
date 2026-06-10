package com.senai.PI_mecado_preso.iam.api;

import java.util.UUID;

public record PedidoClienteExibicaoDTO(
        UUID id,
        String nome,
        String cpf
) {}
