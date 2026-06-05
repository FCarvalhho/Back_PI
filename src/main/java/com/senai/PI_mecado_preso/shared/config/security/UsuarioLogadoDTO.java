package com.senai.PI_mecado_preso.shared.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.UUID;

public class UsuarioLogadoDTO implements UserDetails {

    private final UUID id;
    private final String email;
    private final String senha;
    private final Collection<? extends GrantedAuthority> authorities;

    public UsuarioLogadoDTO(UUID id, String email, String senha, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.authorities = authorities;
    }

    public UUID getId() { return id; }
    @Override public String getUsername() { return email; }
    @Override public String getPassword() { return senha; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    // métodos de controle (pode retornar true para todos)
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
