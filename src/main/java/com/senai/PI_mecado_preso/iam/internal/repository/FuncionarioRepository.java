/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.internal.repository;

import com.senai.PI_mecado_preso.iam.internal.entity.Funcionario;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Cansei2
 */
@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario,UUID> {
    Optional<Funcionario> findByMatricula(String matricula);
}
