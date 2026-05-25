package com.senai.PI_mecado_preso.catalog.internal.mapper;

import com.senai.PI_mecado_preso.catalog.api.dto.ProdutoRequestDTO;
import com.senai.PI_mecado_preso.catalog.api.dto.ProdutoResponseDTO;
import com.senai.PI_mecado_preso.catalog.internal.entity.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProdutoMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "variacoes", ignore = true)
    @Mapping(target = "atributos", ignore = true)
    Produto toEntity(ProdutoRequestDTO dto);

    ProdutoResponseDTO toResponse(Produto entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "variacoes", ignore = true)
    @Mapping(target = "atributos", ignore = true)
    @Mapping(target = "vendedorId", ignore = true)
    void updateEntityFromDto(ProdutoRequestDTO dto, @MappingTarget Produto entity);
}