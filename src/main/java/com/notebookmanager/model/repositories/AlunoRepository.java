package com.notebookmanager.model.repositories;

import com.notebookmanager.model.Aluno;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


public interface AlunoRepository extends CrudRepository<Aluno, Integer>, PagingAndSortingRepository<Aluno, Integer> {
    boolean existsByRa(String ra);
    boolean existsByEmail(String email);
    Optional<Aluno> findByRa(String ra);
}
