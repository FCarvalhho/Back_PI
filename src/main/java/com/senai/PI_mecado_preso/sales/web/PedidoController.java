package com.senai.PI_mecado_preso.sales.web;

import com.senai.PI_mecado_preso.sales.api.dtos.CheckoutResponseDTO;
import com.senai.PI_mecado_preso.sales.api.dtos.PedidoDetalhadoResponseDTO;
import com.senai.PI_mecado_preso.sales.api.dtos.PedidoRequestDTO;
import com.senai.PI_mecado_preso.sales.internal.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CheckoutResponseDTO> processarPedido(@Valid @RequestBody PedidoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarPedido(request));
    }

    @GetMapping
    public ResponseEntity<List<PedidoDetalhadoResponseDTO>> obterTodosPedidos() {
        List<PedidoDetalhadoResponseDTO> pedidos = service.listarTodos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/meus")
    public ResponseEntity<List<PedidoDetalhadoResponseDTO>> obterMeusPedidos() {
        List<PedidoDetalhadoResponseDTO> meusPedidos = service.listarPedidosDoClienteLogado();
        return ResponseEntity.ok(meusPedidos);
    }
}
