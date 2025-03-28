package com.notebookmanager.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.notebookmanager.controller.payloadvalidator.PayloadValidator;
import com.notebookmanager.model.dto.createfields.NotebookCreateFields;
import com.notebookmanager.model.enums.StatusNotebook;
import com.notebookmanager.model.dto.updatefields.NotebookUpdateFields;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotebookControllerFailureTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void encontrarNotebookPorIdStatus404() {
        ResponseEntity<String> response = restTemplate.getForEntity("/notebooks/96", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        PayloadValidator.validateErrorPayload(documentContext, "NOT_FOUND");
    }

    @Test
    void cadastrarNotebookStatus409() {
        NotebookCreateFields notebookCreateFields = new NotebookCreateFields("983410");

        ResponseEntity<String> response = restTemplate.postForEntity("/notebooks", notebookCreateFields, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        PayloadValidator.validateErrorPayload(documentContext, "CONFLICT");
    }

    @Test
    void gerenciarStatusStatus400() {

        StatusNotebook[] statusInvalidos = {StatusNotebook.DISPONIVEL, null};

        for(StatusNotebook statusInvalido : statusInvalidos) {
            NotebookUpdateFields notebookUpdateFields = new NotebookUpdateFields(statusInvalido);

            HttpEntity<NotebookUpdateFields> request = new HttpEntity<>(notebookUpdateFields);

            ResponseEntity<String> response = restTemplate.exchange("/notebooks/1/gerenciar-status", HttpMethod.PATCH, request, String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

            DocumentContext documentContext = JsonPath.parse(response.getBody());

            PayloadValidator.validateErrorPayload(documentContext, "BAD_REQUEST");
        }
    }



    @Test
    void deletarNotebookPorIdStatus404() {
        ResponseEntity<String> response = restTemplate.exchange("/notebooks/70",  HttpMethod.DELETE ,null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        PayloadValidator.validateErrorPayload(documentContext, "NOT_FOUND");
    }

    // TESTES DE VALIDAÇÃO DE BODY:


    @Test
    void naoAceitaPatrimonioInvalido() {
        String[] patrimoniosInvalidos = {"12345", "1234567" , "", null};

        for (String patrimonio : patrimoniosInvalidos) {
            NotebookCreateFields notebookCreateFields = new NotebookCreateFields(patrimonio);

            ResponseEntity<String> response = restTemplate.postForEntity("/notebooks", notebookCreateFields, String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

            DocumentContext documentContext = JsonPath.parse(response.getBody());

            String status =  documentContext.read("$.status");

            assertThat(status).isEqualTo("BAD_REQUEST");

            String message = documentContext.read("$.message");

            assertThat(message).isEqualTo("Erro de validação ao criar notebook.");

        }
    }

}
