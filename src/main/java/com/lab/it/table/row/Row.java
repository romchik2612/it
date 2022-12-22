package com.lab.it.table.row;

import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Map;

@Data
@Table("lab_row")
public class Row {
    @Column("database_id")
    private long databaseId;

    @Column("table_id")
    private long tableId;

    @Column("row_id")
    private long rowId;

    @Column("last_update")
    private Instant timestamp;

    @Transient
    private Map<Long, Object> values;
}
