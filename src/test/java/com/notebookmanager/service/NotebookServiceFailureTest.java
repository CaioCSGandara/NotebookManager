package com.notebookmanager.service;

import com.notebookmanager.exception.RecursoJaExistenteException;
import com.notebookmanager.exception.RecursoNaoEncontradoException;
import com.notebookmanager.exception.ValidationException;
import com.notebookmanager.model.Notebook;
import com.notebookmanager.model.enums.StatusNotebook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class NotebookServiceFailureTest {

    @Autowired
    private NotebookService alunoService;
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
    public void naoCadastraNotebookComPatrimonioRepetido() {

        RecursoJaExistenteException exception = assertThrows(RecursoJaExistenteException.class,
                () -> {
                notebookService.cadastrarNotebook(new Notebook(1, "Asus Vivobook 5", "49103423", StatusNotebook.DISPONIVEL,
                        43,  LocalDateTime.of(2021, 6, 30, 9, 14, 9)));
                });

        assertThat(exception.getMessage()).isEqualTo("O Notebook com este patrimonio já está cadastrado.");
    }

    @Test
    public void naoAlteraStatusDeManeiraInvalida() {

        HashMap<String, StatusNotebook> disponivelParaDisponivel = new HashMap<>();
        disponivelParaDisponivel.put("status", StatusNotebook.DISPONIVEL);

        ValidationException disponivelParaDisponivelException = assertThrows(ValidationException.class,
                ()-> {
                    alunoService.alteraStatusNotebook(1, disponivelParaDisponivel);
                });
        assertThat(disponivelParaDisponivelException.getMessage()).isEqualTo("Para tornar um notebook DISPONIVEL, ele deve estar EMPRESTADO ou AFASTADO.");

        HashMap<String, StatusNotebook> naoDisponivelParaNaoDisponivel = new HashMap<>();
        naoDisponivelParaNaoDisponivel.put("status", StatusNotebook.AFASTADO);

        ValidationException naoDisponivelParaNaoDisponivelException = assertThrows(ValidationException.class,
                ()-> {
            alunoService.alteraStatusNotebook(2, naoDisponivelParaNaoDisponivel);
                });

        assertThat(naoDisponivelParaNaoDisponivelException.getMessage()).isEqualTo("Para tornar um notebook EMPRESTADO ou AFASTADO, ele deve estar DISPONIVEL.");

    }

    @Test
    public void naoAtualizaStatusComNull() {
        HashMap<String, StatusNotebook> map = new HashMap<>();
        map.put("status", null);

        ValidationException exception = assertThrows(ValidationException.class,
                ()->{
            alunoService.alteraStatusNotebook(1, map);
                });

        assertThat(exception.getMessage()).isEqualTo("Novo status não encontrado em: {status=null}");
    }

    @Test
    public void naoDeletaNotebookComIdInvalido() {

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
                ()-> {
            alunoService.deletaNotebookPorId(314);
                });

        assertThat(exception.getMessage()).isEqualTo("Notebook não encontrado.");
    }
}
