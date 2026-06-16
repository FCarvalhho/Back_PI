package com.senai.PI_mecado_preso.iam.internal.service;

import com.senai.PI_mecado_preso.iam.api.dtos.ClienteRequestDTO;
import com.senai.PI_mecado_preso.iam.api.dtos.ClienteResponseDTO;
import com.senai.PI_mecado_preso.iam.internal.entity.Cliente;
import com.senai.PI_mecado_preso.iam.internal.entity.Role;
import com.senai.PI_mecado_preso.iam.internal.mapper.ClienteMapper;
import com.senai.PI_mecado_preso.iam.internal.repository.ClienteRepository;
import com.senai.PI_mecado_preso.shared.exception.RecursoNaoEncontradoException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public List<ClienteResponseDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Cliente buscarEntityPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com o ID: " + id));

    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(UUID id) {
        return mapper.toResponse(buscarEntityPorId(id));
    }

    @Transactional
    public ClienteResponseDTO salvar(ClienteRequestDTO request) {
        if (request.senha() == null || request.senha().isBlank()) {
            throw new IllegalArgumentException("A senha é obrigatória para efetuar o cadastro.");
        }
        if (request.senha().length() < 6 || request.senha().length() > 100) {
            throw new IllegalArgumentException("A senha deve conter entre 6 e 100 caracteres.");
        }

        Cliente entidade = mapper.toEntity(request);
        entidade.setRoles(Set.of(Role.ROLE_CLIENTE));
        entidade.setSenha(passwordEncoder.encode(request.senha()));

        Cliente salvo = repository.save(entidade);
        return mapper.toResponse(salvo);
    }

    @Transactional
    public ClienteResponseDTO atualizar(UUID id, ClienteRequestDTO request) {
        Cliente existente = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Não foi possível atualizar. Cliente não encontrado com o ID: " + id));

        String senhaOriginalDoBanco = existente.getSenha();

        mapper.updateEntityFromDto(request, existente);
        if (request.senha() != null && !request.senha().isBlank()) {
            if (request.senha().length() < 6 || request.senha().length() > 100) {
                throw new IllegalArgumentException("A nova senha deve conter entre 6 e 100 caracteres.");
            }
            existente.setSenha(passwordEncoder.encode(request.senha()));
        } else {
            existente.setSenha(senhaOriginalDoBanco);
        }

        repository.save(existente);
        return mapper.toResponse(existente);
    }

    @Transactional
    public void deletar(UUID id) {
        repository.delete(buscarEntityPorId(id));
    }
}
