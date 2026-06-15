package com.senai.PI_mecado_preso.sales.internal.service;

import com.senai.PI_mecado_preso.billing.api.CobrancaRequestDTO;
import com.senai.PI_mecado_preso.billing.api.CobrancaResponseDTO;
import com.senai.PI_mecado_preso.billing.api.PagamentoPublicaAPI;
import com.senai.PI_mecado_preso.catalog.api.*;
import com.senai.PI_mecado_preso.iam.api.IamPublicaApi;
import com.senai.PI_mecado_preso.iam.api.PedidoUsuarioDTO;
import com.senai.PI_mecado_preso.sales.api.dtos.*;
import com.senai.PI_mecado_preso.sales.internal.entity.ItemPedido;
import com.senai.PI_mecado_preso.sales.internal.entity.Pedido;
import com.senai.PI_mecado_preso.sales.internal.mapper.PedidoMapper;
import com.senai.PI_mecado_preso.sales.internal.repository.PedidoRepository;
import com.senai.PI_mecado_preso.shared.config.security.UsuarioLogadoDTO;
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import com.senai.PI_mecado_preso.shared.exception.RegraDeNegocioException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;
    private final IamPublicaApi iamPublicAPI;
    private final CatalogoPublicaAPI catalogoEstoqueAPI;
    private final PagamentoPublicaAPI pagamentoPublicaAPI;
    private final CarrinhoService carrinhoService;

    public PedidoService(PedidoRepository pedidoRepository,
                         PedidoMapper pedidoMapper,
                         IamPublicaApi iamPublicAPI,
                         CatalogoPublicaAPI catalogoEstoqueAPI,
                         PagamentoPublicaAPI pagamentoPublicaAPI, CarrinhoService carrinhoService) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
        this.iamPublicAPI = iamPublicAPI;
        this.catalogoEstoqueAPI = catalogoEstoqueAPI;
        this.pagamentoPublicaAPI = pagamentoPublicaAPI;
        this.carrinhoService = carrinhoService;
    }

    @Transactional
    public CheckoutResponseDTO criarPedido(PedidoRequestDTO request) {

        ResultadoPadrao<PedidoUsuarioDTO> resultadoUsuario =
                iamPublicAPI.obterUsuario(request.clienteId());

        if (!resultadoUsuario.isValid()) {
            throw new RegraDeNegocioException(
                    "Falha no checkout: "
                            + resultadoUsuario.failureReason()
            );
        }

        Map<UUID, ItemValidacaoRequestDTO> itensValidacao =
                request.itens()
                        .stream()
                        .collect(Collectors.toMap(
                                ItemPedidoRequestDTO::variacaoId,
                                item -> new ItemValidacaoRequestDTO(
                                        item.quantidade(),
                                        item.precoUnitario()
                                )
                        ));

        ResultadoPadrao<ValidacaoProdutosDTO> validacaoProdutos =
                catalogoEstoqueAPI.validarProdutos(itensValidacao);

        if (!validacaoProdutos.isValid()) {
            throw new RegraDeNegocioException(
                    validacaoProdutos.failureReason()
            );
        }

        ValidacaoProdutosDTO dadosProdutos =
                validacaoProdutos.dado();

        Pedido pedido = new Pedido();
        pedido.setClienteId(request.clienteId());
        pedido.setStatus("PENDENTE");
        pedido.setValorTotal(dadosProdutos.valorTotal());

        Map<UUID, Integer> itensParaBaixa = new HashMap<>();

        for (ItemValidadoDTO itemValidado :
                dadosProdutos.itens().values()) {

            ItemPedido itemPedido = new ItemPedido();

            itemPedido.setVariacaoId(
                    itemValidado.variacaoId()
            );

            itemPedido.setQuantidade(
                    itemValidado.quantidade()
            );

            itemPedido.setPrecoUnitario(
                    itemValidado.precoAtual()
            );

            pedido.adicionarItem(itemPedido);

            itensParaBaixa.merge(
                    itemValidado.variacaoId(),
                    itemValidado.quantidade(),
                    Integer::sum
            );
        }

        ResultadoPadrao<?> baixaEstoque =
                catalogoEstoqueAPI.baixarEstoque(itensParaBaixa);

        if (!baixaEstoque.isValid()) {
            throw new RegraDeNegocioException(
                    "Erro ao baixar estoque: "
                            + baixaEstoque.failureReason()
            );
        }

        pedido = pedidoRepository.save(pedido);

        CobrancaRequestDTO cobrancaRequest =
                new CobrancaRequestDTO(
                        pedido.getId(),
                        pedido.getValorTotal(),
                        request.metodoPagamento(),
                        request.parcelas()
                );

        ResultadoPadrao<CobrancaResponseDTO> resultadoCobranca =
                pagamentoPublicaAPI
                        .processarCobranca(cobrancaRequest)
                        .join();

        if (resultadoCobranca == null
                || !resultadoCobranca.isValid()) {

            throw new RegraDeNegocioException(
                    "Erro ao processar faturamento do pedido."
            );
        }

        CobrancaResponseDTO dadosCobranca =
                resultadoCobranca.dado();

        if ("PAGO".equals(dadosCobranca.statusSugerido())) {

            pedido.setStatus("APROVADO");

        } else if ("FALHADO".equals(
                dadosCobranca.statusSugerido())) {

            pedido.setStatus("CANCELADO");

        } else {

            pedido.setStatus("AGUARDANDO_PAGAMENTO");
        }

        pedido = pedidoRepository.saveAndFlush(pedido);

        Set<UUID> variacoesCompradasIds = request.itens()
                .stream()
                .map(ItemPedidoRequestDTO::variacaoId)
                .collect(Collectors.toSet());

        this.carrinhoService.removerItensCompradosDoCarrinho(request.clienteId(), variacoesCompradasIds);

        PedidoCriadoResponseDTO pedidoResponse =
                pedidoMapper.toCriadoResponse(pedido);

        return new CheckoutResponseDTO(
                pedidoResponse,
                dadosCobranca.processadoSincronamente(),
                dadosCobranca.statusSugerido(),
                dadosCobranca.mensagem(),
                dadosCobranca.pixCopiaECola(),
                dadosCobranca.linhaDigitavel()
        );
    }

    @Transactional(readOnly = true)
    public List<PedidoDetalhadoResponseDTO> listarTodos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return enriquecerEConverterPedidos(pedidos);
    }

    @Transactional(readOnly = true)
    public List<PedidoDetalhadoResponseDTO> listarPedidosDoClienteLogado() {
        UUID clienteId = obterClienteIdLogado();
        List<Pedido> pedidos = pedidoRepository.findByClienteIdOrderByCriadoEmDesc(clienteId);
        return enriquecerEConverterPedidos(pedidos);
    }

    private List<PedidoDetalhadoResponseDTO> enriquecerEConverterPedidos(List<Pedido> pedidos) {
        if (pedidos.isEmpty()) {
            return List.of();
        }

        Set<UUID> clientesIds = pedidos.stream()
                .map(Pedido::getClienteId)
                .collect(Collectors.toSet());

        Set<UUID> variacoesIds = pedidos.stream()
                .flatMap(p -> p.getItens().stream())
                .map(ItemPedido::getVariacaoId)
                .collect(Collectors.toSet());

        ResultadoPadrao<Map<UUID, PedidoUsuarioDTO>> resultadoUsuarios = iamPublicAPI.obterUsuarios(clientesIds);
        Map<UUID, PedidoUsuarioDTO> mapaUsuarios = resultadoUsuarios.isValid() ? resultadoUsuarios.dado() : Map.of();

        ResultadoPadrao<Map<UUID, DetalheItemCatalogoDTO>> resultadoItens = catalogoEstoqueAPI.obterItens(variacoesIds);
        Map<UUID, DetalheItemCatalogoDTO> mapaItens = resultadoItens.isValid() ? resultadoItens.dado() : Map.of();

        return pedidos.stream().map(pedido -> {
            PedidoUsuarioDTO clienteDto = mapaUsuarios.getOrDefault(pedido.getClienteId(),
                    new PedidoUsuarioDTO(pedido.getClienteId(), "Usuário Desconhecido", "", "", "", false));

            List<ItemPedidoDetalhadoResponseDTO> itensDtos = pedido.getItens().stream().map(item -> {
                DetalheItemCatalogoDTO detalheCatalogo = mapaItens.get(item.getVariacaoId());

                PedidoVariacaoExibicaoDTO variacaoExibicao = null;
                if (detalheCatalogo != null) {
                    variacaoExibicao = new PedidoVariacaoExibicaoDTO(
                            item.getVariacaoId(),
                            detalheCatalogo.nomeProduto(),
                            detalheCatalogo.sku(),
                            detalheCatalogo.detalhes() != null ? detalheCatalogo.detalhes() : "Variação Padrão"
                    );
                }

                BigDecimal subtotalCalculado = item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()));

                return new ItemPedidoDetalhadoResponseDTO(
                        item.getId(),
                        variacaoExibicao,
                        item.getQuantidade(),
                        item.getPrecoUnitario(),
                        subtotalCalculado
                );
            }).collect(Collectors.toList());

            return new PedidoDetalhadoResponseDTO(
                    pedido.getId(),
                    clienteDto,
                    pedido.getStatus(),
                    pedido.getValorTotal(),
                    pedido.getCriadoEm(),
                    itensDtos
            );
        }).collect(Collectors.toList());
    }

    private UUID obterClienteIdLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UsuarioLogadoDTO principal)) {
            throw new RegraDeNegocioException("Usuário não autenticado.");
        }
        return principal.getId();
    }
}
