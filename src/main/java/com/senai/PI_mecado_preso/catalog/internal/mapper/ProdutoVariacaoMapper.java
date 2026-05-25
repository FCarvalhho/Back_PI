package com.senai.PI_mecado_preso.catalog.internal.mapper;

import com.senai.PI_mecado_preso.catalog.api.dto.ProdutoVariacaoRequestDTO;
import com.senai.PI_mecado_preso.catalog.api.dto.ProdutoVariacaoResponseDTO;
import com.senai.PI_mecado_preso.catalog.internal.entity.ProdutoVariacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProdutoVariacaoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "opcoes", ignore = true)
    ProdutoVariacao toEntity(ProdutoVariacaoRequestDTO dto);

    ProdutoVariacaoResponseDTO toResponse(ProdutoVariacao entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "opcoes", ignore = true)
    void updateEntityFromDto(ProdutoVariacaoRequestDTO dto, @MappingTarget ProdutoVariacao entity);

}
