package com.notebookmanager.exceptionhandler;

import com.notebookmanager.exception.AlunoJaExistenteException;
import com.notebookmanager.exception.AlunoNaoEncontradoException;
import com.notebookmanager.exception.ValidationException;
import com.notebookmanager.model.payload.Payload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {AlunoJaExistenteException.class})
    private ResponseEntity<Payload> alunoJaExistenteHandler(AlunoJaExistenteException exception) {
        Payload payload = new Payload(HttpStatus.CONFLICT, null,"O Aluno com este RA já está cadastrado.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(payload);
    }

    @ExceptionHandler(value = {AlunoNaoEncontradoException.class})
    private ResponseEntity<Payload> alunoNaoEncontradoHandler(AlunoNaoEncontradoException exception) {
        Payload payload = new Payload(HttpStatus.NOT_FOUND, null, "Aluno não encontrado.");
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
