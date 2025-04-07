package com.notebookmanager.model.mapper;

import com.notebookmanager.model.Aluno;
import com.notebookmanager.model.Notebook;
import com.notebookmanager.model.Reserva;
import com.notebookmanager.model.enums.Curso;
import com.notebookmanager.model.enums.StatusNotebook;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ReservaMapper implements RowMapper<Reserva> {
    public Reserva mapRow(ResultSet rs, int rowNum) throws SQLException {

        Aluno aluno = new Aluno();
        aluno.setId(rs.getInt("aluno_id"));
        aluno.setNome(rs.getString("nome"));
        aluno.setRa(rs.getString("ra"));
        aluno.setEmail(rs.getString("email"));
        aluno.setTelefone(rs.getString("telefone"));
        aluno.setCurso(Curso.valueOf(rs.getString("curso")));

        Notebook notebook = new Notebook();
        notebook.setId(rs.getInt("notebook_id"));
        notebook.setPatrimonio(rs.getString("patrimonio"));
        notebook.setStatus(StatusNotebook.valueOf(rs.getString("status")));

        Reserva reserva = new Reserva();
        reserva.setId(rs.getInt("id"));
        reserva.setAluno(aluno);
        reserva.setNotebook(notebook);
        reserva.setInicioEm(rs.getTimestamp("inicio_em").toLocalDateTime());
        reserva.setTerminoEm(rs.getTimestamp("termino_em") != null ? rs.getTimestamp("termino_em").toLocalDateTime() : null);

        return reserva;
    }
}
