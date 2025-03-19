package com.notebookmanager.exception;

public class AlunoNaoEncontradoException extends RuntimeException {
  public AlunoNaoEncontradoException(String message) {
    super(message);
  }
  public AlunoNaoEncontradoException() {}
}
