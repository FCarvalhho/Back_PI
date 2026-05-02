/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.sales.internal.mapper;

import com.senai.PI_mecado_preso.sales.api.dtos.ItemPedidoRequestDTO;
import com.senai.PI_mecado_preso.sales.api.dtos.ItemPedidoResponseDTO;
import com.senai.PI_mecado_preso.sales.internal.entity.ItemPedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 *
 * @author Cansei2
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemPedidoMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "variacaoId", ignore = true)
    @Mapping(target = "precoUnitario", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    ItemPedido toEntity(ItemPedidoRequestDTO dto);
    
    ItemPedidoResponseDTO toResponse(ItemPedido entity);
    
    void updateEntityFromDto(ItemPedidoRequestDTO dto, @MappingTarget ItemPedido entity);
}
