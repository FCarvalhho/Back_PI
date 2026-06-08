/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.shared.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author Cansei2
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // 1. Liberação do Swagger UI e API Docs
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // 2. Rotas Públicas
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/files/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/produto/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/iam/cliente").permitAll()

                        // 3. Rotas de Catálogo - Ajustado para Authority Literal para evitar duplicidade de ROLE_
                        .requestMatchers(HttpMethod.POST, "/api/produto/**").hasAnyAuthority("ROLE_ESTOQUE", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/produto/**").hasAnyAuthority("ROLE_ESTOQUE", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/produto/**").hasAnyAuthority("ROLE_ESTOQUE", "ROLE_ADMIN")

                        // 4. Rotas de Vendas
                        .requestMatchers(HttpMethod.GET, "/admin/vendas-geral").hasAnyAuthority("ROLE_FATURAMENTO", "ROLE_ADMIN")
                        .requestMatchers("/pedidos/**").hasAnyAuthority("ROLE_CLIENTE", "ROLE_ADMIN")

                        // 5. Qualquer outra requisição precisa estar logada
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/files/**");
    }

}
