package com.senai.PI_mecado_preso.catalog.internal.repository;

import com.senai.PI_mecado_preso.catalog.api.DetalheItemCatalogoDTO;
import com.senai.PI_mecado_preso.catalog.internal.entity.ProdutoVariacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProdutoVariacaoRepository
        extends JpaRepository<ProdutoVariacao, UUID> {

    @Query("""
        select new com.senai.PI_mecado_preso.catalog.api.DetalheItemCatalogoDTO(
            pv.id,
            p.nome,
            pv.preco,
            pv.estoque
        )
        from ProdutoVariacao pv
        join pv.produto p
        where pv.id in :ids
        and p.ativo = true
    """)
    List<DetalheItemCatalogoDTO> buscarDetalhes(Set<UUID> ids);
}
