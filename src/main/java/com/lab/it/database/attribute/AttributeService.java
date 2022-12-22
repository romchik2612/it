package com.lab.it.database.attribute;

import com.lab.it.table.value.RowColumnValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AttributeService {
    @Autowired
    private AttributeRepository repository;

    @Autowired
    private RowColumnValueService rowColumnValueService;

    public Mono<Attribute> createAttribute(long databaseId, Attribute attribute) {
        return repository.createAttribute(databaseId, attribute);
    }

    public Mono<Attribute> getAttribute(long databaseId, long attributeId) {
        return repository.getAttribute(databaseId, attributeId)
                .flatMap(attribute -> Flux.fromIterable(attribute.getColumnsIds())
                        .flatMap(columnId -> rowColumnValueService.getValuesByColumn(databaseId, columnId))
                        .collectList().map(values -> {
                            attribute.setValues(values);
                            return attribute;
                        }));
    }

    public Flux<Attribute> getAttributes(long databaseId) {
        return repository.getAttributes(databaseId)
                .flatMap(attribute -> Flux.fromIterable(attribute.getColumnsIds())
                        .flatMap(columnId -> rowColumnValueService.getValuesByColumn(databaseId, columnId))
                        .collectList().map(values -> {
                            attribute.setValues(values);
                            return attribute;
                        }));
    }

    public Mono<Attribute> updateAttribute(long databaseId, long attributeId, Attribute attribute) {
        return repository.updateAttribute(databaseId, attributeId, attribute);
    }

    public Mono<Void> deleteAttribute(long databaseId, long attributeId) {
        return repository.deleteAttribute(databaseId, attributeId);
    }
}
