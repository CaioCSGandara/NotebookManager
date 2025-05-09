package com.notebookmanager.model.dto.createfields;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlunoLoginFields {

    @NotBlank
    @Size(min = 8, max = 8)
    private String ra;

    @NotBlank
    @Size(min = 5, max = 25)
    private String senha;

}
