package com.notebookmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reserva implements  Cloneable {

    @Id
    private Integer id;

    private Aluno aluno;

    private Notebook notebook;

    private LocalDateTime inicioEm;

    private LocalDateTime terminoEm;


    public Reserva(Aluno aluno, Notebook notebook, LocalDateTime inicioEm, LocalDateTime terminoEm ) {
        this(null,  aluno, notebook, inicioEm, terminoEm);
    }

    public Reserva(Reserva reserva){
        this.id = reserva.getId();
        this.aluno = (Aluno) reserva.getAluno().clone();
        this.notebook = (Notebook) reserva.getNotebook().clone();
        this.inicioEm = reserva.getInicioEm();
        this.terminoEm = reserva.getTerminoEm();
    }


    public Object clone() {
        Reserva copia = null;
        try {
            copia = new Reserva(this);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return copia;
    }
}


