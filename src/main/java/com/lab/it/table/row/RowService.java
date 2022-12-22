package com.lab.it.table.row;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.it.table.column.ColumnService;
import com.lab.it.table.value.RowColumnValue;
import com.lab.it.table.value.RowColumnValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
public class RowService {
    @Autowired
    private RowRepository repository;

    @Autowired
    private RowColumnValueService rowColumnValueService;

    @Autowired
    @Lazy
    private ColumnService columnService;

    private final ObjectMapper mapper = new ObjectMapper();

    public Mono<Row> createRow(long databaseId, long tableId, Row row) {
        return repository.createRow(databaseId, tableId, row)
                .flatMap(created -> columnService.getColumns(databaseId, tableId)
                        .flatMap(column -> rowColumnValueService.insertValue(new RowColumnValue(databaseId, tableId,
                                created.getRowId(), column.getColumnId(), row.getValues().containsKey(column.getColumnId())
                        ? toString(row.getValues().get(column.getColumnId())) : column.getDefaultValue(), column.getType()))).then(Mono.just(created)));
    }

    public Mono<Row> getRow(long databaseId, long tableId, long rowId) {
        return repository.getRow(databaseId, tableId, rowId)
                .flatMap(row -> rowColumnValueService.getValuesByRow(databaseId, tableId, row.getRowId()).collectList()
                        .map(values -> {
                            row.setValues(values.stream().collect(Collectors.toMap(RowColumnValue::getColumnId, value -> fromString(value.getValue(), value.getType().aClass()))));
                            return row;
                        }));
    }

    public Flux<Row> getRows(long databaseId, long tableId) {
        return repository.getRows(databaseId, tableId)
                .flatMap(row -> rowColumnValueService.getValuesByRow(databaseId, tableId, row.getRowId()).collectList()
                        .map(values -> {
                            row.setValues(values.stream().collect(Collectors.toMap(RowColumnValue::getColumnId, value -> fromString(value.getValue(), value.getType().aClass()))));
                            return row;
                        }));
    }

    public Mono<Void> deleteRow(long databaseId, long tableId, long rowId) {
        return repository.deleteRow(databaseId, tableId, rowId)
                .then(rowColumnValueService.deleteValuesByRow(databaseId, tableId, rowId));
    }

    public Mono<Void> deleteRows(long databaseId, long tableId) {
        return repository.deleteRows(databaseId, tableId);
    }

    private <T> T fromString(String value, Class<T> toClass) {
        try {
            return mapper.readValue(value, toClass);
        } catch (JsonProcessingException e) {
            if (toClass.equals(String.class)) {
                return (T) value.toString();
            }
            throw new RuntimeException(e);
        }
    }

    private String toString(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
