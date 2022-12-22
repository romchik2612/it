package com.lab.it.table;

import com.lab.it.table.column.ColumnService;
import com.lab.it.table.row.RowService;
import com.lab.it.table.value.RowColumnValue;
import com.lab.it.table.value.RowColumnValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TableService {
    @Autowired
    private TableRepository repository;

    @Autowired
    private ColumnService columnService;

    @Autowired
    private RowService rowService;

    @Autowired
    private RowColumnValueService rowColumnValueService;

    public Mono<DatabaseTable> createTable(long databaseId, DatabaseTable table) {
        return repository.createTable(databaseId, table);
    }

    public Mono<DatabaseTable> getTable(long databaseId, long tableId) {
        return repository.getTable(databaseId, tableId)
                .flatMap(table -> Mono.zip(columnService.getColumns(databaseId, table.getTableId()).collectList(),
                                rowService.getRows(databaseId, table.getTableId()).collectList())
                        .map(tuple -> {
                            table.setColumns(tuple.getT1());
                            table.setRows(tuple.getT2());
                            return table;
                        }));
    }

    public Flux<DatabaseTable> getTables(long databaseId) {
        return repository.getTables(databaseId)
                .flatMap(table -> Mono.zip(columnService.getColumns(databaseId, table.getTableId()).collectList(),
                                rowService.getRows(databaseId, table.getTableId()).collectList())
                        .map(tuple -> {
                            table.setColumns(tuple.getT1());
                            table.setRows(tuple.getT2());
                            return table;
                        }));
    }

    public Mono<DatabaseTable> updateTable(long databaseId, long tableId, DatabaseTable table) {
        return repository.updateTable(databaseId, tableId, table);
    }

    public Mono<Void> deleteTable(long databaseId, long tableId) {
        return repository.deleteTable(databaseId, tableId)
                .then(Mono.zip(rowService.deleteRows(databaseId, tableId),
                        columnService.deleteColumns(databaseId, tableId),
                        rowColumnValueService.deleteValuesByTable(databaseId, tableId)).then());
    }
}
