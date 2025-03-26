package com.notebookmanager.controller;

import com.notebookmanager.infra.exception.ValidationException;
import com.notebookmanager.model.Payload;
import com.notebookmanager.model.Aluno;
import com.notebookmanager.model.createfields.AlunoCreateFields;
import com.notebookmanager.model.updatefields.AlunoUpdateFields;
import com.notebookmanager.service.AlunoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

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
    private ResponseEntity<Void> createAluno(@RequestBody @Valid AlunoCreateFields alunoCreateFields, BindingResult bindingResult, UriComponentsBuilder ucb) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Erro de validação ao criar aluno.");
        }

        Aluno savedAluno = alunoService.cadastrarAluno(alunoCreateFields);

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
    private ResponseEntity<Void> updateAluno(@PathVariable Integer id, @RequestBody @Valid AlunoUpdateFields alunoUpdateFields, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("Erro de validação ao atualizar o aluno.");
        }

        alunoService.atualizaDadosDoAlunoPorId(id, alunoUpdateFields);

        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteAluno(@PathVariable Integer id) {

        alunoService.deletaAlunoPorId(id);
        return ResponseEntity.noContent().build();

    }
}
