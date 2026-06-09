package com.senai.PI_mecado_preso.iam.internal.service;

import com.senai.PI_mecado_preso.iam.api.IamPublicaApi;
import com.senai.PI_mecado_preso.iam.internal.entity.Usuario;
import com.senai.PI_mecado_preso.iam.internal.repository.UsuarioRepository;
import com.senai.PI_mecado_preso.shared.config.security.UsuarioLogadoDTO;
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsuarioService implements UserDetailsService, IamPublicaApi {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + username));

        return new UsuarioLogadoDTO(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getAuthorities()
        );
    }

    @Override
    public ResultadoPadrao<?> validarUsuario(UUID usuarioId) {

        Usuario usuario = repository.findById(usuarioId).orElse(null);

        if (usuario == null) return ResultadoPadrao.failure("Cliente não encontrado no sistema.");
        if (!usuario.getAtivo()) return ResultadoPadrao.failure("A conta do cliente está inativa.");

        return ResultadoPadrao.success();
    }
}