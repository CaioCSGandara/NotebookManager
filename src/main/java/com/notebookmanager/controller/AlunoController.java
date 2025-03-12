package com.notebookmanager.controller;

import com.notebookmanager.model.entities.Aluno;
import com.notebookmanager.model.entities.enums.Curso;
import com.notebookmanager.model.repositories.AlunoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoRepository alunoRepository;

    private AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @GetMapping("/{requestedRa}")
    private ResponseEntity<Aluno> findByRa(@PathVariable String requestedRa) {
        Aluno aluno = alunoRepository.findByRa(requestedRa);
        if(aluno!=null) {
            return ResponseEntity.ok(aluno);
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping
    private ResponseEntity<Void> createAluno(@RequestBody Aluno newAlunoRequest, UriComponentsBuilder ucb) {
        Aluno savedAluno = alunoRepository.save(newAlunoRequest);

        URI locationOfNewAluno = ucb
                .path("alunos/{ra}")
                .buildAndExpand(savedAluno.getRa())
                .toUri();

        return ResponseEntity.created(locationOfNewAluno).build();
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
    private ResponseEntity<Void> updateAluno(@PathVariable String requestedRa, @RequestBody Aluno alunoUpdate) {
        Aluno aluno = alunoRepository.findByRa(requestedRa);
        if(aluno!=null) {
        Aluno alunoAtualizado = new Aluno(
                aluno.getId(),
                alunoUpdate.getNome(),
                aluno.getRa(),
                aluno.getEmail(),
                alunoUpdate.getTelefone(),
                Curso.toCurso(alunoUpdate.getCurso()),
                aluno.getUltimoLogin(),
                alunoUpdate.getAtualizadoEm()
        );

        alunoRepository.save(alunoAtualizado);

        return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
}

}
