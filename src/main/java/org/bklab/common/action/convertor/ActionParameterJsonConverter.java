/*
 * Class: org.bklab.common.action.convertor.ActionParameterJsonConverter
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action.convertor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bklab.common.action.ActionParameter;

public class ActionParameterJsonConverter implements JsonConverter<ActionParameter> {
    @Override
    public ActionParameter fromJsonElement(JsonElement jsonElement) {
        try {
            JsonObject o = jsonElement.getAsJsonObject();
            return new ActionParameter()
                    .setName(o.get("name").getAsString())
                    .setCaption(o.get("caption").getAsString())
                    .setDescription(o.get("description").getAsString())
                    .setDefaultValue(o.get("defaultValue").getAsString());
        } catch (Exception e) {
            return new ActionParameter();
        }
    }

    @Override
    public JsonElement toJsonElement(ActionParameter parameter) {
        JsonObject o = new JsonObject();
        o.addProperty("name", parameter.getName());
        o.addProperty("caption", parameter.getCaption());
        o.addProperty("description", parameter.getDescription());
        o.addProperty("defaultValue", parameter.getDefaultValue());
        return o;
    }
}
