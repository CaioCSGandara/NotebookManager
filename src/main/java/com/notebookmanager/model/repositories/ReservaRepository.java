package com.notebookmanager.model.repositories;

import com.notebookmanager.model.Reserva;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository {
    Reserva save(Reserva reserva);
    Optional<Reserva> findById(Integer id);
    Optional<Reserva> findByNotebook(Integer notebookId);
    List<Reserva> findAllReservasAtivas(Pageable pageable);
    void update(Integer id, LocalDateTime termino_em);
}
