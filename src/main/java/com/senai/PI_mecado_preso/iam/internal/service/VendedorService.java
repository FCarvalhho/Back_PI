/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.internal.service;

import com.senai.PI_mecado_preso.iam.api.dtos.VendedorRequestDTO;
import com.senai.PI_mecado_preso.iam.api.dtos.VendedorResponseDTO;
import com.senai.PI_mecado_preso.iam.internal.entity.Vendedor;
import com.senai.PI_mecado_preso.iam.internal.mapper.VendedorMapper;
import com.senai.PI_mecado_preso.iam.internal.repository.VendedorRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Cansei2
 */

@Service
public class VendedorService {
    
    private final VendedorRepository repository;
    private final VendedorMapper mapper;

    public VendedorService(VendedorRepository repository, VendedorMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    @Transactional
    public VendedorResponseDTO salvar(VendedorRequestDTO request){
        Vendedor salvo = repository.save(mapper.toEntity(request));
        return mapper.toResponse(salvo);
    }
    
    @Transactional
    public VendedorResponseDTO atualizar(UUID id, VendedorRequestDTO request){
        Vendedor exitente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("nao encontrado"));
        
        mapper.updateEntityFromDto(request, exitente);
        
        repository.save(exitente);
        return mapper.toResponse(exitente);
    }
}

