package com.senai.PI_mecado_preso.catalog.internal.mapper;

import com.senai.PI_mecado_preso.catalog.api.dtos.ImagemVariacaoResponseDTO;
import com.senai.PI_mecado_preso.catalog.internal.entity.ImagemVariacao;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImagemVariacaoMapper {

    ImagemVariacaoResponseDTO toResponse (ImagemVariacao entity);
}
