package com.notebookmanager.service;
import com.notebookmanager.model.Notebook;
import com.notebookmanager.model.Reserva;
import com.notebookmanager.model.dto.createfields.ReservaCreateFields;
import com.notebookmanager.model.dto.updatefields.NotebookUpdateFields;
import com.notebookmanager.model.dto.updatefields.ReservaUpdateFields;
import com.notebookmanager.model.enums.StatusNotebook;
import com.notebookmanager.model.repositories.ReservaRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
public class ReservaServiceSuccessTest {

    @Autowired
    private ReservaService reservaService;
    @Autowired
    private NotebookService notebookService;
    @Autowired
    private ReservaRepositoryImpl reservaRepository;

    @Test
    public void retornaReservaPorNotebook() {
        Reserva reserva = reservaService.encontrarReservaPorNotebook(2);
        assertThat(reserva.getAluno().getId()).isEqualTo(4);
        assertThat(reserva.getNotebook().getId()).isEqualTo(2);
    }

    @Test
    @DirtiesContext
    public void criaNovaReserva() {
        ReservaCreateFields reservaCreateFields = new ReservaCreateFields("43218765", "491034");

        Reserva reserva = reservaService.criarReserva(reservaCreateFields);

        assertThat(reserva.getAluno().getId()).isEqualTo(7);
        assertThat(reserva.getNotebook().getId()).isEqualTo(1);
    }

    @Test
    @DirtiesContext
    public void encerraReserva() {
        reservaService.encerrarReserva(2, new NotebookUpdateFields(StatusNotebook.DISPONIVEL));

        Notebook notebook = notebookService.encontraNotebookPorId(2);

        assertThat(notebook.getStatus()).isEqualTo(StatusNotebook.DISPONIVEL);
    }

    @Test
    public void listaPaginaDeReservasAtivas() {
        List<Reserva> reservas = reservaService.listarPaginaDeReservasAtivas(PageRequest.of(1, 10));
        assertThat(reservas.size()).isEqualTo(3);
        assertThat(reservas.get(0).getInicioEm()).isEqualTo("2025-03-28T12:21:03");
        assertThat(reservas.get(1).getInicioEm()).isEqualTo("2025-03-28T14:30");
        assertThat(reservas.get(2).getInicioEm()).isEqualTo("2025-03-28T19:44:21");
    }

    @Test
    @DirtiesContext
    public void trocarNotebookDuranteReserva() {
        Reserva reserva = reservaRepository.findById(1).get();
        assertThat(reserva.getNotebook().getId()).isEqualTo(2);

        reservaService.trocarNotebookDuranteReserva(reserva.getId(), new ReservaUpdateFields("491034"));

        Reserva reservaAtualizada = reservaRepository.findByNotebook(1).get();
        assertThat(reservaAtualizada.getNotebook().getId()).isEqualTo(1);
    }
}
