package com.lab.it.table;

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
public class TableRepository {
    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    @Autowired
    private SequenceUniqueIdGenerator idGenerator;

    public Mono<DatabaseTable> createTable(long databaseId, DatabaseTable table) {
        table.setDatabaseId(databaseId);
        return idGenerator.nextId()
                .doOnNext(table::setTableId)
                .flatMap(id -> entityTemplate.insert(table));
    }

    public Mono<DatabaseTable> getTable(long databaseId, long tableId) {
        return entityTemplate.select(DatabaseTable.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("table_id").is(tableId)))
                .one();
    }

    public Flux<DatabaseTable> getTables(long databaseId) {
        return entityTemplate.select(DatabaseTable.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId))).all();
    }

    public Mono<DatabaseTable> updateTable(long databaseId, long tableId, DatabaseTable table) {
        return entityTemplate.update(DatabaseTable.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("table_id").is(tableId)))
                .apply(Update.update("name", table.getName())).thenReturn(table);
    }

    public Mono<Void> deleteTable(long databaseId, long tableId) {
        return entityTemplate.delete(DatabaseTable.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("table_id").is(tableId)))
                .all().then();
    }
}
