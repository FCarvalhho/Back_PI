package com.senai.PI_mecado_preso.sales.internal.repository;

import com.senai.PI_mecado_preso.sales.internal.entity.Pedido;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido,UUID>{
    List<Pedido> findByClienteIdOrderByCriadoEmDesc(UUID clienteId);
}
