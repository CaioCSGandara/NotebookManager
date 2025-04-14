package com.notebookmanager.service;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.notebookmanager.container.MailHogContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @BeforeAll
    static void setUp() {
        MailHogContainer.init();
    }

    @Test
    public void testSendEmail() {
        emailService.enviarEmail("zabumba@puccampinas.edu.br", "Email Importante", "Ola, me manda zap, rápido!");
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8025/api/v2/messages", String.class);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        String sender = documentContext.read("$.items[0].From.Mailbox");
        assertThat(sender).isEqualTo("caiocsgandara");

        String receiver = documentContext.read("$.items[0].To[0].Mailbox");
        assertThat(receiver).isEqualTo("zabumba");

    }
}
