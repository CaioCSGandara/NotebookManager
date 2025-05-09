package com.notebookmanager.contract;

import com.notebookmanager.model.Aluno;
import com.notebookmanager.model.Notebook;
import com.notebookmanager.model.Reserva;
import com.notebookmanager.model.dto.createfields.ReservaCreateFields;
import com.notebookmanager.model.dto.updatefields.ReservaUpdateFields;
import com.notebookmanager.model.enums.Curso;
import com.notebookmanager.model.enums.StatusNotebook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ReservaJsonTest {

    @Autowired
    JacksonTester<Reserva> json;

    @Autowired
    JacksonTester<List<Reserva>> jsonList;

    @Autowired
    JacksonTester<ReservaCreateFields> jsonCreate;

    @Autowired
    JacksonTester<ReservaUpdateFields> jsonUpdate;

    @Test
    public void reservaSerializationTest() throws Exception {
        Aluno aluno = new Aluno(1, "Julio Correa", "09135616", "jcorrea@puccampinas.edu.br",
                "(19)90914-3014", Curso.MEDICINA, "senha123");

        Notebook notebook = new Notebook(2, "983410", StatusNotebook.EMPRESTADO);


        Reserva reserva = new Reserva(1, aluno, notebook,
                LocalDateTime.of(2022, 5, 23, 15, 32, 21),
                LocalDateTime.of(2022,5,23,18,22,59));

        assertThat(json.write(reserva)).isEqualToJson("reserva.json");
    }

    @Test
    public void listaReservasDeserializationTest() throws Exception {

        Aluno aluno1 = new Aluno(1, "Julio Correa", "09135616", "jcorrea@puccampinas.edu.br", "(19)90914-3014", Curso.MEDICINA, "senha123");
        Notebook notebook1 = new Notebook(1, "491034", StatusNotebook.EMPRESTADO);
        Reserva reserva1 = new Reserva(1, aluno1, notebook1,
                LocalDateTime.of(2021, 12, 21, 19, 50, 13),
                LocalDateTime.of(2021, 12, 21, 22, 33, 4));

        Aluno aluno2 = new Aluno(2, "Maria Ferreira", "03781923", "maria.ferreira@puccampinas.edu.br", "(19)90814-2314", Curso.TERAPIA_OCUPACIONAL, "senha123");
        Notebook notebook2 = new Notebook(2, "983410", StatusNotebook.EMPRESTADO);
        Reserva reserva2 = new Reserva(2, aluno2, notebook2,
                LocalDateTime.of(2021, 12, 21, 9, 13, 25),
                LocalDateTime.of(2021, 12, 21, 12, 54, 47));

        Aluno aluno3 = new Aluno(3, "Fernando Pontes", "90174823", "fernandohpontes@puccampinas.edu.br", "(19)83914-0945", Curso.BIOMEDICINA, "senha123");
        Notebook notebook3 = new Notebook(3, "123098", StatusNotebook.EMPRESTADO);
        Reserva reserva3 = new Reserva(3, aluno3, notebook3,
                LocalDateTime.of(2021, 12, 21, 12, 30, 23),
                LocalDateTime.of(2021, 12, 21, 16, 14, 54));


        List<Reserva> reservas = new ArrayList<>();
        reservas.add(reserva1);
        reservas.add(reserva2);
        reservas.add(reserva3);

        assertThat(jsonList.write(reservas)).isEqualToJson("reserva-list.json");
    }

    @Test
    public void reservaCreateFieldsDeserializationTest() throws Exception {
        String expected = """
                {
                    "alunoRa": "81940283",
                    "notebookPatrimonio": "381035"
                }
                """;

        assertThat(jsonCreate.parseObject(expected)).isEqualTo(new ReservaCreateFields("81940283", "381035"));
    }

    @Test
    public void reservaUpdateFieldsDeserializationTest() throws Exception {
        String expected = """
                {
                    "notebookPatrimonio": "381035"
                }
                """;

        assertThat(jsonUpdate.parseObject(expected)).isEqualTo(new ReservaUpdateFields("381035"));
    }
}
