package com.notebookmanager.model;

import com.notebookmanager.model.enums.StatusNotebook;
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

    @NotBlank @Size(min = 5, max = 50)
    private String modelo;

    @NotNull @Size(min = 8, max = 8)
    private String patrimonio;

    @NotNull
    private StatusNotebook status;

    @NotNull @Min(0)
    private Integer qtdEmprestimos;

    @NotNull
    private LocalDateTime atualizadoEm;


    public Notebook(String modelo, String patrimonio, StatusNotebook status, Integer qtdEmprestimos, LocalDateTime atualizadoEm) {
        this(null, modelo, patrimonio, status, qtdEmprestimos, atualizadoEm);
    }


    public Notebook(Notebook notebook) {
        this.id = notebook.getId();
        this.modelo = notebook.getModelo();
        this.patrimonio = notebook.getPatrimonio();
        this.status = notebook.getStatus();
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
