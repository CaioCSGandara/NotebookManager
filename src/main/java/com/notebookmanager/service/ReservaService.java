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
    private EmailService emailService;


    public Reserva encontrarReservaPorId(Integer id) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(id);
        if(reservaOpt.isEmpty()){
            throw new RecursoNaoEncontradoException("Reserva com o id especificado não foi encontrada.");
        }
        return reservaOpt.get();
    }


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

        notebookService.alteraStatusNotebook(notebook.getId(), new NotebookUpdateFields(StatusNotebook.EMPRESTADO));

        Reserva reserva = new Reserva(aluno, notebook, LocalDateTime.now(), null);

        Reserva reservaSalva = reservaRepository.save(reserva);

        String emailMessage = emailService.criaMsgEmprestimo(reserva.getAluno().getNome(),
         reserva.getNotebook().getPatrimonio());

        emailService.enviarEmail(reserva.getAluno().getEmail(),
        "Comprovante de Empréstimo de Notebook",
        emailMessage);
        
        return reservaSalva;
    }


    public List<Reserva>listarPaginaDeReservasAtivas(Pageable pageable) {
               return reservaRepository.findAllReservasAtivas(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize()));
    }


    public void encerrarReserva(Integer id, NotebookUpdateFields notebookUpdateFields) {

        Reserva reserva = encontrarReservaPorId(id);

        Integer notebookId = reserva.getNotebook().getId();

        notebookService.alteraStatusNotebook(notebookId, notebookUpdateFields);
        reservaRepository.update(id, LocalDateTime.now());

        String emailMessage = emailService.criaMsgDevolucao(reserva.getAluno().getNome(),
         reserva.getNotebook().getPatrimonio());

        emailService.enviarEmail(reserva.getAluno().getEmail(),
        "Comprovante de Devolução de Notebook",
        emailMessage);
    }

    public void trocarNotebookDuranteReserva(Integer id, ReservaUpdateFields reservaUpdateFields) {

        Reserva reservaAtual = encontrarReservaPorId(id);

        encerrarReserva(reservaAtual.getId(), new NotebookUpdateFields(StatusNotebook.DISPONIVEL));

        Notebook notebookNovo = notebookService.encontraNotebookPorPatrimonio(reservaUpdateFields.getNotebookPatrimonio());

        criarReserva(new ReservaCreateFields(
                reservaAtual.getAluno().getRa(),
                notebookNovo.getPatrimonio()
        ));

    }
}
