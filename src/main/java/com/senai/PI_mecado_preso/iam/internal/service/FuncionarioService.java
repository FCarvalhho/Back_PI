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
    public List<FuncionarioResponseDTO> listarTodos() {
        return repositoryFuncionario.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
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
    public FuncionarioResponseDTO salvar(FuncionarioRequestDTO dto) {
        if (dto.senha() == null || dto.senha().isBlank()) {
            throw new IllegalArgumentException("A senha é obrigatória para registrar um funcionário.");
        }
        if (dto.senha().length() < 6 || dto.senha().length() > 100) {
            throw new IllegalArgumentException("A senha deve conter entre 6 e 100 caracteres.");
        }

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
    public FuncionarioResponseDTO atualizar(UUID id, FuncionarioRequestDTO dto) {
        Funcionario existente = buscarEntityPorId(id);
        String senhaOriginalDoBanco = existente.getSenha();
        mapper.updateEntityFromDto(dto, existente);

        if (dto.roles() != null && !dto.roles().isEmpty()) {
            Set<Role> rolesMapeadas = dto.roles().stream()
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());
            existente.setRoles(rolesMapeadas);
        }

        if (dto.senha() != null && !dto.senha().isBlank()) {
            if (dto.senha().length() < 6 || dto.senha().length() > 100) {
                throw new IllegalArgumentException("A nova senha deve conter entre 6 e 100 caracteres.");
            }
            existente.setSenha(passwordEncoder.encode(dto.senha()));
        } else {
            existente.setSenha(senhaOriginalDoBanco);
        }

        existente = repositoryFuncionario.save(existente);
        return mapper.toResponse(existente);
    }

    @Transactional
    public void deletar(UUID id) {
        repositoryFuncionario.delete(buscarEntityPorId(id));
    }
}