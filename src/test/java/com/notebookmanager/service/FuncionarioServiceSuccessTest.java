package com.notebookmanager.service;

import com.notebookmanager.model.Funcionario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FuncionarioServiceSuccessTest {

    @Autowired
    private FuncionarioService funcionarioService;

    @Test
    void realizaLogin() {

        Optional<Funcionario> funcionario = funcionarioService.loginComRfESenha("443322", "senha321");

        assertTrue(funcionario.isPresent());
    }
}
