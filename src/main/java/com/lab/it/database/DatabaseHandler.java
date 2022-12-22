package com.lab.it.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class DatabaseHandler {
    @Autowired
    private DatabaseService service;

    public Mono<ServerResponse> createDatabase(ServerRequest request) {
        return request.bodyToMono(Database.class)
                        .flatMap(database -> service.createDatabase(database)
                                .flatMap(created -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(created)));
    }

    public Mono<ServerResponse> getDatabase(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));

        return service.getDatabase(databaseId)
                .flatMap(database -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(database));
    }

    public Mono<ServerResponse> getDatabases(ServerRequest request) {
        return service.getDatabases().collectList()
                .flatMap(databases -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(databases));
    }

    public Mono<ServerResponse> updateDatabase(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));

        return request.bodyToMono(Database.class)
                .flatMap(database -> service.updateDatabase(databaseId, database)
                        .flatMap(updated -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(updated)));
    }

    public Mono<ServerResponse> deleteDatabase(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));

        return service.deleteDatabase(databaseId).then(ServerResponse.ok().build());
    }
}
