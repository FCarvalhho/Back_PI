package com.senai.PI_mecado_preso.sales.web;

import com.senai.PI_mecado_preso.sales.api.dtos.CarrinhoResponseDTO;
import com.senai.PI_mecado_preso.sales.api.dtos.ItemCarrinhoRequestDTO;
import com.senai.PI_mecado_preso.sales.internal.service.CarrinhoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}