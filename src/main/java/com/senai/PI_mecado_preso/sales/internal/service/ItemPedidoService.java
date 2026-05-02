/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.sales.internal.service;

import com.senai.PI_mecado_preso.sales.api.dtos.ItemPedidoRequestDTO;
import com.senai.PI_mecado_preso.sales.internal.entity.ItemPedido;
import com.senai.PI_mecado_preso.sales.internal.entity.Pedido;
import org.springframework.stereotype.Service;

/**
 *
 * @author Cansei2
 */
@Service
public class ItemPedidoService {
    
    // Interface pública do módulo Catalog (não o repository internal)
    /*private final CatalogAPI catalogAPI; 

    public ItemPedido criarEntidadeItem(ItemPedidoRequestDTO dto, Pedido pedido) {
        // Validação da Soft FK: O catálogo confirma se esse ID é válido
        var infoCatalogo = catalogAPI.buscarPrecoEEstoque(dto.variacaoId());
        
        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setVariacaoId(dto.variacaoId()); // Atribuição da Soft FK[cite: 3]
        item.setQuantidade(dto.quantidade());
        
        // Regra de Ouro: O preço unitário vem do Catálogo, mas morre no Pedido
        item.setPrecoUnitario(infoCatalogo.getPreco()); 
        
        return item;
    }*/
    
}
