package com.senai.PI_mecado_preso.iam.internal.repository;

import com.senai.PI_mecado_preso.iam.internal.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);

    @Query("""
    select u
    from Usuario u
    where u.id in :ids
      and u.ativo = true
    """)
    List<Usuario> buscarUsuarios(Set<UUID> ids);
}
