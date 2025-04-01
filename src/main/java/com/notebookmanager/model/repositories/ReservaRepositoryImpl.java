package com.notebookmanager.model.repositories;


import com.notebookmanager.model.Reserva;
import com.notebookmanager.model.mapper.ReservaMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ReservaRepositoryImpl implements ReservaRepository {

    JdbcClient jdbcClient;

    private final static String SQL_STANDART_RESERVA_QUERY = """
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
            """;

    @Override
    public Optional<Reserva> findByNotebook(Integer notebookId) {

        String sql = SQL_STANDART_RESERVA_QUERY + " WHERE notebook.id = ?";


        return jdbcClient
                .sql(sql)
                .param(notebookId)
                .query(new ReservaMapper())
                .optional();
    }


    @Override
    public Optional<Reserva> findById(Integer id) {

        String sql = SQL_STANDART_RESERVA_QUERY + " WHERE reserva.id = ?";


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

        String sql = SQL_STANDART_RESERVA_QUERY + " WHERE reserva.termino_em is NULL ORDER BY reserva.inicio_em ASC LIMIT ? OFFSET ?";

        Integer limit = pageable.getPageSize();
        Integer offset = (pageable.getPageNumber()) * pageable.getPageSize();

        return jdbcClient
                .sql(sql)
                .param(limit)
                .param(offset)
                .query(new ReservaMapper())
                .list();

    }

    @Override
    public void update(Integer id, LocalDateTime termino_em) {

        String sql = """
                UPDATE reserva
                SET termino_em = ?
                WHERE id = ?
                """;

        jdbcClient
                .sql(sql)
                .param(termino_em)
                .param(id)
                .update();
    }

}
