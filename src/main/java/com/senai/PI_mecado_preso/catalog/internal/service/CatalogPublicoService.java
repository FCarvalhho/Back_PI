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

        List<ProdutoVariacao> variacoes = variacaoRepository.buscarVariacoesComDetalhes(itens.keySet());

        if (variacoes.size() != itens.size()) {
            return ResultadoPadrao.failure(
                    "Um ou mais produtos não foram encontrados ou estão inativos."
            );
        }

        BigDecimal valorTotal = BigDecimal.ZERO;
        Map<UUID, ItemValidadoDTO> itensValidados = new HashMap<>();

        for (ProdutoVariacao variacao : variacoes) {
            ItemValidacaoRequestDTO itemRequest = itens.get(variacao.getId());

            if (itemRequest == null) {
                continue;
            }

            if (variacao.getEstoque() < itemRequest.quantidade()) {
                return ResultadoPadrao.failure(
                        "Estoque insuficiente para o produto " + variacao.getProduto().getNome() + "."
                );
            }

            if (variacao.getPreco().compareTo(itemRequest.precoVisualizado()) != 0) {
                return ResultadoPadrao.failure(
                        "O preço do produto foi alterado. Valor atual: R$ " + variacao.getPreco()
                );
            }

            valorTotal = valorTotal.add(
                    variacao.getPreco().multiply(BigDecimal.valueOf(itemRequest.quantidade()))
            );

            itensValidados.put(
                    variacao.getId(),
                    new ItemValidadoDTO(
                            variacao.getId(),
                            variacao.getProduto().getNome(),
                            variacao.getPreco(),
                            itemRequest.quantidade()
                    )
            );
        }

        return ResultadoPadrao.success(new ValidacaoProdutosDTO(valorTotal, itensValidados));
    }

    @Override
    @Transactional(readOnly = true)
    public ResultadoPadrao<Map<UUID, DetalheItemCatalogoDTO>> obterItens(Set<UUID> itens) {

        List<ProdutoVariacao> variacoes = variacaoRepository.buscarVariacoesComDetalhes(itens);

        if (variacoes.size() != itens.size()) {
            return ResultadoPadrao.failure(
                    "Uma ou mais variações não foram encontradas ou estão inativas."
            );
        }

        Map<UUID, DetalheItemCatalogoDTO> resultado = new HashMap<>();

        for (ProdutoVariacao pv : variacoes) {


            String detalhesString = pv.getOpcoes().stream()
                    .map(opcao -> {
                        String nomeAtributo = (opcao.getAtributo() != null) ? opcao.getAtributo().getNome() : "Opção";
                        return nomeAtributo + ": " + opcao.getValor();
                    })
                    .collect(Collectors.joining(" | "));

            if (detalhesString.isEmpty()) {
                detalhesString = "Variação Padrão";
            }

            DetalheItemCatalogoDTO dto = new DetalheItemCatalogoDTO(
                    pv.getId(),
                    pv.getProduto().getNome(),
                    pv.getPreco(),
                    pv.getEstoque(),
                    pv.getSku() != null ? pv.getSku() : "S/K",
                    detalhesString
            );

            resultado.put(pv.getId(), dto);
        }

        return ResultadoPadrao.success(resultado);
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

}