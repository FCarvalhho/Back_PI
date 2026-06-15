package com.senai.PI_mecado_preso.iam.internal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

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
