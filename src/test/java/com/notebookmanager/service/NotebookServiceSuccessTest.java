package com.notebookmanager.service;

import com.notebookmanager.infra.exception.RecursoNaoEncontradoException;
import com.notebookmanager.model.Notebook;
import com.notebookmanager.model.createfields.NotebookCreateFields;
import com.notebookmanager.model.enums.StatusNotebook;
import com.notebookmanager.model.repositories.NotebookRepository;
import com.notebookmanager.model.updatefields.NotebookUpdateFields;
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
        assertThat(notebook.getPatrimonio()).isEqualTo("491034");
    }

    @Test
    @DirtiesContext
    public void cadastraNotebook() {
        NotebookCreateFields notebookCreateFields = new NotebookCreateFields("MacBook Pro", "135183");
        Notebook savedNotebook = notebookService.cadastrarNotebook(notebookCreateFields);
        assertThat(savedNotebook.getModelo()).isEqualTo("MacBook Pro");
        assertThat(savedNotebook.getPatrimonio()).isEqualTo("135183");
    }

    @Test
    public void listaNotebooksDefault() {
        List<Notebook> listaNotebooks = notebookService.listaPaginaDeNotebooks(PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.ASC, "status")));
        assertThat(listaNotebooks.size()).isEqualTo(3);
        assertThat(listaNotebooks.get(0).getPatrimonio()).isEqualTo("123098");
        assertThat(listaNotebooks.get(1).getPatrimonio()).isEqualTo("491034");
        assertThat(listaNotebooks.get(2).getPatrimonio()).isEqualTo("983410");

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
        for (StatusNotebook novoStatus : ordemDeAlteracao) {
            NotebookUpdateFields notebookUpdateFields = new NotebookUpdateFields(novoStatus);
            notebookService.alteraStatusNotebook(1, notebookUpdateFields);
            Notebook notebookAlterado = notebookService.encontraNotebookPorId(1);
            assertThat(notebookAlterado.getStatus()).isEqualTo(novoStatus);
        }

    }

    @Test
    @DirtiesContext
    public void incrementaQtdEmprestimos() {
        NotebookUpdateFields notebookUpdateFields = new NotebookUpdateFields(StatusNotebook.EMPRESTADO);
        Integer qtdAnterior = notebookRepository.findById(1).get().getQtdEmprestimos();
        notebookService.alteraStatusNotebook(1, notebookUpdateFields);
        Integer qtdAposIncremento = notebookRepository.findById(1).get().getQtdEmprestimos();
        assertThat(qtdAnterior).isEqualTo(qtdAposIncremento-1);
    }

    @Test
    @DirtiesContext
    public void naoIncrementaQtdEmprestimos() {
        NotebookUpdateFields notebookUpdateFields = new NotebookUpdateFields(StatusNotebook.AFASTADO);
        Integer qtdAnterior = notebookRepository.findById(1).get().getQtdEmprestimos();
        notebookService.alteraStatusNotebook(1, notebookUpdateFields);
        Integer qtdAposAlteracao = notebookRepository.findById(1).get().getQtdEmprestimos();
        assertThat(qtdAnterior).isEqualTo(qtdAposAlteracao);
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


