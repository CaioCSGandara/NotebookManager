package com.notebookmanager.model;

import com.notebookmanager.model.enums.StatusNotebook;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notebook implements Cloneable {

    @Id
    private Integer id;

    private String patrimonio;

    private StatusNotebook status;



    public Notebook(String patrimonio, StatusNotebook status) {
        this(null, patrimonio, status);
    }


    public Notebook(Notebook notebook) {
        this.id = notebook.getId();
        this.patrimonio = notebook.getPatrimonio();
        this.status = notebook.getStatus();
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
