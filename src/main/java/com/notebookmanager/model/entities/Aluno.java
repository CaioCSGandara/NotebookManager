package com.notebookmanager.model.entities;

import com.notebookmanager.model.entities.enums.Curso;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aluno implements Cloneable{
    @Id
    private String id;
    private String nome;
    private String ra;
    private String email;
    private String telefone;
    private String curso;
    private LocalDateTime ultimoLogin;
    private LocalDateTime atualizadoEm;


    public Aluno(String id, String nome, String ra, String email, String telefone, Curso curso, LocalDateTime ultimoLogin, LocalDateTime atualizadoEm) {
        this.id = id;
        this.nome = nome;
        this.ra = ra;
        this.email = email;
        this.telefone = telefone;
        this.curso = curso.getNomeFormatado();
        this.ultimoLogin = ultimoLogin;
        this.atualizadoEm = atualizadoEm;

    }

    public Aluno(String nome, String ra, String email, String telefone, Curso curso, LocalDateTime ultimoLogin, LocalDateTime atualizadoEm) {
        this(null, nome, ra, email, telefone, curso.getNomeFormatado(), ultimoLogin, atualizadoEm);
    }

    public Aluno(Aluno aluno) {
        this.id = aluno.getId();
        this.nome = aluno.getNome();
        this.ra = aluno.getRa();
        this.email = aluno.getEmail();
        this.telefone = aluno.getTelefone();
        this.curso = aluno.getCurso();
        this.ultimoLogin = aluno.getUltimoLogin();
        this.atualizadoEm = aluno.getAtualizadoEm();
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
