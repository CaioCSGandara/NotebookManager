package com.notebookmanager.service;

import com.notebookmanager.infra.exception.RecursoNaoEncontradoException;
import com.notebookmanager.infra.exception.SenhaInvalidaException;
import com.notebookmanager.model.Funcionario;
import com.notebookmanager.model.repositories.FuncionarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public Optional<Funcionario> loginComRfESenha(String rf, String senha) {
        Optional<Funcionario> funcionario = funcionarioRepository.findByRf(rf);

        if(funcionario.isEmpty()) throw new RecursoNaoEncontradoException("RF inválido.");
        if(!funcionario.get().getSenha().equals(senha)) throw new SenhaInvalidaException();

        return funcionario;
    }
}
