/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-20 15:06:34
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.TextAreaFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory;

import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextAreaVariant;
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

    public TextAreaFactory lumoSmall() {
        component.addThemeVariants(TextAreaVariant.LUMO_SMALL);
        return this;
    }

    public TextAreaFactory lumoAlignCenter() {
        component.addThemeVariants(TextAreaVariant.LUMO_ALIGN_CENTER);
        return this;
    }

    public TextAreaFactory lumoAlignRight() {
        component.addThemeVariants(TextAreaVariant.LUMO_ALIGN_RIGHT);
        return this;
    }

    public TextAreaFactory materialAlwaysFloatLabel() {
        component.addThemeVariants(TextAreaVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        return this;
    }
}
