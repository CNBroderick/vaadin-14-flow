/*
 * Class: org.bklab.common.action.http.JsonResultFormatter
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action.http;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bklab.common.action.RawResultFormatter;

public class JsonResultFormatter implements RawResultFormatter {
    @Override
    public String format(String content) {
        try {
            if (content == null) return null;
            Class<?> c = null;
            if (content.startsWith("{")) c = JsonObject.class;
            if (content.startsWith("[")) c = JsonArray.class;
            if (c != null) return new GsonBuilder().setPrettyPrinting().create().fromJson(content, c).toString();
        } catch (Exception e) {
            return content;
        }
        return content;
    }

    @Override
    public String getName() {
        return "JSON";
    }
}
