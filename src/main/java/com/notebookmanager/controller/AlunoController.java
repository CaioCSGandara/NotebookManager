package com.notebookmanager.controller;

import com.notebookmanager.model.entities.Aluno;
import com.notebookmanager.model.entities.enums.Curso;
import com.notebookmanager.model.repositories.AlunoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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
        Optional<Aluno> alunoOptional = alunoRepository.findByRa(requestedRa);
        if(alunoOptional.isPresent()) {
            return ResponseEntity.ok(alunoOptional.get());
        }
        return ResponseEntity.notFound().build();

    }

}
