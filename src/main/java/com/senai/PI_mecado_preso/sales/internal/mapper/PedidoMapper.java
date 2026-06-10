/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.sales.internal.mapper;

import com.senai.PI_mecado_preso.sales.api.dtos.PedidoCriadoResponseDTO;
import com.senai.PI_mecado_preso.sales.api.dtos.PedidoDetalhadoResponseDTO;
import com.senai.PI_mecado_preso.sales.internal.entity.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 *
 * @author Cansei2
 */
@Mapper(componentModel = "spring", uses = {ItemPedidoMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PedidoMapper {

    PedidoCriadoResponseDTO toCriadoResponse(Pedido entity);

    @Mapping(target = "cliente", ignore = true)
    PedidoDetalhadoResponseDTO toDetalhadoResponse(Pedido entity);
}
