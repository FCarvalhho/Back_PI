package com.senai.PI_mecado_preso.catalog.internal.mapper;

import com.senai.PI_mecado_preso.catalog.api.dtos.ProdutoRequestDTO;
import com.senai.PI_mecado_preso.catalog.api.dtos.ProdutoResponseDTO;
import com.senai.PI_mecado_preso.catalog.internal.entity.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ProdutoAtributoMapper.class, ProdutoVariacaoMapper.class})
public interface ProdutoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "variacoes", ignore = true)
    @Mapping(target = "atributos", ignore = true)
    Produto toEntity(ProdutoRequestDTO dto);

    @Mapping(target = "variacoes", source = "variacoes")
    ProdutoResponseDTO toResponse(Produto entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "variacoes", ignore = true)
    @Mapping(target = "atributos", ignore = true)
    void updateEntityFromDto(ProdutoRequestDTO dto, @MappingTarget Produto entity);
}