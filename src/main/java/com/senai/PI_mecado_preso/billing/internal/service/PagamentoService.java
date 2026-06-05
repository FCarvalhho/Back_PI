/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.billing.internal.service;

import com.senai.PI_mecado_preso.billing.api.PagamentoPublicoAPi;
import com.senai.PI_mecado_preso.billing.internal.entity.Pagamento;
import com.senai.PI_mecado_preso.billing.internal.mapper.PagamentoMapper;
import com.senai.PI_mecado_preso.billing.internal.repository.PagamentoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author a
 */
@Service
public class PagamentoService implements PagamentoPublicoAPi {

    private final PagamentoRepository pagamentoRepository;
    private final PagamentoMapper pagamentoMapper;
    private final ApplicationEventPublisher eventPublisher;

    public PagamentoService(PagamentoRepository pagamentoRepository, PagamentoMapper pagamentoMapper, ApplicationEventPublisher eventPublisher) {
        this.pagamentoRepository = pagamentoRepository;
        this.pagamentoMapper = pagamentoMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public ResultadoPadrao<Boolean> pagamentoPublico(UUID variacaoId, BigDecimal valor) {
        return null;
    }
}