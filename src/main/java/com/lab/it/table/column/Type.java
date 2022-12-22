package com.lab.it.table.column;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Type {
    STRING, INT, LONG, HTML, ARRAY, BOOLEAN, CHAR, JSON, DOUBLE, FLOAT;

    @JsonCreator
    public Type forValue(String type) {
        return valueOf(type.toUpperCase());
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public Class aClass() {
        switch (this) {
            case LONG:
                return Long.class;
            case INT:
                return Integer.class;
            case STRING:
            case HTML:
            case JSON:
                return String.class;
            case BOOLEAN:
                return Boolean.class;
            case CHAR:
                return Character.class;
            case DOUBLE:
                return Double.class;
            case FLOAT:
                return Float.class;
            default:
                return Object.class;
        }
    }
}
