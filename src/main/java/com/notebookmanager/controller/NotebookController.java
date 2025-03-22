package com.notebookmanager.controller;

import com.notebookmanager.exception.RecursoJaExistenteException;
import com.notebookmanager.exception.RecursoNaoEncontradoException;
import com.notebookmanager.exception.ValidationException;
import com.notebookmanager.model.Notebook;
import com.notebookmanager.model.enums.StatusNotebook;
import com.notebookmanager.model.payload.Payload;
import com.notebookmanager.model.repositories.AlunoRepository;
import com.notebookmanager.model.repositories.NotebookRepository;
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
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/notebooks")
public class NotebookController {

    private final NotebookRepository notebookRepository;

    @GetMapping("/{id}")
    private ResponseEntity<Payload> findById(@PathVariable Integer id) {

        Optional<Notebook> notebook = notebookRepository.findById(id);
        if (notebook.isEmpty()) {
            throw new RecursoNaoEncontradoException("Notebook não encontrado.");
        }
        Payload payload = new Payload(HttpStatus.OK, notebook.get(), null);
        return ResponseEntity.ok(payload);

    }

    @PostMapping
    private ResponseEntity<Payload> createNotebook(@RequestBody @Valid Notebook notebook, BindingResult bindingResult, UriComponentsBuilder ucb) {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("Erro de validação ao criar notebook.");
        }

        if(notebookRepository.existsByPatrimonio(notebook.getPatrimonio())) {
            throw new RecursoJaExistenteException("O notebook com este patrimonio já está cadastrado.");
        }

        Notebook savedNotebook = notebookRepository.save(notebook);

        URI locationOfNewNotebook = ucb
                .path("notebooks/{id}")
                .buildAndExpand(savedNotebook.getId())
                .toUri();

        return ResponseEntity.created(locationOfNewNotebook).build();
    }


    @GetMapping
    private ResponseEntity<Payload> listaNotebooks(Pageable pageable) {
        Page<Notebook> notebooks = notebookRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC,"status"))));

        Payload payload = new Payload(HttpStatus.OK, notebooks.getContent(), null);
        return  ResponseEntity.ok(payload);

    }


    @PatchMapping("/{id}/gerenciar-afastamento")
    private ResponseEntity<Payload> gerenciaAfastamentoNotebook(@PathVariable Integer id, @RequestBody StatusNotebook novoStatus) {

        if(novoStatus.equals(StatusNotebook.EMPRESTADO)) {
            throw new ValidationException("Não é possível alterar o status enquanto notebook está emprestado.");
        }

        Optional<Notebook> optNotebook = notebookRepository.findById(id);
        if (optNotebook.isEmpty()) {
            throw new RecursoNaoEncontradoException("Notebook não encontrado.");
        }
        Notebook notebook = optNotebook.get();

        if(notebook.getStatus().equals(novoStatus)) {
            throw new ValidationException("Status novo é igual status atual.");
        }

        notebook.setStatus(novoStatus);
        notebook.setAtualizadoEm(LocalDateTime.now());

        notebookRepository.save(notebook);

        return ResponseEntity.noContent().build();
    }



    @DeleteMapping("/{id}")
    private ResponseEntity<Payload> deleteNotebook(@PathVariable Integer id) {

        if(!notebookRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Notebook não encontrado.");
        }

        notebookRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}