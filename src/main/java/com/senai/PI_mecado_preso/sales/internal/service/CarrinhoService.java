package com.senai.PI_mecado_preso.sales.internal.service;

import com.senai.PI_mecado_preso.catalog.api.CatalogoPublicaAPI;
import com.senai.PI_mecado_preso.sales.api.dtos.CarrinhoResponseDTO;
import com.senai.PI_mecado_preso.sales.api.dtos.ItemCarrinhoRequestDTO;
import com.senai.PI_mecado_preso.sales.internal.entity.Carrinho;
import com.senai.PI_mecado_preso.sales.internal.mapper.CarrinhoMapper;
import com.senai.PI_mecado_preso.sales.internal.repository.CarrinhoRepository;
import com.senai.PI_mecado_preso.shared.config.security.UsuarioLogadoDTO;
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final CatalogoPublicaAPI catalogoPublicaAPI;
    private final CarrinhoMapper mapper;

    public CarrinhoService(CarrinhoRepository carrinhoRepository, CatalogoPublicaAPI catalogoPublicaAPI, CarrinhoMapper mapper) {
        this.carrinhoRepository = carrinhoRepository;
        this.catalogoPublicaAPI = catalogoPublicaAPI;
        this.mapper = mapper;
    }

    @Transactional
    public void adicionarProdutoAoCarrinho(ItemCarrinhoRequestDTO dto) {

        ResultadoPadrao<Boolean> validacaoEstoque = catalogoPublicaAPI.verificarEstoque(dto.variacaoId(), dto.quantidade());
        if (!validacaoEstoque.isValid()) {
            throw new RuntimeException("Não foi possível adicionar ao carrinho: " + validacaoEstoque.failureReason());
        }

        UsuarioLogadoDTO principal = (UsuarioLogadoDTO) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        UUID clienteId = principal.getId();

        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId)
                .orElseGet(() -> {
                    Carrinho novo = new Carrinho();
                    novo.setClienteId(clienteId);
                    return novo;
                });

        // Chama o método interno da entidade Carrinho usando os nomes corrigidos
        carrinho.adicionarItem(dto.variacaoId(), dto.quantidade());
        carrinhoRepository.save(carrinho);
    }

    @Transactional(readOnly = true)
    public CarrinhoResponseDTO mostrarCarrinho(){
        UsuarioLogadoDTO principal = (UsuarioLogadoDTO) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        UUID clienteId = principal.getId();
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId).orElseGet(() -> {
            Carrinho novo = new Carrinho();
            novo.setClienteId(clienteId);
            return novo;
        });
        return mapper.toResponse(carrinho);
    }
}