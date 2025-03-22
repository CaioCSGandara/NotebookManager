package com.notebookmanager.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.notebookmanager.model.Aluno;
import com.notebookmanager.model.enums.Curso;
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
    void naoRetornaAlunoComIdInvalido() {
        ResponseEntity<String> response = restTemplate.getForEntity("/alunos/96", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        String status = documentContext.read("$.status");

        assertThat(status).isEqualTo("NOT_FOUND");

        String message =  documentContext.read("$.message");
        assertThat(message).isEqualTo("Aluno não encontrado.");

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

    // TESTES DE VALIDAÇÃO DE BODY:

    @Test
    void naoAceitaNomeInvalido() {

        String[] nomesInvalidos = {"Jorge Ferreira de Lima Almeida Santos da Silva", "", " ", null};


        for (String nomeInvalido : nomesInvalidos) {

            Aluno aluno = new Aluno (nomeInvalido, "13279102", "jorgefdlass@puccampinas.edu.br", "(19)99182-4125",
                    Curso.NUTRICAO, LocalDateTime.now(), LocalDateTime.now());

            HttpEntity<Aluno> request = new HttpEntity<>(aluno);

            testaBodyAlunoInvalido("/alunos", HttpMethod.POST, request, "Erro de validação ao criar aluno.");
            testaBodyAlunoInvalido("/alunos/3", HttpMethod.PUT, request, "Erro de validação ao atualizar o aluno.");

        }
    }


    @Test
    void naoAceitaRaInvalido() {
        String[] rasInvalidos = {"1234567", "123456789", "", " ", null};

        for (String raInvalido : rasInvalidos) {
            Aluno aluno = new Aluno ("Jorge Flavio Silva", raInvalido, "jorgefdlass@puccampinas.edu.br", "(19)99182-4125",
                    Curso.NUTRICAO, LocalDateTime.now(), LocalDateTime.now());

            HttpEntity<Aluno> request = new HttpEntity<>(aluno);

            testaBodyAlunoInvalido("/alunos", HttpMethod.POST, request, "Erro de validação ao criar aluno.");
            testaBodyAlunoInvalido("/alunos/3", HttpMethod.PUT, request, "Erro de validação ao atualizar o aluno.");

        }
    }


    @Test
    void naoAceitaEmailInvalido() {
        String[] emailsInvalidos = {"jorge^f@puccampinas.edu.br", "jorge.2f@gmail.com", "jorge#f@puccampinas.edu.br","", " ", null};

        for (String emailInvalido : emailsInvalidos) {
            Aluno aluno = new Aluno ("Jorge Flavio Silva", "12345678", emailInvalido, "(19)99182-4125",
                    Curso.NUTRICAO, LocalDateTime.now(), LocalDateTime.now());

            HttpEntity<Aluno> request = new HttpEntity<>(aluno);

            testaBodyAlunoInvalido("/alunos", HttpMethod.POST, request, "Erro de validação ao criar aluno.");
            testaBodyAlunoInvalido("/alunos/3", HttpMethod.PUT, request, "Erro de validação ao atualizar o aluno.");

        }
    }


    @Test
    void naoAceitaTelefoneInvalido() {
        String[] telefonesInvalidos = {"(d9)12345-0987", "(19)1$345-0987", "(11)12345-0h87", "(19)123450987", "11 12345-0987", "", " ", null};

        for (String telefoneInvalido : telefonesInvalidos) {
            Aluno aluno = new Aluno ("Jorge Flavio Silva", "12345678", "jorge.fs@puccampinas.edu.br", telefoneInvalido,
                    Curso.NUTRICAO, LocalDateTime.now(), LocalDateTime.now());

            HttpEntity<Aluno> request = new HttpEntity<>(aluno);

            testaBodyAlunoInvalido("/alunos", HttpMethod.POST, request, "Erro de validação ao criar aluno.");
            testaBodyAlunoInvalido("/alunos/3", HttpMethod.PUT, request, "Erro de validação ao atualizar o aluno.");

        }
    }


    @Test
    void naoAceitaCursoNulo() {
        Aluno aluno = new Aluno ("Jorge Flavio Silva", "12345678", "jorge.fs@puccampinas.edu.br", "(19)99182-4125",
                null, LocalDateTime.now(), LocalDateTime.now());

        HttpEntity<Aluno> request = new HttpEntity<>(aluno);

        testaBodyAlunoInvalido("/alunos", HttpMethod.POST, request, "Erro de validação ao criar aluno.");
        testaBodyAlunoInvalido("/alunos/3", HttpMethod.PUT, request, "Erro de validação ao atualizar o aluno.");
    }

    @Test
    void naoAceitaUltimoLoginNulo() {
        Aluno aluno = new Aluno ("Jorge Flavio Silva", "12345678", "jorge.fs@puccampinas.edu.br", "(19)99182-4125",
                Curso.NUTRICAO, null, LocalDateTime.now());

        HttpEntity<Aluno> request = new HttpEntity<>(aluno);

        testaBodyAlunoInvalido("/alunos", HttpMethod.POST, request, "Erro de validação ao criar aluno.");
        testaBodyAlunoInvalido("/alunos/3", HttpMethod.PUT, request, "Erro de validação ao atualizar o aluno.");
    }


    @Test
    void naoAceitaAtualizadoEmNulo() {
        Aluno aluno = new Aluno ("Jorge Flavio Silva", "12345678", "jorge.fs@puccampinas.edu.br", "(19)99182-4125",
                Curso.NUTRICAO, LocalDateTime.now(), null);

        HttpEntity<Aluno> request = new HttpEntity<>(aluno);

        testaBodyAlunoInvalido("/alunos", HttpMethod.POST, request, "Erro de validação ao criar aluno.");
        testaBodyAlunoInvalido("/alunos/3", HttpMethod.PUT, request, "Erro de validação ao atualizar o aluno.");
    }



    private void testaBodyAlunoInvalido(String url, HttpMethod method, HttpEntity<Aluno> request, String errorMessage) {
        ResponseEntity<String> response = restTemplate.exchange(url, method, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        String status = documentContext.read("$.status");
        assertThat(status).isEqualTo("BAD_REQUEST");
        String message = documentContext.read("$.message");
        assertThat(message).isEqualTo(errorMessage);
    }


}
