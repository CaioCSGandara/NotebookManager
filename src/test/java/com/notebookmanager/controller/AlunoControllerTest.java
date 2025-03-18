package com.notebookmanager.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.notebookmanager.model.entities.Aluno;
import com.notebookmanager.model.entities.enums.Curso;
import com.notebookmanager.model.repositories.AlunoRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlunoControllerTest {

    @Autowired
    TestRestTemplate restTemplate;


    @Test
    void retornaAlunoComRaValido() {

        ResponseEntity<String> response = restTemplate.getForEntity("/alunos/1", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Integer id = documentContext.read("$.id");
        assertThat(id).isEqualTo(1);

        String ra = documentContext.read("$.ra");
        assertThat(ra).isEqualTo("09135616");

        String curso = documentContext.read("$.curso");
        assertThat(curso).isEqualTo("MEDICINA");
    }

    @Test
    void naoRetornaAlunoComRaInvalido() {
        ResponseEntity<String> response = restTemplate.getForEntity("/alunos/96", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(response.getBody()).isBlank();

    }

    @Test
    @DirtiesContext
    void salvaAlunoNoBanco() {
        Aluno aluno = new Aluno("Oscar Moura", "87019341", "oscarmoura@puccampinas.edu.br", "(19)98017-7111",
                Curso.NUTRICAO, LocalDateTime.now(), LocalDateTime.now());

        ResponseEntity<Void> response = restTemplate.postForEntity("/alunos", aluno, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI localDoNovoAluno = response.getHeaders().getLocation();

        ResponseEntity<String> getResponse = restTemplate.getForEntity(localDoNovoAluno, String.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());

        Integer id = documentContext.read("$.id");
        assertThat(id).isNotNull();


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
    void retornaListaDeAlunos() {

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

        ResponseEntity<String> response = restTemplate.getForEntity("/alunos?page=0&size=2", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        List<String> page = documentContext.read("$[*]");

        assertThat(page.size()).isEqualTo(2);
    }

    @Test
    void retornaPaginaOrdenadaPorNome() {

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

        ResponseEntity<String> response = restTemplate.getForEntity("/alunos", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        List<String> page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(3);

        List<String> ras = documentContext.read("$..ra");
        assertThat(ras).containsExactly("90174823", "09135616", "03781923");
    }

    @Test
    @DirtiesContext
    void atualizaAlunoExistente() {

        Aluno alunoAtualizado = new Aluno("Julio Correa da Silva", "09135616", "jcorrea@puccampinas.edu.br", "(19)91831-5123",
                Curso.MEDICINA, LocalDateTime.of(2012, 11, 10, 21, 12, 37),
                LocalDateTime.of(2015, 7, 10, 14, 22, 17));

        HttpEntity<Aluno> request = new HttpEntity<>(alunoAtualizado);

        ResponseEntity<Void> response = restTemplate.exchange("/alunos/1", HttpMethod.PUT,
                request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/alunos/1", String.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());

        String nome = documentContext.read("$.nome");
        assertThat(nome).isEqualTo("Julio Correa da Silva");

        String telefone = documentContext.read("$.telefone");
        assertThat(telefone).isEqualTo("(19)91831-5123");

        String curso = documentContext.read("$.curso");
        assertThat(curso).isEqualTo("MEDICINA");

        String atualizadoEm = documentContext.read("$.atualizadoEm");
        assertThat(atualizadoEm).isNotEqualTo("2012-11-10T21:12:37");
    }

    @Test
    void naoAtualizaAlunoNaoExistente() {
        Aluno aluno = new Aluno("Josue Nao Existe No Banco", "09135616", "josuenenb@puccampinas.edu.br", "(19)93123-4231",
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
    @DirtiesContext
    void deleteAlunoExistente() {

        ResponseEntity<Void> response = restTemplate.exchange("/alunos/1", HttpMethod.DELETE,
                null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/alunos/09135616", String.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
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
