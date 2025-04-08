package com.notebookmanager.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.notebookmanager.controller.payloadvalidator.PayloadValidator;
import com.notebookmanager.model.dto.createfields.ReservaCreateFields;
import com.notebookmanager.model.dto.updatefields.NotebookUpdateFields;
import com.notebookmanager.model.dto.updatefields.ReservaUpdateFields;
import com.notebookmanager.model.enums.StatusNotebook;
import com.notebookmanager.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean(EmailService.class)
public class ReservaControllerFailureTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void naoCriaReservaComParametroInexistente() {
        ReservaCreateFields reservaCreateFields = new ReservaCreateFields("04040404", "491034");

        ResponseEntity<String> response = restTemplate.postForEntity("/reservas", reservaCreateFields, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        PayloadValidator.validateErrorPayload(documentContext, "NOT_FOUND");
    }

    @Test
    void naoEncerraReservaInexistente() {

        HttpEntity<NotebookUpdateFields> request = new HttpEntity(new NotebookUpdateFields(StatusNotebook.DISPONIVEL));
        ResponseEntity<String> response = restTemplate.exchange("/reservas/86/encerrar-reserva", HttpMethod.PATCH, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        PayloadValidator.validateErrorPayload(documentContext, "NOT_FOUND");
    }

    @Test
    void naoTrocaParaNotebookInexistente() {

        HttpEntity<NotebookUpdateFields> request = new HttpEntity(new ReservaUpdateFields("010101"));
        ResponseEntity<String> response = restTemplate.exchange("/reservas/1/trocar-notebook", HttpMethod.PATCH, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        PayloadValidator.validateErrorPayload(documentContext, "NOT_FOUND");
    }

    // TESTES DE VALIDAÇÃO DE BODY


    @Test
    void naoCriaReservaComPatrimonioInvalido() {

        String[] patrimoniosInvalidos = {null, "93010", "3124567", ""};

        for (String patrimonio : patrimoniosInvalidos) {
            ReservaCreateFields reservaCreateFields = new ReservaCreateFields("04040404", patrimonio);
            ResponseEntity<String> response = restTemplate.postForEntity("/reservas", null, String.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

    }

    @Test
    void naoCriaReservaComRaInvalido() {

        String[] rasInvalidos = {null, "93010", "3124567", ""};

        for (String ra : rasInvalidos) {
            ReservaCreateFields reservaCreateFields = new ReservaCreateFields(ra, "123456");
            ResponseEntity<String> response = restTemplate.postForEntity("/reservas", null, String.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

    }


    @Test
    void naoEncerraReservaComParametroNulo() {

        HttpEntity<NotebookUpdateFields> request = new HttpEntity(new NotebookUpdateFields(null));
        ResponseEntity<String> response = restTemplate.exchange("/reservas/1/encerrar-reserva", HttpMethod.PATCH, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        PayloadValidator.validateErrorPayload(documentContext, "BAD_REQUEST");

        String message =  documentContext.read("$.message");
        assertThat(message).isEqualTo("Erro de validação ao encerrar reserva");
    }


    @Test
    void naoEncerraTrocaNotebookComParametroInvalido() {

        String[] patrimoniosInvalidos = {null, "93010", "3124567", ""};

        for (String patrimonio : patrimoniosInvalidos) {

            HttpEntity<NotebookUpdateFields> request = new HttpEntity(new ReservaUpdateFields(patrimonio));
            ResponseEntity<String> response = restTemplate.exchange("/reservas/1/trocar-notebook", HttpMethod.PATCH, request, String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

            DocumentContext documentContext = JsonPath.parse(response.getBody());

            PayloadValidator.validateErrorPayload(documentContext, "BAD_REQUEST");

            String message =  documentContext.read("$.message");
            assertThat(message).isEqualTo("Erro de validação ao trocar de notebook");
        }
    }


}
