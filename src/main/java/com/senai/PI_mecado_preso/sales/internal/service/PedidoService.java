package com.senai.PI_mecado_preso.sales.internal.service;

import com.senai.PI_mecado_preso.billing.api.CobrancaRequestDTO;
import com.senai.PI_mecado_preso.billing.api.CobrancaResponseDTO;
import com.senai.PI_mecado_preso.billing.api.PagamentoPublicaAPI;
import com.senai.PI_mecado_preso.catalog.api.CatalogoPublicaAPI;
import com.senai.PI_mecado_preso.iam.api.IamPublicaApi;
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
import java.util.List;
import java.util.UUID;

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

        ResultadoPadrao<?> validacaoIam = iamPublicAPI.validarUsuario(request.clienteId());
        if (!validacaoIam.isValid()) {
            throw new RegraDeNegocioException("Falha no checkout: " + validacaoIam.failureReason());
        }

        Pedido pedido = new Pedido();
        pedido.setClienteId(request.clienteId());
        pedido.setStatus("PENDENTE");

        BigDecimal valorTotalPedido = BigDecimal.ZERO;

        for (ItemPedidoRequestDTO itemDto : request.itens()) {

            ResultadoPadrao<Boolean> validacaoEstoque = catalogoEstoqueAPI.verificarEstoque(itemDto.variacaoId(), itemDto.quantidade());
            if (!validacaoEstoque.isValid()) {
                throw new RegraDeNegocioException("Falha no checkout para o item " + itemDto.variacaoId() + ": " + validacaoEstoque.failureReason());
            }

            ResultadoPadrao<BigDecimal> consultaPreco = catalogoEstoqueAPI.obterPreco(itemDto.variacaoId());
            if (!consultaPreco.isValid()) {
                throw new RegraDeNegocioException("Falha na validação de preços: " + consultaPreco.failureReason());
            }
            BigDecimal precoOficialServidor = consultaPreco.dado();

            if (itemDto.precoUnitario().compareTo(precoOficialServidor) != 0) {
                throw new RegraDeNegocioException("🚨 Segurança: Divergência de preço detectada para a variação " + itemDto.variacaoId()
                        + ". Valor enviado pelo cliente: R$ " + itemDto.precoUnitario()
                        + " | Valor oficial do servidor: R$ " + precoOficialServidor);
            }

            ResultadoPadrao<?> baixaEstoque = catalogoEstoqueAPI.baixarEstoque(itemDto.variacaoId(), itemDto.quantidade());
            if (!baixaEstoque.isValid()) {
                throw new RegraDeNegocioException("Erro ao deduzir estoque: " + baixaEstoque.failureReason());
            }

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setVariacaoId(itemDto.variacaoId());
            itemPedido.setQuantidade(itemDto.quantidade());
            itemPedido.setPrecoUnitario(precoOficialServidor);
            pedido.adicionarItem(itemPedido);

            BigDecimal subtotalItem = precoOficialServidor.multiply(BigDecimal.valueOf(itemDto.quantidade()));
            valorTotalPedido = valorTotalPedido.add(subtotalItem);
        }

        pedido.setValorTotal(valorTotalPedido);
        pedido = pedidoRepository.save(pedido);

        CobrancaRequestDTO cobrancaRequest = new CobrancaRequestDTO(
                pedido.getId(),
                pedido.getValorTotal(),
                request.metodoPagamento(),
                request.parcelas()
        );

        ResultadoPadrao<CobrancaResponseDTO> resultadoCobranca = pagamentoPublicaAPI.processarCobranca(cobrancaRequest).join();

        if (resultadoCobranca == null || !resultadoCobranca.isValid()) {
            throw new RegraDeNegocioException("Erro ao processar faturamento do pedido.");
        }

        CobrancaResponseDTO dadosCobranca = resultadoCobranca.dado();

        if ("PAGO".equals(dadosCobranca.statusSugerido())) {
            pedido.setStatus("APROVADO");
        } else if ("FALHADO".equals(dadosCobranca.statusSugerido())) {
            pedido.setStatus("CANCELADO");
        } else {
            pedido.setStatus("AGUARDANDO_PAGAMENTO");
        }

        pedido = pedidoRepository.saveAndFlush(pedido);

        PedidoCriadoResponseDTO pedidoResponse = pedidoMapper.toCriadoResponse(pedido);
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
