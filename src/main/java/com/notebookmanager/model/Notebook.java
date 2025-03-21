package com.notebookmanager.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notebook implements Cloneable {

    @Id
    private Integer id;

    @NotBlank
    private String modelo;

    @NotNull @Size(min = 8, max = 8)
    private String patrimonio;

    @NotNull
    private boolean emprestado;

    @NotNull
    private Integer qtdEmprestimos;

    @NotNull
    private LocalDateTime atualizadoEm;


    public Notebook(String modelo, String patrimonio,  boolean emprestado, Integer qtdEmprestimos, LocalDateTime atualizadoEm) {
        this(null, modelo, patrimonio, emprestado, qtdEmprestimos, atualizadoEm);
    }


    public Notebook(Notebook notebook) {
        this.id = notebook.getId();
        this.modelo = notebook.getModelo();
        this.patrimonio = notebook.getPatrimonio();
        this.emprestado = notebook.isEmprestado();
        this.qtdEmprestimos = notebook.getQtdEmprestimos();
        this.atualizadoEm = notebook.getAtualizadoEm();
    }

    public Object clone() {
        Notebook copia = null;
        try {
            copia = new Notebook(this);
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
        return copia;
    }


}
