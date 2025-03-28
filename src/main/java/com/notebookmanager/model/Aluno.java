package com.notebookmanager.model;

import com.notebookmanager.model.enums.Curso;
import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aluno implements Cloneable {
    @Id
    private Integer id;

    private String nome;

    private String ra;

    private String email;

    private String telefone;

    private Curso curso;


    public Aluno(String nome, String ra, String email, String telefone, Curso curso) {
        this(null, nome, ra, email, telefone, curso);
    }

    public Aluno(Aluno aluno) {
        this.id = aluno.getId();
        this.nome = aluno.getNome();
        this.ra = aluno.getRa();
        this.email = aluno.getEmail();
        this.telefone = aluno.getTelefone();
        this.curso = aluno.getCurso();
    }


    @Override
    public Object clone() {
        Aluno copia = null;
        try {
            copia = new Aluno(this);

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return copia;
    }
}
