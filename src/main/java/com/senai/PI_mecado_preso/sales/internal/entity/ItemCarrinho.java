package com.senai.PI_mecado_preso.sales.internal.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "item_carrinho", schema = "sales")
public class ItemCarrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrinho_id", nullable = false)
    private Carrinho carrinho;

    @Column(name = "variacao_id", nullable = false)
    private UUID variacaoId;

    @Column(nullable = false)
    private int quantidade;

    public ItemCarrinho() {}

    public ItemCarrinho(Carrinho carrinho, UUID variacaoId, int quantidade) {
        this.carrinho = carrinho;
        this.variacaoId = variacaoId;
        this.quantidade = quantidade;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }

    public UUID getVariacaoId() {
        return variacaoId;
    }

    public void setVariacaoId(UUID variacaoId) {
        this.variacaoId = variacaoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
