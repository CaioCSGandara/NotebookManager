package com.notebookmanager.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.notebookmanager.controller.payloadvalidator.PayloadValidator;
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
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
    void cadastrarNotebookPorIdStatus409() {
        Notebook notebook = new Notebook("Acer Aspire 5", "98341099", StatusNotebook.EMPRESTADO,
                19, LocalDateTime.of(2023,1,5,14,12,20));

        ResponseEntity<String> response = restTemplate.postForEntity("/notebooks", notebook, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        PayloadValidator.validateErrorPayload(documentContext, "CONFLICT");
    }

    @Test
    void gerenciarStatusStatus400() {

        HashMap<String, StatusNotebook> mapNovoStatus = new HashMap<>();
        mapNovoStatus.put("status", StatusNotebook.DISPONIVEL);
        HttpEntity<Map<String, StatusNotebook>> request = new HttpEntity<>(mapNovoStatus);

        ResponseEntity<String> response = restTemplate.exchange("/notebooks/1/gerenciar-status", HttpMethod.PATCH, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        PayloadValidator.validateErrorPayload(documentContext, "BAD_REQUEST");
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
    void naoAceitaModeloInvalido() {
        String[] modelosInvalidos = {"mod", "", "NomeComMaisDe50CaracteresNomeComMaisDe50Caracteres2", null};

        for (String modelo : modelosInvalidos) {
            Notebook notebook = new Notebook(modelo, "12345678", StatusNotebook.EMPRESTADO,
                    19, LocalDateTime.of(2023,1,5,14,12,20));

            ResponseEntity<String> response = restTemplate.postForEntity("/notebooks", notebook, String.class);

            assertResponseParaPropriedadeInvalida(response);

        }
    }


    @Test
    void naoAceitaPatrimonioInvalido() {
        String[] patrimoniosInvalidos = {"1234567", "123456789" , "", null};

        for (String patrimonio : patrimoniosInvalidos) {
            Notebook notebook = new Notebook("Acer Aspire 5", patrimonio, StatusNotebook.EMPRESTADO,
                    19, LocalDateTime.of(2023,1,5,14,12,20));

            ResponseEntity<String> response = restTemplate.postForEntity("/notebooks", notebook, String.class);

            assertResponseParaPropriedadeInvalida(response);

        }
    }

    @Test
    void naoAceitaStatusNotebookNull() {

            Notebook notebook = new Notebook("Acer Aspire 5", "12345678", null,
                    19, LocalDateTime.of(2023,1,5,14,12,20));

            ResponseEntity<String> response = restTemplate.postForEntity("/notebooks", notebook, String.class);

            assertResponseParaPropriedadeInvalida(response);
    }


    @Test
    void naoAceitaQtdEmprestimosInvalida() {
        Integer[] qtdEmprestimosInvalidas = {-1, -2};

        for (Integer qtdEmprestimos : qtdEmprestimosInvalidas) {
            Notebook notebook = new Notebook("Acer Aspire 5", "12345678", StatusNotebook.EMPRESTADO,
                    qtdEmprestimos, LocalDateTime.of(2023,1,5,14,12,20));

            ResponseEntity<String> response = restTemplate.postForEntity("/notebooks", notebook, String.class);

            assertResponseParaPropriedadeInvalida(response);

        }
    }


    @Test
    void naoAceitaAtualizadoEmNull() {

        Notebook notebook = new Notebook("Acer Aspire 5", "12345678", StatusNotebook.AFASTADO,
                19, null);

        ResponseEntity<String> response = restTemplate.postForEntity("/notebooks", notebook, String.class);

        assertResponseParaPropriedadeInvalida(response);
    }



    private void assertResponseParaPropriedadeInvalida(ResponseEntity<String> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        String status =  documentContext.read("$.status");

        assertThat(status).isEqualTo("BAD_REQUEST");

        String message = documentContext.read("$.message");

        assertThat(message).isEqualTo("Erro de validação ao criar notebook.");
    }
}
