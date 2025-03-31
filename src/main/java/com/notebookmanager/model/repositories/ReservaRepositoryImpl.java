package com.notebookmanager.model.repositories;


import com.notebookmanager.model.Reserva;
import com.notebookmanager.model.mapper.ReservaMapper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class ReservaRepositoryImpl implements ReservaRepositoryCustom {

    JdbcClient jdbcClient;

    @Override
    public Optional<Reserva> salvar(Reserva reserva) {

        String sqlInsert  = "INSERT INTO reserva (INICIO_EM, TERMINO_EM, ALUNO, NOTEBOOK) VALUES (?, ?, ?, ?)";

        jdbcClient
                .sql(sqlInsert)
                .param(reserva.getInicioEm())
                .param(reserva.getTerminoEm())
                .param(reserva.getAluno().getId())
                .param(reserva.getNotebook().getId())
                .update();


        String sqlQuery = """
                SELECT\s
                    reserva.id,
                    reserva.inicio_em,
                    reserva.termino_em,
                    aluno.id as aluno_id,
                    aluno.nome,
                    aluno.ra,
                    aluno.email,
                    aluno.telefone,
                    aluno.curso,
                    notebook.id as notebook_id,
                    notebook.patrimonio,
                    notebook.status
                FROM reserva
                JOIN aluno ON aluno.id = reserva.aluno
                JOIN notebook ON notebook.id = reserva.notebook
                WHERE notebook.id = ?""";


        return jdbcClient
                .sql(sqlQuery)
                .param(reserva.getNotebook().getId())
                .query(new ReservaMapper())
                .optional();


    }
}
