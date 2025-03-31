package com.notebookmanager.service;

import com.notebookmanager.infra.exception.RecursoNaoEncontradoException;
import com.notebookmanager.infra.exception.ValidationException;
import com.notebookmanager.model.Aluno;
import com.notebookmanager.model.Notebook;
import com.notebookmanager.model.Reserva;
import com.notebookmanager.model.dto.createfields.ReservaCreateFields;
import com.notebookmanager.model.enums.StatusNotebook;
import com.notebookmanager.model.repositories.ReservaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ReservaService {

    private ReservaRepository reservaRepository;
    private AlunoService alunoService;
    private NotebookService notebookService;

    public void encontrarReservaporId(Integer id) {

        if (reservaRepository.existsById(id)) {
            System.out.println("aluno salvo!!!");
            return;
        }

        throw new RecursoNaoEncontradoException("A reserva não foi criada.");
    }

    public Optional<Reserva> criarReserva(ReservaCreateFields reservaCreateFields) {

        Aluno aluno = alunoService.encontrarAlunoPorRa(reservaCreateFields.getAlunoRa());
        Notebook notebook = notebookService.encontraNotebookPorPatrimonio(reservaCreateFields.getNotebookPatrimonio());

        if(!notebook.getStatus().equals(StatusNotebook.DISPONIVEL)) {
            throw new ValidationException("Este notebook não está disponível para empréstimo no momento.");
        }

        Reserva reserva = new Reserva(aluno, notebook, LocalDateTime.now(), null);

        return reservaRepository.salvar(reserva);
    }
}
