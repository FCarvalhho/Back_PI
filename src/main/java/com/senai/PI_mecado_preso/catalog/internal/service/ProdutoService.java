package com.senai.PI_mecado_preso.catalog.internal.service;

import com.senai.PI_mecado_preso.catalog.api.dtos.ProdutoRequestDTO;
import com.senai.PI_mecado_preso.catalog.api.dtos.ProdutoResponseDTO;
import com.senai.PI_mecado_preso.catalog.internal.entity.Atributo;
import com.senai.PI_mecado_preso.catalog.internal.entity.Produto;
import com.senai.PI_mecado_preso.catalog.internal.entity.ProdutoAtributo;
import com.senai.PI_mecado_preso.catalog.internal.mapper.ProdutoMapper;
import com.senai.PI_mecado_preso.catalog.internal.repository.AtributoRepository;
import com.senai.PI_mecado_preso.catalog.internal.repository.ProdutoRepository;
import com.senai.PI_mecado_preso.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;
    private final AtributoRepository atributoRepository;

    public ProdutoService(ProdutoRepository repository, ProdutoMapper mapper, AtributoRepository atributoRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.atributoRepository = atributoRepository;
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listar(){
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Produto buscarEntityPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("arrumar aqui com exception global"));
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(UUID id) {
        return mapper.toResponse(buscarEntityPorId(id));
    }

    @Transactional
    public ProdutoResponseDTO salvar(ProdutoRequestDTO dto){
        Produto produto = mapper.toEntity(dto);
        produto.setAtributos(new ArrayList<>());

        if (dto.atributosIds() != null && !dto.atributosIds().isEmpty()) {
            vincularAtributos(produto, dto.atributosIds());
        }

        produto = repository.save(produto);
        return mapper.toResponse(produto);
    }

    @Transactional
    public ProdutoResponseDTO atualizar(UUID id, ProdutoRequestDTO dto){
        Produto produto = buscarEntityPorId(id);
        mapper.updateEntityFromDto(dto, produto);

        produto.getAtributos().clear();

        if (dto.atributosIds() != null && !dto.atributosIds().isEmpty()) {
            vincularAtributos(produto, dto.atributosIds());
        }

        produto = repository.save(produto);
        return mapper.toResponse(produto);
    }

    @Transactional
    public void deletar(UUID id){
        Produto produto = buscarEntityPorId(id);
        produto.setAtivo(false);
        repository.save(produto);
    }

    private void vincularAtributos(Produto produto, List<UUID> atributosIds) {
        for (UUID atributoId : atributosIds) {
            Atributo atributo = atributoRepository.findById(atributoId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Atributo não encontrado: " + atributoId));

            ProdutoAtributo produtoAtributo = new ProdutoAtributo();
            produtoAtributo.setProduto(produto);
            produtoAtributo.setAtributo(atributo);

            produto.getAtributos().add(produtoAtributo);
        }
    }

}
