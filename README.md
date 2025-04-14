# API que gerencia empréstimo de notebooks para alunos em uma comunidade acadêmica

## Overview

- Um aluno pode realizar uma reserva de notebook após cadastrar informações de contato no sistema
- Controle de estados de um notebook (disponível, emprestado, afastado)
- Listagem de todas as reservas ativas
- Possibilidade de trocar de notebook durante a reserva
- Encerramento de reserva é feita pelo funcionário responsável por receber o equipamento de volta
- Envio de comprovante por e-mail nas ações de empréstimo, troca e devolução

#

## Install requirements

- Java OpenJDK 17
- Maven 4.0.0
- Docker Engine

#

## Use guide

### Install dependencies

`mvn compile`

### Run tests

`mvn test`

### Run app

`mvn spring-boot:run`

#

# Endpoints

## 🎓 Alunos (`/alunos`)

### 🟢 `GET /alunos`

**Descrição**: Lista todos os alunos com paginação ordenação padrão (page 0, size 20, sort by nome asc).

<br>

**Query Params**:

- page
- size
- sort

<br>

**Resposta esperada (200 OK)**:

```json
{
  "status": "OK",
  "data": [
    {
      "id": 6,
      "nome": "Mario Souza",
      "ra": "09318492",
      "email": "marios2@puccampinas.edu.br",
      "telefone": "(19)99083-2415",
      "curso": "MEDICINA_VETERINARIA"
    },
    {
      "id": 4,
      "nome": "Oscar Moura",
      "ra": "87019341",
      "email": "oscarmoura@puccampinas.edu.br",
      "telefone": "(19)98017-7111",
      "curso": "NUTRICAO"
    }
  ],
  "message": null
}
```

---

### 🟢 `GET /alunos/{id}`

**Descrição**: Busca os dados de um aluno pelo ID.

<br>

**Resposta esperada**:

- 200 OK

```json
{
  "status": "OK",
  "data": {
    "id": 4,
    "nome": "Oscar Moura",
    "ra": "87019341",
    "email": "oscarmoura@puccampinas.edu.br",
    "telefone": "(19)98017-7111",
    "curso": "NUTRICAO"
  },
  "message": null
}
```

<br>

<details>
<summary><b>Erros comuns</b></summary>

<br>

- 404 NOT FOUND

```json
{
  "status": "NOT_FOUND",
  "data": null,
  "message": "Aluno não encontrado."
}
```

</details>

---

### 🔵 `POST /alunos`

**Descrição**: Cadastra um novo aluno.

- Cursos disponíveis: BIOLOGIA,
  BIOMEDICINA,
  ENFERMAGEM,
  FARMACIA,
  FISIOTERAPIA,
  FONOAUDIOLOGIA,
  MEDICINA,
  MEDICINA_VETERINARIA,
  NUTRICAO,
  ODONTOLOGIA,
  PSICOLOGIA,
  TERAPIA_OCUPACIONAL.

<br>

**Exemplo de body:**

```json
{
  "nome": "Jose Almeida",
  "ra": "54135215",
  "email": "josedsdaa@puccampinas.edu.br",
  "telefone": "(19)99414-8554",
  "curso": "NUTRICAO"
}
```

<br>

**Resposta esperada**:

- 201 CREATED
- Rota para encontrar o novo aluno criado pode ser acessada na head da resposta.

<br>

<details>
<summary><b>Erros comuns</b></summary>

<br>

- 400 BAD REQUEST
- Ocorre quando são enviados dados inválidos no body.

```json
{
  "status": "BAD_REQUEST",
  "data": null,
  "message": "Erro de validação ao criar aluno."
}
```

<br>

- 409 CONFLICT
- Ocorre quando um RA ou um e-mail que já existe no banco é enviado no body.

```json
{
  "status": "CONFLICT",
  "data": null,
  "message": "O Aluno com este RA e/ou e-mail já está cadastrado."
}
```

</details>

---

### 🟠 `PUT /alunos/{id}`

**Descrição**: Atualiza os dados (possíveis) de um aluno existente.

<br>

**Exemplo de body:**

```json
{
  "nome": "José Almdeida Fonseca",
  "telefone": "(19)94235-4212",
  "curso": "ENFERMAGEM"
}
```

<br>

**Resposta esperada**:

- 204 NO CONTENT

<br>

<details>
<summary><b>Erros comuns</b></summary>

<br>

- 400 BAD REQUEST
- Ocorre quando são enviados dados inválidos no body.

```json
{
  "status": "BAD_REQUEST",
  "data": null,
  "message": "Erro de validação ao atualizar o aluno."
}
```

<br>

- 404 NOT FOUND

```json
{
  "status": "NOT_FOUND",
  "data": null,
  "message": "Aluno não encontrado."
}
```

</details>

---

### 🔴 `DELETE /alunos/{id}`

**Descrição**: Remove um aluno pelo ID.

<br>

**Resposta esperada**:

- 204 NO CONTENT

<br>

<details>
<summary><b>Erros comuns</b></summary>

<br>

- 404 NOT FOUND

```json
{
  "status": "NOT_FOUND",
  "data": null,
  "message": "Aluno não encontrado."
}
```

</details>

---

## 💻 Notebooks (`/notebooks`)

### 🟢 `GET /notebooks`

**Descrição**: Lista todos os notebooks com paginação e ordenação padrão (page 0, size 20, sort by status asc).

<br>

**Query Params**:

- page
- size
- sort

<br>

**Resposta esperada (200 OK)**:

```json
{
  "status": "OK",
  "data": [
    {
      "id": 4,
      "patrimonio": "983410",
      "status": "DISPONIVEL"
    },
    {
      "id": 5,
      "patrimonio": "222222",
      "status": "DISPONIVEL"
    }
  ],
  "message": null
}
```

---

### 🟢 `GET /notebooks/{id}`

**Descrição**: Busca os dados de um notebook pelo ID.

<br>

**Resposta esperada**:

- 200 OK

```json
{
  "status": "OK",
  "data": {
    "id": 4,
    "patrimonio": "983410",
    "status": "DISPONIVEL"
  },
  "message": null
}
```

<br>

<details>
<summary><b>Erros comuns</b></summary>

<br>

- 404 NOT FOUND

```json
{
  "status": "NOT_FOUND",
  "data": null,
  "message": "Notebook não encontrado."
}
```

</details>

---

### 🔵 `POST /notebooks`

**Descrição**: Cadastra um novo notebook.

<br>

**Exemplo de body:**

```json
{
  "patrimonio": "341045"
}
```

<br>

**Resposta esperada**:

- 201 CREATED
- Rota para encontrar o novo notebook criado pode ser acessada na head da resposta.

<br>

<details>
<summary><b>Erros comuns</b></summary>

<br>

- 400 BAD REQUEST
- Ocorre quando são enviados dados inválidos no body.

```json
{
  "status": "BAD_REQUEST",
  "data": null,
  "message": "Erro de validação ao criar notebook."
}
```

<br>

- 409 CONFLICT
- Ocorre quando o número de patrimônio que já existe no banco é enviado no body.

```json
{
  "status": "CONFLICT",
  "data": null,
  "message": "O Notebook com este patrimonio já está cadastrado."
}
```

</details>

---

### 🟡 `PATCH /notebooks/{id}/gerenciar-status`

**Descrição**: Atualiza o status do notebook. Status possíveis: AFASTADO, DISPONIVEL, EMPRESTADO. O body deve conter o novo status do notebook.

<br>

**Exemplo de body:**

```json
{
  "status": "DISPONIVEL"
}
```

<br>

**Resposta esperada**:

- 204 NO CONTENT

<br>

<details>
<summary><b>Erros comuns</b></summary>

<br>

- 404 NOT FOUND

```json
{
  "status": "NOT_FOUND",
  "data": null,
  "message": "Notebook não encontrado."
}
```

<br>

- 400 BAD REQUEST
- Ocorre quando são enviados dados inválidos no body.

```json
{
  "status": "BAD_REQUEST",
  "data": null,
  "message": "Erro de validação ao atualizar notebook."
}
```

<br>

- 400 BAD REQUEST
- Ocorre ao tentar tornar DISPONIVEL o status de um notebook que já está DISPONIVEL.

```json
{
  "status": "BAD_REQUEST",
  "data": null,
  "message": "Para tornar um notebook DISPONIVEL, ele deve estar EMPRESTADO ou AFASTADO."
}
```

<br>

- 400 BAD REQUEST
- Ocorre ao tentar tornar EMPRESTADO ou AFASTADO o status de um notebook que já está EMPRESTADO ou AFASTADO.

```json
{
  "status": "BAD_REQUEST",
  "data": null,
  "message": "Para tornar um notebook EMPRESTADO ou AFASTADO, ele deve estar DISPONIVEL."
}
```

</details>

---

### 🔴 `DELETE /notebooks/{id}`

**Descrição**: Remove um notebook pelo ID.

<br>

**Resposta esperada**:

- 204 NO CONTENT

<br>

<details>
<summary><b>Erros comuns</b></summary>

<br>

- 404 NOT FOUND

```json
{
  "status": "NOT_FOUND",
  "data": null,
  "message": "Notebook não encontrado."
}
```

</details>

---

## 📝 Reservas (`/reservas`)

### 🟢 `GET /reservas`

**Descrição**: Lista todas as reservas que ainda estão ativas (TERMINO_EM is NULL no banco de dados) com paginação e ordenação padrão (page 0, size 20).

<br>

**Query params**:

- page
- size

<br>

**Resposta esperada**:

- 200 OK

```json
{
  "status": "OK",
  "data": [
    {
      "id": 27,
      "aluno": {
        "id": 3,
        "nome": "Caio Santos",
        "ra": "23015616",
        "email": "caio.cgs2@puccampinas.edu.br",
        "telefone": "(19)99414-8554",
        "curso": "NUTRICAO"
      },
      "notebook": {
        "id": 2,
        "patrimonio": "939082",
        "status": "EMPRESTADO"
      },
      "inicioEm": "2025-04-07T19:56:59.725815",
      "terminoEm": null
    }
  ],
  "message": null
}
```

---

### 🔵 `POST /reservas`

**Descrição**: Cria uma nova reserva de notebook para um aluno. Url para acessar a nova reserva pode ser acessada pelo head da resposta.

<br>

**Exemplo de body:**

```json
{
  "alunoRa": "54135215",
  "notebookPatrimonio": "939082"
}
```

<br>

**Resposta esperada**:

- 201 CREATED

<br>

<details>
<summary><b>Erros comuns</b></summary>

<br>

- 404 NOT FOUND

```json
{
  "status": "NOT_FOUND",
  "data": null,
  "message": "Notebook não encontrado."
}
```

- 404 NOT FOUND

```json
{
  "status": "NOT_FOUND",
  "data": null,
  "message": "Aluno não encontrado."
}
```

<br>

- 400 BAD REQUEST
- Ocorre ao tentar emprestar um notebook que está com status EMPRESTADO ou AFASTADO.

```json
{
  "status": "BAD_REQUEST",
  "data": null,
  "message": "Para tornar um notebook EMPRESTADO ou AFASTADO, ele deve estar DISPONIVEL."
}
```

<br>

- 400 BAD REQUEST
- Ocorre quando os parâmetros passados no body são inválidos.

```json
{
  "status": "BAD_REQUEST",
  "data": null,
  "message": "Erro de validação ao criar reserva"
}
```

</details>



---


### 🟡 `PATCH /reservas/{id}/encerrar-reserva`

**Descrição**: Encerra uma reserva ativa e atualiza o status do notebook.

<br>

**Exemplo de body:**

- Passa o status DISPONIVEL para o notebook que será devolvido (SUJEITO A ALTERAÇÃO).

```json
{
  "status": "DISPONIVEL"
}
```

<br>

**Resposta esperada**:

- 204 NO CONTENT

<br>

<details>
<summary><b>Erros comuns</b></summary>

<br>

- 400 NOT FOUND
- Ocorre quando o id da reserva passado na url não existe.

```json
{
  "status": "NOT_FOUND",
  "data": null,
  "message": "Reserva com o id especificado não foi encontrada."
}
```
</details>

---

### 🟡 `PATCH /reservas/{id}/trocar-notebook`

**Descrição**: Troca o notebook do aluno durante o empréstimo (encerra reserva atual e cria outra com o notebook referenciado no body).

<br>

**Exemplo de body**:

```json
{
  "notebookPatrimonio": "939082"
}
```

<br>

**Resposta esperada**:

- 204 NO CONTENT

<br>

<details>
<summary><b>Erros comuns</b></summary>

<br>

- 404 NOT FOUND

```json
{
  "status": "NOT_FOUND",
  "data": null,
  "message": "Notebook não encontrado."
}
```

<br>

- 400 BAD REQUEST
- Erro de validação no patrimônio do novo notebook.

```json
{
  "status": "BAD_REQUEST",
  "data": null,
  "message": "Erro de validação ao trocar de notebook"
}
```

<br>

- 400 BAD REQUEST
- Ocorre ao tentar emprestar um notebook que está com status EMPRESTADO ou AFASTADO.

```json
{
  "status": "BAD_REQUEST",
  "data": null,
  "message": "Para tornar um notebook EMPRESTADO ou AFASTADO, ele deve estar DISPONIVEL."
}
```

<br>

- 400 NOT FOUND
- Ocorre quando o id da reserva passado na url não existe.

```json
{
  "status": "NOT_FOUND",
  "data": null,
  "message": "Reserva com o id especificado não foi encontrada."
}
```

</details>
