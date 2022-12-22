package com.lab.it.table.column;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ColumnHandler {
    @Autowired
    private ColumnService service;

    public Mono<ServerResponse> createColumn(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));
        long tableId = Long.parseLong(request.pathVariable("tableId"));

        return request.bodyToMono(TableColumn.class)
                .flatMap(column -> service.createColumn(databaseId, tableId, column)
                        .flatMap(created -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(created)));
    }

    public Mono<ServerResponse> getColumn(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));
        long tableId = Long.parseLong(request.pathVariable("tableId"));
        long columnId = Long.parseLong(request.pathVariable("columnId"));

        return service.getColumn(databaseId, tableId, columnId)
                .flatMap(column -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(column));
    }

    public Mono<ServerResponse> getColumns(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));
        long tableId = Long.parseLong(request.pathVariable("tableId"));

        return service.getColumns(databaseId, tableId).collectList()
                .flatMap(columns -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(columns));
    }

    public Mono<ServerResponse> updateColumn(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));
        long tableId = Long.parseLong(request.pathVariable("tableId"));
        long columnId = Long.parseLong(request.pathVariable("columnId"));

        return request.bodyToMono(TableColumn.class)
                .flatMap(column -> service.updateColumn(databaseId, tableId, columnId, column)
                        .flatMap(updated -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(updated)));
    }

    public Mono<ServerResponse> deleteColumn(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));
        long tableId = Long.parseLong(request.pathVariable("tableId"));
        long columnId = Long.parseLong(request.pathVariable("columnId"));

        return service.deleteColumn(databaseId, tableId, columnId).then(ServerResponse.ok().build());
    }
}
