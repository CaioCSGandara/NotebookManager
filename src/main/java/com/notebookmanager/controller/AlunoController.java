package com.notebookmanager.controller;

import com.notebookmanager.exception.RecursoJaExistenteException;
import com.notebookmanager.exception.RecursoNaoEncontradoException;
import com.notebookmanager.exception.ValidationException;
import com.notebookmanager.model.payload.Payload;
import com.notebookmanager.model.Aluno;
import com.notebookmanager.model.repositories.AlunoRepository;
import com.notebookmanager.service.AlunoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;


    @GetMapping("/{id}")
    private ResponseEntity<Payload> findAlunoById(@PathVariable Integer id) {

        Aluno aluno = alunoService.encontrarAlunoPorId(id);
        return ResponseEntity.ok(new Payload(HttpStatus.OK, aluno, null));
    }

    @PostMapping
    private ResponseEntity<Void> createAluno(@RequestBody @Valid Aluno aluno, BindingResult bindingResult, UriComponentsBuilder ucb) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Erro de validação ao criar aluno.");
        }

        Aluno savedAluno = alunoService.cadastrarAluno(aluno);

        URI locationOfNewAluno = ucb
                .path("alunos/{id}")
                .buildAndExpand(savedAluno.getId())
                .toUri();

        return ResponseEntity.created(locationOfNewAluno).build();
    }


    @GetMapping
    private ResponseEntity<Payload> findAllAlunos(Pageable pageable) {

        List<Aluno> page = alunoService.listaPaginaDeAlunos(pageable);
        return ResponseEntity.ok(new Payload(HttpStatus.OK, page, null));
    }

    @PutMapping("/{id}")
    private ResponseEntity<Void> updateAluno(@PathVariable Integer id, @RequestBody @Valid Aluno alunoNovosDados, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("Erro de validação ao atualizar o aluno.");
        }

        alunoService.atualizaDadosDoAlunoPorId(id, alunoNovosDados);

        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteAluno(@PathVariable Integer id) {

        alunoService.deletaAlunoPorId(id);
        return ResponseEntity.noContent().build();

    }
}
