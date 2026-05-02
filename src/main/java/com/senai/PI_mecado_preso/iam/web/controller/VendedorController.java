/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.web.controller;

import com.senai.PI_mecado_preso.iam.api.dtos.VendedorRequestDTO;
import com.senai.PI_mecado_preso.iam.api.dtos.VendedorResponseDTO;
import com.senai.PI_mecado_preso.iam.internal.service.VendedorService;
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
@RequestMapping("/api/iam/vendedor")
public class VendedorController {
    
    private final VendedorService service;

    public VendedorController(VendedorService service) {
        this.service = service;
    }
    
    @PostMapping("/register/vendedor")
    public ResponseEntity<VendedorResponseDTO> registrarVendedor(@RequestBody VendedorRequestDTO dto) {
        return ResponseEntity.ok(service.salvar(dto)); 
    }
}
