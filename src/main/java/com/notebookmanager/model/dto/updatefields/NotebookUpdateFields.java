package com.notebookmanager.model.dto.updatefields;

import com.notebookmanager.model.enums.StatusNotebook;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotebookUpdateFields {

    @NotNull
    private StatusNotebook status;

}
