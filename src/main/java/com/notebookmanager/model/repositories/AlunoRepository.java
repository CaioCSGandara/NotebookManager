package com.notebookmanager.model.repositories;

import com.notebookmanager.model.Aluno;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


public interface AlunoRepository extends CrudRepository<Aluno, Integer>, PagingAndSortingRepository<Aluno, Integer> {
    boolean existsByRa(String ra);
    Optional<Aluno> findByRa(String ra);
}
