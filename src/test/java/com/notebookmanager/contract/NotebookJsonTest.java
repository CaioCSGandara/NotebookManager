package com.notebookmanager.contract;

import com.notebookmanager.model.Notebook;
import com.notebookmanager.model.createfields.NotebookCreateFields;
import com.notebookmanager.model.enums.StatusNotebook;
import com.notebookmanager.model.updatefields.NotebookUpdateFields;
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

        Notebook notebook = new Notebook(4, "Acer Aspire 5", "02134918", StatusNotebook.DISPONIVEL,
                5, LocalDateTime.of(2025, 3, 17, 22, 34, 45));

        assertThat(json.write(notebook)).isStrictlyEqualToJson("notebook.json");

    }

    @Test
    void notebookListSerializationTest() throws IOException {

        List<Notebook> listaNotebooks = new ArrayList<>();

        listaNotebooks.add(new Notebook(1, "Asus Vivobook 5", "49103423", StatusNotebook.DISPONIVEL,
                43,  LocalDateTime.of(2021, 6, 30, 9, 14, 9)));
        listaNotebooks.add(new Notebook(2, "Acer Aspire 5", "98341099", StatusNotebook.EMPRESTADO,
                19, LocalDateTime.of(2023,1,5,14,12,20)));
        listaNotebooks.add(new Notebook(3, "Acer Nitro", "12309845", StatusNotebook.AFASTADO,
                130, LocalDateTime.of(2023, 11, 20, 18, 12, 21)));

        assertThat(jsonList.write(listaNotebooks)).isStrictlyEqualToJson("notebook-list.json");
    }


    @Test
    void notebookCreateFieldsDeserializationTest() throws IOException {
        String expected = """
                {
                    "modelo": "Acer Aspire 5",
                    "patrimonio": "02134918"
                }
                """;
        assertThat(jsonCreate.parseObject(expected)).isEqualTo(new NotebookCreateFields("Acer Aspire 5", "02134918"));
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
