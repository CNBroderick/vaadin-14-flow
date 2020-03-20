/*
 * Class: org.bklab.common.ssh2.SchemaFieldFactory
 * Modify date: 2020/3/20 上午10:58
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.ssh2;

import dataq.core.data.schema.DataType;
import dataq.core.data.schema.Field;

import java.util.function.Supplier;

public class SchemaFieldFactory implements Supplier<Field> {
    private final Field field;

    public SchemaFieldFactory(String name) {
        this.field = new Field(name);
    }

    public SchemaFieldFactory(String name, DataType dataType) {
        this.field = new Field(name, dataType);
    }

    public SchemaFieldFactory(String name, String dataType) {
        this.field = new Field(name, dataType);
    }

    public SchemaFieldFactory(Field field) {
        this.field = field;
    }

    public static SchemaFieldFactory unknown(String name) {
        return new SchemaFieldFactory(name, DataType.UNKNOWN_TYPE);
    }

    public static SchemaFieldFactory intType(String name) {
        return new SchemaFieldFactory(name, DataType.INT);
    }

    public static SchemaFieldFactory longType(String name) {
        return new SchemaFieldFactory(name, DataType.LONG);
    }

    public static SchemaFieldFactory floatType(String name) {
        return new SchemaFieldFactory(name, DataType.FLOAT);
    }

    public static SchemaFieldFactory doubleType(String name) {
        return new SchemaFieldFactory(name, DataType.DOUBLE);
    }

    public static SchemaFieldFactory string(String name) {
        return new SchemaFieldFactory(name, DataType.STRING);
    }

    public static SchemaFieldFactory date(String name) {
        return new SchemaFieldFactory(name, DataType.DATE);
    }

    public static SchemaFieldFactory datetime(String name) {
        return new SchemaFieldFactory(name, DataType.DATETIME);
    }

    public static SchemaFieldFactory booleanType(String name) {
        return new SchemaFieldFactory(name, DataType.BOOLEAN);
    }

    public static SchemaFieldFactory blob(String name) {
        return new SchemaFieldFactory(name, DataType.BLOB);
    }

    public static SchemaFieldFactory object(String name) {
        return new SchemaFieldFactory(name, DataType.OBJECT);
    }

    public SchemaFieldFactory name(String name) {
        this.field.setName(name);
        return this;
    }

    public SchemaFieldFactory caption(String caption) {
        this.field.setCaption(caption);
        return this;
    }

    public SchemaFieldFactory show() {
        this.field.setShow(true);
        return this;
    }

    public SchemaFieldFactory influxTag() {
        this.field.setInfluxTag(true);
        return this;
    }

    public SchemaFieldFactory show(boolean show) {
        this.field.setShow(show);
        return this;
    }

    public SchemaFieldFactory influxTag(boolean influxTag) {
        this.field.setInfluxTag(influxTag);
        return this;
    }

    public SchemaFieldFactory aliases(String aliases) {
        this.field.setAliases(aliases);
        return this;
    }

    public SchemaFieldFactory description(String description) {
        this.field.setDescription(description);
        return this;
    }

    @Override
    public Field get() {
        return field;
    }
}
