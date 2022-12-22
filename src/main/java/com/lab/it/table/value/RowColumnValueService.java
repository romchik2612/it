package com.lab.it.table.value;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RowColumnValueService {
    @Autowired
    private RowColumnValueRepository repository;

    public Mono<RowColumnValue> insertValue(RowColumnValue value) {
        return repository.insertValue(value);
    }

    public Flux<RowColumnValue> getValuesByRow(long databaseId, long tableId, long rowId) {
        return repository.getValuesByRow(databaseId, tableId, rowId);
    }

    public Flux<RowColumnValue> getValuesByColumn(long databaseId, long columnId) {
        return repository.getValuesByColumn(databaseId, columnId);
    }

    public Mono<Void> deleteValuesByRow(long databaseId, long tableId, long rowId) {
        return repository.deleteValuesByRow(databaseId, tableId, rowId);
    }

    public Mono<Void> deleteValuesByColumn(long databaseId, long tableId, long columnId) {
        return repository.deleteValuesByColumn(databaseId, tableId, columnId);
    }

    public Mono<Void> deleteValuesByTable(long databaseId, long tableId) {
        return repository.deleteValuesByTable(databaseId, tableId);
    }
}
