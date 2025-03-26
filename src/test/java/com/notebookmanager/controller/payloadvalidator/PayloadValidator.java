package com.notebookmanager.controller.payloadvalidator;
import com.jayway.jsonpath.DocumentContext;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class PayloadValidator {

    public static void validateDataPayload(DocumentContext documentContext, String expectedStatus) {
        String status = documentContext.read("$.status");
        assertThat(status).isEqualTo(expectedStatus);

        Object data = documentContext.read("$.data");
        assertThat(data).isNotNull();

        String message = documentContext.read("$.message");
        assertThat(message).isNull();
    }


    public static void validateErrorPayload(DocumentContext documentContext, String expectedStatus) {
        String status = documentContext.read("$.status");
        assertThat(status).isEqualTo(expectedStatus);

        Object data = documentContext.read("$.data");
        assertThat(data).isNull();

        String message = documentContext.read("$.message");
        assertThat(message).isNotNull();
    }


}
