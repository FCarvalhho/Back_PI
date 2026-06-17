package com.senai.PI_mecado_preso.iam.internal.service;

import com.senai.PI_mecado_preso.iam.api.dtos.LoginRequest;
import com.senai.PI_mecado_preso.iam.api.dtos.TokenResponse;
import com.senai.PI_mecado_preso.iam.internal.repository.UsuarioRepository;
import com.senai.PI_mecado_preso.shared.config.security.JwtService;
import com.senai.PI_mecado_preso.shared.exception.NaoAutenticadoException;
import com.senai.PI_mecado_preso.shared.exception.RegraDeNegocioException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UsuarioRepository usuarioRepository,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        var usuario = usuarioRepository.findByEmail(request.username())
                .orElseThrow(() -> new NaoAutenticadoException("Credenciais inválidas. Usuário não encontrado no sistema."));

        if (!usuario.isEnabled()) { 
            throw new RegraDeNegocioException("Esta conta está desativada no sistema. Entre em contato com o administrador.");
        }
        String jwtToken = jwtService.generateToken(usuario);

        List<String> roles = usuario.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new TokenResponse(
                jwtToken,
                usuario.getId(),
                usuario.getNome(),
                roles
        );
    }
}
