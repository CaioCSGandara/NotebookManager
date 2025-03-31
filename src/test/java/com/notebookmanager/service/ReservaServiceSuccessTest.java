package com.notebookmanager.service;
import com.notebookmanager.model.Reserva;
import com.notebookmanager.model.dto.createfields.ReservaCreateFields;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;


@SpringBootTest
public class ReservaServiceSuccessTest {

    @Autowired
    private ReservaService reservaService;

    @Test
    @DirtiesContext
    public void criaNovaReserva() {
        ReservaCreateFields reservaCreateFields = new ReservaCreateFields("43218765", "491034");

        Optional<Reserva> reserva = reservaService.criarReserva(reservaCreateFields);

        System.out.println(reserva.toString());


    }
}
