package com.notebookmanager.service;

import com.notebookmanager.infra.exception.RecursoJaExistenteException;
import com.notebookmanager.infra.exception.RecursoNaoEncontradoException;
import com.notebookmanager.infra.exception.ValidationException;
import com.notebookmanager.model.dto.createfields.NotebookCreateFields;
import com.notebookmanager.model.enums.StatusNotebook;
import com.notebookmanager.model.dto.updatefields.NotebookUpdateFields;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class NotebookServiceFailureTest {

    @Autowired
    private NotebookService notebookService;

    @Test
    public void naoRetornaNotebookComIdInvalido() {

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
                () -> {
            notebookService.encontraNotebookPorId(312);
                });

        assertThat(exception.getMessage()).isEqualTo("Notebook não encontrado.");
    }

    @Test
    public void naoRetornaNotebookComPatrimonioInvalido() {

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
                () -> {
                    notebookService.encontraNotebookPorPatrimonio("000000");
                });

        assertThat(exception.getMessage()).isEqualTo("Notebook não encontrado.");
    }

    @Test
    public void naoCadastraNotebookComPatrimonioRepetido() {

        RecursoJaExistenteException exception = assertThrows(RecursoJaExistenteException.class,
                () -> {
                notebookService.cadastrarNotebook(new NotebookCreateFields("491034"));
                });

        assertThat(exception.getMessage()).isEqualTo("O Notebook com este patrimonio já está cadastrado.");
    }

    @Test
    public void naoAlteraStatusDeManeiraInvalida() {

        NotebookUpdateFields disponivelParaDisponivel = new NotebookUpdateFields(StatusNotebook.DISPONIVEL);

        ValidationException disponivelParaDisponivelException = assertThrows(ValidationException.class,
                ()-> {
                    notebookService.alteraStatusNotebook(1, disponivelParaDisponivel);
                });
        assertThat(disponivelParaDisponivelException.getMessage()).isEqualTo("Para tornar um notebook DISPONIVEL, ele deve estar EMPRESTADO ou AFASTADO.");

        NotebookUpdateFields naoDisponivelParaNaoDisponivel = new NotebookUpdateFields(StatusNotebook.AFASTADO);

        ValidationException naoDisponivelParaNaoDisponivelException = assertThrows(ValidationException.class,
                ()-> {
            notebookService.alteraStatusNotebook(4, naoDisponivelParaNaoDisponivel);
                });

        assertThat(naoDisponivelParaNaoDisponivelException.getMessage()).isEqualTo("Para tornar um notebook EMPRESTADO ou AFASTADO, ele deve estar DISPONIVEL.");

    }


    @Test
    public void naoDeletaNotebookComIdInvalido() {

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
                ()-> {
            notebookService.deletaNotebookPorId(314);
                });

        assertThat(exception.getMessage()).isEqualTo("Notebook não encontrado.");
    }
}
