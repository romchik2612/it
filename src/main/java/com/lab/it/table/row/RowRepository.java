package com.lab.it.table.row;

import com.lab.it.SequenceUniqueIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
public class RowRepository {
    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    @Autowired
    private SequenceUniqueIdGenerator idGenerator;

    public Mono<Row> createRow(long databaseId, long tableId, Row row) {
        row.setDatabaseId(databaseId);
        row.setTableId(tableId);
        row.setTimestamp(Instant.now());
        return idGenerator.nextId()
                .doOnNext(row::setRowId)
                .flatMap(id -> entityTemplate.insert(row));
    }

    public Mono<Row> getRow(long databaseId, long tableId, long rowId) {
        return entityTemplate.select(Row.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("table_id").is(tableId)
                        .and("row_id").is(rowId)))
                .one();
    }

    public Flux<Row> getRows(long databaseId, long tableId) {
        return entityTemplate.select(Row.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("table_id").is(tableId))).all();
    }

    public Mono<Void> deleteRow(long databaseId, long tableId, long rowId) {
        return entityTemplate.delete(Row.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("table_id").is(tableId)
                        .and("row_id").is(rowId)))
                .all().then();
    }

    public Mono<Void> deleteRows(long databaseId, long tableId) {
        return entityTemplate.delete(Row.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("table_id").is(tableId)))
                .all().then();
    }
}
