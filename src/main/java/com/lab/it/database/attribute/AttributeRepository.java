package com.lab.it.database.attribute;

import com.lab.it.SequenceUniqueIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class AttributeRepository {
    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    @Autowired
    private SequenceUniqueIdGenerator idGenerator;

    public Mono<Attribute> createAttribute(long databaseId, Attribute attribute) {
        attribute.setDatabaseId(databaseId);
        return idGenerator.nextId()
                .doOnNext(attribute::setAttributeId)
                .flatMap(id -> entityTemplate.insert(attribute));
    }

    public Mono<Attribute> getAttribute(long databaseId, long attributeId) {
        return entityTemplate.select(Attribute.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("attribute_id").is(attributeId)))
                .one();
    }

    public Flux<Attribute> getAttributes(long databaseId) {
        return entityTemplate.select(Attribute.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId))).all();
    }

    public Mono<Attribute> updateAttribute(long databaseId, long attributeId, Attribute attribute) {
        return entityTemplate.update(Attribute.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("attribute_id").is(attributeId)))
                .apply(Update.update("columns_ids", attribute.getColumnsIds().toArray(new Long[0]))
                        .set("name", attribute.getName())).thenReturn(attribute);
    }

    public Mono<Void> deleteAttribute(long databaseId, long attributeId) {
        return entityTemplate.delete(Attribute.class)
                .matching(Query.query(Criteria.where("database_id").is(databaseId)
                        .and("attribute_id").is(attributeId)))
                .all().then();
    }
}
