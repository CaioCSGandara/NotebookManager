package com.notebookmanager.model.entities.enums;

public enum Curso {
    BIOLOGIA("Biologia"),
    BIOMEDICINA("Biomedicina"),
    ENFERMAGEM("Enfermagem"),
    FARMACIA("Farmácia"),
    FISIOTERAPIA("Fisioterapia"),
    FONOAUDIOLOGIA("Fonoaudiologia"),
    MEDICINA("Medicina"),
    MEDICINA_VETERINARIA("Medicina Veterinária"),
    NUTRICAO("Nutrição"),
    ODONTOLOGIA("Odontologia"),
    PSICOLOGIA("Psicologia"),
    TERAPIA_OCUPACIONAL("Terapia Ocupacional");

    private String nomeFormatado;

    Curso(String nomeFormatado) {
        this.nomeFormatado = nomeFormatado;
    }

    public String getNomeFormatado() {
        return nomeFormatado;
    }

    public static Curso toCurso(String nome) {
        for(Curso c : Curso.values()) {
            if (c.getNomeFormatado().equals(nome)) {
                return c;
            }
        }

        throw new IllegalArgumentException("Curso não encontrado na lista.");
    }


}
