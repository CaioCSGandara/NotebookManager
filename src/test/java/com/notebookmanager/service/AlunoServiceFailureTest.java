package com.notebookmanager.service;

import com.notebookmanager.exception.RecursoJaExistenteException;
import com.notebookmanager.exception.RecursoNaoEncontradoException;
import com.notebookmanager.model.Aluno;
import com.notebookmanager.model.enums.Curso;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AlunoServiceFailureTest {

    @Autowired
    private AlunoService alunoService;


    @Test
    void naoRetornaAlunoComIdInvalido() {

        RecursoNaoEncontradoException exception =  Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> {
            alunoService.encontrarAlunoPorId(99);
        });

        assertThat(exception.getMessage()).isEqualTo("Aluno não encontrado.");
    }

    @Test
    void naoCadastraAlunoComRaRepetido() {

        RecursoJaExistenteException exception =  Assertions.assertThrows(RecursoJaExistenteException.class, () -> {
            alunoService.cadastrarAluno(new Aluno(1,"Julio Correa", "09135616", "jcorrea@puccampinas.edu.br", "(19)90914-3014",
                    Curso.MEDICINA, LocalDateTime.of(2012, 11, 10, 21, 12, 37),
                    LocalDateTime.of(2012, 11, 10, 21, 12, 37)));
        });

        assertThat(exception.getMessage()).isEqualTo("O Aluno com este RA já está cadastrado.");
    }

    @Test
    void naoAlteraAlunoComIdInvalido() {

        RecursoNaoEncontradoException exception =  Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> {
            alunoService.atualizaDadosDoAlunoPorId(90,
                    new Aluno(90, "Josue Nao Existe No Banco", "02020202", "josuenenb@puccampinas.edu.br", "(19)93123-4231",
                    Curso.ODONTOLOGIA, LocalDateTime.of(2012, 11, 10, 21, 12, 37),
                    LocalDateTime.of(2012, 11, 10, 21, 12, 37)));
        });

        assertThat(exception.getMessage()).isEqualTo("Aluno não encontrado.");
    }

    @Test
    void naoDeletaAlunoComIdInvalido() {

        RecursoNaoEncontradoException exception =  Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> {
            alunoService.deletaAlunoPorId(31);
        });

        assertThat(exception.getMessage()).isEqualTo("Aluno não encontrado.");
    }

}
