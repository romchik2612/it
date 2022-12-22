package com.lab.it.table.column;

import com.lab.it.table.row.RowService;
import com.lab.it.table.value.RowColumnValue;
import com.lab.it.table.value.RowColumnValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ColumnService {
    @Autowired
    private ColumnRepository repository;

    @Autowired
    private RowColumnValueService rowColumnValueService;

    @Autowired
    @Lazy
    private RowService rowService;

    public Mono<TableColumn> createColumn(long databaseId, long tableId, TableColumn column) {
        return repository.createColumn(databaseId, tableId, column)
                .flatMap(created -> rowService.getRows(databaseId, tableId)
                        .flatMap(row -> rowColumnValueService.insertValue(new RowColumnValue(databaseId, tableId,
                                row.getRowId(), created.getColumnId(), created.getDefaultValue(), created.getType())))
                        .then(Mono.just(created)));
    }

    public Mono<TableColumn> getColumn(long databaseId, long tableId, long columnId) {
        return repository.getColumn(databaseId, tableId, columnId);
    }

    public Flux<TableColumn> getColumns(long databaseId, long tableId) {
        return repository.getColumns(databaseId, tableId);
    }

    public Mono<TableColumn> updateColumn(long databaseId, long tableId, long columnId, TableColumn column) {
        return repository.updateColumn(databaseId, tableId, columnId, column);
    }

    public Mono<Void> deleteColumn(long databaseId, long tableId, long columnId) {
        return repository.deleteColumn(databaseId, tableId, columnId)
                .then(rowColumnValueService.deleteValuesByColumn(databaseId, tableId, columnId));
    }

    public Mono<Void> deleteColumns(long databaseId, long tableId) {
        return repository.deleteColumns(databaseId, tableId);
    }
}
