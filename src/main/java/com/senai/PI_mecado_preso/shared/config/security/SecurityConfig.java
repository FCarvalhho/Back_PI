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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/error"
                        ).permitAll()

                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/files/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/produto/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/iam/cliente").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/produto/**").hasAnyAuthority("ROLE_ESTOQUE", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/produto/**").hasAnyAuthority("ROLE_ESTOQUE", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/produto/**").hasAnyAuthority("ROLE_ESTOQUE", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/produto/**").hasAnyAuthority("ROLE_ESTOQUE", "ROLE_ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/iam/cliente").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/iam/funcionario").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/iam/funcionario").hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers("/api/iam/cliente/*").hasAnyAuthority("ROLE_CLIENTE", "ROLE_ADMIN")
                        .requestMatchers("/api/iam/funcionario/*").hasAnyAuthority("ROLE_ADMIN", "ROLE_ESTOQUE", "ROLE_FATURAMENTO")

                        .requestMatchers(HttpMethod.GET, "/admin/vendas-geral").hasAnyAuthority("ROLE_FATURAMENTO", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/pedidos").hasAnyAuthority("ROLE_ADMIN", "ROLE_FATURAMENTO")

                        .requestMatchers(HttpMethod.GET, "/api/pedidos/meus").hasAnyAuthority("ROLE_CLIENTE", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/pedidos").hasAnyAuthority("ROLE_CLIENTE", "ROLE_ADMIN")

                        .requestMatchers("/api/pedidos/carrinho", "/api/pedidos/carrinho/**").hasAnyAuthority("ROLE_CLIENTE", "ROLE_ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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