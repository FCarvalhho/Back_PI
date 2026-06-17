package com.senai.PI_mecado_preso.catalog.internal.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "produto", schema = "catalog")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private Boolean ativo = true;

    @CreationTimestamp
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoVariacao> variacoes;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoAtributo> atributos;

    public Produto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public List<ProdutoVariacao> getVariacoes() {
        return variacoes;
    }

    public void setVariacoes(List<ProdutoVariacao> variacoes) {
        this.variacoes = variacoes;
    }

    public List<ProdutoAtributo> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<ProdutoAtributo> atributos) {
        this.atributos = atributos;
    }
    
}
