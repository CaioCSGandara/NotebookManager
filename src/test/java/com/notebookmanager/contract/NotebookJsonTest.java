package com.notebookmanager.contract;

import com.notebookmanager.model.Notebook;
import com.notebookmanager.model.dto.createfields.NotebookCreateFields;
import com.notebookmanager.model.enums.StatusNotebook;
import com.notebookmanager.model.dto.updatefields.NotebookUpdateFields;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class NotebookJsonTest {

    @Autowired
    JacksonTester<Notebook> json;

    @Autowired
    JacksonTester<NotebookCreateFields> jsonCreate;

    @Autowired
    JacksonTester<NotebookUpdateFields> jsonUpdate;

    @Autowired
    JacksonTester<List<Notebook>> jsonList;


    @Test
    void notebookSerializationTest() throws IOException {

        Notebook notebook = new Notebook(4, "021349", StatusNotebook.DISPONIVEL);

        assertThat(json.write(notebook)).isStrictlyEqualToJson("notebook.json");

    }

    @Test
    void notebookListSerializationTest() throws IOException {

        List<Notebook> listaNotebooks = new ArrayList<>();

        listaNotebooks.add(new Notebook(1, "491034", StatusNotebook.DISPONIVEL));
        listaNotebooks.add(new Notebook(2, "983410", StatusNotebook.EMPRESTADO));
        listaNotebooks.add(new Notebook(3,"123098", StatusNotebook.AFASTADO));

        assertThat(jsonList.write(listaNotebooks)).isStrictlyEqualToJson("notebook-list.json");
    }


    @Test
    void notebookCreateFieldsDeserializationTest() throws IOException {
        String expected = """
                {
                    "modelo": "Acer Aspire 5",
                    "patrimonio": "021349"
                }
                """;
        assertThat(jsonCreate.parseObject(expected)).isEqualTo(new NotebookCreateFields("021349"));
    }


    @Test
    void notebookUpdateFieldsDeserializationTest() throws IOException {
        String expected = """
                {
                    "status": "DISPONIVEL"
                }
                """;
        assertThat(jsonUpdate.parseObject(expected)).isEqualTo(new NotebookUpdateFields(StatusNotebook.DISPONIVEL));
    }

}
