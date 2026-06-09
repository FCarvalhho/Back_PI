package com.senai.PI_mecado_preso.catalog.internal.service;

import com.senai.PI_mecado_preso.catalog.api.dtos.AtributoRequestDTO;
import com.senai.PI_mecado_preso.catalog.api.dtos.AtributoResponseDTO;
import com.senai.PI_mecado_preso.catalog.internal.entity.Atributo;
import com.senai.PI_mecado_preso.catalog.internal.mapper.AtributoMapper;
import com.senai.PI_mecado_preso.catalog.internal.repository.AtributoRepository;
import com.senai.PI_mecado_preso.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AtributoService {

    private final AtributoRepository repository;
    private final AtributoMapper mapper;

    public AtributoService(AtributoRepository repository, AtributoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<AtributoResponseDTO> listar(){
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Atributo buscarEntityPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("arrumar aqui com exception global"));
    }

    @Transactional(readOnly = true)
    public AtributoResponseDTO buscarPorId(UUID id) {
        return mapper.toResponse(buscarEntityPorId(id));
    }

    @Transactional
    public AtributoResponseDTO salvar (AtributoRequestDTO dto){
        Atributo salvo = mapper.toEntity(dto);
        repository.save(salvo);
        return mapper.toResponse(salvo);
    }

    @Transactional
    public AtributoResponseDTO atualizar (UUID id,AtributoRequestDTO dto){
        Atributo salvo = buscarEntityPorId(id);
        mapper.updateEntityFromDto(dto,salvo);
        repository.save(salvo);
        return mapper.toResponse(salvo);
    }

    @Transactional
    public void deletar (UUID id){
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Não foi possível deletar. Atributo não encontrado com o ID: " + id);
        }
        repository.deleteById(id);
    }
}
