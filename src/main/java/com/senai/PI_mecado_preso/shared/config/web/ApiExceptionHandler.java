/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.shared.config.web;

/**
 *
 * @author GabrielB
 */
import com.senai.PI_mecado_preso.shared.dto.ApiErrorResponse;
import com.senai.PI_mecado_preso.shared.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice // Substitui o @ControllerAdvice + @ResponseBody de forma elegante
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    // 404 Not Found
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiErrorResponse> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiErrorResponse error = new ApiErrorResponse(
                status.value(),
                "Recurso Não Encontrado",
                ex.getMessage(),
                OffsetDateTime.now(),
                List.of()
        );
        return ResponseEntity.status(status).body(error);
    }

    // 401 Unauthorized (Usuário não autenticado)
    @ExceptionHandler(NaoAutenticadoException.class)
    public ResponseEntity<ApiErrorResponse> handleNaoAutenticado(
            NaoAutenticadoException ex) {

        HttpStatus status = HttpStatus.UNAUTHORIZED;

        ApiErrorResponse error = new ApiErrorResponse(
                status.value(),
                "Não Autenticado",
                ex.getMessage(),
                OffsetDateTime.now(),
                List.of()
        );

        return ResponseEntity.status(status).body(error);
    }

    // 403 Forbidden (Usuário autenticado sem permissão)
    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<ApiErrorResponse> handleAcessoNegado(
            AcessoNegadoException ex) {

        HttpStatus status = HttpStatus.FORBIDDEN;

        ApiErrorResponse error = new ApiErrorResponse(
                status.value(),
                "Acesso Negado",
                ex.getMessage(),
                OffsetDateTime.now(),
                List.of()
        );

        return ResponseEntity.status(status).body(error);
    }

    // 400 Bad Request (Regras de Negócio Gerais)
    @ExceptionHandler({RegraDeNegocioException.class, QueryInvalidaException.class})
    public ResponseEntity<ApiErrorResponse> handleRegraDeNegocio(RuntimeException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiErrorResponse error = new ApiErrorResponse(
                status.value(),
                "Regra de Negócio Violada",
                ex.getMessage(),
                OffsetDateTime.now(),
                List.of()
        );
        return ResponseEntity.status(status).body(error);
    }

    // 409 Conflict (Emails repetidos, chaves duplicadas no banco)
    @ExceptionHandler({ConflitoException.class, EntidadeEmUsoException.class})
    public ResponseEntity<ApiErrorResponse> handleConflito(RuntimeException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        ApiErrorResponse error = new ApiErrorResponse(
                status.value(),
                "Conflito de Estado",
                ex.getMessage(),
                OffsetDateTime.now(),
                List.of()
        );
        return ResponseEntity.status(status).body(error);
    }

    // 422 Unprocessable Entity (Erros semânticos na validação de dados)
    @ExceptionHandler(ErroValidacaoException.class)
    public ResponseEntity<ApiErrorResponse> handleErroValidacao(ErroValidacaoException ex) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ApiErrorResponse error = new ApiErrorResponse(
                status.value(),
                "Entidade Não Processável",
                ex.getMessage(),
                OffsetDateTime.now(),
                List.of()
        );
        return ResponseEntity.status(status).body(error);
    }

    // Captura automática de falhas do @Valid (Beans Validation) nos Controllers
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<ApiErrorResponse.CampoErro> camposComErro = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ApiErrorResponse.CampoErro(
                fieldError.getField(),
                fieldError.getDefaultMessage()
        ))
                .toList();

        ApiErrorResponse error = new ApiErrorResponse(
                status.value(),
                "Erro de Validação de Dados",
                "Um ou mais campos estão inválidos. Faça a correção e tente novamente.",
                OffsetDateTime.now(),
                camposComErro
        );

        return ResponseEntity.status(status).body(error);
    }

    // Captura genérica (Fallback) para evitar vazamento de Stack Trace interna em produção (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUncaughtException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiErrorResponse error = new ApiErrorResponse(
                status.value(),
                "Erro Interno no Servidor",
                "Ocorreu um erro inesperado no sistema. Por favor, tente novamente mais tarde.",
                OffsetDateTime.now(),
                List.of()
        );
        return ResponseEntity.status(status).body(error);
    }
}
