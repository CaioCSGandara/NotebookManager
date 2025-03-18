package com.notebookmanager.contract;

import com.notebookmanager.model.entities.Aluno;
import com.notebookmanager.model.entities.enums.Curso;
import lombok.Getter;
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

    @Getter
    private static Aluno aluno = null;
    @Getter
    private static List<Aluno> listaAlunos = null;

    static {
        aluno = new Aluno(1,"Caio Gandara", "22415616", "caio.cgs@puccampinas.edu.br", "(19)99414-8554",
                Curso.ENFERMAGEM, LocalDateTime.of(2010, 12, 30, 12, 14, 22),
                LocalDateTime.of(2010, 12, 30, 12, 14, 22));

        listaAlunos = new ArrayList<Aluno>();

        listaAlunos.add(new Aluno(1,"Julio Correa", "09135616", "jcorrea@puccampinas.edu.br", "(19)90914-3014",
                Curso.MEDICINA, LocalDateTime.of(2012, 11, 10, 21, 12, 37),
                LocalDateTime.of(2012, 11, 10, 21, 12, 37)));
        listaAlunos.add(new Aluno(2,"Maria Ferreira", "03781923", "maria.ferreira@puccampinas.edu.br", "(19)90814-2314",
                Curso.TERAPIA_OCUPACIONAL, LocalDateTime.of(2021, 8, 12, 21, 21, 45),
                LocalDateTime.of(2021, 8, 12, 21, 21, 45)));
        listaAlunos.add(new Aluno(3,"Fernando Pontes", "90174823", "fernandohpontes@puccampinas.edu.br", "(19)83914-0945",
                Curso.BIOMEDICINA, LocalDateTime.of(2013, 1, 10, 21, 12, 37),
                LocalDateTime.of(2013, 1, 10, 21, 12, 37)));
    }

    @Test
    void alunoSerializationTest() throws IOException {

        Aluno aluno = AlunoJsonTest.getAluno();

        assertThat(json.write(aluno)).isStrictlyEqualToJson("aluno.json");
    }

    @Test
    void alunoDeserializationTest() throws IOException {
        String expected = """
                {
                "id": 1,
                "nome": "Caio Gandara",
                "ra": "22415616",
                "email": "caio.cgs@puccampinas.edu.br",
                "telefone": "(19)99414-8554",
                "curso": "ENFERMAGEM",
                "ultimoLogin": "2010-12-30T12:14:22",
                "atualizadoEm": "2010-12-30T12:14:22"
                }""";

        assertThat(json.parseObject(expected)).isEqualTo(AlunoJsonTest.getAluno());

    }


    @Test
    void alunoListSerializationTest() throws IOException {

        List<Aluno> lista = AlunoJsonTest.getListaAlunos();

        assertThat(jsonList.write(lista)).isStrictlyEqualToJson("aluno-list.json");

    }

    @Test
    void alunoListDeserialitazionTest() throws IOException {
        String expected = """
                [
                  {
                    "id": 1,
                    "nome": "Julio Correa",
                    "ra": "09135616",
                    "email": "jcorrea@puccampinas.edu.br",
                    "telefone": "(19)90914-3014",
                    "curso": "MEDICINA",
                    "ultimoLogin": "2012-11-10T21:12:37",
                    "atualizadoEm": "2012-11-10T21:12:37"
                  },
                  {
                    "id": 2,
                    "nome": "Maria Ferreira",
                    "ra": "03781923",
                    "email": "maria.ferreira@puccampinas.edu.br",
                    "telefone": "(19)90814-2314",
                    "curso": "TERAPIA_OCUPACIONAL",
                    "ultimoLogin": "2021-08-12T21:21:45",
                    "atualizadoEm": "2021-08-12T21:21:45"
                  },
                  {
                    "id": 3,
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

        List<Aluno> lista = AlunoJsonTest.getListaAlunos();

        assertThat(jsonList.parseObject(expected)).isEqualTo(lista);
    }
}
