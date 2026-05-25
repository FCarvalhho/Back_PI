package com.senai.PI_mecado_preso.catalog.web.controller;

import com.senai.PI_mecado_preso.catalog.api.dto.AtributoRequestDTO;
import com.senai.PI_mecado_preso.catalog.api.dto.AtributoResponseDTO;
import com.senai.PI_mecado_preso.catalog.internal.service.AtributoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/produto/atributo")
public class AtributoController {

    private final AtributoService service;

    public AtributoController(AtributoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AtributoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtributoResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AtributoResponseDTO> salvar(@Valid @RequestBody AtributoRequestDTO request) {
        AtributoResponseDTO response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtributoResponseDTO> atualizar(@PathVariable UUID id, @Valid @RequestBody AtributoRequestDTO request) {
        AtributoResponseDTO response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/delete")
    public ResponseEntity<Void> alterarStatus(@PathVariable @Valid UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
