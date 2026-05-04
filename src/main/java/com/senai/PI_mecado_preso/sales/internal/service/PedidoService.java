/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.sales.internal.service;

import com.senai.PI_mecado_preso.sales.api.dtos.ItemPedidoRequestDTO;
import com.senai.PI_mecado_preso.sales.api.dtos.PedidoCriadoEvent;
import com.senai.PI_mecado_preso.sales.api.dtos.PedidoResponseDTO;
import com.senai.PI_mecado_preso.sales.internal.entity.ItemPedido;
import com.senai.PI_mecado_preso.sales.internal.entity.Pedido;
import com.senai.PI_mecado_preso.sales.internal.repository.PedidoRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Cansei2
 */
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoService itemPedidoService;
    private final ApplicationEventPublisher eventPublisher;

    public PedidoService(PedidoRepository pedidoRepository, ItemPedidoService itemPedidoService, ApplicationEventPublisher eventPublisher) {
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoService = itemPedidoService;
        this.eventPublisher = eventPublisher;
    }

    /*
    @Transactional
    public PedidoResponseDTO realizarVenda(List<ItemPedidoRequestDTO> itensDTO, UUID clienteId) {
        // 1. Cria a base do pedido com a Soft FK do Cliente[cite: 4]
        Pedido pedido = new Pedido();
        pedido.setClienteId(clienteId); 
        pedido.setStatus("PROCESSANDO");

        // 2. Transforma os DTOs em entidades usando o serviço especialista
        List<ItemPedido> itens = itensDTO.stream()
            .map(dto -> itemPedidoService.criarEntidadeItem(dto, pedido))
            .toList();
        
        pedido.setItens(itens);
        
        // 3. Calcula o total (Regra de negócio do Pedido)
        BigDecimal total = itens.stream()
            .map(i -> i.getPrecoUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido.setValorTotal(total);

        // 4. Salva no banco (Sales Schema)[cite: 4]
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        // 5. DISPARO DE EVENTO (Outbox Pattern)
        // Isso vai para sua tabela event_publication para que o módulo 
        // de Catálogo saiba que precisa diminuir o estoque.
        eventPublisher.publishEvent(new PedidoCriadoEvent(pedidoSalvo.getId(),pedidoSalvo.getItens()));

        return converterParaDTO(pedidoSalvo);
    }
    */
}
