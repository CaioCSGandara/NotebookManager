package com.notebookmanager.controller;

import com.notebookmanager.exception.AlunoJaExistenteException;
import com.notebookmanager.exception.AlunoNaoEncontradoException;
import com.notebookmanager.model.entities.Aluno;
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
import java.util.Optional;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoRepository alunoRepository;

    private AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }


    @GetMapping("/{id}")
    private ResponseEntity<Optional<Aluno>> findById(@PathVariable Integer id) {
        Optional<Aluno> aluno = alunoRepository.findById(id);
        if(aluno.isPresent()) {
            return ResponseEntity.ok(aluno);
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping
    private ResponseEntity<Void> createAluno(@RequestBody @Valid Aluno aluno, UriComponentsBuilder ucb) {
        if(!alunoRepository.existsByRa(aluno.getRa())) {
            Aluno savedAluno = alunoRepository.save(aluno);

            URI locationOfNewAluno = ucb
                    .path("alunos/{id}")
                    .buildAndExpand(savedAluno.getId())
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

    @PutMapping("/{id}")
    private ResponseEntity<Void> updateAluno(@PathVariable Integer id, @RequestBody @Valid Aluno alunoUpdate) {
        Optional<Aluno> aluno = alunoRepository.findById(id);
        if(aluno.isPresent()) {
            Aluno alunoAtualizado = new Aluno(
                aluno.get().getId(),
                alunoUpdate.getNome(),
                aluno.get().getRa(),
                aluno.get().getEmail(),
                alunoUpdate.getTelefone(),
                alunoUpdate.getCurso(),
                aluno.get().getUltimoLogin(),
                alunoUpdate.getAtualizadoEm()
        );

        alunoRepository.save(alunoAtualizado);

        return ResponseEntity.noContent().build();
    }
    throw new AlunoNaoEncontradoException();
}


    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteAluno(@PathVariable Integer id) {

        if(alunoRepository.existsById(id)) {
            alunoRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }
        throw new AlunoNaoEncontradoException();
    }

}
