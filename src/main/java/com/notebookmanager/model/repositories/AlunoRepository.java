package com.notebookmanager.model.repositories;

import com.notebookmanager.model.entities.Aluno;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlunoRepository extends MongoRepository<Aluno, String> {
    public Aluno findByRa(String ra);
}
