/*
 * Class: org.bklab.common.action.convertor.JsonConverter
 * Modify date: 2020/3/20 上午10:58
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action.convertor;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.function.Consumer;
import java.util.function.Function;

public interface JsonConverter<T> {

    default T convert(String json) {
        try {
            return fromJsonElement(new Gson().fromJson(json, JsonObject.class));
        } catch (Exception e) {
            e.printStackTrace();
            return fromJsonElement(new JsonObject());
        }
    }

    default String convert(T object) {
        return toJsonElement(object).toString();
    }

    JsonElement toJsonElement(T object);

    T fromJsonElement(JsonElement jsonElement);

    default void ifHas(JsonObject o, String memberName, Consumer<JsonElement> elementConsumer) {
        try {
            if (o.has(memberName)) {
                JsonElement element = o.get(memberName);
                if (!element.isJsonNull()) elementConsumer.accept(element);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default <E> E get(JsonObject o, String memberName, Function<JsonElement, E> elementConsumer, E defaultValue) {
        return o.has(memberName) && !o.get(memberName).isJsonNull() ? elementConsumer.apply(o.get(memberName)) : defaultValue;
    }
}
