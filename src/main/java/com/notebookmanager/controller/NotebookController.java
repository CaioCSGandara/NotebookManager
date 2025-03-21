package com.notebookmanager.controller;

import com.notebookmanager.model.Notebook;
import com.notebookmanager.model.payload.Payload;
import com.notebookmanager.model.repositories.AlunoRepository;
import com.notebookmanager.model.repositories.NotebookRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final AlunoRepository alunoRepository;

    @GetMapping("/{id}")
    private ResponseEntity<Payload> findById(@PathVariable Integer id) {

        Optional<Notebook> notebook = notebookRepository.findById(id);
        Payload payload = new Payload(HttpStatus.OK, notebook.get(), null);
        return ResponseEntity.ok(payload);

    }

    @PostMapping
    private ResponseEntity<Payload> createNotebook(@RequestBody Notebook notebook, UriComponentsBuilder ucb) {

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


//    @PatchMapping("/{id}/alterar-estado-emprestimo")
//    private ResponseEntity<Payload> updateNotebook(@PathVariable Integer id, UriComponentsBuilder ucb) {
//        Optional<Notebook> optNotebookToUpdate = notebookRepository.findById(id);
//        Notebook notebookToUpdate = optNotebookToUpdate.get();
//
//        if(!notebookToUpdate.isEmprestado()) {
//            notebookToUpdate.setQtdEmprestimos(notebookToUpdate.getQtdEmprestimos()+1);
//        }
//
//        notebookToUpdate.setEmprestado(!notebookToUpdate.isEmprestado());
//        notebookToUpdate.setAtualizadoEm(LocalDateTime.now());
//
//        notebookRepository.save(notebookToUpdate);
//
//
//        return ResponseEntity.noContent().build();
//
//    }


    @DeleteMapping("/{id}")
    private ResponseEntity<Payload> deleteNotebook(@PathVariable Integer id) {
        alunoRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}