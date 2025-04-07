package com.notebookmanager.model.dto.createfields;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaCreateFields {

    @NotBlank
    @Size(min = 8, max = 8)
    private String alunoRa;

    @NotBlank
    @Size(min = 6, max = 6)
    private String notebookPatrimonio;
}
