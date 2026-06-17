package com.senai.PI_mecado_preso.sales.internal.service;

import com.senai.PI_mecado_preso.catalog.api.CatalogoPublicaAPI;
import com.senai.PI_mecado_preso.catalog.api.DetalheItemCatalogoDTO;
import com.senai.PI_mecado_preso.sales.api.dtos.CarrinhoResponseDTO;
import com.senai.PI_mecado_preso.sales.api.dtos.ItemCarrinhoRequestDTO;
import com.senai.PI_mecado_preso.sales.api.dtos.ItemCarrinhoResponseDTO;
import com.senai.PI_mecado_preso.sales.internal.entity.Carrinho;
import com.senai.PI_mecado_preso.sales.internal.entity.ItemCarrinho;
import com.senai.PI_mecado_preso.sales.internal.repository.CarrinhoRepository;
import com.senai.PI_mecado_preso.shared.config.security.UsuarioLogadoDTO;
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import com.senai.PI_mecado_preso.shared.exception.RegraDeNegocioException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final CatalogoPublicaAPI catalogoPublicaAPI;

    public CarrinhoService(CarrinhoRepository carrinhoRepository, CatalogoPublicaAPI catalogoPublicaAPI) {
        this.carrinhoRepository = carrinhoRepository;
        this.catalogoPublicaAPI = catalogoPublicaAPI;
    }

    @Transactional
    public void adicionarProdutoAoCarrinho(ItemCarrinhoRequestDTO dto) {
        ResultadoPadrao<Map<UUID, DetalheItemCatalogoDTO>> resultado =
                catalogoPublicaAPI.obterItens(Set.of(dto.variacaoId()));

        if (!resultado.isValid()) {
            throw new RegraDeNegocioException(resultado.failureReason());
        }

        UUID clienteId = obterClienteIdLogado();
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId)
                .orElseGet(() -> {
                    Carrinho novo = new Carrinho();
                    novo.setClienteId(clienteId);
                    return novo;
                });

        carrinho.adicionarItem(dto.variacaoId(), dto.quantidade());
        carrinhoRepository.save(carrinho);
    }

    @Transactional(readOnly = true)
    public CarrinhoResponseDTO mostrarCarrinho() {
        UUID clienteId = obterClienteIdLogado();

        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId)
                .orElseGet(() -> {
                    Carrinho novo = new Carrinho();
                    novo.setClienteId(clienteId);
                    return novo;
                });

        Set<UUID> variacoesIds = carrinho.getItens().stream()
                .map(ItemCarrinho::getVariacaoId)
                .collect(Collectors.toSet());

        Map<UUID, DetalheItemCatalogoDTO> mapaCatalogo = new HashMap<>();

        if (!variacoesIds.isEmpty()) {
            ResultadoPadrao<Map<UUID, DetalheItemCatalogoDTO>> resultado = catalogoPublicaAPI.obterItens(variacoesIds);
            if (resultado.isValid() && resultado.dado() != null) {
                mapaCatalogo = resultado.dado();
            }
        }

        final Map<UUID, DetalheItemCatalogoDTO> mapaFinal = mapaCatalogo;
        Set<ItemCarrinhoResponseDTO> itensEnriquecidos = carrinho.getItens().stream()
                .map(item -> {
                    DetalheItemCatalogoDTO produtoDetalhe = mapaFinal.get(item.getVariacaoId());
                    return new ItemCarrinhoResponseDTO(
                            item.getId(),
                            produtoDetalhe,
                            item.getQuantidade()
                    );
                })
                .collect(Collectors.toSet());

        return new CarrinhoResponseDTO(
                carrinho.getId(),
                carrinho.getAtualizadoEm(),
                itensEnriquecidos
        );
    }

    @Transactional
    public void removerItemDoCarrinho(UUID variacaoId) {
        UUID clienteId = obterClienteIdLogado();
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new RegraDeNegocioException("Carrinho não encontrado para este cliente."));

        carrinho.removerItem(variacaoId);
        carrinhoRepository.save(carrinho);
    }

    @Transactional
    public void limparCarrinho() {
        UUID clienteId = obterClienteIdLogado();
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new RegraDeNegocioException("Carrinho não encontrado para este cliente."));

        carrinho.limparItens();
        carrinhoRepository.save(carrinho);
    }

    @Transactional
    public void incrementarUnidade(UUID variacaoId) {
        UUID clienteId = obterClienteIdLogado();
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new RegraDeNegocioException("Carrinho não encontrado para este cliente."));
        carrinho.adicionarItem(variacaoId, 1);
        carrinhoRepository.save(carrinho);
    }

    @Transactional
    public void decrementarUnidade(UUID variacaoId) {
        UUID clienteId = obterClienteIdLogado();
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new RegraDeNegocioException("Carrinho não encontrado para este cliente."));
        carrinho.decrementarItem(variacaoId);
        carrinhoRepository.save(carrinho);
    }

    @Transactional
    public void removerItensCompradosDoCarrinho(UUID clienteId, Set<UUID> variacoesIds) {
        if (variacoesIds == null || variacoesIds.isEmpty()) {
            return;
        }
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId).orElse(null);
        if (carrinho != null) {
            carrinho.removerItensComprados(variacoesIds);
            carrinhoRepository.save(carrinho);
        }
    }

    private UUID obterClienteIdLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UsuarioLogadoDTO principal)) {
            throw new RegraDeNegocioException("Usuário não autenticado.");
        }
        return principal.getId();
    }
}