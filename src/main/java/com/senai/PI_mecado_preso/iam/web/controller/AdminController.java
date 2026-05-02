/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.web.controller;

import com.senai.PI_mecado_preso.iam.api.dtos.AdminRequestDTO;
import com.senai.PI_mecado_preso.iam.api.dtos.AdminResponseDTO;
import com.senai.PI_mecado_preso.iam.internal.service.AdminService;
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
@RequestMapping("/api/iam/admin")
public class AdminController {
    
    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }
    
    @PostMapping("/register/admin")
    public ResponseEntity<AdminResponseDTO> registrarAdmin(@RequestBody AdminRequestDTO dto) {
        return ResponseEntity.ok(service.salvarAdmin(dto));
    }
}
