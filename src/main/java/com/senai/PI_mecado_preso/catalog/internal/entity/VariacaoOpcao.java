/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.catalog.internal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 *
 * @author Cansei2
 */
@Entity
@Table(name = "variacao_opcao", schema = "catalog")
public class VariacaoOpcao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "variacao_id")
    private ProdutoVariacao variacao;

    @ManyToOne
    @JoinColumn(name = "atributo_id")
    private Atributo atributo;

    @Column(nullable = false)
    private String valor;

    public VariacaoOpcao() {}

    public UUID getId() {return id;}
    public void setId(UUID id) {this.id = id;}
    public ProdutoVariacao getVariacao() {return variacao;}
    public void setVariacao(ProdutoVariacao variacao) {this.variacao = variacao;}
    public Atributo getAtributo() {return atributo;}
    public void setAtributo(Atributo atributo) {this.atributo = atributo;}
    public String getValor() {return valor;}
    public void setValor(String valor) {this.valor = valor;}
}