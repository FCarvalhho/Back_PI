/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.internal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 *
 * @author Cansei2
 */
@Entity
@Table(name = "vendedor", schema = "iam")
public class Vendedor extends Usuario {
    
    @Column(nullable = false, unique = true)
    private String telefone;

    @Column(nullable = false, unique = true, length = 18) // Ex: 00.000.000/0000-00
    private String cnpj;

    @Column(name = "nome_responsavel", nullable = false)
    private String nomeResponsavel;

    @Column(name = "email_responsavel", nullable = false)
    private String emailResponsavel;

    @Column(name = "telefone_responsavel", nullable = false)
    private String telefoneResponsavel;

    public Vendedor() {
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getEmailResponsavel() {
        return emailResponsavel;
    }

    public void setEmailResponsavel(String emailResponsavel) {
        this.emailResponsavel = emailResponsavel;
    }

    public String getTelefoneResponsavel() {
        return telefoneResponsavel;
    }

    public void setTelefoneResponsavel(String telefoneResponsavel) {
        this.telefoneResponsavel = telefoneResponsavel;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }  
}
