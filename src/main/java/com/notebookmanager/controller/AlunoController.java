package com.notebookmanager.controller;

import com.notebookmanager.exception.AlunoJaExistenteException;
import com.notebookmanager.exception.AlunoNaoEncontradoException;
import com.notebookmanager.model.entities.Aluno;
import com.notebookmanager.model.entities.enums.Curso;
import com.notebookmanager.model.repositories.AlunoRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoRepository alunoRepository;

    private AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    //ra pode nao existir
    //runtime exception
    @GetMapping("/{requestedRa}")
    private ResponseEntity<Aluno> findByRa(@PathVariable String requestedRa) {
        Aluno aluno = alunoRepository.findByRa(requestedRa);
        if(aluno!=null) {
            return ResponseEntity.ok(aluno);
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping
    private ResponseEntity<Void> createAluno(@RequestBody @Valid Aluno aluno, UriComponentsBuilder ucb) {
        if(!alunoRepository.existsByRa(aluno.getRa())) {
            Aluno savedAluno = alunoRepository.save(aluno);

            URI locationOfNewAluno = ucb
                    .path("alunos/{ra}")
                    .buildAndExpand(savedAluno.getRa())
                    .toUri();

            return ResponseEntity.created(locationOfNewAluno).build();
        }
        throw new AlunoJaExistenteException();
    }


    @GetMapping
    private ResponseEntity<List<Aluno>> findAll(Pageable pageable)
    {
        Page<Aluno> alunoPage = alunoRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "nome"))));

        return ResponseEntity.ok(alunoPage.getContent());
    }

    @PutMapping("/{requestedRa}")
    private ResponseEntity<Void> updateAluno(@PathVariable String requestedRa, @RequestBody @Valid Aluno alunoUpdate) {
        Aluno aluno = alunoRepository.findByRa(requestedRa);
        if(aluno!=null) {
        Aluno alunoAtualizado = new Aluno(
                aluno.getId(),
                alunoUpdate.getNome(),
                aluno.getRa(),
                aluno.getEmail(),
                alunoUpdate.getTelefone(),
                alunoUpdate.getCurso(),
                aluno.getUltimoLogin(),
                alunoUpdate.getAtualizadoEm()
        );

        alunoRepository.save(alunoAtualizado);

        return ResponseEntity.noContent().build();
    }
    throw new AlunoNaoEncontradoException();
}


    @DeleteMapping("/{requestedRa}")
    private ResponseEntity<Void> deleteAluno(@PathVariable String requestedRa) {

        if(alunoRepository.existsByRa(requestedRa)) {
            alunoRepository.deleteByRa(requestedRa);
            return ResponseEntity.noContent().build();
        }
        throw new AlunoNaoEncontradoException();
    }

}
