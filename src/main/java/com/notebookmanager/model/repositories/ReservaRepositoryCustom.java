package com.notebookmanager.model.repositories;

import com.notebookmanager.model.Reserva;

import java.util.Optional;

public interface ReservaRepositoryCustom {
    Optional<Reserva> salvar(Reserva reserva);
}
