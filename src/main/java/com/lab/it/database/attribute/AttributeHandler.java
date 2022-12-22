package com.lab.it.database.attribute;

import com.lab.it.table.DatabaseTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AttributeHandler {
    @Autowired
    private AttributeService service;

    public Mono<ServerResponse> createAttribute(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));

        return request.bodyToMono(Attribute.class)
                .flatMap(attribute -> service.createAttribute(databaseId, attribute)
                        .flatMap(created -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(created)));
    }

    public Mono<ServerResponse> getAttribute(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));
        long attributeId = Long.parseLong(request.pathVariable("attributeId"));

        return service.getAttribute(databaseId, attributeId)
                .flatMap(attribute -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(attribute));
    }

    public Mono<ServerResponse> getAttributes(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));

        return service.getAttributes(databaseId).collectList()
                .flatMap(attributes -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(attributes));
    }

    public Mono<ServerResponse> updateAttribute(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));
        long attributeId = Long.parseLong(request.pathVariable("attributeId"));

        return request.bodyToMono(Attribute.class)
                .flatMap(attribute -> service.updateAttribute(databaseId, attributeId, attribute)
                        .flatMap(updated -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(updated)));
    }

    public Mono<ServerResponse> deleteAttribute(ServerRequest request) {
        long databaseId = Long.parseLong(request.pathVariable("databaseId"));
        long attributeId = Long.parseLong(request.pathVariable("attributeId"));

        return service.deleteAttribute(databaseId, attributeId).then(ServerResponse.ok().build());
    }
}
