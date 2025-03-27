package com.notebookmanager.service;

import com.notebookmanager.infra.exception.RecursoNaoEncontradoException;
import com.notebookmanager.model.Aluno;
import com.notebookmanager.model.createfields.AlunoCreateFields;
import com.notebookmanager.model.enums.Curso;
import com.notebookmanager.model.repositories.AlunoRepository;
import com.notebookmanager.model.updatefields.AlunoUpdateFields;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AlunoServiceSuccessTest {

    @Autowired
    private AlunoService alunoService;
    @Autowired
    private AlunoRepository alunoRepository;

    @Test
    void retornaAlunoPorId() {
        Aluno aluno = alunoService.encontrarAlunoPorId(1);
        assertThat(aluno.getId()).isEqualTo(1);
        assertThat(aluno.getNome()).isEqualTo("Julio Correa");
    }

    @Test
    @DirtiesContext
    void cadastraAluno() {
        AlunoCreateFields alunoCreateFields = new AlunoCreateFields("Mario Souza", "09318492", "marios2@puccampinas.edu.br", "(19)99083-2415",
                Curso.MEDICINA_VETERINARIA);

        Aluno alunoCadastrado = alunoService.cadastrarAluno(alunoCreateFields);

        assertThat(alunoCadastrado.getNome()).isEqualTo(alunoCreateFields.getNome());
    }

    @Test
    void listaAlunosDefault() {
        List<Aluno> listaAlunos = alunoService.listaPaginaDeAlunos(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nome")));
        assertThat(listaAlunos.size()).isEqualTo(3);
        assertThat(listaAlunos.get(0).getRa()).isEqualTo("90174823");
        assertThat(listaAlunos.get(1).getRa()).isEqualTo("09135616");
        assertThat(listaAlunos.get(2).getRa()).isEqualTo("03781923");
    }

    @Test
    @DirtiesContext
    void listaAlunosVazia() {

        alunoRepository.deleteAll();
        List<Aluno> listaAlunos = alunoService.listaPaginaDeAlunos(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nome")));
        assertThat(listaAlunos.size()).isEqualTo(0);
    }

    @Test
    @DirtiesContext
    void atualizaAlunoPorId() {
        AlunoUpdateFields alunoUpdateFields = new AlunoUpdateFields("Julio Correa das Neves", "(19)98312-3215",
                Curso.ODONTOLOGIA);

        alunoService.atualizaDadosDoAlunoPorId(1, alunoUpdateFields);

        Aluno alunoAtualizado = alunoService.encontrarAlunoPorId(1);

        assertThat(alunoAtualizado.getNome()).isEqualTo(alunoUpdateFields.getNome());
        assertThat(alunoAtualizado.getTelefone()).isEqualTo(alunoUpdateFields.getTelefone());
        assertThat(alunoAtualizado.getCurso()).isEqualTo(alunoUpdateFields.getCurso());
    }

    @Test
    @DirtiesContext
    void deletaAlunoPorId() {
        alunoService.deletaAlunoPorId(1);

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
                () -> alunoService.encontrarAlunoPorId(1));

        assertThat(exception.getMessage()).isEqualTo("Aluno não encontrado.");
    }



}
