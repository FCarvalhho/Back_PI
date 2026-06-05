/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.catalog.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Cansei2
 */
@Entity
@Table(name = "produto_variacao", schema = "catalog")
public class ProdutoVariacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    @JsonBackReference
    private Produto produto;

    @Column(length = 100)
    private String sku;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    private Integer estoque = 0;

    @OneToMany(mappedBy = "variacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VariacaoOpcao> opcoes;

    @OneToMany(mappedBy = "variacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagemVariacao> imagens;

    public ProdutoVariacao() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }
    public List<VariacaoOpcao> getOpcoes() { return opcoes; }
    public void setOpcoes(List<VariacaoOpcao> opcoes) { this.opcoes = opcoes; }
    public List<ImagemVariacao> getImagens() { return imagens; }
    public void setImagens(List<ImagemVariacao> imagens) { this.imagens = imagens; }
}
