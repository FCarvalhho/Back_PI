package com.senai.PI_mecado_preso.catalog.api;

import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface CatalogoPublicaAPI {

    ResultadoPadrao<ValidacaoProdutosDTO> validarProdutos(
            Map<UUID, ItemValidacaoRequestDTO> itens
    );

    ResultadoPadrao<?> baixarEstoque(
            Map<UUID, Integer> quantidades
    );

    ResultadoPadrao<Map<UUID, DetalheItemCatalogoDTO>> obterItens(
            Set<UUID> itens
    );
}
