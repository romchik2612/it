package com.lab.it.table;

import com.lab.it.AutoConfigurePostgres;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigurePostgres
@AutoConfigureWebTestClient
public class TableHandlerTest {
    @Autowired
    private WebTestClient webClient;

    private final long databaseId = 1;

    @Test
    void test() {
        DatabaseTable table = new DatabaseTable();
        table.setName("table");

        table = webClient.post().uri(builder -> builder.path("/databases/{databaseId}/tables").build(databaseId))
                .body(Mono.just(table), DatabaseTable.class)
                .exchange().returnResult(DatabaseTable.class).getResponseBody().blockFirst();

        assert table != null;
        assert table.getDatabaseId() == 1;
        assert table.getTableId() > 0;
        assert table.getName().equals("table");

        webClient.get().uri(builder -> builder.path("/databases/{databaseId}/tables").build(databaseId))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("[0].databaseId").isEqualTo(databaseId)
                .jsonPath("[0].tableId").isEqualTo(table.getTableId())
                .jsonPath("[0].name").isEqualTo("table")
                .jsonPath("[1]").doesNotExist();

        final long tableId = table.getTableId();
        table.setName("table_updated");

        webClient.put().uri(builder -> builder.path("/databases/{databaseId}/tables/{tableId}").build(databaseId, tableId))
                .body(Mono.just(table), DatabaseTable.class)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("name").isEqualTo("table_updated");

        webClient.get().uri(builder -> builder.path("/databases/{databaseId}/tables/{tableId}").build(databaseId, tableId))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("name").isEqualTo("table_updated");

        webClient.delete().uri(builder -> builder.path("/databases/{databaseId}/tables/{tableId}").build(databaseId, tableId))
                .exchange().expectStatus().is2xxSuccessful();
    }
}
