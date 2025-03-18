package com.notebookmanager.exceptionhandler;

import com.notebookmanager.exception.AlunoJaExistenteException;
import com.notebookmanager.exception.AlunoNaoEncontradoException;
import com.notebookmanager.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {AlunoJaExistenteException.class})
    private ResponseEntity<RestErrorMessage> alunoJaExistenteHandler(AlunoJaExistenteException exception) {
        RestErrorMessage response = new RestErrorMessage(HttpStatus.CONFLICT, "O Aluno com este RA já está cadastrado.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(value = {AlunoNaoEncontradoException.class})
    private ResponseEntity<RestErrorMessage> alunoNaoEncontradoHandler(AlunoNaoEncontradoException exception) {
        RestErrorMessage response = new RestErrorMessage(HttpStatus.NOT_FOUND, "Aluno não encontrado.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    private ResponseEntity<RestErrorMessage> exceptionHandler(RuntimeException exception) {
        RestErrorMessage response = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Ops! Ocorreu um erro interno no servidor.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(value = {ValidationException.class})
    private ResponseEntity<RestErrorMessage> validationExceptionHandler(ValidationException exception) {
        RestErrorMessage response = new RestErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }




}
