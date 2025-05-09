package com.notebookmanager.contract;

import com.notebookmanager.model.Aluno;
import com.notebookmanager.model.Funcionario;
import com.notebookmanager.model.dto.createfields.AlunoCreateFields;
import com.notebookmanager.model.dto.createfields.FuncionarioLoginFields;
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
public class FuncionarioJsonTest {

    @Autowired
    private JacksonTester<Funcionario> json;

    @Autowired
    private JacksonTester<FuncionarioLoginFields> jsonCreate;


    @Test
    void funcionarioSerializationTest() throws IOException {

        Funcionario funcionario = new Funcionario(1,"José Pedro", "381014", "josep@empresa.edu.br",
                "senha0987");


        assertThat(json.write(funcionario)).isStrictlyEqualToJson("funcionario.json");
    }


    @Test
    void funcionarioLoginFieldsDeserializationTest() throws IOException {
        String expected = """
                        {
                          "id": 1,
                          "nome": "José Pedro",
                          "rf": "381014",
                          "email": "josep@empresa.edu.br",
                          "senha": "senha0987"
                        }""";

        assertThat(jsonCreate.parseObject(expected)).isEqualTo(new FuncionarioLoginFields("381014", "senha0987"));

    }




}
