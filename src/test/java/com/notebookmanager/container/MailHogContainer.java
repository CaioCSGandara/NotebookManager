package com.notebookmanager.container;


import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.List;

public class MailHogContainer {

    private static GenericContainer<?> mailHog ;
    static {
        mailHog = new GenericContainer<>(DockerImageName.parse("mailhog/mailhog"))
                .withExposedPorts(1025, 8025);

        mailHog.setPortBindings(List.of("1025:1025",  "8025:8025"));
    }

    public static void init() {
        if(!mailHog.isRunning()) {
            mailHog.start();
        }
    }

}
