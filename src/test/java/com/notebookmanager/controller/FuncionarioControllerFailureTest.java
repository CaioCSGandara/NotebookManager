package com.notebookmanager.controller;

import com.notebookmanager.model.dto.createfields.FuncionarioLoginFields;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FuncionarioControllerFailureTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void tentaLoginComRfInexistente401() {
        FuncionarioLoginFields funcionarioLoginFields = new FuncionarioLoginFields("091111", "senhaerrada");
        ResponseEntity<String> response = restTemplate.postForEntity("/funcionarios/login", funcionarioLoginFields, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void tentaLoginComSenhaErrada401() {
        FuncionarioLoginFields funcionarioLoginFields = new FuncionarioLoginFields("123456", "senhaerrada");
        ResponseEntity<String> response = restTemplate.postForEntity("/funcionarios/login", funcionarioLoginFields, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void tentaLoginComRfInvalido400() {

        String[] rfsInvalidos = {null, "", "1234567", "12345"};

        for(String rfInvalido: rfsInvalidos) {
            FuncionarioLoginFields funcionarioLoginFields = new FuncionarioLoginFields(rfInvalido, "senha123");
            ResponseEntity<String> response = restTemplate.postForEntity("/funcionarios/login", funcionarioLoginFields, String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    void tentaLoginComSenhaInvalida400() {

        String[] senhasInvalidas = {null, ""};

        for(String senhaInvalida: senhasInvalidas) {
            FuncionarioLoginFields funcionarioLoginFields = new FuncionarioLoginFields("123456", senhaInvalida);
            ResponseEntity<String> response = restTemplate.postForEntity("/funcionarios/login", funcionarioLoginFields, String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }
}
