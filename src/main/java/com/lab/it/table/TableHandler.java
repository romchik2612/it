package com.lab.it.table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TableHandler {
    @Autowired
    private TableService service;

    public Mono<ServerResponse> createTable(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));

        return request.bodyToMono(DatabaseTable.class)
                .flatMap(table -> service.createTable(databaseId, table)
                        .flatMap(created -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(created)));
    }

    public Mono<ServerResponse> getTable(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));
        long tableId = Long.parseLong(request.pathVariable("tableId"));

        return service.getTable(databaseId, tableId)
                .flatMap(table -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(table));
    }

    public Mono<ServerResponse> getTables(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));

        return service.getTables(databaseId).collectList()
                .flatMap(tables -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(tables));
    }

    public Mono<ServerResponse> updateTable(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));
        long tableId = Long.parseLong(request.pathVariable("tableId"));

        return request.bodyToMono(DatabaseTable.class)
                .flatMap(database -> service.updateTable(databaseId, tableId, database)
                        .flatMap(updated -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(updated)));
    }

    public Mono<ServerResponse> deleteTable(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));
        long tableId = Long.parseLong(request.pathVariable("tableId"));

        return service.deleteTable(databaseId, tableId).then(ServerResponse.ok().build());
    }
}
