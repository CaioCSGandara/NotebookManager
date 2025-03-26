package com.notebookmanager.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.notebookmanager.controller.payloadvalidator.PayloadValidator;
import com.notebookmanager.model.Aluno;
import com.notebookmanager.model.createfields.AlunoCreateFields;
import com.notebookmanager.model.enums.Curso;
import com.notebookmanager.model.updatefields.AlunoUpdateFields;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlunoControllerSuccessTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void retornaAlunoPorIdStatus200() {

        ResponseEntity<String> response = restTemplate.getForEntity("/alunos/1", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        PayloadValidator.validateDataPayload(documentContext, "OK");
    }


    @Test
    @DirtiesContext
    void cadastraAlunoStatus201() {
        AlunoCreateFields alunoCreateFields = new AlunoCreateFields("Oscar Moura", "87019341", "oscarmoura@puccampinas.edu.br", "(19)98017-7111",
                Curso.NUTRICAO);

        ResponseEntity<Void> response = restTemplate.postForEntity("/alunos", alunoCreateFields, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String localDoNovoAluno = response.getHeaders().getLocation().getPath();

        assertThat(localDoNovoAluno).isEqualTo("/alunos/4");

    }


    @Test
    void listaAlunosDefaultStatus200() {

        ResponseEntity<String> response = restTemplate.getForEntity("/alunos", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        PayloadValidator.validateDataPayload(documentContext, "OK");

    }



    @Test
    @DirtiesContext
    void atualizaAlunoPorIdStatus204() {

        AlunoUpdateFields alunoUpdateFields = new AlunoUpdateFields("Julio Correa da Silva", "(19)91831-5123",
                Curso.MEDICINA);

        HttpEntity<AlunoUpdateFields> request = new HttpEntity<>(alunoUpdateFields);

        ResponseEntity<Void> response = restTemplate.exchange("/alunos/1", HttpMethod.PUT,
                request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }


    @Test
    @DirtiesContext
    void deleteAlunoPorIdStatus204() {

        ResponseEntity<Void> response = restTemplate.exchange("/alunos/1", HttpMethod.DELETE,
                null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }



}
