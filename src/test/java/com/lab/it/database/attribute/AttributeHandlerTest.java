package com.lab.it.database.attribute;

import com.lab.it.AutoConfigurePostgres;
import com.lab.it.table.DatabaseTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.List;

@SpringBootTest
@AutoConfigurePostgres
@AutoConfigureWebTestClient
public class AttributeHandlerTest {
    @Autowired
    private WebTestClient webClient;

    private final long databaseId = 1;

    @Test
    void test() {
        Attribute attribute = new Attribute();
        attribute.setName("attribute");
        attribute.setColumnsIds(new LinkedList<>(List.of(1L, 2L)));

        attribute = webClient.post().uri(builder -> builder.path("/databases/{databaseId}/attributes").build(databaseId))
                .body(Mono.just(attribute), Attribute.class)
                .exchange().returnResult(Attribute.class).getResponseBody().blockFirst();

        assert attribute != null;
        assert attribute.getDatabaseId() == 1;
        assert attribute.getAttributeId() > 0;
        assert attribute.getName().equals("attribute");
        assert attribute.getColumnsIds().size() == 2;

        webClient.get().uri(builder -> builder.path("/databases/{databaseId}/attributes").build(databaseId))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("[0].databaseId").isEqualTo(databaseId)
                .jsonPath("[0].attributeId").isEqualTo(attribute.getAttributeId())
                .jsonPath("[0].name").isEqualTo("attribute")
                .jsonPath("[1]").doesNotExist();

        final long attributeId = attribute.getAttributeId();
        attribute.setName("attribute_updated");

        webClient.put().uri(builder -> builder.path("/databases/{databaseId}/attributes/{attributeId}").build(databaseId, attributeId))
                .body(Mono.just(attribute), Attribute.class)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("name").isEqualTo("attribute_updated");

        webClient.get().uri(builder -> builder.path("/databases/{databaseId}/attributes/{attributeId}").build(databaseId, attributeId))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("name").isEqualTo("attribute_updated");

        webClient.delete().uri(builder -> builder.path("/databases/{databaseId}/attributes/{attributeId}").build(databaseId, attributeId))
                .exchange().expectStatus().is2xxSuccessful();
    }
}
