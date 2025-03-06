package com.notebookmanager.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.notebookmanager.integration.BaseContainer;
import com.notebookmanager.model.entities.Aluno;
import com.notebookmanager.model.entities.enums.Curso;
import com.notebookmanager.model.repositories.AlunoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlunoControllerTest extends BaseContainer {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    AlunoRepository alunoRepository;

    private static Stream<Aluno> retornaAluno() {
        return Stream.of(new Aluno("Caio Gandara", "22415616", "caio.cgs2@gmail.com", "(19)99414-8554",
                Curso.ENFERMAGEM, LocalDateTime.of(2025, 2, 27, 16, 51, 11),
                LocalDateTime.of(2025, 2, 27, 16, 51, 11)));
    }

    @AfterEach
    public void limparColecao() {
        alunoRepository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("retornaAluno")
    void retornaAlunoComRaValido(Aluno aluno) {

        alunoRepository.save(aluno);

        ResponseEntity<String> response = restTemplate.getForEntity("/alunos/22415616", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        String id = documentContext.read("$.id");
        assertThat(id).isNotNull();
    }

    @Test
    void naoRetornaAlunoComRaInvalido() {
        ResponseEntity<String> response = restTemplate.getForEntity("/alunos/32323232", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(response.getBody()).isBlank();

    }

    @ParameterizedTest
    @MethodSource("retornaAluno")
    void salvaAlunoNoBanco(Aluno aluno) {
        ResponseEntity<Void> response = restTemplate.postForEntity("/alunos", aluno, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI localDoNovoAluno = response.getHeaders().getLocation();

        ResponseEntity<String> getResponse = restTemplate.getForEntity(localDoNovoAluno, String.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());

        String id = documentContext.read("$.id");
        assertThat(id).isNotNull();


    }


}
