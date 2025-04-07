package com.notebookmanager.model.dto.createfields;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotebookCreateFields {

    @NotBlank
    @Size(min = 6, max = 6)
    private String patrimonio;

}
