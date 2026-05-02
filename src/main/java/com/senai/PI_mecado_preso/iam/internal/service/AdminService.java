/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.internal.service;

import com.senai.PI_mecado_preso.iam.api.dtos.AdminRequestDTO;
import com.senai.PI_mecado_preso.iam.api.dtos.AdminResponseDTO;
import com.senai.PI_mecado_preso.iam.internal.entity.Admin;
import com.senai.PI_mecado_preso.iam.internal.mapper.AdminMapper;
import com.senai.PI_mecado_preso.iam.internal.repository.AdminRepository;
import com.senai.PI_mecado_preso.iam.internal.repository.UsuarioRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Cansei2
 */

@Service
public class AdminService {
    
    
    private final AdminRepository repositoryAdmin;
    private final UsuarioRepository repositoryUsuario;
    private final AdminMapper mapper;

    public AdminService(AdminRepository repository, UsuarioRepository repositoryUsuario, AdminMapper mapper) {
        this.repositoryAdmin = repository;
        this.repositoryUsuario = repositoryUsuario;
        this.mapper = mapper;
    }
    
    @Transactional(readOnly = true)
    public List<AdminResponseDTO>listarTodosAdmin(){
        return repositoryAdmin.findAll().stream().map(mapper :: toResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Admin buscarAdminEntityPorId(UUID id) {
        return repositoryAdmin.findById(id)
                .orElseThrow(() -> new RuntimeException("nao encontrado"));
    }

    @Transactional(readOnly = true)
    public AdminResponseDTO buscarAdminPorId(UUID id) {
        return mapper.toResponse(buscarAdminEntityPorId(id));
    }
    
    @Transactional
    public AdminResponseDTO salvarAdmin(AdminRequestDTO request){
        Admin salvo = repositoryAdmin.save(mapper.toEntity(request));
        return mapper.toResponse(salvo);
    }
    
    @Transactional
    public AdminResponseDTO atualizarAdmin(UUID id, AdminRequestDTO request){
        Admin exitente = buscarAdminEntityPorId(id);
        
        mapper.updateEntityFromDto(request, exitente);
        
        repositoryAdmin.save(exitente);
        return mapper.toResponse(exitente);
    }
    
    @Transactional
    public void deletar(UUID id){
        repositoryAdmin.delete(buscarAdminEntityPorId(id));
    }
}

