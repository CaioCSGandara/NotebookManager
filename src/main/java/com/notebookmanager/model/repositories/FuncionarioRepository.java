package com.notebookmanager.model.repositories;

import com.notebookmanager.model.Funcionario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


public interface FuncionarioRepository extends CrudRepository<Funcionario, Integer>, PagingAndSortingRepository<Funcionario, Integer> {
    Optional<Funcionario> findByRf(String rf);
}
