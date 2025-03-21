package com.notebookmanager.contract;

import com.notebookmanager.model.Notebook;
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
public class NotebookJsonTest {

    @Autowired
    JacksonTester<Notebook> json;

    @Autowired
    JacksonTester<List<Notebook>> jsonList;

    @Getter
    private static Notebook notebook = null;
    @Getter
    private static List<Notebook> listaNotebooks = null;

    static {
        notebook = new Notebook(4, "Acer Aspire 5", "02134918", false,
                5, LocalDateTime.of(2025, 3, 17, 22, 34, 45));

        listaNotebooks = new ArrayList<>();

        listaNotebooks.add(new Notebook(1, "Asus Vivobook 5", "49103423", true,
                43,  LocalDateTime.of(2021, 6, 30, 9, 14, 9)));
        listaNotebooks.add(new Notebook(2, "Acer Aspire 5", "98341099", false,
                19, LocalDateTime.of(2023,1,5,14,12,20)));
        listaNotebooks.add(new Notebook(3, "Acer Nitro", "12309845", true,
                130, LocalDateTime.of(2023, 11, 20, 18, 12, 21)));

        System.out.println(listaNotebooks.get(0));
        System.out.println(listaNotebooks.get(1));
        System.out.println(listaNotebooks.get(2));
    }

    @Test
    void notebookSerializationTest() throws IOException {

        assertThat(json.write(notebook)).isStrictlyEqualToJson("notebook.json");

    }

    @Test
    void notebookDeserializationTest() throws IOException {
        String expected = """
                {
                    "id": 4,
                    "modelo": "Acer Aspire 5",
                    "patrimonio": "02134918",
                    "emprestado": false,
                    "qtdEmprestimos": 5,
                    "atualizadoEm": "2025-03-17T22:34:45"
                }
                """;
        assertThat(json.parseObject(expected)).isEqualTo(notebook);
    }

    @Test
    void notebookListSerializationTest() throws IOException {
        assertThat(jsonList.write(listaNotebooks)).isStrictlyEqualToJson("notebook-list.json");
    }

    @Test
    void notebookListDeserializationTest() throws IOException {
        String expected = """
                [
                  {
                    "id": 1,
                    "modelo": "Asus Vivobook 5",
                    "patrimonio": "49103423",
                    "emprestado": true,
                    "qtdEmprestimos": 43,
                    "atualizadoEm": "2021-06-30T09:14:09"
                  },
                  {
                    "id": 2,
                    "modelo": "Acer Aspire 5",
                    "patrimonio": "98341099",
                    "emprestado": false,
                    "qtdEmprestimos": 19,
                    "atualizadoEm": "2023-01-05T14:12:20"
                  },
                  {
                    "id": 3,
                    "modelo": "Acer Nitro",
                    "patrimonio": "12309845",
                    "emprestado": true,
                    "qtdEmprestimos": 130,
                    "atualizadoEm": "2023-11-20T18:12:21"
                  }
                ]
                """;

        assertThat(jsonList.parseObject(expected)).isEqualTo(listaNotebooks);
    }
}
