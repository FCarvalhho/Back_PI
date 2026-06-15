package com.senai.PI_mecado_preso.billing.internal.mapper;

import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {LocalDateTime.class})
public interface PagamentoMapper {
}