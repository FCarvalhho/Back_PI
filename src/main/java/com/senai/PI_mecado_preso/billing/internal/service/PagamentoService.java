/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.billing.internal.service;

import com.senai.PI_mecado_preso.billing.api.dtos.PagamentoRequestDTO;
import com.senai.PI_mecado_preso.billing.api.dtos.PagamentoResponseDTO;
import com.senai.PI_mecado_preso.billing.internal.entity.Pagamento;
import com.senai.PI_mecado_preso.billing.internal.mapper.PagamentoMapper;
import com.senai.PI_mecado_preso.billing.internal.repository.PagamentoRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author a
 */
@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final PagamentoMapper pagamentoMapper;
    private final ApplicationEventPublisher eventPublisher;

    public PagamentoService(PagamentoRepository pagamentoRepository, PagamentoMapper pagamentoMapper, ApplicationEventPublisher eventPublisher) {
        this.pagamentoRepository = pagamentoRepository;
        this.pagamentoMapper = pagamentoMapper;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public List<PagamentoResponseDTO> listarTodos() {
        return pagamentoRepository.findAll().stream()
                .map(pagamentoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Pagamento buscarEntityPorId(UUID id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
    }

    @Transactional(readOnly = true)
    public PagamentoResponseDTO buscarPorId(UUID id) {
        return pagamentoMapper.toResponse(buscarEntityPorId(id));
    }

    @Transactional
    public PagamentoResponseDTO salvarPagamento(PagamentoRequestDTO request) {
        Pagamento pagamento = pagamentoMapper.toEntity(request);
        Pagamento salvo = pagamentoRepository.save(pagamento);

        // Dispara o evento para o Modulith (ex: Sales escutar e baixar estoque)
        eventPublisher.publishEvent(pagamentoMapper.toEvent(salvo));

        return pagamentoMapper.toResponse(salvo);
    }

    @Transactional
    public PagamentoResponseDTO atualizarPagamento(UUID id, PagamentoRequestDTO request) {
        Pagamento existente = buscarEntityPorId(id);
        pagamentoMapper.updateEntityFromDto(request, existente);

        Pagamento atualizado = pagamentoRepository.save(existente);

        // Se a atualização mudar o status, você pode disparar o evento novamente aqui
        eventPublisher.publishEvent(pagamentoMapper.toEvent(atualizado));

        return pagamentoMapper.toResponse(atualizado);
    }

    @Transactional
    public void deletar(UUID id) {
        pagamentoRepository.delete(buscarEntityPorId(id));
    }
}