package com.lab.it.database;

import com.lab.it.table.DatabaseTable;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Transient;

import java.util.LinkedList;
import java.util.List;

@Data
@Table("database")
public class Database {
    @Column("database_id")
    private long databaseId;

    @Column("name")
    private String name;

    @Transient
    private List<DatabaseTable> tables = new LinkedList<>();
}
