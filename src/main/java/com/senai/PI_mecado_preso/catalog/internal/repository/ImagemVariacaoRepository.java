package com.senai.PI_mecado_preso.catalog.internal.repository;

import com.senai.PI_mecado_preso.catalog.internal.entity.ImagemVariacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImagemVariacaoRepository extends JpaRepository<ImagemVariacao, UUID> {
}
