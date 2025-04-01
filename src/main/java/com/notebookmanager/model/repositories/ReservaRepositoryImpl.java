package com.notebookmanager.model.repositories;


import com.notebookmanager.model.Reserva;
import com.notebookmanager.model.mapper.ReservaMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ReservaRepositoryImpl implements ReservaRepository {

    JdbcClient jdbcClient;

    @Override
    public Optional<Reserva> findByNotebook(Integer notebookId) {
        String sql = """
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
                .sql(sql)
                .param(notebookId)
                .query(new ReservaMapper())
                .optional();
    }


    @Override
    public Optional<Reserva> findById(Integer id) {
        String sql = """
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
                WHERE reserva.id = ?""";


        return jdbcClient
                .sql(sql)
                .param(id)
                .query(new ReservaMapper())
                .optional();
    }



    @Override
    public Reserva save(Reserva reserva) {

        String sql  = "INSERT INTO reserva (INICIO_EM, TERMINO_EM, ALUNO, NOTEBOOK) VALUES (?, ?, ?, ?)";

        jdbcClient
                .sql(sql)
                .param(reserva.getInicioEm())
                .param(reserva.getTerminoEm())
                .param(reserva.getAluno().getId())
                .param(reserva.getNotebook().getId())
                .update();

        return findByNotebook(reserva.getNotebook().getId()).get();
    }


    @Override
    public List<Reserva> findAllReservasAtivas(Pageable pageable) {

        String sql = """
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
                WHERE notebook.status = ?
                ORDER BY reserva.inicio_em ASC
                LIMIT ?
                OFFSET ?
                """;

        String statusQuery = "EMPRESTADO";
        Integer limit = pageable.getPageSize();
        Integer offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();

        return jdbcClient
                .sql(sql)
                .param(statusQuery)
                .param(limit)
                .param(offset)
                .query(new ReservaMapper())
                .list();

    }



}
