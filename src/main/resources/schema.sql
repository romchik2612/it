--liquibase formatted sql

--create database table
create table IF NOT EXISTS database (
    database_id bigint not null,
    name varchar (255) unique,
    primary key (database_id)
);

--create sequence
create sequence IF NOT EXISTS id_generator_seq START 1;

--create table table
create table IF NOT EXISTS lab_table (
    database_id bigint not null,
    table_id bigint not null,
    name varchar (255),
    unique (database_id, name),
    primary key (database_id, table_id)
);

--create column table
create table IF NOT EXISTS lab_column (
    database_id bigint not null,
    table_id bigint not null,
    column_id bigint not null,
    name varchar (255),
    type varchar (30),
    default_value varchar,
    unique (database_id, table_id, name),
    primary key (database_id, table_id, column_id)
);

--create row table
create table IF NOT EXISTS lab_row (
    database_id bigint not null,
    table_id bigint not null,
    row_id bigint not null,
    last_update timestamp,
    primary key (database_id, table_id, row_id)
);

--create row column value table
create table IF NOT EXISTS row_column_value (
    database_id bigint not null,
    table_id bigint not null,
    row_id bigint not null,
    column_id bigint not null,
    type varchar (30),
    value varchar,
    primary key (database_id, table_id, row_id, column_id)
);

--create attribute table
create table IF NOT EXISTS lab_attribute (
    database_id bigint not null,
    attribute_id bigint not null,
    columns_ids bigint[],
    name varchar (255),
    unique (database_id, name),
    primary key (database_id, attribute_id)
);