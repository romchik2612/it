package com.lab.it.database.attribute;

import com.lab.it.table.value.RowColumnValue;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@Table("lab_attribute")
public class Attribute {
    @Column("database_id")
    private long databaseId;

    @Column("attribute_id")
    private long attributeId;

    @Column("name")
    private String name;

    @Column("columns_ids")
    private List<Long> columnsIds;

    @Transient
    private List<RowColumnValue> values;
}
