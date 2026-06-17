package com.senai.PI_mecado_preso.catalog.internal.service;

import com.senai.PI_mecado_preso.catalog.internal.entity.ProdutoVariacao;
import com.senai.PI_mecado_preso.catalog.internal.repository.ProdutoVariacaoRepository;
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogPublicoEstoqueTest {

    @Mock
    private ProdutoVariacaoRepository variacaoRepository;

    @InjectMocks
    private CatalogPublicoService catalogPublicoService;

    @Test
    @DisplayName("Deve retornar falha estruturada quando a quantidade solicitada for maior que o estoque")
    void deveRetornarFalhaQuandoEstoqueForInsuficiente() {
        UUID variacaoId = UUID.randomUUID();
        Map<UUID, Integer> quantidadesSolicitadas = Map.of(variacaoId, 10);

        ProdutoVariacao variacaoFicticia = new ProdutoVariacao();
        variacaoFicticia.setId(variacaoId);
        variacaoFicticia.setEstoque(4);
        variacaoFicticia.setPreco(new BigDecimal("100.00"));

        when(variacaoRepository.findAllById(quantidadesSolicitadas.keySet()))
                .thenReturn(List.of(variacaoFicticia));

        ResultadoPadrao<?> resultado = catalogPublicoService.baixarEstoque(quantidadesSolicitadas);

        assertNotNull(resultado, "O resultado não deve ser nulo");
        assertFalse(resultado.isValid(), "O resultado deveria ser inválido devido à falta de estoque");
        assertTrue(resultado.failureReason().contains("Falha ao baixar estoque"), "A mensagem de falha deve ser condizente");
        
        verify(variacaoRepository, times(1)).findAllById(anySet());
        verifyNoMoreInteractions(variacaoRepository);
    }
}