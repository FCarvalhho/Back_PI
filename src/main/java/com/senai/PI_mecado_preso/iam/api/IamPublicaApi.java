package com.senai.PI_mecado_preso.iam.api;

import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IamPublicaApi {

    ResultadoPadrao<PedidoUsuarioDTO> obterUsuario(UUID usuarioId);

    ResultadoPadrao<Map<UUID, PedidoUsuarioDTO>> obterUsuarios(
            Set<UUID> usuariosIds
    );
}
