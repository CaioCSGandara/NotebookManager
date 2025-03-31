package com.notebookmanager.service;

import com.notebookmanager.infra.exception.RecursoNaoEncontradoException;
import com.notebookmanager.model.Notebook;
import com.notebookmanager.model.dto.createfields.NotebookCreateFields;
import com.notebookmanager.model.enums.StatusNotebook;
import com.notebookmanager.model.repositories.NotebookRepository;
import com.notebookmanager.model.dto.updatefields.NotebookUpdateFields;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class NotebookServiceSuccessTest {
    @Autowired
    private NotebookService notebookService;

    @Test
    public void retornaNotebookPorId() {
        Notebook notebook = notebookService.encontraNotebookPorId(1);
        assertThat(notebook.getId()).isEqualTo(1);
        assertThat(notebook.getPatrimonio()).isEqualTo("491034");
    }

    @Test
    public void retornaNotebookPorPatrimonio() {
        Notebook notebook = notebookService.encontraNotebookPorPatrimonio("491034");
        assertThat(notebook.getPatrimonio()).isEqualTo("491034");
        assertThat(notebook.getStatus()).isEqualTo(StatusNotebook.DISPONIVEL);
    }

    @Test
    @DirtiesContext
    public void cadastraNotebook() {
        NotebookCreateFields notebookCreateFields = new NotebookCreateFields("135183");
        Notebook savedNotebook = notebookService.cadastrarNotebook(notebookCreateFields);
        assertThat(savedNotebook.getPatrimonio()).isEqualTo("135183");
    }

    @Test
    public void listaNotebooksDefault() {
        List<Notebook> listaNotebooks = notebookService.listaPaginaDeNotebooks(PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.ASC, "status")));
        assertThat(listaNotebooks.size()).isEqualTo(5);
        assertThat(listaNotebooks.get(0).getPatrimonio()).isEqualTo("123098");
        assertThat(listaNotebooks.get(1).getPatrimonio()).isEqualTo("491034");
        assertThat(listaNotebooks.get(2).getPatrimonio()).isEqualTo("930183");
        assertThat(listaNotebooks.get(3).getPatrimonio()).isEqualTo("398145");
        assertThat(listaNotebooks.get(4).getPatrimonio()).isEqualTo("983410");

    }


    @Test
    @DirtiesContext
    public void alteraStatusNotebook() {

        StatusNotebook[] ordemDeAlteracao = {StatusNotebook.AFASTADO, StatusNotebook.DISPONIVEL, StatusNotebook.EMPRESTADO, StatusNotebook.DISPONIVEL};
        for (StatusNotebook novoStatus : ordemDeAlteracao) {
            NotebookUpdateFields notebookUpdateFields = new NotebookUpdateFields(novoStatus);
            notebookService.alteraStatusNotebook(1, notebookUpdateFields);
            Notebook notebookAlterado = notebookService.encontraNotebookPorId(1);
            assertThat(notebookAlterado.getStatus()).isEqualTo(novoStatus);
        }

    }


    @Test
    @DirtiesContext
    public void deletaNotebookPorId() {
        notebookService.deletaNotebookPorId(1);

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
                () -> notebookService.encontraNotebookPorId(1));

        assertThat(exception.getMessage()).isEqualTo("Notebook não encontrado.");
    }
}


