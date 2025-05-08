package com.notebookmanager.infra.exception;

public class SenhaInvalidaException extends RuntimeException {
    public SenhaInvalidaException(String message) {
        super(message);
    }
    public SenhaInvalidaException() {}
}
