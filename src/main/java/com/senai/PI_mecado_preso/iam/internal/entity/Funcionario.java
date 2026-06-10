
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
@Table(name = "funcionario", schema = "iam")
public class Funcionario extends Usuario {

    @Column(nullable = false, unique = true)
    private String matricula;

    @Override
    public String getDocumentoExibicao() {
        return matricula;
    }

    @Override
    public String getTipoUsuario() {
        return "FUNCIONARIO";
    }

    public Funcionario() {
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

}
