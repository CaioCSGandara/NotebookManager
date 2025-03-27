package com.notebookmanager.model.createfields;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotebookCreateFields {

    @NotBlank
    @Size(min = 5, max = 50)
    private String modelo;

    @NotNull
    @Size(min = 6, max = 6)
    private String patrimonio;

}
