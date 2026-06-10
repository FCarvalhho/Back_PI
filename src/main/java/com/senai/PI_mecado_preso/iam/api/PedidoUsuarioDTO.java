package com.senai.PI_mecado_preso.iam.api;

import java.util.UUID;

public record PedidoUsuarioDTO(
        UUID id,
        String nome,
        String email,
        String documento,
        String tipoUsuario,
        Boolean ativo
) {}
