/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.billing.internal.repository;

import com.senai.PI_mecado_preso.billing.internal.entity.Pagamento;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author a
 */
public interface PagamentoRepository extends JpaRepository<Pagamento,UUID>{

}