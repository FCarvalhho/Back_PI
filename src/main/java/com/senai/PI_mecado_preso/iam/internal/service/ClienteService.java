/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.internal.service;

import com.senai.PI_mecado_preso.iam.api.dtos.ClienteRequestDTO;
import com.senai.PI_mecado_preso.iam.api.dtos.ClienteResponseDTO;
import com.senai.PI_mecado_preso.iam.internal.entity.Cliente;
import com.senai.PI_mecado_preso.iam.internal.mapper.ClienteMapper;
import com.senai.PI_mecado_preso.iam.internal.repository.ClienteRepository;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Cansei2
 */
@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final ClienteMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository repository, ClienteMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ClienteResponseDTO salvar(ClienteRequestDTO request) {
        Cliente entidade = mapper.toEntity(request);
        entidade.setSenha(passwordEncoder.encode(entidade.getSenha()));
        Cliente salvo = repository.save(entidade);
        return mapper.toResponse(salvo);
    }

    @Transactional
    public ClienteResponseDTO atualizar(UUID id, ClienteRequestDTO request) {
        Cliente exitente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("nao encontrado"));

        mapper.updateEntityFromDto(request, exitente);

        repository.save(exitente);
        return mapper.toResponse(exitente);
    }
}
