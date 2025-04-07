package com.notebookmanager.service;

import com.notebookmanager.infra.exception.RecursoJaExistenteException;
import com.notebookmanager.infra.exception.RecursoNaoEncontradoException;
import com.notebookmanager.infra.exception.ValidationException;
import com.notebookmanager.model.Notebook;
import com.notebookmanager.model.dto.createfields.NotebookCreateFields;
import com.notebookmanager.model.enums.StatusNotebook;
import com.notebookmanager.model.repositories.NotebookRepository;
import com.notebookmanager.model.dto.updatefields.NotebookUpdateFields;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class NotebookService {

    private NotebookRepository notebookRepository;

    public Notebook encontraNotebookPorId(Integer id) {

        Optional<Notebook> notebook = notebookRepository.findById(id);
        if(notebook.isEmpty()) {
            throw new RecursoNaoEncontradoException("Notebook não encontrado.");
        }
        return notebook.get();
    }

    public Notebook encontraNotebookPorPatrimonio(String patrimonio) {

        Optional<Notebook> notebook = notebookRepository.findByPatrimonio(patrimonio);
        if(notebook.isEmpty()) {
            throw new RecursoNaoEncontradoException("Notebook não encontrado.");
        }
        return notebook.get();
    }

    public Notebook cadastrarNotebook(NotebookCreateFields notebookCreateFields) {
        if(notebookRepository.existsByPatrimonio(notebookCreateFields.getPatrimonio())) {
            throw new RecursoJaExistenteException("O Notebook com este patrimonio já está cadastrado.");
        }

        Notebook notebook = new Notebook(notebookCreateFields.getPatrimonio(), StatusNotebook.DISPONIVEL);

        return notebookRepository.save(notebook);
    }

    public List<Notebook> listaPaginaDeNotebooks(Pageable pageable) {

        Page<Notebook> notebookPage = notebookRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "status"))));

        return notebookPage.getContent();
    }

    public void alteraStatusNotebook(Integer id, NotebookUpdateFields notebookUpdateFields) {
        Optional<Notebook> optNotebook = notebookRepository.findById(id);
        if (optNotebook.isEmpty()) {
            throw new RecursoNaoEncontradoException("Notebook não encontrado.");
        }
        Notebook notebookExistente = optNotebook.get();

        StatusNotebook statusAtual = notebookExistente.getStatus();
        StatusNotebook statusAtualizado = notebookUpdateFields.getStatus();

        if (statusAtualizado.equals(StatusNotebook.DISPONIVEL) && statusAtual.equals(StatusNotebook.DISPONIVEL)) {
            throw new ValidationException("Para tornar um notebook DISPONIVEL, ele deve estar EMPRESTADO ou AFASTADO.");
        }

        if (!statusAtualizado.equals(StatusNotebook.DISPONIVEL) && !statusAtual.equals(StatusNotebook.DISPONIVEL)) {
            throw new ValidationException("Para tornar um notebook EMPRESTADO ou AFASTADO, ele deve estar DISPONIVEL.");
        }

        notebookExistente.setStatus(statusAtualizado);
        notebookRepository.save(notebookExistente);

    }


    public void deletaNotebookPorId(Integer id) {
        if(!notebookRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Notebook não encontrado.");
        }
        notebookRepository.deleteById(id);
    }


}
