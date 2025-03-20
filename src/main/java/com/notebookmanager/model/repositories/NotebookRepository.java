package com.notebookmanager.model.repositories;

import com.notebookmanager.model.Notebook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface NotebookRepository extends CrudRepository<Notebook, Integer>, PagingAndSortingRepository<Notebook, Integer> {
    boolean existsByPatrimonio(String patrimonio);
    Optional<Notebook> findByPatrimonio(String patrimonio);
}
