package com.notebookmanager.infra.exception;

public class RecursoNaoEncontradoException extends RuntimeException {
  public RecursoNaoEncontradoException(String message) {
    super(message);
  }
  public RecursoNaoEncontradoException() {}
}
