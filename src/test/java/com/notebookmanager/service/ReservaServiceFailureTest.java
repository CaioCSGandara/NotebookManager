package com.notebookmanager.service;

import com.notebookmanager.infra.exception.RecursoNaoEncontradoException;
import com.notebookmanager.infra.exception.ValidationException;
import com.notebookmanager.model.dto.createfields.ReservaCreateFields;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ReservaServiceFailureTest {

    @Autowired
    private ReservaService reservaService;

    @Test
    public void naoRetornaReservaComIdInvalido() {

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
                () -> {
            reservaService.encontrarReservaPorId(18);
                });
        assertThat(exception.getMessage()).isEqualTo("Reserva com o id especificado não foi encontrada.");
    }

    @Test
    public void naoRetornaReservaComNotebookInvalido() {

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
                () -> {
                    reservaService.encontrarReservaPorNotebook(42);
                });
        assertThat(exception.getMessage()).isEqualTo("Reserva com o notebook especificado não foi encontrada.");
    }

    @Test
    public void naoCriaReservaComNotebookIndisponivel() {

        ValidationException exception = assertThrows(ValidationException.class,
                ()->{
            reservaService.criarReserva(new ReservaCreateFields("09135616", "123098"));
                });
        assertThat(exception.getMessage()).isEqualTo("Este notebook não está disponível para empréstimo no momento.");
    }


}
