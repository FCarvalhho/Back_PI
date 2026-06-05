package com.senai.PI_mecado_preso.sales.internal.repository;

import com.senai.PI_mecado_preso.sales.internal.entity.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CarrinhoRepository extends JpaRepository<Carrinho, UUID> {
    Optional<Carrinho> findByClienteId(UUID clienteId);
}
