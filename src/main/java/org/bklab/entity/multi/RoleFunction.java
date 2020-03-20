/*
 * Class: org.bklab.entity.multi.RoleFunction
 * Modify date: 2020/3/20 上午11:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.entity.multi;

import java.util.StringJoiner;

public class RoleFunction {

    private int id;
    private String name;
    private String caption;
    private String icon;
    private String description;
    private String url;
    private Boolean defaultFunction = Boolean.FALSE;

    public int getId() {
        return id;
    }

    public RoleFunction setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public RoleFunction setName(String name) {
        this.name = name;
        return this;
    }

    public String getCaption() {
        return caption;
    }

    public RoleFunction setCaption(String caption) {
        this.caption = caption;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public RoleFunction setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RoleFunction setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public RoleFunction setUrl(String url) {
        this.url = url;
        return this;
    }

    public Boolean isDefaultFunction() {
        return defaultFunction;
    }

    public int getDefaultFunction() {
        return defaultFunction ? 1 : 0;
    }

    public RoleFunction setDefaultFunction(Boolean defaultFunction) {
        this.defaultFunction = defaultFunction;
        return this;
    }

    public RoleFunction setDefaultFunction() {
        this.defaultFunction = Boolean.TRUE;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RoleFunction.class.getSimpleName() + "{", "\n}")
                .add("\n\tid: " + id)
                .add("\n\tname: '" + name + "'")
                .add("\n\tcaption: '" + caption + "'")
                .add("\n\ticon: '" + icon + "'")
                .add("\n\tdescription: '" + description + "'")
                .add("\n\turl: '" + url + "'")
                .add("\n\tdefaultFunction: " + defaultFunction)
                .toString();
    }
}
