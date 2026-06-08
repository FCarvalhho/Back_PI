package com.senai.PI_mecado_preso.catalog.internal.service;

import com.senai.PI_mecado_preso.catalog.api.CatalogoPublicaAPI;
import com.senai.PI_mecado_preso.catalog.internal.entity.ProdutoVariacao;
import com.senai.PI_mecado_preso.catalog.internal.repository.ProdutoVariacaoRepository;
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
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
                .orElseGet(() -> ResultadoPadrao.<Boolean>failure("Produto/Variação não encontrada no catálogo."));
    }

    @Override
    @Transactional
    public ResultadoPadrao<?> baixarEstoque(UUID variacaoId, Integer quantity) { // Alinhado ao contrato da API
        ProdutoVariacao variacao = variacaoRepository.findById(variacaoId).orElse(null);

        if (variacao == null) {
            return ResultadoPadrao.failure("Variação não encontrada.");
        }

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
                .orElseGet(() -> ResultadoPadrao.failure("Variação de produto não encontrada para consulta de valores."));
    }
}