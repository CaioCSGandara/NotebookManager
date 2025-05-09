package com.notebookmanager.service;

import com.notebookmanager.infra.exception.RecursoNaoEncontradoException;
import com.notebookmanager.infra.exception.SenhaInvalidaException;
import com.notebookmanager.model.Funcionario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FuncionarioServiceFailureTest {

    @Autowired
    private FuncionarioService funcionarioService;

    @Test
    void naoEncontraFuncionarioComRFInexistente() {

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
                ()->{
                    Optional<Funcionario> funcionario = funcionarioService.loginComRfESenha("999999", "senha321");
                });

        assertThat(exception.getMessage()).isEqualTo("RF inválido.");
    }

    @Test
    void naoAceitaSenhaErrada() {

        SenhaInvalidaException exception = assertThrows(SenhaInvalidaException.class,
                ()->{
                    Optional<Funcionario> funcionario = funcionarioService.loginComRfESenha("443322", "senha321312");
                });

    }


}
