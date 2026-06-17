package com.senai.PI_mecado_preso.shared.exception;

public class NaoAutenticadoException extends RuntimeException {

    public NaoAutenticadoException(String mensagem) {
        super(mensagem);
    }
}