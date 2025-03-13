package com.notebookmanager.generator;

import com.notebookmanager.model.entities.Aluno;
import com.notebookmanager.model.entities.enums.Curso;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlunoGenerator {

    @Getter
    private static Aluno aluno = null;
    @Getter
    private static List<Aluno> listaAlunos = null;

    static {
        aluno = new Aluno("Caio Gandara", "22415616", "caio.cgs@gmail.com", "(19)99414-8554",
                Curso.ENFERMAGEM, LocalDateTime.of(2010, 12, 30, 12, 14, 22),
                LocalDateTime.of(2010, 12, 30, 12, 14, 22));

        listaAlunos = new ArrayList<Aluno>();

        listaAlunos.add(new Aluno("Julio Correa", "09135616", "jcorrea@gmail.com", "(19)90914-3014",
                Curso.MEDICINA, LocalDateTime.of(2012, 11, 10, 21, 12, 37),
                LocalDateTime.of(2012, 11, 10, 21, 12, 37)));
        listaAlunos.add(new Aluno("Maria Ferreira", "03781923", "maria.ferreira@gmail.com", "(19)90814-2314",
                Curso.TERAPIA_OCUPACIONAL, LocalDateTime.of(2021, 8, 12, 21, 21, 45),
                LocalDateTime.of(2021, 8, 12, 21, 21, 45)));
        listaAlunos.add(new Aluno("Fernando Pontes", "90174823", "fernandohpontes@gmail.com", "(19)83914-0945",
                Curso.BIOMEDICINA, LocalDateTime.of(2013, 1, 10, 21, 12, 37),
                LocalDateTime.of(2013, 1, 10, 21, 12, 37)));
    }

}
