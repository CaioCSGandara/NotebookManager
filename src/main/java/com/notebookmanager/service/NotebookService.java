package com.notebookmanager.service;

import com.notebookmanager.exception.RecursoJaExistenteException;
import com.notebookmanager.exception.RecursoNaoEncontradoException;
import com.notebookmanager.exception.ValidationException;
import com.notebookmanager.model.Notebook;
import com.notebookmanager.model.enums.StatusNotebook;
import com.notebookmanager.model.repositories.AlunoRepository;
import com.notebookmanager.model.repositories.NotebookRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    public Notebook cadastrarNotebook(Notebook notebook) {
        if(notebookRepository.existsByPatrimonio(notebook.getPatrimonio())) {
            throw new RecursoJaExistenteException("O Notebook com este patrimonio já está cadastrado.");
        }
        return notebookRepository.save(notebook);
    }

    public List<Notebook> listaPaginaDeNotebooks(Pageable pageable) {

        Page<Notebook> notebookPage = notebookRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "status"))));

        return notebookPage.getContent();
    }

    public void alteraStatusNotebook(Integer id, Map<String, StatusNotebook> mapNovoStatus) {
        Optional<Notebook> optNotebook = notebookRepository.findById(id);
        if (optNotebook.isEmpty()) {
            throw new RecursoNaoEncontradoException("Notebook não encontrado.");
        }
        Notebook notebookExistente = optNotebook.get();

        if (mapNovoStatus.get("status") == null) {
            throw new ValidationException("Novo status deve ter o formato: {\"status\": \"AFASTADO\" ou \"EMPRESTADO\" ou \"DISPONIVEL\" } ");
        }

        StatusNotebook statusAtual = notebookExistente.getStatus();
        StatusNotebook statusAtualizado = mapNovoStatus.get("status");

        if (statusAtualizado.equals(StatusNotebook.DISPONIVEL) && statusAtual.equals(StatusNotebook.DISPONIVEL)) {
            throw new ValidationException("Para tornar um notebook DISPONIVEL, ele deve estar EMPRESTADO ou AFASTADO.");
        }

        if (!statusAtualizado.equals(StatusNotebook.DISPONIVEL) && !statusAtual.equals(StatusNotebook.DISPONIVEL)) {
            throw new ValidationException("Para tornar um notebook EMPRESTADO ou AFASTADO, ele deve estar DISPONIVEL.");
        }

        if(statusAtualizado.equals(StatusNotebook.EMPRESTADO)) {
            notebookExistente.setQtdEmprestimos(notebookExistente.getQtdEmprestimos()+1);
        }

        notebookExistente.setStatus(statusAtualizado);
        notebookExistente.setAtualizadoEm(LocalDateTime.now());
        notebookRepository.save(notebookExistente);

    }


    public void deletaNotebookPorId(Integer id) {
        if(!notebookRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Notebook não encontrado.");
        }
        notebookRepository.deleteById(id);
    }


}
