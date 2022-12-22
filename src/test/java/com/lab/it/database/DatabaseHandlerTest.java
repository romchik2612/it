package com.lab.it.database;

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
public class DatabaseHandlerTest {
    @Autowired
    private WebTestClient webClient;

    @Test
    void test() {
        Database database = new Database();
        database.setName("db1");

        database = webClient.post().uri(builder -> builder.path("/databases").build())
                .body(Mono.just(database), Database.class)
                .exchange().returnResult(Database.class).getResponseBody().blockFirst();

        assert database != null;
        assert database.getDatabaseId() > 0;
        assert database.getName().equals("db1");

        webClient.get().uri(builder -> builder.path("/databases").build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("[0].databaseId").isEqualTo(database.getDatabaseId())
                .jsonPath("[0].name").isEqualTo("db1")
                .jsonPath("[1]").doesNotExist();

        final long databaseId = database.getDatabaseId();
        database.setName("db2");

        webClient.put().uri(builder -> builder.path("/databases/{databaseId}").build(databaseId))
                .body(Mono.just(database), Database.class)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("name").isEqualTo("db2");

        webClient.get().uri(builder -> builder.path("/databases/{databaseId}").build(databaseId))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("name").isEqualTo("db2");

        webClient.delete().uri(builder -> builder.path("/databases/{databaseId}").build(databaseId))
                .exchange().expectStatus().is2xxSuccessful();
    }
}
