package com.notebookmanager.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.notebookmanager.model.entities.Aluno;
import com.notebookmanager.model.entities.enums.Curso;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlunoControllerFailureTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void naoRetornaAlunoComRaInvalido() {
        ResponseEntity<String> response = restTemplate.getForEntity("/alunos/96", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(response.getBody()).isBlank();

    }

    @Test
    void naoSalvaAlunoComRaRepetido() {
        Aluno aluno = new Aluno("Jonathan Luciano", "09135616", "jonathanluciano@puccampinas.edu.br", "(19)94291-7013",
                Curso.NUTRICAO, LocalDateTime.now(), LocalDateTime.now());

        ResponseEntity<String> response = restTemplate.postForEntity("/alunos", aluno, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        String status =  documentContext.read("$.status");
        assertThat(status).isEqualTo("CONFLICT");

        String message = documentContext.read("$.message");
        assertThat(message).isEqualTo("O Aluno com este RA já está cadastrado.");
    }


    @Test
    void naoAtualizaAlunoNaoExistente() {
        Aluno aluno = new Aluno("Josue Nao Existe No Banco", "02020202", "josuenenb@puccampinas.edu.br", "(19)93123-4231",
                Curso.ODONTOLOGIA, LocalDateTime.of(2012, 11, 10, 21, 12, 37),
                LocalDateTime.of(2012, 11, 10, 21, 12, 37));
        HttpEntity<Aluno> request = new HttpEntity<>(aluno);

        ResponseEntity<String> response = restTemplate.exchange("/alunos/90", HttpMethod.PUT,
                request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        DocumentContext documentContext =  JsonPath.parse(response.getBody());

        String status =  documentContext.read("$.status");
        assertThat(status).isEqualTo("NOT_FOUND");

        String message = documentContext.read("$.message");
        assertThat(message).isEqualTo("Aluno não encontrado.");
    }


    @Test
    void naoDeleteAlunoNaoExistente() {
        ResponseEntity<String> response = restTemplate.exchange("/alunos/31", HttpMethod.DELETE,
                null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        DocumentContext documentContext =  JsonPath.parse(response.getBody());

        String status =  documentContext.read("$.status");
        assertThat(status).isEqualTo("NOT_FOUND");

        String message = documentContext.read("$.message");
        assertThat(message).isEqualTo("Aluno não encontrado.");
    }


}
