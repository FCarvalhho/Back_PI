package com.senai.PI_mecado_preso.iam.internal.repository;

import com.senai.PI_mecado_preso.iam.internal.entity.Cliente;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,UUID> {
    Optional<Cliente> findByCpf(String cpf);
}
