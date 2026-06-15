package com.senai.PI_mecado_preso.sales.internal.mapper;

import com.senai.PI_mecado_preso.sales.api.dtos.ItemPedidoCriadoResponseDTO;
import com.senai.PI_mecado_preso.sales.api.dtos.ItemPedidoDetalhadoResponseDTO;
import com.senai.PI_mecado_preso.sales.api.dtos.ItemPedidoRequestDTO;
import com.senai.PI_mecado_preso.sales.internal.entity.ItemPedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import java.math.BigDecimal;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemPedidoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    @Mapping(target = "precoUnitario", ignore = true)
    ItemPedido toEntity(ItemPedidoRequestDTO dto);

    @Mapping(target = "subtotal", source = "entity", qualifiedByName = "calcularSubtotal")
    ItemPedidoCriadoResponseDTO toCriadoResponse(ItemPedido entity);

    @Mapping(target = "variacao", ignore = true)
    @Mapping(target = "subtotal", source = "entity", qualifiedByName = "calcularSubtotal")
    ItemPedidoDetalhadoResponseDTO toDetalhadoResponse(ItemPedido entity);

    @Named("calcularSubtotal")
    default BigDecimal verificarCalcularSubtotal(ItemPedido entity) {
        if (entity == null || entity.getPrecoUnitario() == null || entity.getQuantidade() == null) {
            return BigDecimal.ZERO;
        }
        return entity.getPrecoUnitario().multiply(BigDecimal.valueOf(entity.getQuantidade()));
    }
}