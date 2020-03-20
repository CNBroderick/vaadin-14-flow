/*
 * Class: org.bklab.common.action.HasProperty
 * Modify date: 2020/3/20 上午10:58
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Properties;

@SuppressWarnings("unchecked")
public interface HasProperty<T extends HasProperty<T>> {

    Properties getProperties();

    default T addProperty(Object key, Object value) {
        getProperties().put(key, value);
        return (T) this;
    }

    default <E> E getProperty(Object key) {
        return (E) getProperties().get(key);
    }

    default <E> E getProperty(Object key, E defaultValue) {
        return (E) getProperties().getOrDefault(key, defaultValue);
    }

    default boolean hasProperty(Object key) {
        return getProperties().contains(key);
    }

    default String toJson() {
        JsonObject o = new JsonObject();
        getProperties().forEach((k, v) -> {
            if (v == null) o.add(String.valueOf(k), null);
            else if (v instanceof JsonElement) o.add(String.valueOf(k), (JsonElement) v);
            else if (v instanceof Number) o.addProperty(String.valueOf(k), (Number) v);
            else if (v instanceof Boolean) o.addProperty(String.valueOf(k), (Boolean) v);
            else if (v instanceof Character) o.addProperty(String.valueOf(k), (Character) v);
            else o.addProperty(String.valueOf(k), String.valueOf(o));
        });
        return o.toString();
    }

    default T fromJson(String json) {
        try {
            JsonObject o = new Gson().fromJson(json, JsonObject.class);
            for (String s : o.keySet()) {
                JsonElement element = o.get(s);
                if (element.isJsonNull()) {
                    getProperties().put(s, null);
                    continue;
                }

                if (element.isJsonPrimitive()) {
                    try {
                        getProperties().put(s, element.getAsNumber());
                    } catch (Exception e) {
                        getProperties().put(s, element.getAsString());
                    }
                    continue;
                }

                if (element.isJsonObject()) {
                    getProperties().put(s, element.getAsJsonObject());
                }
            }
        } catch (Exception ignore) {
        }
        return (T) this;
    }

}
