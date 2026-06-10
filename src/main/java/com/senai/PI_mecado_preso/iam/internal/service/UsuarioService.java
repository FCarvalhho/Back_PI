package com.senai.PI_mecado_preso.iam.internal.service;

import com.senai.PI_mecado_preso.iam.api.IamPublicaApi;
import com.senai.PI_mecado_preso.iam.api.PedidoUsuarioDTO;
import com.senai.PI_mecado_preso.iam.internal.entity.Usuario;
import com.senai.PI_mecado_preso.iam.internal.repository.UsuarioRepository;
import com.senai.PI_mecado_preso.shared.config.security.UsuarioLogadoDTO;
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements UserDetailsService, IamPublicaApi {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Usuario usuario = repository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuário não encontrado com o e-mail: "
                                        + username));

        return new UsuarioLogadoDTO(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getAuthorities()
        );
    }

    @Override
    public ResultadoPadrao<PedidoUsuarioDTO> obterUsuario(UUID usuarioId) {

        Usuario usuario = repository.findById(usuarioId)
                .orElse(null);

        if (usuario == null) {
            return ResultadoPadrao.failure(
                    "Usuário não encontrado."
            );
        }

        if (!usuario.getAtivo()) {
            return ResultadoPadrao.failure(
                    "A conta do usuário está inativa."
            );
        }

        return ResultadoPadrao.success(
                toPedidoUsuarioDTO(usuario)
        );
    }

    @Override
    public ResultadoPadrao<Map<UUID, PedidoUsuarioDTO>>
    obterUsuarios(Set<UUID> usuariosIds) {

        var usuarios = repository.buscarUsuarios(usuariosIds);

        Map<UUID, PedidoUsuarioDTO> resultado =
                usuarios.stream()
                        .collect(Collectors.toMap(
                                Usuario::getId,
                                this::toPedidoUsuarioDTO
                        ));

        return ResultadoPadrao.success(resultado);
    }

    private PedidoUsuarioDTO toPedidoUsuarioDTO(
            Usuario usuario) {

        return new PedidoUsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDocumentoExibicao(),
                usuario.getTipoUsuario(),
                usuario.getAtivo()
        );
    }
}