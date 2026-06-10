/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.internal.service;

import com.senai.PI_mecado_preso.iam.api.dtos.FuncionarioRequestDTO;
import com.senai.PI_mecado_preso.iam.api.dtos.FuncionarioResponseDTO;
import com.senai.PI_mecado_preso.iam.internal.entity.Funcionario;
import com.senai.PI_mecado_preso.iam.internal.entity.Role;
import com.senai.PI_mecado_preso.iam.internal.mapper.FuncionarioMapper;
import com.senai.PI_mecado_preso.iam.internal.repository.FuncionarioRepository;
import com.senai.PI_mecado_preso.shared.exception.RecursoNaoEncontradoException;
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
public class FuncionarioService {

    private final FuncionarioRepository repositoryFuncionario;
    private final FuncionarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(FuncionarioRepository repository, FuncionarioMapper mapper, PasswordEncoder passwordEncoder) {
        this.repositoryFuncionario = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Transactional(readOnly = true)
    public List<FuncionarioResponseDTO>listarTodos(){
        return repositoryFuncionario.findAll().stream().map(mapper :: toResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Funcionario buscarEntityPorId(UUID id) {
        return repositoryFuncionario.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public FuncionarioResponseDTO buscarPorId(UUID id) {
        return mapper.toResponse(buscarEntityPorId(id));
    }

    @Transactional
    public FuncionarioResponseDTO salvar(FuncionarioRequestDTO dto){
        Funcionario entidade = mapper.toEntity(dto);

        if (dto.roles() != null && !dto.roles().isEmpty()) {
            Set<Role> rolesMapeadas = dto.roles().stream()
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());

            entidade.setRoles(rolesMapeadas);
        } else {
            entidade.setRoles(new java.util.HashSet<>());
        }

        entidade.setSenha(passwordEncoder.encode(dto.senha()));

        entidade = repositoryFuncionario.save(entidade);
        return mapper.toResponse(entidade);
    }
    
    @Transactional
    public FuncionarioResponseDTO atualizar(UUID id, FuncionarioRequestDTO dto){
        Funcionario exitente = buscarEntityPorId(id);
        mapper.updateEntityFromDto(dto, exitente);

        if (dto.roles() != null && !dto.roles().isEmpty()) {
            Set<Role> rolesMapeadas = dto.roles().stream()
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());

            exitente.setRoles(rolesMapeadas);
        } else {
            exitente.setRoles(new java.util.HashSet<>());
        }

        exitente.setSenha(passwordEncoder.encode(dto.senha()));
        exitente = repositoryFuncionario.save(exitente);
        return mapper.toResponse(exitente);
    }
    
    @Transactional
    public void deletar(UUID id){
        repositoryFuncionario.delete(buscarEntityPorId(id));
    }
}

