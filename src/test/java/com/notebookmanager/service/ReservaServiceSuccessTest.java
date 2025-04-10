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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
@MockBean(EmailService.class)
public class ReservaServiceSuccessTest {

    @Autowired
    private ReservaService reservaService;
    @Autowired
    private NotebookService notebookService;
    @Autowired
    private ReservaRepositoryImpl reservaRepository;
    @Autowired
    private EmailService emailService;

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
        verify(emailService, times(1)).enviarEmail(eq("rodrigo.oliveira...@puccampinas.edu.br"),
                eq("Comprovante de Empréstimo de Notebook"),
                any());
    }


    @Test
    public void listaPaginaDeReservasAtivas() {
        List<Reserva> reservas = reservaService.listarPaginaDeReservasAtivas(PageRequest.of(0, 10));
        assertThat(reservas.size()).isEqualTo(2);
        assertThat(reservas.get(0).getInicioEm()).isEqualTo("2025-03-28T12:21:03");
        assertThat(reservas.get(1).getInicioEm()).isEqualTo("2025-03-28T14:30");
    }

    @Test
    @DirtiesContext
    public void encerraReserva() {
        reservaService.encerrarReserva(2, new NotebookUpdateFields(StatusNotebook.DISPONIVEL));

        Reserva reserva = reservaService.encontrarReservaPorId(2);
        Notebook notebook = notebookService.encontraNotebookPorId(3);
        System.out.println(reserva);
        assertThat(reserva.getTerminoEm()).isNotNull();
        assertThat(notebook.getStatus()).isEqualTo(StatusNotebook.DISPONIVEL);
        verify(emailService, times(1)).enviarEmail(eq("carlos.mendes...@puccampinas.edu.br"),
                eq("Comprovante de Devolução de Notebook"),
                any());
    }


    @Test
    @DirtiesContext
    public void trocarNotebookDuranteReserva() {
        Reserva reserva = reservaRepository.findById(1).get();
        assertThat(reserva.getNotebook().getId()).isEqualTo(2);

        reservaService.trocarNotebookDuranteReserva(reserva.getId(), new ReservaUpdateFields("491034"));

        Reserva reservaAtualizada = reservaRepository.findByNotebook(1).get();
        assertThat(reservaAtualizada.getNotebook().getId()).isEqualTo(1);
        verify(emailService, times(2)).enviarEmail(any(), any(), any());
    }
}
