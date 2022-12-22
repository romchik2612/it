package com.lab.it.table.column;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("lab_column")
public class TableColumn {
    @Column("database_id")
    private long databaseId;

    @Column("table_id")
    private long tableId;

    @Column("column_id")
    private long columnId;

    @Column("type")
    private Type type;

    @Column("name")
    private String name;

    @Column("default_value")
    private String defaultValue;
}
