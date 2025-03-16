package com.notebookmanager.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestConfiguration
@Testcontainers
@SpringBootTest
public abstract class BaseContainer {
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.7").withExposedPorts(27017);

    static {
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void containersProperties(DynamicPropertyRegistry registry) {
        String mongoUri = mongoDBContainer.getReplicaSetUrl();
        registry.add("spring.data.mongodb.uri", ()-> mongoUri);
        registry.add("spring.data.mongodb.database", ()-> "test");
    }

}
