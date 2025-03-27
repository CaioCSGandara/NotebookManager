package com.notebookmanager.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.notebookmanager.controller.payloadvalidator.PayloadValidator;
import com.notebookmanager.model.createfields.NotebookCreateFields;
import com.notebookmanager.model.enums.StatusNotebook;
import com.notebookmanager.model.updatefields.NotebookUpdateFields;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotebookControllerSuccessTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void retornaNotebookPorIdStatus200() {

        ResponseEntity<String> response = restTemplate.getForEntity("/notebooks/1", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        PayloadValidator.validateDataPayload(documentContext, "OK");
    }


    @Test
    @DirtiesContext
    public void cadastraNotebookStatus201() {

        NotebookCreateFields notebookCreateFields = new NotebookCreateFields("Acer Aspire 5", "222222");

        ResponseEntity<Void> response = restTemplate.postForEntity("/notebooks", notebookCreateFields, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String localNovoNotebook =  response.getHeaders().getLocation().getPath();

        assertThat(localNovoNotebook).isEqualTo("/notebooks/4");
    }


    @Test
    public void listaNotebooksDefaultStatus200() {
        ResponseEntity<String> response = restTemplate.getForEntity("/notebooks", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        PayloadValidator.validateDataPayload(documentContext, "OK");

    }


    @Test
    @DirtiesContext
    void gerenciaStatusStatus204() {

        NotebookUpdateFields notebookUpdateFields = new NotebookUpdateFields(StatusNotebook.EMPRESTADO);
        HttpEntity<NotebookUpdateFields> request = new HttpEntity<>(notebookUpdateFields);

        ResponseEntity<Void> response = restTemplate.exchange("/notebooks/1/gerenciar-status", HttpMethod.PATCH, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }



    @Test
    @DirtiesContext
    void deletaNotebookPorIdStatus204() {
        ResponseEntity<Void> response = restTemplate.exchange("/notebooks/1", HttpMethod.DELETE,
                null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}
