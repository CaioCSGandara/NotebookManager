package com.notebookmanager.service;

import com.notebookmanager.exception.RecursoJaExistenteException;
import com.notebookmanager.exception.RecursoNaoEncontradoException;
import com.notebookmanager.model.Aluno;
import com.notebookmanager.model.repositories.AlunoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public Aluno encontrarAlunoPorId(Integer id) {

        Optional<Aluno> aluno = alunoRepository.findById(id);
        if (aluno.isEmpty()) {
            throw new RecursoNaoEncontradoException("Aluno não encontrado.");
        }
        return aluno.get();
    }

    public Aluno cadastrarAluno(Aluno aluno) {
        if(alunoRepository.existsByRa(aluno.getRa())) {
            throw new RecursoJaExistenteException("O Aluno com este RA já está cadastrado.");
        }
        return alunoRepository.save(aluno);
    }

    public List<Aluno> listaPaginaDeAlunos(Pageable pageable) {

        Page<Aluno> alunoPage = alunoRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "nome"))));

        return alunoPage.getContent();
    }
    
    public void atualizaDadosDoAlunoPorId(Integer id, Aluno alunoNovosDados) {
        
        Aluno alunoAtualizado = encontrarAlunoPorId(id);
        
        alunoAtualizado.setNome(alunoNovosDados.getNome());
        alunoAtualizado.setTelefone(alunoNovosDados.getTelefone());
        alunoAtualizado.setCurso(alunoNovosDados.getCurso());
        alunoAtualizado.setAtualizadoEm(alunoNovosDados.getAtualizadoEm());
        
        alunoRepository.save(alunoAtualizado);
    }

    public void deletaAlunoPorId(Integer id) {
        encontrarAlunoPorId(id);
        alunoRepository.deleteById(id);
    }
}
