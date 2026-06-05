package com.senai.PI_mecado_preso.sales.internal.mapper;

import com.senai.PI_mecado_preso.sales.api.dtos.CarrinhoResponseDTO;
import com.senai.PI_mecado_preso.sales.api.dtos.ItemCarrinhoResponseDTO;
import com.senai.PI_mecado_preso.sales.internal.entity.Carrinho;
import com.senai.PI_mecado_preso.sales.internal.entity.ItemCarrinho;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CarrinhoMapper {

    CarrinhoResponseDTO toResponse (Carrinho entity);

    ItemCarrinhoResponseDTO toResponseItem(ItemCarrinho entity);
}
