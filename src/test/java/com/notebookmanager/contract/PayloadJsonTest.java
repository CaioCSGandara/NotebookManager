package com.notebookmanager.contract;

import com.notebookmanager.model.payload.Payload;
import com.notebookmanager.model.Aluno;
import com.notebookmanager.model.enums.Curso;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class PayloadJsonTest {

    @Autowired
    private JacksonTester<Payload> json;


    @Test
    void payloadSerializationTest() throws IOException {

        Aluno aluno = new Aluno(1,"Caio Gandara", "22415616", "caio.cgs@puccampinas.edu.br", "(19)99414-8554",
                Curso.ENFERMAGEM, LocalDateTime.of(2010, 12, 30, 12, 14, 22),
                LocalDateTime.of(2010, 12, 30, 12, 14, 22));

        Payload payload = new Payload(HttpStatus.OK, aluno, null);

        assertThat(json.write(payload)).isStrictlyEqualToJson("payload.json");
    }

}
