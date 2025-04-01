package com.notebookmanager.model.dto.updatefields;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaUpdateFields {

    @Size(min = 6, max = 6)
    private String notebookPatrimonio;
}
