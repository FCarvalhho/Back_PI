
package com.senai.PI_mecado_preso.catalog.internal.service;

import com.senai.PI_mecado_preso.catalog.api.dtos.*;
import com.senai.PI_mecado_preso.catalog.internal.entity.Atributo;
import com.senai.PI_mecado_preso.catalog.internal.entity.ImagemVariacao;
import com.senai.PI_mecado_preso.catalog.internal.entity.ProdutoVariacao;
import com.senai.PI_mecado_preso.catalog.internal.entity.VariacaoOpcao;
import com.senai.PI_mecado_preso.catalog.internal.mapper.ProdutoVariacaoMapper;
import com.senai.PI_mecado_preso.catalog.internal.repository.AtributoRepository;
import com.senai.PI_mecado_preso.catalog.internal.repository.ProdutoVariacaoRepository;
import com.senai.PI_mecado_preso.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProdutoVariacaoService {

    private final ProdutoVariacaoRepository repository;
    private final ProdutoVariacaoMapper mapper;
    private final ProdutoService produtoService;
    private final AtributoRepository atributoRepository;

    public ProdutoVariacaoService(ProdutoVariacaoRepository repository, ProdutoVariacaoMapper mapper, ProdutoService produtoService, AtributoRepository atributoRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.produtoService = produtoService;
        this.atributoRepository = atributoRepository;
    }

    @Transactional(readOnly = true)
    public List<ProdutoVariacaoResponseDTO> listar() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProdutoVariacao buscarEntityPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Variação não encontrada com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public ProdutoVariacaoResponseDTO buscarPorId(UUID id) {
        return mapper.toResponse(buscarEntityPorId(id));
    }

    @Transactional
    public ProdutoVariacaoResponseDTO salvar(UUID produtoID, ProdutoVariacaoRequestDTO dto) {
        ProdutoVariacao produtoVariacao = mapper.toEntity(dto);
        produtoVariacao.setProduto(produtoService.buscarEntityPorId(produtoID));
        produtoVariacao.setOpcoes(new ArrayList<>());
        produtoVariacao.setImagens(new ArrayList<>());

        processarOpcoes(produtoVariacao, dto.opcoes());
        processarImagens(produtoVariacao, dto.imagens());

        produtoVariacao = repository.save(produtoVariacao);
        return mapper.toResponse(produtoVariacao);
    }

    @Transactional
    public ProdutoVariacaoResponseDTO atualizar(UUID id, ProdutoVariacaoUpdateDTO dto) {
        ProdutoVariacao produtoVariacao = buscarEntityPorId(id);

        if (dto.sku() != null && !dto.sku().isBlank()) {
            produtoVariacao.setSku(dto.sku());
        }
        if (dto.preco() != null) {
            produtoVariacao.setPreco(dto.preco());
        }
        if (dto.estoque() != null) {
            produtoVariacao.setEstoque(dto.estoque());
        }

        if (dto.opcoes() != null) {
            produtoVariacao.getOpcoes().clear();
            repository.saveAndFlush(produtoVariacao);
            processarOpcoes(produtoVariacao, dto.opcoes());
        }

        if (dto.imagens() != null) {
            produtoVariacao.getImagens().clear();
            repository.saveAndFlush(produtoVariacao);
            processarImagens(produtoVariacao, dto.imagens());
        }

        produtoVariacao = repository.save(produtoVariacao);
        return mapper.toResponse(produtoVariacao);
    }

    @Transactional
    public void deletar(UUID id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Não foi possível deletar. Variação não encontrada com o ID: " + id);
        }
        repository.deleteById(id);
    }

    private void processarOpcoes(ProdutoVariacao produtoVariacao, List<VariacaoOpcaoRequestDTO> opcoesDto) {
        if (opcoesDto == null || opcoesDto.isEmpty()) {
            return;
        }

        for (VariacaoOpcaoRequestDTO opcao : opcoesDto) {
            Atributo atributo = atributoRepository.findById(opcao.atributoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Não foi possível processar opção. Atributo não encontrado com o ID: " + opcao.atributoId()));

            VariacaoOpcao variacaoOpcao = new VariacaoOpcao();
            variacaoOpcao.setVariacao(produtoVariacao);
            variacaoOpcao.setAtributo(atributo);
            variacaoOpcao.setValor(opcao.valor());

            produtoVariacao.getOpcoes().add(variacaoOpcao);
        }
    }

    private void processarImagens(ProdutoVariacao produtoVariacao, List<ImagemVariacaoRequestDTO> imagensDto) {
        if (imagensDto == null || imagensDto.isEmpty()) {
            return;
        }

        for (ImagemVariacaoRequestDTO imagem : imagensDto) {
            ImagemVariacao imagemVariacao = new ImagemVariacao();
            imagemVariacao.setUrlImagem(imagem.urlImagem());
            imagemVariacao.setOrdem(imagem.ordem());
            imagemVariacao.setVariacao(produtoVariacao);

            produtoVariacao.getImagens().add(imagemVariacao);
        }
    }
}
