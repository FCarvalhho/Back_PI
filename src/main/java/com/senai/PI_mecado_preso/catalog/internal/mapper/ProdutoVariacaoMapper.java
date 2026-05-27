package com.senai.PI_mecado_preso.catalog.internal.mapper;

import com.senai.PI_mecado_preso.catalog.api.dto.ProdutoVariacaoIDsDTO;
import com.senai.PI_mecado_preso.catalog.api.dto.ProdutoVariacaoRequestDTO;
import com.senai.PI_mecado_preso.catalog.api.dto.ProdutoVariacaoResponseDTO;
import com.senai.PI_mecado_preso.catalog.internal.entity.ProdutoVariacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ImagemVariacaoMapper.class, VariacaoOpcaoMapper.class})
public interface ProdutoVariacaoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "opcoes", ignore = true)
    ProdutoVariacao toEntity(ProdutoVariacaoRequestDTO dto);

    ProdutoVariacaoResponseDTO toResponse(ProdutoVariacao entity);

    ProdutoVariacaoIDsDTO toResponseIDs(ProdutoVariacao entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "opcoes", ignore = true)
    void updateEntityFromDto(ProdutoVariacaoRequestDTO dto, @MappingTarget ProdutoVariacao entity);

}
