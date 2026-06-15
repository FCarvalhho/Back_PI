package com.senai.PI_mecado_preso.catalog.internal.repository;

import com.senai.PI_mecado_preso.catalog.internal.entity.ProdutoVariacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProdutoVariacaoRepository
        extends JpaRepository<ProdutoVariacao, UUID> {

    @Query("""
        select distinct pv 
        from ProdutoVariacao pv
        join fetch pv.produto p
        left join fetch pv.opcoes o
        left join fetch o.atributo a
        where pv.id in :ids
        and p.ativo = true
    """)
    List<ProdutoVariacao> buscarVariacoesComDetalhes(Set<UUID> ids);
}
