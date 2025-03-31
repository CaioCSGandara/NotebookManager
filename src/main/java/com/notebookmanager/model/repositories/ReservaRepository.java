package com.notebookmanager.model.repositories;

import com.notebookmanager.model.Reserva;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ReservaRepository extends CrudRepository<Reserva, Integer>, PagingAndSortingRepository<Reserva, Integer>, ReservaRepositoryCustom {
    boolean existsById(Integer id);
}
