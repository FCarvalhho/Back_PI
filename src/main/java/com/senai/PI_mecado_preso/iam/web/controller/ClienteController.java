/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.web.controller;

import com.senai.PI_mecado_preso.iam.api.dtos.ClienteRequestDTO;
import com.senai.PI_mecado_preso.iam.api.dtos.ClienteResponseDTO;
import com.senai.PI_mecado_preso.iam.internal.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Cansei2
 */
@RestController
@RequestMapping("/api/iam/cliente")
public class ClienteController {
    
    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }
    
    @PostMapping("/register/cliente")
    public ResponseEntity<ClienteResponseDTO> registrarCliente(@RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }
}