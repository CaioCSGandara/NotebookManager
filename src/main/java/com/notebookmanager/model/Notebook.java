package com.notebookmanager.model;

import com.notebookmanager.model.enums.StatusEquipamento;
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
    private StatusEquipamento status;

    @NotNull
    private Integer qtd_emprestimos;

    @NotNull
    private LocalDateTime atualizadoEm;


    public Notebook(String modelo, String patrimonio,  StatusEquipamento status, Integer qtd_emprestimos, LocalDateTime atualizadoEm) {
        this(null, modelo, patrimonio, status, qtd_emprestimos, atualizadoEm);
    }


    public Notebook(Notebook notebook) {
        this.id = notebook.id;
        this.modelo = notebook.modelo;
        this.patrimonio = notebook.patrimonio;
        this.status = notebook.status;
        this.qtd_emprestimos = notebook.qtd_emprestimos;
        this.atualizadoEm = notebook.atualizadoEm;
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
