package com.notebookmanager.controller;

import com.notebookmanager.infra.exception.ValidationException;
import com.notebookmanager.model.Funcionario;
import com.notebookmanager.model.dto.Payload;
import com.notebookmanager.model.dto.createfields.FuncionarioLoginFields;
import com.notebookmanager.service.FuncionarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping("/login")
    public ResponseEntity<Payload> loginComRfESenha(@RequestBody @Valid FuncionarioLoginFields funcionarioLoginFields, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException("Erro de validação ao fazer login com RF e senha.");
        }
        Optional<Funcionario> funcionario = funcionarioService.loginComRfESenha(funcionarioLoginFields.getRf(),
                funcionarioLoginFields.getSenha());

        return ResponseEntity.ok(new Payload(HttpStatus.OK, funcionario.get(), null));
    }
}
