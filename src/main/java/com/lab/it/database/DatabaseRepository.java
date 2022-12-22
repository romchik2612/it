package com.lab.it.database;

import com.lab.it.SequenceUniqueIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class DatabaseRepository {
    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    @Autowired
    private SequenceUniqueIdGenerator idGenerator;

    public Mono<Database> createDatabase(Database database) {
        return idGenerator.nextId()
                .doOnNext(database::setDatabaseId)
                .flatMap(id -> entityTemplate.insert(database));
    }

    public Mono<Database> getDatabase(long databaseId) {
        return entityTemplate.select(Database.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)))
                .one();
    }

    public Flux<Database> getDatabases() {
        return entityTemplate.select(Database.class).all();
    }

    public Mono<Database> updateDatabase(long databaseId, Database database) {
        return entityTemplate.update(Database.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)))
                .apply(Update.update("name", database.getName())).thenReturn(database);
    }

    public Mono<Void> deleteDatabase(long databaseId) {
        return entityTemplate.delete(Database.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)))
                .all().then();
    }
}
