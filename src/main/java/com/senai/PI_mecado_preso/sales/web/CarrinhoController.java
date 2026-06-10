package com.senai.PI_mecado_preso.sales.web;

import com.senai.PI_mecado_preso.sales.api.dtos.CarrinhoResponseDTO;
import com.senai.PI_mecado_preso.sales.api.dtos.ItemCarrinhoRequestDTO;
import com.senai.PI_mecado_preso.sales.internal.service.CarrinhoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos/carrinho")
public class CarrinhoController {

    private final CarrinhoService service;

    public CarrinhoController(CarrinhoService service) {
        this.service = service;
    }

    @PostMapping("/itens")
    public ResponseEntity<Void> adicionarItem(@Valid @RequestBody ItemCarrinhoRequestDTO request) {
        service.adicionarProdutoAoCarrinho(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<CarrinhoResponseDTO> obterCarrinho() {
        CarrinhoResponseDTO carrinho = service.mostrarCarrinho();
        return ResponseEntity.ok(carrinho);
    }

    @DeleteMapping("/itens/{variacaoId}")
    public ResponseEntity<Void> removerItem(@PathVariable UUID variacaoId) {
        service.removerItemDoCarrinho(variacaoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> limparCarrinho() {
        service.limparCarrinho();
        return ResponseEntity.noContent().build();
    }
}