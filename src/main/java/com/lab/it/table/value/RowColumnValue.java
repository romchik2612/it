package com.lab.it.table.value;

import com.lab.it.table.column.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("row_column_value")
@AllArgsConstructor
@NoArgsConstructor
public class RowColumnValue {
    @Column("database_id")
    private long databaseId;

    @Column("table_id")
    private long tableId;

    @Column("row_id")
    private long rowId;

    @Column("column_id")
    private long columnId;

    @Column("value")
    private String value;

    @Column("type")
    private Type type;
}
