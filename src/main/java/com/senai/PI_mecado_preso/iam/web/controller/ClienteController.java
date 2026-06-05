/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.web.controller;

import com.senai.PI_mecado_preso.iam.api.dtos.ClienteRequestDTO;
import com.senai.PI_mecado_preso.iam.api.dtos.ClienteResponseDTO;
import com.senai.PI_mecado_preso.iam.internal.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> registrarCliente(@Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable UUID id, @Valid @RequestBody ClienteRequestDTO request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @PatchMapping("/{id}/delete")
    public ResponseEntity<Void> alterarStatus(@PathVariable UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}