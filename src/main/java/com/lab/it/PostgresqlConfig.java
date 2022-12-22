package com.lab.it;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
public class PostgresqlConfig {
    private final ConnectionFactory connectionFactory;

    private final R2dbcProperties r2dbcProps;

    @Autowired
    public PostgresqlConfig(ConnectionFactory connectionFactory, R2dbcProperties r2dbcProps) {
        this.connectionFactory = connectionFactory;
        this.r2dbcProps = r2dbcProps;
    }

    @Configuration(proxyBeanMethods = false)
    static class DatabaseInitializationConfiguration {
        @Autowired
        void initializeDatabase(ConnectionFactory connectionFactory) {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource[] scripts = new Resource[]{resourceLoader.getResource("classpath:schema.sql")};
            new ResourceDatabasePopulator(scripts).populate(connectionFactory).block();
        }
    }
}
