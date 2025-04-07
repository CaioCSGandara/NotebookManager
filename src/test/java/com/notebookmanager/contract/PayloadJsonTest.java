package com.notebookmanager.contract;

import com.notebookmanager.model.dto.Payload;
import com.notebookmanager.model.dto.createfields.AlunoCreateFields;
import com.notebookmanager.model.enums.Curso;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class PayloadJsonTest {

    @Autowired
    private JacksonTester<Payload> json;


    @Test
    void payloadSerializationTest() throws IOException {

        AlunoCreateFields alunoCreateFields = new AlunoCreateFields("Caio Gandara", "22415616", "caio.cgs@puccampinas.edu.br", "(19)99414-8554",
                Curso.ENFERMAGEM);

        Payload payload = new Payload(HttpStatus.OK, alunoCreateFields, null);

        assertThat(json.write(payload)).isStrictlyEqualToJson("payload.json");
    }

}
