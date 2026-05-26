/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.billing.internal.mapper;

import com.senai.PI_mecado_preso.billing.api.dtos.PagamentoEvent;
import com.senai.PI_mecado_preso.billing.api.dtos.PagamentoRequestDTO;
import com.senai.PI_mecado_preso.billing.api.dtos.PagamentoResponseDTO;
import com.senai.PI_mecado_preso.billing.internal.entity.Pagamento;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 *
 * @author a
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {LocalDateTime.class})
public interface PagamentoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDENTE")
    @Mapping(target = "pagoEm", ignore = true)
    Pagamento toEntity(PagamentoRequestDTO dto);

    PagamentoResponseDTO toResponse(Pagamento pagamento);

    @Mapping(target = "dataOcorrencia", expression = "java(LocalDateTime.now())")
    PagamentoEvent toEvent(Pagamento pagamento);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedidoId", ignore = true)
    void updateEntityFromDto(PagamentoRequestDTO dto, @MappingTarget Pagamento pagamento);
}