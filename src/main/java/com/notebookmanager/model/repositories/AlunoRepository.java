package com.notebookmanager.model.repositories;

import com.notebookmanager.model.entities.Aluno;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AlunoRepository extends MongoRepository<Aluno, String> {
    Optional<Aluno> findByRa(String ra);
}
