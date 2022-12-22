package com.lab.it.table.row;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class RowHandler {
    @Autowired
    private RowService service;

    public Mono<ServerResponse> createRow(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));
        long tableId = Long.parseLong(request.pathVariable("tableId"));

        return request.bodyToMono(Row.class)
                .flatMap(row -> service.createRow(databaseId, tableId, row)
                        .flatMap(created -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(created)));
    }

    public Mono<ServerResponse> getRow(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));
        long tableId = Long.parseLong(request.pathVariable("tableId"));
        long rowId = Long.parseLong(request.pathVariable("rowId"));

        return service.getRow(databaseId, tableId, rowId)
                .flatMap(row -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(row));
    }

    public Mono<ServerResponse> getRows(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));
        long tableId = Long.parseLong(request.pathVariable("tableId"));

        return service.getRows(databaseId, tableId).collectList()
                .flatMap(rows -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(rows));
    }

    public Mono<ServerResponse> deleteRow(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));
        long tableId = Long.parseLong(request.pathVariable("tableId"));
        long rowId = Long.parseLong(request.pathVariable("rowId"));

        return service.deleteRow(databaseId, tableId, rowId).then(ServerResponse.ok().build());
    }
}
