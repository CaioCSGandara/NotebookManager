package com.notebookmanager.infra.exceptionhandler;

import com.notebookmanager.infra.exception.RecursoJaExistenteException;
import com.notebookmanager.infra.exception.RecursoNaoEncontradoException;
import com.notebookmanager.infra.exception.ValidationException;
import com.notebookmanager.model.dto.Payload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {RecursoJaExistenteException.class})
    private ResponseEntity<Payload> recursoJaExistenteHandler(RecursoJaExistenteException exception) {
        Payload payload = new Payload(HttpStatus.CONFLICT, null, exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(payload);
    }

    @ExceptionHandler(value = {RecursoNaoEncontradoException.class})
    private ResponseEntity<Payload> recursoNaoEncontradoHandler(RecursoNaoEncontradoException exception) {
        Payload payload = new Payload(HttpStatus.NOT_FOUND, null, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(payload);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    private ResponseEntity<Payload> exceptionHandler(RuntimeException exception) {
        Payload payload = new Payload(HttpStatus.INTERNAL_SERVER_ERROR, null,"Ops! Ocorreu um erro interno no servidor.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(payload);
    }

    @ExceptionHandler(value = {ValidationException.class})
    private ResponseEntity<Payload> validationExceptionHandler(ValidationException exception) {
        Payload payload = new Payload(HttpStatus.BAD_REQUEST, null, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(payload);
    }




}
