package com.notebookmanager.infra.exception;

public class RecursoJaExistenteException extends RuntimeException {

    public RecursoJaExistenteException(String message) {
        super(message);
    }

    public RecursoJaExistenteException() {}
}
