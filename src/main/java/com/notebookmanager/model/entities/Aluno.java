package com.notebookmanager.model.entities;

import com.notebookmanager.model.entities.enums.Curso;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aluno implements Cloneable {
    @Id
    private Integer id;

    @NotBlank @Size(min = 1, max = 30)
    private String nome;

    @NotBlank
    @Size(min = 8, max = 8)
    private String ra;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._-]+@puccampinas\\.edu\\.br$")
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\(\\d{2}\\)\\d{5}-\\d{4}$")
    private String telefone;

    @NotNull
    private Curso curso;

    @NotNull
    private LocalDateTime ultimoLogin;

    @NotNull
    private LocalDateTime atualizadoEm;


    public Aluno(String nome, String ra, String email, String telefone, Curso curso, LocalDateTime ultimoLogin, LocalDateTime atualizadoEm) {
        this(null, nome, ra, email, telefone, curso, ultimoLogin, atualizadoEm);
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
