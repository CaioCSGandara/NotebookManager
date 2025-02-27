package com.notebookmanager;

import com.notebookmanager.model.entities.Aluno;
import com.notebookmanager.model.entities.enums.Curso;
import com.notebookmanager.model.repositories.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class NotebookManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotebookManagerApplication.class, args);
	}

}
