package com.senai.PI_mecado_preso.catalog.internal.service;

import com.senai.PI_mecado_preso.catalog.api.CatalogoPublicaAPI;
import com.senai.PI_mecado_preso.catalog.internal.entity.ProdutoVariacao;
import com.senai.PI_mecado_preso.catalog.internal.repository.ProdutoVariacaoRepository;
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import com.senai.PI_mecado_preso.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
class CatalogPublicoService implements CatalogoPublicaAPI {

    private final ProdutoVariacaoRepository variacaoRepository;

    public CatalogPublicoService(ProdutoVariacaoRepository variacaoRepository) {
        this.variacaoRepository = variacaoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResultadoPadrao<Boolean> verificarEstoque(UUID variacaoId, Integer quantidade) {
        return variacaoRepository.findById(variacaoId)
                .map(variacao -> {
                    boolean possuiEstoque = variacao.getEstoque() >= quantidade;
                    return possuiEstoque
                            ? ResultadoPadrao.success(true)
                            : ResultadoPadrao.<Boolean>failure("Estoque insuficiente para a variação informada. Disponível: " + variacao.getEstoque());
                })
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto/Variação não encontrada no catálogo com o ID: " + variacaoId));
    }

    @Override
    @Transactional
    public ResultadoPadrao<?> baixarEstoque(UUID variacaoId, Integer quantity) { // Alinhado ao contrato da API
        ProdutoVariacao variacao = variacaoRepository.findById(variacaoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Variação de produto não encontrada para baixa com o ID: " + variacaoId));

        if (variacao.getEstoque() < quantity) {
            return ResultadoPadrao.failure("Falha ao baixar estoque: Saldo insuficiente.");
        }

        variacao.setEstoque(variacao.getEstoque() - quantity);
        variacaoRepository.save(variacao);

        return ResultadoPadrao.success();
    }

    @Override
    @Transactional(readOnly = true)
    public ResultadoPadrao<BigDecimal> obterPreco(UUID variacaoId) {
        return variacaoRepository.findById(variacaoId)
                .map(variacao -> ResultadoPadrao.success(variacao.getPreco()))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Variação não encontrada para consulta de valores com o ID: " + variacaoId));
    }
}