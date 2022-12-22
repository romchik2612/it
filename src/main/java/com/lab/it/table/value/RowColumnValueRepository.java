package com.lab.it.table.value;

import com.lab.it.table.row.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class RowColumnValueRepository {
    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    public Mono<RowColumnValue> insertValue(RowColumnValue value) {
        return entityTemplate.insert(value);
    }

    public Flux<RowColumnValue> getValuesByRow(long databaseId, long tableId, long rowId) {
        return entityTemplate.select(RowColumnValue.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("table_id").is(tableId)
                        .and("row_id").is(rowId)))
                .all();
    }

    public Flux<RowColumnValue> getValuesByColumn(long databaseId, long columnId) {
        return entityTemplate.select(RowColumnValue.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("column_id").is(columnId)))
                .all();
    }

    public Mono<Void> deleteValuesByRow(long databaseId, long tableId, long rowId) {
        return entityTemplate.delete(RowColumnValue.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("table_id").is(tableId)
                        .and("row_id").is(rowId)))
                .all().then();
    }

    public Mono<Void> deleteValuesByColumn(long databaseId, long tableId, long columnId) {
        return entityTemplate.delete(RowColumnValue.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("table_id").is(tableId)
                        .and("column_id").is(columnId)))
                .all().then();
    }

    public Mono<Void> deleteValuesByTable(long databaseId, long tableId) {
        return entityTemplate.delete(RowColumnValue.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("table_id").is(tableId)))
                .all().then();
    }
}
