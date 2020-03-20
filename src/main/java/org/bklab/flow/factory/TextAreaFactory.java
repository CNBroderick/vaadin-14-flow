/*
 * Class: org.bklab.flow.factory.TextAreaFactory
 * Modify date: 2020/3/20 上午10:14
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory;

import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;

public class TextAreaFactory extends FlowFactory<TextArea, TextAreaFactory> {

    public TextAreaFactory(TextArea component) {
        super(component);
    }

    public TextAreaFactory() {
        super(new TextArea());
    }

    public TextAreaFactory valueChangeMode(ValueChangeMode valueChangeMode) {
        component.setValueChangeMode(valueChangeMode);
        return this;
    }

    public TextAreaFactory valueChangeTimeout(int valueChangeTimeout) {
        component.setValueChangeTimeout(valueChangeTimeout);
        return this;
    }

    public TextAreaFactory errorMessage(String errorMessage) {
        component.setErrorMessage(errorMessage);
        return this;
    }

    public TextAreaFactory invalid(boolean invalid) {
        component.setInvalid(invalid);
        return this;
    }

    public TextAreaFactory label(String label) {
        component.setLabel(label);
        return this;
    }

    public TextAreaFactory placeholder(String placeholder) {
        component.setPlaceholder(placeholder);
        return this;
    }

    public TextAreaFactory autoselect(boolean autoselect) {
        component.setAutoselect(autoselect);
        return this;
    }

    public TextAreaFactory clearButtonVisible(boolean clearButtonVisible) {
        component.setClearButtonVisible(clearButtonVisible);
        return this;
    }

    public TextAreaFactory autofocus(boolean autofocus) {
        component.setAutofocus(autofocus);
        return this;
    }

    public TextAreaFactory maxLength(int maxLength) {
        component.setMaxLength(maxLength);
        return this;
    }

    public TextAreaFactory minLength(int minLength) {
        component.setMinLength(minLength);
        return this;
    }

    public TextAreaFactory required(boolean required) {
        component.setRequired(required);
        return this;
    }

    public TextAreaFactory preventInvalidInput(boolean preventInvalidInput) {
        component.setPreventInvalidInput(preventInvalidInput);
        return this;
    }

    public TextAreaFactory value(String value) {
        if (value != null) component.setValue(value);
        return this;
    }

    public TextAreaFactory requiredIndicatorVisible(boolean requiredIndicatorVisible) {
        component.setRequiredIndicatorVisible(requiredIndicatorVisible);
        return this;
    }

}
