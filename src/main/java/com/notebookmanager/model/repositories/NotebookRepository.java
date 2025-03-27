package com.notebookmanager.model.repositories;

import com.notebookmanager.model.Notebook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface NotebookRepository extends CrudRepository<Notebook, Integer>, PagingAndSortingRepository<Notebook, Integer> {
    boolean existsByPatrimonio(String patrimonio);
}
