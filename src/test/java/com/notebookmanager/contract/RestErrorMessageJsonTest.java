package com.notebookmanager.contract;

import com.notebookmanager.exceptionhandler.RestErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class RestErrorMessageJsonTest {

    @Autowired
    private JacksonTester<RestErrorMessage> json;


    @Test
    void restErrorMessageSerializationTest() throws IOException {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.NOT_FOUND, "O recurso não foi encontrado.");

        assertThat(json.write(restErrorMessage)).isStrictlyEqualToJson("rest-error-message.json");
    }

}
