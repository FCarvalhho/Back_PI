package com.senai.PI_mecado_preso.catalog.web.controller;

import com.senai.PI_mecado_preso.catalog.api.dtos.ProdutoVariacaoRequestDTO;
import com.senai.PI_mecado_preso.catalog.api.dtos.ProdutoVariacaoResponseDTO;
import com.senai.PI_mecado_preso.catalog.api.dtos.ProdutoVariacaoUpdateDTO;
import com.senai.PI_mecado_preso.catalog.internal.service.ProdutoVariacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/produto/variacao")
public class ProdutoVariacaoController {

    private final ProdutoVariacaoService service;

    public ProdutoVariacaoController(ProdutoVariacaoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoVariacaoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoVariacaoResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ProdutoVariacaoResponseDTO> salvar(@PathVariable UUID id,@Valid @RequestBody ProdutoVariacaoRequestDTO request) {
        ProdutoVariacaoResponseDTO response = service.salvar(id,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoVariacaoResponseDTO> atualizar(@PathVariable UUID id, @Valid @RequestBody ProdutoVariacaoUpdateDTO request) {
        ProdutoVariacaoResponseDTO response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/delete")
    public ResponseEntity<Void> alterarStatus(@PathVariable @Valid UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
