/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.internal.service;

import com.senai.PI_mecado_preso.iam.api.dtos.ClienteRequestDTO;
import com.senai.PI_mecado_preso.iam.api.dtos.ClienteResponseDTO;
import com.senai.PI_mecado_preso.iam.internal.entity.Cliente;
import com.senai.PI_mecado_preso.iam.internal.entity.Role;
import com.senai.PI_mecado_preso.iam.internal.mapper.ClienteMapper;
import com.senai.PI_mecado_preso.iam.internal.repository.ClienteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodos(){
        return repository.findAll().stream().map(mapper :: toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Cliente buscarEntityPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("nao encontrado"));
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(UUID id) {
        return mapper.toResponse(buscarEntityPorId(id));
    }

    @Transactional
    public ClienteResponseDTO salvar(ClienteRequestDTO request) {
        Cliente entidade = mapper.toEntity(request);

        entidade.setRoles(Set.of(Role.ROLE_CLIENTE));

        entidade.setSenha(passwordEncoder.encode(request.senha()));

        Cliente salvo = repository.save(entidade);
        return mapper.toResponse(salvo);
    }

    @Transactional
    public ClienteResponseDTO atualizar(UUID id, ClienteRequestDTO request) {
        Cliente existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        mapper.updateEntityFromDto(request, existente);

        existente.setSenha(passwordEncoder.encode(request.senha()));

        repository.save(existente);
        return mapper.toResponse(existente);
    }

    @Transactional
    public void deletar(UUID id){
        repository.delete(buscarEntityPorId(id));
    }
}
