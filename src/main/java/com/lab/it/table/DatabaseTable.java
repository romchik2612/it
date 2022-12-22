package com.lab.it.table;

import com.lab.it.table.column.TableColumn;
import com.lab.it.table.row.Row;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@Table("lab_table")
public class DatabaseTable {
    @Column("database_id")
    private long databaseId;

    @Column("table_id")
    private long tableId;

    @Column("name")
    private String name;

    @Transient
    private List<TableColumn> columns;

    @Transient
    private List<Row> rows;
}
