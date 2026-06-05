package com.senai.PI_mecado_preso.sales.internal.service;

import com.senai.PI_mecado_preso.catalog.api.CatalogoPublicaAPI;
import com.senai.PI_mecado_preso.iam.api.IamPublicaApi;
import com.senai.PI_mecado_preso.sales.api.dtos.*;
import com.senai.PI_mecado_preso.sales.internal.entity.Pedido;
import com.senai.PI_mecado_preso.sales.internal.entity.ItemPedido;
import com.senai.PI_mecado_preso.sales.internal.repository.PedidoRepository;
import com.senai.PI_mecado_preso.sales.internal.mapper.PedidoMapper;
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;
    private final IamPublicaApi iamPublicAPI;
    private final CatalogoPublicaAPI catalogoEstoqueAPI;

    public PedidoService(PedidoRepository pedidoRepository, PedidoMapper pedidoMapper, IamPublicaApi iamPublicAPI, CatalogoPublicaAPI catalogoEstoqueAPI) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
        this.iamPublicAPI = iamPublicAPI;
        this.catalogoEstoqueAPI = catalogoEstoqueAPI;
    }

    @Transactional
    public PedidoCriadoResponseDTO criarPedido(PedidoRequestDTO request) {
        ResultadoPadrao<?> validacaoIam = iamPublicAPI.validarUsuario(request.clienteId());
        if (!validacaoIam.isValid()) {
            throw new RuntimeException("Falha no checkout: " + validacaoIam.failureReason());
        }

        Pedido pedido = new Pedido();
        pedido.setClienteId(request.clienteId());
        pedido.setStatus("PENDENTE");

        BigDecimal valorTotalPedido = BigDecimal.ZERO;

        for (ItemPedidoRequestDTO itemDto : request.itens()) {

            ResultadoPadrao<Boolean> validacaoEstoque = catalogoEstoqueAPI.verificarEstoque(
                    itemDto.variacaoId(),
                    itemDto.quantidade()
            );

            if (!validacaoEstoque.isValid()) {
                throw new RuntimeException("Falha no checkout para o item " + itemDto.variacaoId() + ": " + validacaoEstoque.failureReason());
            }

            ResultadoPadrao<?> baixaEstoque = catalogoEstoqueAPI.baixarEstoque(itemDto.variacaoId(), itemDto.quantidade());
            if (!baixaEstoque.isValid()) {
                throw new RuntimeException("Erro ao deduzir estoque: " + baixaEstoque.failureReason());
            }

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setVariacaoId(itemDto.variacaoId());
            itemPedido.setQuantidade(itemDto.quantidade());
            itemPedido.setPrecoUnitario(itemDto.precoUnitario());

            pedido.adicionarItem(itemPedido);

            BigDecimal subtotalItem = itemDto.precoUnitario().multiply(BigDecimal.valueOf(itemDto.quantidade()));
            valorTotalPedido = valorTotalPedido.add(subtotalItem);
        }

        pedido.setValorTotal(valorTotalPedido);
        pedido = pedidoRepository.save(pedido);

        // 5. TODO: Disparar PedidoCriadoEvent via ApplicationEventPublisher (Passo 3 do gráfico)
        // exemplo: eventPublisher.publishEvent(new PedidoCriadoEvent(pedido.getId(), pedido.getClienteId(), pedido.getValorTotal()));

        return pedidoMapper.toCriadoResponse(pedido);
    }

    public List<PedidoDetalhadoResponseDTO> listarTodos() {
        List<Pedido> pedidos = pedidoRepository.findAll();

        // Para cada pedido:
        // 1. Busca os dados do cliente no IAM usando o clienteId
        // 2. Busca os dados das variações envolvidas no Catalog
        // 3. Monta e mapeia para PedidoDetalhadoResponseDTO

        return List.of();
    }
}
