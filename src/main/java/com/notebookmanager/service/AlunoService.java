package com.notebookmanager.service;

import com.notebookmanager.infra.exception.RecursoJaExistenteException;
import com.notebookmanager.infra.exception.RecursoNaoEncontradoException;
import com.notebookmanager.model.Aluno;
import com.notebookmanager.model.dto.createfields.AlunoCreateFields;
import com.notebookmanager.model.repositories.AlunoRepository;
import com.notebookmanager.model.dto.updatefields.AlunoUpdateFields;
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

    public Aluno cadastrarAluno(AlunoCreateFields alunoCreateFields) {
        if(alunoRepository.existsByRa(alunoCreateFields.getRa()) || alunoRepository.existsByEmail(alunoCreateFields.getEmail())) {
            throw new RecursoJaExistenteException("O Aluno com este RA e/ou e-mail já está cadastrado.");
        }
        
        Aluno aluno = new Aluno(alunoCreateFields.getNome(), alunoCreateFields.getRa(), alunoCreateFields.getEmail(),
                alunoCreateFields.getTelefone(), alunoCreateFields.getCurso());
        return alunoRepository.save(aluno);
    }

    public List<Aluno> listaPaginaDeAlunos(Pageable pageable) {

        Page<Aluno> alunoPage = alunoRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "nome"))));

        return alunoPage.getContent();
    }
    
    public void atualizaDadosDoAlunoPorId(Integer id, AlunoUpdateFields alunoUpdateFields) {
        
        Optional<Aluno> optAlunoAtualizado = alunoRepository.findById(id);

        if (optAlunoAtualizado.isEmpty()) {
            throw new RecursoNaoEncontradoException("Aluno não encontrado.");
        }

        Aluno alunoAtualizado = optAlunoAtualizado.get();

        alunoAtualizado.setNome(alunoUpdateFields.getNome());
        alunoAtualizado.setTelefone(alunoUpdateFields.getTelefone());
        alunoAtualizado.setCurso(alunoUpdateFields.getCurso());
        
        alunoRepository.save(alunoAtualizado);
    }

    public void deletaAlunoPorId(Integer id) {
        if(!alunoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Aluno não encontrado.");
        }
        alunoRepository.deleteById(id);
    }
}
