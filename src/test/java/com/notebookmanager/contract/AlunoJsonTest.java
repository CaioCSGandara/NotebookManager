package com.notebookmanager.contract;

import com.notebookmanager.model.Aluno;
import com.notebookmanager.model.dto.createfields.AlunoCreateFields;
import com.notebookmanager.model.enums.Curso;
import com.notebookmanager.model.dto.updatefields.AlunoUpdateFields;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class AlunoJsonTest {

    @Autowired
    private JacksonTester<Aluno> json;

    @Autowired
    private JacksonTester<AlunoCreateFields> jsonCreate;

    @Autowired
    private JacksonTester<AlunoUpdateFields> jsonUpdate;

    @Autowired
    private JacksonTester<List<Aluno>> jsonList;




    @Test
    void alunoSerializationTest() throws IOException {

        Aluno aluno = new Aluno(1,"Caio Gandara", "22415616", "caio.cgs@puccampinas.edu.br", "(19)99414-8554",
                Curso.ENFERMAGEM);


        assertThat(json.write(aluno)).isStrictlyEqualToJson("aluno.json");
    }

    @Test
    void alunoListSerializationTest() throws IOException {

        List<Aluno>listaAlunos = new ArrayList<Aluno>();

        listaAlunos.add(new Aluno(1,"Julio Correa", "09135616", "jcorrea@puccampinas.edu.br", "(19)90914-3014",
                Curso.MEDICINA));
        listaAlunos.add(new Aluno(2,"Maria Ferreira", "03781923", "maria.ferreira@puccampinas.edu.br", "(19)90814-2314",
                Curso.TERAPIA_OCUPACIONAL));
        listaAlunos.add(new Aluno(3,"Fernando Pontes", "90174823", "fernandohpontes@puccampinas.edu.br", "(19)83914-0945",
                Curso.BIOMEDICINA));

        assertThat(jsonList.write(listaAlunos)).isStrictlyEqualToJson("aluno-list.json");

    }

    @Test
    void alunoCreateFieldsDeserializationTest() throws IOException {
        String expected = """
                {
                "nome": "Caio Gandara",
                "ra": "22415616",
                "email": "caio.cgs@puccampinas.edu.br",
                "telefone": "(19)99414-8554",
                "curso": "ENFERMAGEM"
                }""";

        assertThat(jsonCreate.parseObject(expected)).isEqualTo(new AlunoCreateFields("Caio Gandara", "22415616", "caio.cgs@puccampinas.edu.br",
                "(19)99414-8554", Curso.ENFERMAGEM));

    }


    @Test
    void alunoUpdateFieldsDeserializationTest() throws IOException {
        String expected = """
                {
                "nome": "Caio Gandara",
                "telefone": "(19)99414-8554",
                "curso": "ENFERMAGEM"
                }""";

        assertThat(jsonUpdate.parseObject(expected)).isEqualTo(new AlunoUpdateFields("Caio Gandara", "(19)99414-8554", Curso.ENFERMAGEM));

    }



}
