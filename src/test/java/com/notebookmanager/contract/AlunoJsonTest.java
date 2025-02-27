package com.notebookmanager.contract;

import com.notebookmanager.model.entities.Aluno;
import com.notebookmanager.model.entities.enums.Curso;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class AlunoJsonTest {

    @Autowired
    private JacksonTester<Aluno> json;

    @Test
    void alunoSerializationTest() throws IOException {
        Aluno aluno = new Aluno("Caio Gandara", "22415616", "caio.cgs@gmail.com", "(19)99414-8554",
                Curso.ENFERMAGEM, LocalDateTime.of(2010, 12, 30, 12, 14, 22), LocalDateTime.of(2010, 12, 30, 12, 14, 22));

        assertThat(json.write(aluno)).isStrictlyEqualToJson("aluno-contract.json");
    }

    @Test
    void alunoDeserializationTest() throws IOException {
        String expected = """
                {
                "id": null,
                "nome": "Caio Gandara",
                "ra": "22415616",
                "email": "caio.cgs@gmail.com",
                "telefone": "(19)99414-8554",
                "curso": "Enfermagem",
                "ultimoLogin": "2010-12-30T12:14:22",
                "atualizadoEm": "2010-12-30T12:14:22"
                }""";

        assertThat(json.parseObject(expected))
                .isEqualTo(new Aluno("Caio Gandara", "22415616", "caio.cgs@gmail.com", "(19)99414-8554",
                        Curso.ENFERMAGEM, LocalDateTime.of(2010, 12, 30, 12, 14, 22), LocalDateTime.of(2010, 12, 30, 12, 14, 22)));

    }
}
