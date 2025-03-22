package com.notebookmanager.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.notebookmanager.model.Notebook;
import com.notebookmanager.model.enums.StatusNotebook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotebookControllerSuccessTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void retornaNotebookPorId() {

        ResponseEntity<String> response = restTemplate.getForEntity("/notebooks/1", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Integer id = documentContext.read("$.data.id");
        assertThat(id).isEqualTo(1);

        String patrimonio = documentContext.read("$.data.patrimonio");
        assertThat(patrimonio).isEqualTo("49103423");
    }


    @Test
    @DirtiesContext
    public void cadastraNotebook() {

        Notebook notebook = new Notebook("Acer Aspire 5", "22222222", StatusNotebook.DISPONIVEL,
                5, LocalDateTime.of(2025, 3, 17, 22, 34, 45));

        ResponseEntity<Void> response = restTemplate.postForEntity("/notebooks", notebook, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI localNovoNotebook =  response.getHeaders().getLocation();

        ResponseEntity<String> responseGet =  restTemplate.getForEntity(localNovoNotebook, String.class);

        assertThat(responseGet.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(responseGet.getBody());

        Integer id = documentContext.read("$.data.id");
        assertThat(id).isEqualTo(4);

        String patrimonio = documentContext.read("$.data.patrimonio");
        assertThat(patrimonio).isEqualTo("22222222");

    }

    @Test
    public void listaNotebooksDefault() {
        ResponseEntity<String> response = restTemplate.getForEntity("/notebooks", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Integer countNotebooks = documentContext.read("$.data.length()");

        assertThat(countNotebooks).isEqualTo(3);

        List<String> listaPatrimonios = documentContext.read("$.data..patrimonio");

        assertThat(listaPatrimonios).containsExactlyInAnyOrder("49103423", "98341099", "12309845");

    }

    @Test
    public void lista02NotebooksPorPagina() {
        ResponseEntity<String> response = restTemplate.getForEntity("/notebooks?page=0&size=2", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Integer countNotebooks = documentContext.read("$.data.length()");

        assertThat(countNotebooks).isEqualTo(2);
    }


    @Test
    void listaNotebooksPorQtdEmprestimosDesc() {
        ResponseEntity<String> response = restTemplate.getForEntity("/notebooks?sort=qtdEmprestimos,desc", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        List<Integer> listaQtdEmprestimosDesc = documentContext.read("$.data..qtdEmprestimos");

        assertThat(listaQtdEmprestimosDesc).containsExactly(130, 43, 19);

    }


    @Test
    @DirtiesContext
    void gerenciaAfastamento() {

        StatusNotebook[] statusNotebookList = {StatusNotebook.AFASTADO, StatusNotebook.DISPONIVEL};

        for(StatusNotebook statusNotebook : statusNotebookList) {

            HttpEntity<StatusNotebook> request = new HttpEntity<StatusNotebook>(statusNotebook);

            ResponseEntity<Void> response = restTemplate.exchange("/notebooks/1/gerenciar-afastamento", HttpMethod.PATCH, request, Void.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

            ResponseEntity<String> responseGet = restTemplate.getForEntity("/notebooks/1", String.class);

            DocumentContext documentContext = JsonPath.parse(responseGet.getBody());

            String status = documentContext.read("$.data.status");
            assertThat(status).isEqualTo(statusNotebook.toString());

            String atualizadoEm = documentContext.read("$.data.atualizadoEm");
            assertThat(atualizadoEm).isNotEqualTo("2021-06-30T09:14:09");
        }
    }



    @Test
    @DirtiesContext
    void deletaNotebookPorId() {
        ResponseEntity<Void> response = restTemplate.exchange("/notebooks/1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}
