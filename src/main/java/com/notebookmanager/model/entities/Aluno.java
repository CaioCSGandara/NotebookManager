package com.notebookmanager.model.entities;

import com.notebookmanager.model.entities.enums.Curso;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Objects;

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

    public Aluno(String nome, String ra, String email, String telefone, Curso curso, LocalDateTime ultimoLogin, LocalDateTime atualizadoEm) {
        this.nome = nome;
        this.ra = ra;
        this.email = email;
        this.telefone = telefone;
        this.curso = curso.getNomeFormatado();
        this.ultimoLogin = ultimoLogin;
        this.atualizadoEm = atualizadoEm;

    }

    public Aluno() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }


    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public void setUltimoLogin(LocalDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }


    @Override
    public String toString() {
        return "Aluno{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", ra='" + ra + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", curso=" + curso +
                ", ultimoLogin=" + ultimoLogin +
                ", atualizadoEm=" + atualizadoEm +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Aluno aluno = (Aluno) o;
        return Objects.equals(id, aluno.id) && Objects.equals(nome, aluno.nome) && Objects.equals(ra, aluno.ra) && Objects.equals(email, aluno.email) && Objects.equals(telefone, aluno.telefone) && Objects.equals(curso, aluno.curso) && Objects.equals(ultimoLogin, aluno.ultimoLogin) && Objects.equals(atualizadoEm, aluno.atualizadoEm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, ra, email, telefone, curso, ultimoLogin, atualizadoEm);
    }

    public Aluno(Aluno aluno) {
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
