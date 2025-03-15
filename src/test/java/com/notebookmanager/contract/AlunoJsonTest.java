package com.notebookmanager.contract;

import com.notebookmanager.generator.AlunoGenerator;
import com.notebookmanager.model.entities.Aluno;
import com.notebookmanager.model.entities.enums.Curso;
import org.assertj.core.data.TemporalUnitOffset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class AlunoJsonTest {

    @Autowired
    private JacksonTester<Aluno> json;

    @Autowired
    private JacksonTester<List<Aluno>> jsonList;

    @Test
    void alunoSerializationTest() throws IOException {

        Aluno aluno = AlunoGenerator.getAluno();

        assertThat(json.write(aluno)).isStrictlyEqualToJson("aluno.json");
    }

    @Test
    void alunoDeserializationTest() throws IOException {
        String expected = """
                {
                "id": null,
                "nome": "Caio Gandara",
                "ra": "22415616",
                "email": "caio.cgs@puccampinas.edu.br",
                "telefone": "(19)99414-8554",
                "curso": "ENFERMAGEM",
                "ultimoLogin": "2010-12-30T12:14:22",
                "atualizadoEm": "2010-12-30T12:14:22"
                }""";

        assertThat(json.parseObject(expected)).isEqualTo(AlunoGenerator.getAluno());

    }


    @Test
    void alunoListSerializationTest() throws IOException {

        List<Aluno> lista = AlunoGenerator.getListaAlunos();

        assertThat(jsonList.write(lista)).isStrictlyEqualToJson("aluno-list.json");

    }

    @Test
    void alunoListDeserialitazionTest() throws IOException {
        String expected = """
                [
                  {
                    "id": null,
                    "nome": "Julio Correa",
                    "ra": "09135616",
                    "email": "jcorrea@puccampinas.edu.br",
                    "telefone": "(19)90914-3014",
                    "curso": "MEDICINA",
                    "ultimoLogin": "2012-11-10T21:12:37",
                    "atualizadoEm": "2012-11-10T21:12:37"
                  },
                  {
                    "id": null,
                    "nome": "Maria Ferreira",
                    "ra": "03781923",
                    "email": "maria.ferreira@puccampinas.edu.br",
                    "telefone": "(19)90814-2314",
                    "curso": "TERAPIA_OCUPACIONAL",
                    "ultimoLogin": "2021-08-12T21:21:45",
                    "atualizadoEm": "2021-08-12T21:21:45"
                  },
                  {
                    "id": null,
                    "nome": "Fernando Pontes",
                    "ra": "90174823",
                    "email": "fernandohpontes@puccampinas.edu.br",
                    "telefone": "(19)83914-0945",
                    "curso": "BIOMEDICINA",
                    "ultimoLogin": "2013-01-10T21:12:37",
                    "atualizadoEm": "2013-01-10T21:12:37"
                  }
                ]
                """;

        List<Aluno> lista = AlunoGenerator.getListaAlunos();

        assertThat(jsonList.parseObject(expected)).isEqualTo(lista);
    }
}
