package com.lab.it.table.column;

import com.lab.it.SequenceUniqueIdGenerator;
import com.lab.it.table.DatabaseTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ColumnRepository {
    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    @Autowired
    private SequenceUniqueIdGenerator idGenerator;

    public Mono<TableColumn> createColumn(long databaseId, long tableId, TableColumn column) {
        column.setDatabaseId(databaseId);
        column.setTableId(tableId);
        return idGenerator.nextId()
                .doOnNext(column::setColumnId)
                .flatMap(id -> entityTemplate.insert(column));
    }

    public Mono<TableColumn> getColumn(long databaseId, long tableId, long columnId) {
        return entityTemplate.select(TableColumn.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("table_id").is(tableId)
                        .and("column_id").is(columnId)))
                .one();
    }

    public Flux<TableColumn> getColumns(long databaseId, long tableId) {
        return entityTemplate.select(TableColumn.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("table_id").is(tableId))).all();
    }

    public Mono<TableColumn> updateColumn(long databaseId, long tableId, long columnId, TableColumn column) {
        return entityTemplate.update(TableColumn.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("table_id").is(tableId)
                        .and("column_id").is(columnId)))
                .apply(Update.update("name", column.getName())).thenReturn(column);
    }

    public Mono<Void> deleteColumn(long databaseId, long tableId, long columnId) {
        return entityTemplate.delete(TableColumn.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("table_id").is(tableId)
                        .and("column_id").is(columnId)))
                .all().then();
    }

    public Mono<Void> deleteColumns(long databaseId, long tableId) {
        return entityTemplate.delete(TableColumn.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("table_id").is(tableId)))
                .all().then();
    }
}
