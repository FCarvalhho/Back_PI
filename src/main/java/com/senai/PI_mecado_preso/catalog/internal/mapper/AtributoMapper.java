package com.senai.PI_mecado_preso.catalog.internal.mapper;

import com.senai.PI_mecado_preso.catalog.api.dtos.AtributoRequestDTO;
import com.senai.PI_mecado_preso.catalog.api.dtos.AtributoResponseDTO;
import com.senai.PI_mecado_preso.catalog.internal.entity.Atributo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AtributoMapper {

    @Mapping(target = "id", ignore = true)
    Atributo toEntity (AtributoRequestDTO dto);

    AtributoResponseDTO toResponse (Atributo entity);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AtributoRequestDTO dto, @MappingTarget Atributo entity);

}
