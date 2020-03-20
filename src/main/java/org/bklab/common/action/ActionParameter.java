/*
 * Class: org.bklab.common.action.ActionParameter
 * Modify date: 2020/3/20 上午10:58
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;

public class ActionParameter {

    private String name = "";
    private String value;
    private String caption = "";
    private String description = "";
    private String defaultValue = "";
    /**
     * return value is null was true;
     */
    private Function<String, String> valueValidator = p -> null;


    public ActionParameter() {
    }

    public ActionParameter(String name) {
        this.name = name;
    }

    public ActionParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String validate() {
        return valueValidator.apply(value);
    }

    public String validate(String value) {
        return valueValidator.apply(value);
    }

    public String getCaption() {
        return caption;
    }

    public ActionParameter setCaption(String caption) {
        this.caption = caption;
        return this;
    }

    public String getName() {
        return name;
    }

    public ActionParameter setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ActionParameter setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getValue() {
        return Objects.toString(value, "");
    }

    public ActionParameter setValue(String value) {
        this.value = value;
        return this;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public ActionParameter setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public Function<String, String> getValueValidator() {
        return valueValidator;
    }

    public ActionParameter setValueValidator(Function<String, String> valueValidator) {
        this.valueValidator = valueValidator;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ActionParameter.class.getSimpleName() + "{", "\n}")
                .add("\n\tname: '" + name + "'")
                .add("\n\tvalue: '" + value + "'")
                .add("\n\tcaption: '" + caption + "'")
                .add("\n\tdescription: '" + description + "'")
                .add("\n\tdefaultValue: '" + defaultValue + "'")
                .add("\n\tvalueValidator: " + valueValidator)
                .toString();
    }
}
