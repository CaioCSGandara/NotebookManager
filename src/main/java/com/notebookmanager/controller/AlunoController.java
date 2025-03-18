package com.notebookmanager.controller;

import com.notebookmanager.exception.AlunoJaExistenteException;
import com.notebookmanager.exception.AlunoNaoEncontradoException;
import com.notebookmanager.exception.ValidationException;
import com.notebookmanager.model.entities.Aluno;
import com.notebookmanager.model.repositories.AlunoRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    private ResponseEntity<Void> createAluno(@RequestBody @Valid Aluno aluno, BindingResult bindingResult, UriComponentsBuilder ucb) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException("Erro de validação ao criar Aluno.");
        }
        if(alunoRepository.existsByRa(aluno.getRa())) {
            throw new AlunoJaExistenteException();
        }

        Aluno savedAluno = alunoRepository.save(aluno);

        URI locationOfNewAluno = ucb
                .path("alunos/{id}")
                .buildAndExpand(savedAluno.getId())
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

    @PutMapping("/{id}")
    private ResponseEntity<Void> updateAluno(@RequestBody @Valid Aluno alunoUpdate, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            throw new ValidationException("Erro de validação ao atualizar o aluno.");
        }

        Optional<Aluno> aluno = alunoRepository.findByRa(alunoUpdate.getRa());
        if(aluno.isEmpty()) {
            throw new AlunoNaoEncontradoException();
    }
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


    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteAluno(@PathVariable Integer id) {

        if(alunoRepository.existsById(id)) {
            alunoRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }
        throw new AlunoNaoEncontradoException();
    }

}
