package com.senai.PI_mecado_preso.sales.internal.entity;

import com.senai.PI_mecado_preso.shared.exception.RegraDeNegocioException;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "carrinho", schema = "sales")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "cliente_id", nullable = false, unique = true)
    private UUID clienteId;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemCarrinho> itens = new HashSet<>();

    public void adicionarItem(UUID variacaoId, int quantidade) {
        this.atualizadoEm = LocalDateTime.now();
        for (ItemCarrinho item : itens) {
            if (item.getVariacaoId().equals(variacaoId)) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                return;
            }
        }
        ItemCarrinho novoItem = new ItemCarrinho(this, variacaoId, quantidade);
        this.itens.add(novoItem);
    }

    public void removerItem(UUID variacaoId) {
        this.atualizadoEm = LocalDateTime.now();

        boolean removido = this.itens.removeIf(item -> item.getVariacaoId().equals(variacaoId));

        if (!removido) {
            throw new com.senai.PI_mecado_preso.shared.exception.RegraDeNegocioException(
                    "Item não encontrado no carrinho para remoção."
            );
        }
    }

    public void limparItens() {
        this.atualizadoEm = LocalDateTime.now();
        this.itens.clear();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }

    public Set<ItemCarrinho> getItens() {
        return itens;
    }

    public void setItens(Set<ItemCarrinho> itens) {
        this.itens = itens;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}