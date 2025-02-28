package com.notebookmanager.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.notebookmanager.integration.BaseContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlunoControllerTest extends BaseContainer {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void retornaAlunoComRaValido() {
        ResponseEntity<String> response = restTemplate.getForEntity("/alunos/22415616", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        String nome = documentContext.read("$.nome");
        assertThat(nome).isEqualTo("Caio Gandara");

        String ra = documentContext.read("$.ra");
        assertThat(ra).isEqualTo("22415616");

        String email = documentContext.read("$.email");
        assertThat(email).isEqualTo("caio.cgs2@gmail.com");

        String telefone = documentContext.read("$.telefone");
        assertThat(telefone).isEqualTo("(19)99414-8554");

        String curso = documentContext.read("$.curso");
        assertThat(curso).isEqualTo("Enfermagem");

        String ultimoLogin = documentContext.read("$.ultimoLogin");
        assertThat(ultimoLogin).isEqualTo("2025-02-27T16:51:11.958");

        String atualizadoEm = documentContext.read("$.atualizadoEm");
        assertThat(atualizadoEm).isEqualTo("2025-02-27T16:51:11.958");
    }

    @Test
    void naoRetornaAlunoComRaInvalido() {
        ResponseEntity<String> response = restTemplate.getForEntity("/alunos/32323232", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(response.getBody()).isBlank();

    }
}
