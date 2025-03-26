package com.notebookmanager.service;

import com.notebookmanager.infra.exception.RecursoNaoEncontradoException;
import com.notebookmanager.model.Notebook;
import com.notebookmanager.model.enums.StatusNotebook;
import com.notebookmanager.model.repositories.NotebookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class NotebookServiceSuccessTest {
    @Autowired
    private NotebookService notebookService;
    @Autowired
    private NotebookRepository notebookRepository;

    @Test
    public void retornaNotebookPorId() {
        Notebook notebook = notebookService.encontraNotebookPorId(1);
        assertThat(notebook.getId()).isEqualTo(1);
        assertThat(notebook.getPatrimonio()).isEqualTo("49103423");
    }

    @Test
    @DirtiesContext
    public void cadastraNotebook() {
        Notebook notebook = new Notebook("MacBook Pro", "13518310", StatusNotebook.DISPONIVEL,
                12, LocalDateTime.now());
        Notebook savedNotebook = notebookService.cadastrarNotebook(notebook);
        assertEquals(notebook, savedNotebook);
    }

    @Test
    public void listaNotebooksDefault() {
        List<Notebook> listaNotebooks = notebookService.listaPaginaDeNotebooks(PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.ASC, "status")));
        assertThat(listaNotebooks.size()).isEqualTo(3);
        assertThat(listaNotebooks.get(0).getPatrimonio()).isEqualTo("12309845");
        assertThat(listaNotebooks.get(1).getPatrimonio()).isEqualTo("49103423");
        assertThat(listaNotebooks.get(2).getPatrimonio()).isEqualTo("98341099");

    }

    @Test
    @DirtiesContext
    public void listaNotebooksVazia() {
        notebookRepository.deleteAll();
        List<Notebook> listaNotebooks = notebookService.listaPaginaDeNotebooks(PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.ASC, "status")));

        assertThat(listaNotebooks.size()).isEqualTo(0);
    }

    @Test
    @DirtiesContext
    public void alteraStatusNotebook() {

        StatusNotebook[] ordemDeAlteracao = {StatusNotebook.AFASTADO, StatusNotebook.DISPONIVEL, StatusNotebook.EMPRESTADO, StatusNotebook.DISPONIVEL};
        HashMap<String, StatusNotebook> field = new HashMap<>();
        for (StatusNotebook novoStatus : ordemDeAlteracao) {
            field.clear();
            field.put("status", novoStatus);
            notebookService.alteraStatusNotebook(1, field);
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


