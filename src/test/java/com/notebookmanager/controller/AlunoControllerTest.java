package com.notebookmanager.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.notebookmanager.generator.AlunoGenerator;
import com.notebookmanager.integration.BaseContainer;
import com.notebookmanager.model.entities.Aluno;
import com.notebookmanager.model.repositories.AlunoRepository;
import org.json.JSONArray;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlunoControllerTest extends BaseContainer {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    AlunoRepository alunoRepository;


    @AfterEach
    public void limparColecao() {
        alunoRepository.deleteAll();
    }

    @Test
    void retornaAlunoComRaValido() {

        Aluno aluno = AlunoGenerator.gerarAluno();

        alunoRepository.save(aluno);

        ResponseEntity<String> response = restTemplate.getForEntity("/alunos/22415616", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        String id = documentContext.read("$.id");
        assertThat(id).isNotNull();

        String ra = documentContext.read("$.ra");
        assertThat(ra).isEqualTo("22415616");
    }

    @Test
    void naoRetornaAlunoComRaInvalido() {
        ResponseEntity<String> response = restTemplate.getForEntity("/alunos/32323232", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(response.getBody()).isBlank();

    }
    @Test
    void salvaAlunoNoBanco() {
        Aluno aluno = AlunoGenerator.gerarAluno();

        ResponseEntity<Void> response = restTemplate.postForEntity("/alunos", aluno, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI localDoNovoAluno = response.getHeaders().getLocation();

        ResponseEntity<String> getResponse = restTemplate.getForEntity(localDoNovoAluno, String.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());

        String id = documentContext.read("$.id");
        assertThat(id).isNotNull();


    }

    @Test
    void retornaListaDeAlunos() {
        List<Aluno> lista = AlunoGenerator.gerarListDeAlunos();
        alunoRepository.saveAll(lista);

        ResponseEntity<String> response = restTemplate.getForEntity("/alunos", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        System.out.println(documentContext.toString());

        int alunoCount = documentContext.read("$.length()");
        assertThat(alunoCount).isEqualTo(3);

        List<String> ras = documentContext.read("$..ra");
        assertThat(ras).containsExactlyInAnyOrder("09135616", "03781923", "90174823");
    }

    @Test
    void retornaPaginaDe02Alunos() {
        List<Aluno> lista = AlunoGenerator.gerarListDeAlunos();
        alunoRepository.saveAll(lista);

        ResponseEntity<String> response = restTemplate.getForEntity("/alunos?page=0&size=2", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        List<String> page = documentContext.read("$[*]");

        assertThat(page.size()).isEqualTo(2);
    }

}
