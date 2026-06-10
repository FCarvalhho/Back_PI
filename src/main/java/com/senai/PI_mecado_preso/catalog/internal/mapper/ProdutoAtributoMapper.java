package com.senai.PI_mecado_preso.catalog.internal.mapper;

import com.senai.PI_mecado_preso.catalog.api.dtos.ProdutoAtributoResponseDTO;
import com.senai.PI_mecado_preso.catalog.internal.entity.ProdutoAtributo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProdutoAtributoMapper {

    @Mapping(source = "atributo.id", target = "atributoId")
    @Mapping(source = "atributo.nome", target = "atributoNome")
    ProdutoAtributoResponseDTO toResponse(ProdutoAtributo entity);
}
