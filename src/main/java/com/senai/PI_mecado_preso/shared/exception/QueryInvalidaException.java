package com.senai.PI_mecado_preso.shared.exception;

public class QueryInvalidaException extends RuntimeException {

    public QueryInvalidaException(String mensagem) {
        super(mensagem);
    }
}