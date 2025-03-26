package com.notebookmanager.controller;

import com.notebookmanager.exception.RecursoJaExistenteException;
import com.notebookmanager.exception.RecursoNaoEncontradoException;
import com.notebookmanager.exception.ValidationException;
import com.notebookmanager.model.Notebook;
import com.notebookmanager.model.enums.StatusNotebook;
import com.notebookmanager.model.payload.Payload;
import com.notebookmanager.model.repositories.AlunoRepository;
import com.notebookmanager.model.repositories.NotebookRepository;
import com.notebookmanager.service.AlunoService;
import com.notebookmanager.service.NotebookService;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/notebooks")
public class NotebookController {

    private final NotebookService notebookService;

    @GetMapping("/{id}")
    private ResponseEntity<Payload> findById(@PathVariable Integer id) {

        Notebook notebook = notebookService.encontraNotebookPorId(id);
        return ResponseEntity.ok(new Payload(HttpStatus.OK, notebook, null));

    }

    @PostMapping
    private ResponseEntity<Payload> createNotebook(@RequestBody @Valid Notebook notebook, BindingResult bindingResult, UriComponentsBuilder ucb) {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("Erro de validação ao criar notebook.");
        }

        Notebook savedNotebook = notebookService.cadastrarNotebook(notebook);

        URI locationOfNewNotebook = ucb
                .path("notebooks/{id}")
                .buildAndExpand(savedNotebook.getId())
                .toUri();

        return ResponseEntity.created(locationOfNewNotebook).build();
    }


    @GetMapping
    private ResponseEntity<Payload> listaNotebooks(Pageable pageable) {

        List<Notebook> listaNotebooks = notebookService.listaPaginaDeNotebooks(pageable);
        return  ResponseEntity.ok(new Payload(HttpStatus.OK, listaNotebooks, null));

    }


    @PatchMapping("/{id}/gerenciar-status")
    private ResponseEntity<Payload> gerenciaAfastamentoNotebook(@PathVariable Integer id, @RequestBody Map<String, StatusNotebook> mapNovoStatus) {

        notebookService.alteraStatusNotebook(id, mapNovoStatus);
        return ResponseEntity.noContent().build();
    }



    @DeleteMapping("/{id}")
    private ResponseEntity<Payload> deleteNotebook(@PathVariable Integer id) {

        notebookService.deletaNotebookPorId(id);
        return ResponseEntity.noContent().build();
    }
}