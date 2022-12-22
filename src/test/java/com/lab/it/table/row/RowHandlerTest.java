package com.lab.it.table.row;

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
public class RowHandlerTest {
    @Autowired
    private WebTestClient webClient;

    private final long databaseId = 1;

    private final long tableId = 2;

    @Test
    void test() {
        Row row = new Row();

        row = webClient.post().uri(builder -> builder.path("/databases/{databaseId}/tables/{tableId}/rows").build(databaseId, tableId))
                .body(Mono.just(row), Row.class)
                .exchange().returnResult(Row.class).getResponseBody().blockFirst();

        assert row != null;
        assert row.getDatabaseId() == 1;
        assert row.getTableId() == 2;
        assert row.getRowId() > 0;

        webClient.get().uri(builder -> builder.path("/databases/{databaseId}/tables/{tableId}/rows").build(databaseId, tableId))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("[0].databaseId").isEqualTo(databaseId)
                .jsonPath("[0].tableId").isEqualTo(tableId)
                .jsonPath("[0].rowId").isEqualTo(row.getRowId())
                .jsonPath("[1]").doesNotExist();

        final long rowId = row.getRowId();

        webClient.get().uri(builder -> builder.path("/databases/{databaseId}/tables/{tableId}/rows/{rowId}").build(databaseId, tableId, rowId))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("databaseId").isEqualTo(databaseId)
                .jsonPath("tableId").isEqualTo(tableId)
                .jsonPath("rowId").isEqualTo(row.getRowId());

        webClient.delete().uri(builder -> builder.path("/databases/{databaseId}/tables/{tableId}/rows/{rowId}").build(databaseId, tableId, rowId))
                .exchange().expectStatus().is2xxSuccessful();
    }
}
