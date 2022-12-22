package com.lab.it;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SequenceUniqueIdGenerator {
    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    public Mono<Long> nextId() {
        return entityTemplate.getDatabaseClient()
            .sql("SELECT nextval('id_generator_seq')")
            .map(row -> row.get("nextval", Long.class))
            .one();
    }
}
