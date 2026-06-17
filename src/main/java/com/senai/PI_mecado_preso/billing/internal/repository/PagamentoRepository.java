package com.senai.PI_mecado_preso.billing.internal.repository;

import com.senai.PI_mecado_preso.billing.internal.entity.Pagamento;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento,UUID>{

}