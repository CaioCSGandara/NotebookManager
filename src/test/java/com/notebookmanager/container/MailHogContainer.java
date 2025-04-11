package com.notebookmanager.container;


import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class MailHogContainer {

    private static final GenericContainer<?> mailHog = new GenericContainer<>(DockerImageName.parse("mailhog/mailhog"))
            .withExposedPorts(1025, 8025);

    public static void init() {
        if(!mailHog.isRunning()) {
            mailHog.start();
            System.setProperty("spring.mail.port", mailHog.getMappedPort(1025).toString());
        }
    }

    public static int getWebPort() {
        return mailHog.getMappedPort(8025);
    }
}
