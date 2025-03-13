package com.notebookmanager.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.notebookmanager.generator.AlunoGenerator;
import com.notebookmanager.integration.BaseContainer;
import com.notebookmanager.model.entities.Aluno;
import com.notebookmanager.model.entities.enums.Curso;
import com.notebookmanager.model.repositories.AlunoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

        Aluno aluno = AlunoGenerator.getAluno();

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
        Aluno aluno = AlunoGenerator.getAluno();

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
        List<Aluno> lista = AlunoGenerator.getListaAlunos();
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
        List<Aluno> lista = AlunoGenerator.getListaAlunos();
        alunoRepository.saveAll(lista);

        ResponseEntity<String> response = restTemplate.getForEntity("/alunos?page=0&size=2", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        List<String> page = documentContext.read("$[*]");

        assertThat(page.size()).isEqualTo(2);
    }

    @Test
    void retornaPaginaOrdenadaPorNome() {
        List<Aluno> lista = AlunoGenerator.getListaAlunos();
        alunoRepository.saveAll(lista);

        ResponseEntity<String> response = restTemplate.getForEntity("/alunos?page=0&size=1&sort=nome,asc", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        List<String> read = documentContext.read("$[*]");

        assertThat(read.size()).isEqualTo(1);

        String nome = documentContext.read("$[0].nome");

        assertThat(nome).isEqualTo("Fernando Pontes");

    }

    @Test
    void retornaPaginaComOrdenacaoDefault() {
        List<Aluno> lista = AlunoGenerator.getListaAlunos();
        alunoRepository.saveAll(lista);

        ResponseEntity<String> response = restTemplate.getForEntity("/alunos", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        List<String> page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(3);

        List<String> ras = documentContext.read("$..ra");
        assertThat(ras).containsExactly("90174823", "09135616", "03781923");
    }

    @Test
    void atualizaAlunoExistente() {
        Aluno aluno = AlunoGenerator.getAluno();
        alunoRepository.save(aluno);

        Aluno alunoAtualizado = new Aluno("Caio Gandara dos Santos", "22415616", "caio.cgs@gmail.com", "(19)90123-9031",
                Curso.ENFERMAGEM, LocalDateTime.of(2010, 12, 30, 12, 14, 22),
                LocalDateTime.of(2015, 4, 22, 18, 9, 12));


        HttpEntity<Aluno> request = new HttpEntity<Aluno>(alunoAtualizado);

        ResponseEntity<Void> response = restTemplate.exchange("/alunos/22415616", HttpMethod.PUT,
                request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/alunos/22415616", String.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());

        String nome = documentContext.read("$.nome");
        assertThat(nome).isEqualTo("Caio Gandara dos Santos");

        String telefone = documentContext.read("$.telefone");
        assertThat(telefone).isEqualTo("(19)90123-9031");

        String atualizadoEm = documentContext.read("$.atualizadoEm");
        assertThat(atualizadoEm).isNotEqualTo("2010-12-30T12:14:22");
    }


    @Test
    void deleteAlunoExistente() {
        Aluno aluno = AlunoGenerator.getAluno();
        alunoRepository.save(aluno);

        ResponseEntity<Void> response = restTemplate.exchange("/alunos/22415616", HttpMethod.DELETE,
                null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/alunos/22415616", String.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void naoDeleteAlunoNaoExistente() {
        ResponseEntity<Void> response = restTemplate.exchange("/alunos/09128475", HttpMethod.DELETE,
                null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
