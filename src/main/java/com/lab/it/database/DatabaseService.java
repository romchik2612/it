package com.lab.it.database;

import com.lab.it.table.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DatabaseService {
    @Autowired
    private DatabaseRepository repository;

    @Autowired
    private TableService tableService;

    public Mono<Database> createDatabase(Database database) {
        return repository.createDatabase(database);
    }

    public Mono<Database> getDatabase(long databaseId) {
        return repository.getDatabase(databaseId)
                .flatMap(database -> tableService.getTables(database.getDatabaseId()).collectList()
                        .map(tables -> {
                            database.setTables(tables);
                            return database;
                        }));
    }

    public Flux<Database> getDatabases() {
        return repository.getDatabases()
                .flatMap(database -> tableService.getTables(database.getDatabaseId()).collectList()
                        .map(tables -> {
                            database.setTables(tables);
                            return database;
                        }));
    }

    public Mono<Database> updateDatabase(long databaseId, Database database) {
        return repository.updateDatabase(databaseId, database);
    }

    public Mono<Void> deleteDatabase(long databaseId) {
        return repository.deleteDatabase(databaseId);
    }
}
