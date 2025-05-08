package com.notebookmanager.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario implements Cloneable {
    @Id
    private String id;

    private String nome;

    private String rf;

    private String email;

    private String senha;

    public Funcionario(String nome, String rf, String email, String senha) {
        this(null, nome, rf, email, senha);
    }

    public Funcionario(Funcionario funcionario) {
        this.id = null;
        this.nome = funcionario.getNome();
        this.rf = funcionario.getRf();
        this.email = funcionario.getEmail();
        this.senha = funcionario.getSenha();
    }

    @Override
    public Object clone() {
        Funcionario copia = null;
        try {
            copia = new Funcionario(this);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return copia;
    }
}