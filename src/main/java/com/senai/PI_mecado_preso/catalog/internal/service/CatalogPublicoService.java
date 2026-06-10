package com.senai.PI_mecado_preso.catalog.internal.service;

import com.senai.PI_mecado_preso.catalog.api.*;
import com.senai.PI_mecado_preso.catalog.internal.entity.ProdutoVariacao;
import com.senai.PI_mecado_preso.catalog.internal.repository.ProdutoVariacaoRepository;
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import com.senai.PI_mecado_preso.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
class CatalogPublicoService implements CatalogoPublicaAPI {

    private final ProdutoVariacaoRepository variacaoRepository;

    public CatalogPublicoService(ProdutoVariacaoRepository variacaoRepository) {
        this.variacaoRepository = variacaoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResultadoPadrao<ValidacaoProdutosDTO> validarProdutos(
            Map<UUID, ItemValidacaoRequestDTO> itens) {

        List<DetalheItemCatalogoDTO> detalhes =
                variacaoRepository.buscarDetalhes(itens.keySet());

        if (detalhes.size() != itens.size()) {
            return ResultadoPadrao.failure(
                    "Um ou mais produtos não foram encontrados ou estão inativos."
            );
        }

        BigDecimal valorTotal = BigDecimal.ZERO;

        Map<UUID, ItemValidadoDTO> itensValidados = new HashMap<>();

        for (DetalheItemCatalogoDTO detalhe : detalhes) {

            ItemValidacaoRequestDTO itemRequest =
                    itens.get(detalhe.variacaoId());

            if (itemRequest == null) {
                continue;
            }

            if (detalhe.estoque() < itemRequest.quantidade()) {
                return ResultadoPadrao.failure(
                        "Estoque insuficiente para o produto "
                                + detalhe.nomeProduto()
                                + ". Disponível: "
                                + detalhe.estoque()
                );
            }

            if (detalhe.preco()
                    .compareTo(itemRequest.precoVisualizado()) != 0) {

                return ResultadoPadrao.failure(
                        "O preço do produto "
                                + detalhe.nomeProduto()
                                + " foi alterado. Valor atual: R$ "
                                + detalhe.preco()
                );
            }

            valorTotal = valorTotal.add(
                    detalhe.preco()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemRequest.quantidade()
                                    )
                            )
            );

            itensValidados.put(
                    detalhe.variacaoId(),
                    new ItemValidadoDTO(
                            detalhe.variacaoId(),
                            detalhe.nomeProduto(),
                            detalhe.preco(),
                            itemRequest.quantidade()
                    )
            );
        }

        return ResultadoPadrao.success(
                new ValidacaoProdutosDTO(
                        valorTotal,
                        itensValidados
                )
        );
    }

    @Override
    @Transactional
    public ResultadoPadrao<?> baixarEstoque(Map<UUID, Integer> quantidades) {

        List<ProdutoVariacao> variacoes =
                variacaoRepository.findAllById(quantidades.keySet());

        if (variacoes.size() != quantidades.size()) {

            Set<UUID> encontrados = variacoes.stream()
                    .map(ProdutoVariacao::getId)
                    .collect(Collectors.toSet());

            UUID faltante = quantidades.keySet().stream()
                    .filter(id -> !encontrados.contains(id))
                    .findFirst()
                    .orElse(null);

            throw new RecursoNaoEncontradoException(
                    "Variação de produto não encontrada para baixa com o ID: "
                            + faltante
            );
        }

        for (ProdutoVariacao variacao : variacoes) {

            Integer quantidadeSolicitada =
                    quantidades.get(variacao.getId());

            if (variacao.getEstoque() < quantidadeSolicitada) {
                return ResultadoPadrao.failure(
                        "Falha ao baixar estoque. Produto "
                                + variacao.getId()
                                + " possui apenas "
                                + variacao.getEstoque()
                                + " unidades disponíveis."
                );
            }
        }

        for (ProdutoVariacao variacao : variacoes) {

            Integer quantidadeSolicitada =
                    quantidades.get(variacao.getId());

            variacao.setEstoque(
                    variacao.getEstoque() - quantidadeSolicitada
            );
        }

        return ResultadoPadrao.success();
    }

    @Override
    @Transactional(readOnly = true)
    public ResultadoPadrao<Map<UUID, DetalheItemCatalogoDTO>> obterItens(
            Set<UUID> itens) {

        List<DetalheItemCatalogoDTO> detalhes =
                variacaoRepository.buscarDetalhes(itens);

        if (detalhes.size() != itens.size()) {
            return ResultadoPadrao.failure(
                    "Uma ou mais variações não foram encontradas ou estão inativas."
            );
        }

        Map<UUID, DetalheItemCatalogoDTO> resultado = new HashMap<>();

        for (DetalheItemCatalogoDTO detalhe : detalhes) {
            resultado.put(
                    detalhe.variacaoId(),
                    detalhe
            );
        }

        return ResultadoPadrao.success(resultado);
    }
}