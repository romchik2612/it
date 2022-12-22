package com.lab.it;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PostgresExtension implements BeforeAllCallback {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:13.0-alpine")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("pass");

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        POSTGRES.start();

        System.setProperty("spring.data.postgres.port", POSTGRES.getMappedPort(5432).toString());
    }
}
