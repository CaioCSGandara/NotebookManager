package com.notebookmanager.model.dto.createfields;

import com.notebookmanager.model.enums.Curso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class AlunoCreateFields {

    @NotBlank
    @Size(min = 1, max = 30)
    private String nome;

    @NotBlank
    @Size(min = 8, max = 8)
    private String ra;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._-]+@puccampinas\\.edu\\.br$")
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\(\\d{2}\\)\\d{5}-\\d{4}$")
    private String telefone;

    @NotNull
    private Curso curso;
}
