package com.notebookmanager.controller;

import com.notebookmanager.infra.exception.ValidationException;
import com.notebookmanager.model.Reserva;
import com.notebookmanager.model.dto.Payload;
import com.notebookmanager.model.dto.createfields.ReservaCreateFields;
import com.notebookmanager.model.dto.updatefields.NotebookUpdateFields;
import com.notebookmanager.model.dto.updatefields.ReservaUpdateFields;
import com.notebookmanager.service.ReservaService;
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
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<Payload> criarReserva(@RequestBody @Valid ReservaCreateFields reservaCreateFields, BindingResult bindingResult, UriComponentsBuilder ucb) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException("Erro de validação ao criar reserva");
        }
        Reserva savedReserva = reservaService.criarReserva(reservaCreateFields);

        URI locationOfNewReserva = ucb
                .path("/reservas/{id}")
                .buildAndExpand(savedReserva.getId())
                .toUri();

        return ResponseEntity.created(locationOfNewReserva).build();
    }

    @GetMapping
    public ResponseEntity<Payload> listarReservasAtivas(Pageable pageable) {
        List<Reserva> listaReservasAtivas = reservaService.listarPaginaDeReservasAtivas(pageable);

        return ResponseEntity.ok(new Payload(HttpStatus.OK, listaReservasAtivas, null));
    }

    @PatchMapping("/{id}/encerrar-reserva")
    public ResponseEntity<Payload> encerrarReserva(@PathVariable Integer id, @RequestBody @Valid NotebookUpdateFields notebookUpdateFields, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException("Erro de validação ao encerrar reserva");
        }
        reservaService.encerrarReserva(id, notebookUpdateFields);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/trocar-notebook")
    public ResponseEntity<Payload> trocarNotebook(@PathVariable Integer id, @RequestBody @Valid ReservaUpdateFields reservaUpdateFields, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException("Erro de validação ao trocar de notebook");
        }
        reservaService.trocarNotebookDuranteReserva(id, reservaUpdateFields);

        return ResponseEntity.noContent().build();
    }

}
