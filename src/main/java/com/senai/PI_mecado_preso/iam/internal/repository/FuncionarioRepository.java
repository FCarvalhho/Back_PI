package com.senai.PI_mecado_preso.iam.internal.repository;

import com.senai.PI_mecado_preso.iam.internal.entity.Funcionario;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario,UUID> {
    Optional<Funcionario> findByMatricula(String matricula);
}
