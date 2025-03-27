package com.notebookmanager.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.notebookmanager.controller.payloadvalidator.PayloadValidator;
import com.notebookmanager.model.createfields.AlunoCreateFields;
import com.notebookmanager.model.enums.Curso;
import com.notebookmanager.model.updatefields.AlunoUpdateFields;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlunoControllerFailureTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void encontrarAlunoPorIdStatus404() {
        ResponseEntity<String> response = restTemplate.getForEntity("/alunos/96", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        PayloadValidator.validateErrorPayload(documentContext, "NOT_FOUND");

    }

    @Test
    void cadastrarAlunoStatus409() {
        AlunoCreateFields alunoCreateFields = new AlunoCreateFields("Jonathan Luciano", "09135616", "jonathanluciano@puccampinas.edu.br",
                "(19)94291-7013", Curso.NUTRICAO);

        ResponseEntity<String> response = restTemplate.postForEntity("/alunos", alunoCreateFields, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        PayloadValidator.validateErrorPayload(documentContext, "CONFLICT");
    }


    @Test
    void atualizarAlunoPorIdStatus404() {
        AlunoUpdateFields alunoUpdateFields = new AlunoUpdateFields("Josue Nao Existe No Banco", "(19)93123-4231", Curso.ODONTOLOGIA);
        HttpEntity<AlunoUpdateFields> request = new HttpEntity<>(alunoUpdateFields);

        ResponseEntity<String> response = restTemplate.exchange("/alunos/90", HttpMethod.PUT,
                request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        DocumentContext documentContext =  JsonPath.parse(response.getBody());

        PayloadValidator.validateErrorPayload(documentContext, "NOT_FOUND");
    }


    @Test
    void deletarAlunoPorIdStatus404() {
        ResponseEntity<String> response = restTemplate.exchange("/alunos/31", HttpMethod.DELETE,
                null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        DocumentContext documentContext =  JsonPath.parse(response.getBody());

        PayloadValidator.validateErrorPayload(documentContext, "NOT_FOUND");
    }

    // TESTES DE VALIDAÇÃO DE BODY:

    @Test
    void naoAceitaNomeInvalido() {

        String[] nomesInvalidos = {"Jorge Ferreira de Lima Almeida Santos da Silva", "", " ", null};


        for (String nomeInvalido : nomesInvalidos) {

            AlunoCreateFields alunoCreateFields = new AlunoCreateFields (nomeInvalido, "13279102", "jorgefdlass@puccampinas.edu.br", "(19)99182-4125",
                    Curso.NUTRICAO);

            HttpEntity<AlunoCreateFields> requestCreate = new HttpEntity<>(alunoCreateFields);

            AlunoUpdateFields alunoUpdateFields = new AlunoUpdateFields(nomeInvalido, "(19)99182-4125", Curso.NUTRICAO);

            HttpEntity<AlunoUpdateFields> requestUpdate = new  HttpEntity<>(alunoUpdateFields);

            assertResponseParaPropriedadeInvalida("/alunos", HttpMethod.POST, requestCreate, "Erro de validação ao criar aluno.");
            assertResponseParaPropriedadeInvalida("/alunos/3", HttpMethod.PUT, requestUpdate, "Erro de validação ao atualizar o aluno.");

        }
    }


    @Test
    void naoAceitaRaInvalido() {
        String[] rasInvalidos = {"1234567", "123456789", "", " ", null};

        for (String raInvalido : rasInvalidos) {
            AlunoCreateFields alunoCreateFields = new AlunoCreateFields ("Jorge Flavio Silva", raInvalido, "jorgefdlass@puccampinas.edu.br", "(19)99182-4125",
                    Curso.NUTRICAO);

            HttpEntity<AlunoCreateFields> requestCreate = new HttpEntity<>(alunoCreateFields);

            assertResponseParaPropriedadeInvalida("/alunos", HttpMethod.POST, requestCreate, "Erro de validação ao criar aluno.");

        }
    }


    @Test
    void naoAceitaEmailInvalido() {
        String[] emailsInvalidos = {"jorge^f@puccampinas.edu.br", "jorge.2f@gmail.com", "jorge#f@puccampinas.edu.br","", " ", null};

        for (String emailInvalido : emailsInvalidos) {
            AlunoCreateFields alunoCreateFields = new AlunoCreateFields ("Jorge Flavio Silva", "13279102", emailInvalido, "(19)99182-4125",
                    Curso.NUTRICAO);

            HttpEntity<AlunoCreateFields> requestCreate = new HttpEntity<>(alunoCreateFields);

            assertResponseParaPropriedadeInvalida("/alunos", HttpMethod.POST, requestCreate, "Erro de validação ao criar aluno.");

        }
    }


    @Test
    void naoAceitaTelefoneInvalido() {
        String[] telefonesInvalidos = {"(d9)12345-0987", "(19)1$345-0987", "(11)12345-0h87", "(19)123450987", "11 12345-0987", "", " ", null};

        for (String telefoneInvalido : telefonesInvalidos) {
            AlunoCreateFields alunoCreateFields = new AlunoCreateFields ("Jorge Flavio Silva", "13279102", "jorgefdlass@puccampinas.edu.br", telefoneInvalido, Curso.NUTRICAO);

            HttpEntity<AlunoCreateFields> requestCreate = new HttpEntity<>(alunoCreateFields);

            AlunoUpdateFields alunoUpdateFields = new AlunoUpdateFields("Jorge Flavio Silva", telefoneInvalido, Curso.NUTRICAO);

            HttpEntity<AlunoUpdateFields> requestUpdate = new  HttpEntity<>(alunoUpdateFields);

            assertResponseParaPropriedadeInvalida("/alunos", HttpMethod.POST, requestCreate, "Erro de validação ao criar aluno.");
            assertResponseParaPropriedadeInvalida("/alunos/3", HttpMethod.PUT, requestUpdate, "Erro de validação ao atualizar o aluno.");

        }
    }


    @Test
    void naoAceitaCursoNulo() {
        AlunoCreateFields alunoCreateFields = new AlunoCreateFields ("Jorge Flavio Silva", "13279102", "jorgefdlass@puccampinas.edu.br", "(19)99182-4125", null);

        HttpEntity<AlunoCreateFields> requestCreate = new HttpEntity<>(alunoCreateFields);

        AlunoUpdateFields alunoUpdateFields = new AlunoUpdateFields("Jorge Flavio Silva", "(19)99182-4125", null);

        HttpEntity<AlunoUpdateFields> requestUpdate = new  HttpEntity<>(alunoUpdateFields);

        assertResponseParaPropriedadeInvalida("/alunos", HttpMethod.POST, requestCreate, "Erro de validação ao criar aluno.");
        assertResponseParaPropriedadeInvalida("/alunos/3", HttpMethod.PUT, requestUpdate, "Erro de validação ao atualizar o aluno.");
    }




    private void assertResponseParaPropriedadeInvalida(String url, HttpMethod method, HttpEntity request, String errorMessage) {
        ResponseEntity<String> response = restTemplate.exchange(url, method, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        PayloadValidator.validateErrorPayload(documentContext, "BAD_REQUEST");

        String message = documentContext.read("$.message");
        assertThat(message).isEqualTo(errorMessage);
    }


}
