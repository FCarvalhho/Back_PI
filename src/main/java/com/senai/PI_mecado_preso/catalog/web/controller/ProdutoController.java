package com.senai.PI_mecado_preso.catalog.web.controller;

import com.senai.PI_mecado_preso.catalog.api.dtos.ProdutoRequestDTO;
import com.senai.PI_mecado_preso.catalog.api.dtos.ProdutoResponseDTO;
import com.senai.PI_mecado_preso.catalog.internal.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/produto")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> salvar(@Valid @RequestBody ProdutoRequestDTO request) {
        ProdutoResponseDTO response = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable UUID id, @Valid @RequestBody ProdutoRequestDTO request) {
        ProdutoResponseDTO response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/delete")
    public ResponseEntity<Void> alterarStatus(@PathVariable UUID id) {
        var produto = service.buscarEntityPorId(id); 

        if (produto.getAtivo()) {
            service.inativar(id);
        } else {
            service.ativar(id);
        }

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable UUID id) {
        service.inativar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content (sucesso sem corpo)
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable UUID id) {
        service.ativar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}
