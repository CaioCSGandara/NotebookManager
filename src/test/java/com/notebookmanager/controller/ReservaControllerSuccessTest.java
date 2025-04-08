package com.notebookmanager.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.notebookmanager.controller.payloadvalidator.PayloadValidator;
import com.notebookmanager.model.dto.createfields.ReservaCreateFields;
import com.notebookmanager.model.dto.updatefields.NotebookUpdateFields;
import com.notebookmanager.model.dto.updatefields.ReservaUpdateFields;
import com.notebookmanager.model.enums.StatusNotebook;
import com.notebookmanager.service.EmailService;
import com.notebookmanager.service.ReservaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean(EmailService.class)
public class ReservaControllerSuccessTest {

    @Autowired
    private ReservaService reservaService;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DirtiesContext
    void createReserva201() {
        ReservaCreateFields reservaCreateFields = new ReservaCreateFields("12345678", "491034");

        ResponseEntity<String> response = restTemplate.postForEntity("/reservas", reservaCreateFields, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String localNovaReserva = response.getHeaders().getLocation().getPath();

        assertThat(localNovaReserva).isEqualTo("/reservas/4");
    }

    @Test
    void listaReservasAtivasDefault200() {

        ResponseEntity<String> response = restTemplate.getForEntity("/reservas?page=0&size=10", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        PayloadValidator.validateDataPayload(documentContext, "OK");
    }

    @Test
    @DirtiesContext
    void encerraReserva204() {

        HttpEntity<NotebookUpdateFields> request = new HttpEntity(new NotebookUpdateFields(StatusNotebook.DISPONIVEL));
        ResponseEntity<String> response = restTemplate.exchange("/reservas/1/encerrar-reserva", HttpMethod.PATCH, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DirtiesContext
    void trocarNotebook204() {

        HttpEntity<NotebookUpdateFields> request = new HttpEntity(new ReservaUpdateFields("491034"));
        ResponseEntity<String> response = restTemplate.exchange("/reservas/1/trocar-notebook", HttpMethod.PATCH, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
