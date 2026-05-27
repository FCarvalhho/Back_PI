package com.senai.PI_mecado_preso.iam.internal.config;

import com.senai.PI_mecado_preso.iam.internal.entity.Funcionario;
import com.senai.PI_mecado_preso.iam.internal.entity.Role;
import com.senai.PI_mecado_preso.iam.internal.repository.FuncionarioRepository;
import com.senai.PI_mecado_preso.iam.internal.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DatabaseSeeder {

    @Value("${api.security.admin.email}")
    private String adminEmail;

    @Value("${api.security.admin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner initDatabase(
            UsuarioRepository usuarioRepository,
            FuncionarioRepository funcionarioRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (usuarioRepository.findByEmail(adminEmail).isEmpty()) {

                Funcionario admin = new Funcionario();
                admin.setNome("Administrador do Sistema");
                admin.setEmail(adminEmail);
                admin.setSenha(passwordEncoder.encode(adminPassword));

                admin.setMatricula("ADMIN-MASTER");
                admin.setRoles(Set.of(Role.ROLE_ADMIN));

                funcionarioRepository.save(admin);

            }
        };
    }
}