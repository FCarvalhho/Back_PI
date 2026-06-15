package com.senai.PI_mecado_preso.catalog.internal.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "produto_atributo", schema = "catalog")
public class ProdutoAtributo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "atributo_id")
    private Atributo atributo;

    public ProdutoAtributo() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Atributo getAtributo() {
        return atributo;
    }

    public void setAtributo(Atributo atributo) {
        this.atributo = atributo;
    }
    
    
}
