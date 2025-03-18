package com.notebookmanager.model.repositories;

import com.notebookmanager.model.entities.Aluno;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface AlunoRepository extends CrudRepository<Aluno, Integer>, PagingAndSortingRepository<Aluno, Integer> {
    public boolean existsByRa(String ra);
}
