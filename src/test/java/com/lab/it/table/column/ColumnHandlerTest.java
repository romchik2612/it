package com.lab.it.table.column;

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
public class ColumnHandlerTest {
    @Autowired
    private WebTestClient webClient;

    private final long databaseId = 1;

    private final long tableId = 2;

    @Test
    void test() {
        TableColumn column = new TableColumn();
        column.setName("column");
        column.setType(Type.LONG);
        column.setDefaultValue("50");

        column = webClient.post().uri(builder -> builder.path("/databases/{databaseId}/tables/{tableId}/columns").build(databaseId, tableId))
                .body(Mono.just(column), TableColumn.class)
                .exchange().returnResult(TableColumn.class).getResponseBody().blockFirst();

        assert column != null;
        assert column.getDatabaseId() == 1;
        assert column.getTableId() == 2;
        assert column.getColumnId() > 0;
        assert column.getName().equals("column");

        webClient.get().uri(builder -> builder.path("/databases/{databaseId}/tables/{tableId}/columns").build(databaseId, tableId))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("[0].databaseId").isEqualTo(databaseId)
                .jsonPath("[0].tableId").isEqualTo(tableId)
                .jsonPath("[0].columnId").isEqualTo(column.getColumnId())
                .jsonPath("[0].name").isEqualTo("column")
                .jsonPath("[0].type").isEqualTo("LONG")
                .jsonPath("[1]").doesNotExist();

        final long columnId = column.getColumnId();
        column.setName("column_updated");

        webClient.put().uri(builder -> builder.path("/databases/{databaseId}/tables/{tableId}/columns/{columnId}").build(databaseId, tableId, columnId))
                .body(Mono.just(column), TableColumn.class)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("name").isEqualTo("column_updated");

        webClient.get().uri(builder -> builder.path("/databases/{databaseId}/tables/{tableId}/columns/{columnId}").build(databaseId, tableId, columnId))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("name").isEqualTo("column_updated");

        webClient.delete().uri(builder -> builder.path("/databases/{databaseId}/tables/{tableId}/columns/{columnId}").build(databaseId, tableId, columnId))
                .exchange().expectStatus().is2xxSuccessful();
    }
}
