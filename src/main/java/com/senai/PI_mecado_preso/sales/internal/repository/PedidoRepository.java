/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.sales.internal.repository;

import com.senai.PI_mecado_preso.sales.internal.entity.Pedido;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Cansei2
 */
public interface PedidoRepository extends JpaRepository<Pedido,UUID>{
    
}
