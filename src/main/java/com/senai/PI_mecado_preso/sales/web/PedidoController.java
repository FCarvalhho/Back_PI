package com.senai.PI_mecado_preso.sales.web;

import com.senai.PI_mecado_preso.sales.api.dtos.CheckoutResponseDTO;
import com.senai.PI_mecado_preso.sales.api.dtos.PedidoRequestDTO;
import com.senai.PI_mecado_preso.sales.internal.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
