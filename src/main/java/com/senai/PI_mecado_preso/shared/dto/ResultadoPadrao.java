package com.senai.PI_mecado_preso.shared.dto;

public record ResultadoPadrao<T>(
        boolean isValid,
        String failureReason,
        T dado
) {
    public static <T> ResultadoPadrao<T> success() {
        return new ResultadoPadrao<>(true, null, null);
    }

    public static <T> ResultadoPadrao<T> success(T dado) {
        return new ResultadoPadrao<>(true, null, dado);
    }

    public static <T> ResultadoPadrao<T> failure(String reason) {
        return new ResultadoPadrao<>(false, reason, null);
    }
}
