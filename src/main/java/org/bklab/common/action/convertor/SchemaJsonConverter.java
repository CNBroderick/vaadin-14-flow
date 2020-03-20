/*
 * Class: org.bklab.common.action.convertor.SchemaJsonConverter
 * Modify date: 2020/3/20 上午10:58
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action.convertor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dataq.core.data.schema.DataType;
import dataq.core.data.schema.Field;
import dataq.core.data.schema.IRecordParser;
import dataq.core.data.schema.Schema;

import java.lang.reflect.InvocationTargetException;

public class SchemaJsonConverter implements JsonConverter<Schema> {

    @Override
    public JsonElement toJsonElement(Schema schema) {
        if (schema == null) return new JsonObject();
        JsonObject object = new JsonObject();
        JsonArray fields = new JsonArray();
        for (Field field : schema.fields()) {
            JsonObject f = new JsonObject();
            f.addProperty("name", field.getName());
            f.addProperty("caption", field.getCaption());
            f.addProperty("description", field.getDescription());
            f.addProperty("aliases", field.getAliases());
            f.addProperty("format", field.getFormat());
            f.addProperty("dataType", field.getDataType().name());
            fields.add(f);
        }
        object.add("fields", fields);
        object.addProperty("skipLines", schema.getSkiplines());
        object.addProperty("csvHeaderLine", schema.getCSVHeaderLine());
        object.addProperty("recordParser", schema.getRecordParser() == null ? null : schema.getRecordParser().getClass().getName());

        return object;
    }

    @Override
    public Schema fromJsonElement(JsonElement jsonElement) {
        try {
            if (jsonElement == null) return null;
            JsonObject object = jsonElement.getAsJsonObject();
            if (object.keySet().isEmpty()) return null;

            Schema schema = new Schema();
            ifHas(object, "csvHeaderLine", e -> schema.setCSVHeaderLine(e.getAsInt()));
            ifHas(object, "skipLines", e -> schema.setSkiplines(e.getAsInt()));
            ifHas(object, "fields", e -> {
                for (JsonElement element : e.getAsJsonArray()) {
                    JsonObject o = element.getAsJsonObject();
                    if (!o.has("name")) continue;
                    Field field = new Field(o.get("name").getAsString(), DataType.valueOf(o.get("dataType").getAsString()));

                    ifHas(o, "caption", a -> field.setCaption(a.getAsString()));
                    ifHas(o, "description", a -> field.setDescription(a.getAsString()));
                    ifHas(o, "aliases", a -> field.setAliases(a.getAsString()));
                    ifHas(o, "format", a -> field.setFormat(a.getAsString()));

                    schema.addField(field);
                }

            });

            String recordParser = get(object, "recordParser", JsonElement::getAsString, null);
            ifHas(object, "recordParser", e -> {
                try {
                    schema.setRecordParser((IRecordParser) Class.forName(e.getAsString()).getDeclaredConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            });


            return schema;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
