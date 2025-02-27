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
public class NotebookManagerApplication implements CommandLineRunner {

	@Autowired
	AlunoRepository alunoRepository;

	public static void main(String[] args) {
		SpringApplication.run(NotebookManagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		alunoRepository.deleteAll();

		alunoRepository.save(new Aluno("Caio Gandara", "22415616", "caio.cgs2@gmail", "(19)99414-8554",
				Curso.ENFERMAGEM, LocalDateTime.now(), LocalDateTime.now()));

		Aluno a1 = alunoRepository.findByRa("22415616");

		System.out.println(a1.toString());
	}

}
