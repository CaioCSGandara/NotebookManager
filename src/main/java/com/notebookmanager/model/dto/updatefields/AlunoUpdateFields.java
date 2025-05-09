package com.notebookmanager.model.dto.updatefields;

import com.notebookmanager.model.enums.Curso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Data
public class AlunoUpdateFields {

    @NotBlank
    @Size(min = 1, max = 30)
    private String nome;

    @NotBlank
    @Pattern(regexp = "^\\(\\d{2}\\)\\d{5}-\\d{4}$")
    private String telefone;

    @NotNull
    private Curso curso;

    @NotBlank
    @Size(min = 5, max = 25)
    private String senha;
}
