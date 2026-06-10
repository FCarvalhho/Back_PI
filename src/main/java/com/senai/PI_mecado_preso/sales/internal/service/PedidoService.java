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
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import com.senai.PI_mecado_preso.shared.exception.RegraDeNegocioException;
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

    public PedidoService(PedidoRepository pedidoRepository,
                         PedidoMapper pedidoMapper,
                         IamPublicaApi iamPublicAPI,
                         CatalogoPublicaAPI catalogoEstoqueAPI,
                         PagamentoPublicaAPI pagamentoPublicaAPI) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
        this.iamPublicAPI = iamPublicAPI;
        this.catalogoEstoqueAPI = catalogoEstoqueAPI;
        this.pagamentoPublicaAPI = pagamentoPublicaAPI;
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

    public List<PedidoDetalhadoResponseDTO> listarTodos() {
        List<Pedido> pedidos = pedidoRepository.findAll();

        // Para cada pedido:
        // 1. Busca os dados do cliente no IAM usando o clienteId
        // 2. Busca os dados das variações envolvidas no Catalog
        // 3. Monta e mapeia para PedidoDetalhadoResponseDTO

        return List.of();
    }
}
