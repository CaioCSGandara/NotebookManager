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
public class FuncionarioControllerSuccessTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void autorizaLogin200() {
        FuncionarioLoginFields funcionarioLoginFields = new FuncionarioLoginFields("123456", "senha123");
        ResponseEntity<String> response = restTemplate.postForEntity("/funcionarios/login", funcionarioLoginFields, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
