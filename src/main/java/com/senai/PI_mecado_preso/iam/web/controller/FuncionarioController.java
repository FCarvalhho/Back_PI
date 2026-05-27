/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.web.controller;

import com.senai.PI_mecado_preso.iam.api.dtos.FuncionarioRequestDTO;
import com.senai.PI_mecado_preso.iam.api.dtos.FuncionarioResponseDTO;
import com.senai.PI_mecado_preso.iam.internal.service.FuncionarioService;
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
@RequestMapping("/api/iam/funcionario")
public class FuncionarioController {
    
    private final FuncionarioService service;

    public FuncionarioController(FuncionarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<FuncionarioResponseDTO> registrarFuncionario(@RequestBody FuncionarioRequestDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> atualizar(@PathVariable UUID id, @Valid @RequestBody FuncionarioRequestDTO request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @PatchMapping("/{id}/delete")
    public ResponseEntity<Void> alterarStatus(@PathVariable @Valid UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
