package com.senai.PI_mecado_preso.catalog.internal.mapper;

import com.senai.PI_mecado_preso.catalog.api.dtos.VariacaoOpcaoResponseDTO;
import com.senai.PI_mecado_preso.catalog.internal.entity.VariacaoOpcao;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AtributoMapper.class})
public interface VariacaoOpcaoMapper {

    VariacaoOpcaoResponseDTO toResponse (VariacaoOpcao entity);
}
