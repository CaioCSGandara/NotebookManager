INSERT INTO aluno (nome, ra, email, telefone, curso)
VALUES
    ('Julio Correa', '09135616', 'jcorrea...@puccampinas.edu.br', '(19)90914-3014', 'MEDICINA'),
    ('Maria Ferreira', '03781923', 'maria.ferreira...@puccampinas.edu.br', '(19)90814-2314', 'TERAPIA_OCUPACIONAL'),
    ('Fernando Pontes', '90174823', 'fernandohpontes...@puccampinas.edu.br', '(19)83914-0945', 'BIOMEDICINA'),
    ('Mariana Souza', '12345678', 'mariana.souza...@puccampinas.edu.br', '(19)99876-5432', 'MEDICINA_VETERINARIA'),
    ('Carlos Mendes', '87654321', 'carlos.mendes...@puccampinas.edu.br', '(19)91234-5678', 'ODONTOLOGIA'),
    ('Ana Paula Lima', '56781234', 'ana.lima...@puccampinas.edu.br', '(19)93456-7890', 'PSICOLOGIA'),
    ('Rodrigo Oliveira', '43218765', 'rodrigo.oliveira...@puccampinas.edu.br', '(19)94567-3210', 'BIOLOGIA');

INSERT INTO notebook (patrimonio, status)
VALUES
    ('491034', 'DISPONIVEL'),
    ('930183', 'EMPRESTADO'),
    ('398145', 'EMPRESTADO'),
    ('983410', 'EMPRESTADO'),
    ('123098', 'AFASTADO');


INSERT INTO reserva(inicio_em, termino_em, aluno, notebook)
VALUES
    ('2025-03-28 14:30:00', NULL, 4, 2),
    ('2025-03-28 12:21:03', NULL, 5, 3),
    ('2025-03-28 19:44:21', '2025-03-28 21:31:56', 6, 5);
