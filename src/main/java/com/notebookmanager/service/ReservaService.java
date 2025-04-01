package com.notebookmanager.service;

import com.notebookmanager.infra.exception.RecursoNaoEncontradoException;
import com.notebookmanager.infra.exception.ValidationException;
import com.notebookmanager.model.Aluno;
import com.notebookmanager.model.Notebook;
import com.notebookmanager.model.Reserva;
import com.notebookmanager.model.dto.createfields.ReservaCreateFields;
import com.notebookmanager.model.dto.updatefields.NotebookUpdateFields;
import com.notebookmanager.model.dto.updatefields.ReservaUpdateFields;
import com.notebookmanager.model.enums.StatusNotebook;
import com.notebookmanager.model.repositories.ReservaRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ReservaService {

    private ReservaRepository reservaRepository;
    private AlunoService alunoService;
    private NotebookService notebookService;


    public Reserva encontrarReservaPorNotebook(Integer notebookId) {
        Optional<Reserva> reservaOpt = reservaRepository.findByNotebook(notebookId);
        if(reservaOpt.isEmpty()){
            throw new RecursoNaoEncontradoException("Reserva com o notebook especificado não foi encontrada.");
        }
        return reservaOpt.get();
    }


    public Reserva criarReserva(ReservaCreateFields reservaCreateFields) {

        Aluno aluno = alunoService.encontrarAlunoPorRa(reservaCreateFields.getAlunoRa());
        Notebook notebook = notebookService.encontraNotebookPorPatrimonio(reservaCreateFields.getNotebookPatrimonio());

        if(!notebook.getStatus().equals(StatusNotebook.DISPONIVEL)) {
            throw new ValidationException("Este notebook não está disponível para empréstimo no momento.");
        }

        Reserva reserva = new Reserva(aluno, notebook, LocalDateTime.now(), null);

        return reservaRepository.save(reserva);
    }


    public List<Reserva>listarPaginaDeReservasAtivas(Pageable pageable) {
               return reservaRepository.findAllReservasAtivas(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize()));
    }


    public void encerrarReserva(Integer notebookId, NotebookUpdateFields notebookUpdateFields) {

        encontrarReservaPorNotebook(notebookId);

        notebookService.alteraStatusNotebook(notebookId, notebookUpdateFields);

    }

    public void trocarNotebookDuranteReserva(Integer id, ReservaUpdateFields reservaUpdateFields) {

        Optional<Reserva> reservaAtualOpt = reservaRepository.findById(id);
        if (reservaAtualOpt.isEmpty()) {
            throw new RecursoNaoEncontradoException("Reserva não encontrada.");
        }

        Reserva reservaAtual = reservaAtualOpt.get();

        Integer idNotebookAtual = reservaAtual.getNotebook().getId();

        encerrarReserva(idNotebookAtual, new NotebookUpdateFields(StatusNotebook.DISPONIVEL));

        Notebook notebookNovo = notebookService.encontraNotebookPorPatrimonio(reservaUpdateFields.getNotebookPatrimonio());

        criarReserva(new ReservaCreateFields(
                reservaAtual.getAluno().getRa(),
                notebookNovo.getPatrimonio()
        ));

    }
}
