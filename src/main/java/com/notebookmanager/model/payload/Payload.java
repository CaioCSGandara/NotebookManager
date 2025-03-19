package com.notebookmanager.model.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class Payload {
    private HttpStatus status;
    private Object data;
    private String message;
}
