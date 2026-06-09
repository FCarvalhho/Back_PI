package com.senai.PI_mecado_preso.iam.api;

import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;

import java.util.UUID;

public interface IamPublicaApi {
    ResultadoPadrao<?> validarUsuario(UUID usuarioId);

}
