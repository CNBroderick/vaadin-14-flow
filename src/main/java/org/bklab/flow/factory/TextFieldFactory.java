/*
 * Class: org.bklab.flow.factory.TextFieldFactory
 * Modify date: 2020/3/20 上午10:14
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;

public class TextFieldFactory extends FlowFactory<TextField, TextFieldFactory> {

    public TextFieldFactory() {
        super(new TextField());
    }

    public TextFieldFactory(TextField component) {
        super(component);
    }

    public TextFieldFactory setValueChangeMode(ValueChangeMode valueChangeMode) {
        component.setValueChangeMode(valueChangeMode);
        return this;
    }

    public TextFieldFactory setValueChangeTimeout(int valueChangeTimeout) {
        component.setValueChangeTimeout(valueChangeTimeout);
        return this;
    }

    public TextFieldFactory setErrorMessage(String errorMessage) {
        component.setErrorMessage(errorMessage);
        return this;
    }

    public TextFieldFactory setInvalid(boolean invalid) {
        component.setInvalid(invalid);
        return this;
    }

    public TextFieldFactory label(String label) {
        component.setLabel(label);
        return this;
    }

    public TextFieldFactory placeholder(String placeholder) {
        component.setPlaceholder(placeholder);
        return this;
    }

    public TextFieldFactory setAutoselect(boolean autoselect) {
        component.setAutoselect(autoselect);
        return this;
    }

    public TextFieldFactory setClearButtonVisible(boolean clearButtonVisible) {
        component.setClearButtonVisible(clearButtonVisible);
        return this;
    }

    public TextFieldFactory setAutofocus(boolean autofocus) {
        component.setAutofocus(autofocus);
        return this;
    }

    public TextFieldFactory setMaxLength(int maxLength) {
        component.setMaxLength(maxLength);
        return this;
    }

    public TextFieldFactory setMinLength(int minLength) {
        component.setMinLength(minLength);
        return this;
    }

    public TextFieldFactory setRequired(boolean required) {
        component.setRequired(required);
        return this;
    }

    public TextFieldFactory setPreventInvalidInput(boolean preventInvalidInput) {
        component.setPreventInvalidInput(preventInvalidInput);
        return this;
    }

    public TextFieldFactory setPattern(String pattern) {
        component.setPattern(pattern);
        return this;
    }

    public TextFieldFactory setTitle(String title) {
        component.setTitle(title);
        return this;
    }

    public TextFieldFactory value(String value) {
        component.setValue(value);
        return this;
    }

    public TextFieldFactory setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        component.setRequiredIndicatorVisible(requiredIndicatorVisible);
        component.addThemeVariants();
        return this;
    }

    public TextFieldFactory lumoSmall() {
        component.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        return this;
    }

    public TextFieldFactory lumoAlignCenter() {
        component.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        return this;
    }

    public TextFieldFactory lumoAlignRight() {
        component.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        return this;
    }

    public TextFieldFactory materialAlwaysFloatLabel() {
        component.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        return this;
    }

    public TextFieldFactory addClassName(String className) {
        component.addClassName(className);
        return this;
    }

    public TextFieldFactory addClassName(String... classNames) {
        component.addClassNames(classNames);
        return this;
    }

    public TextFieldFactory removeClassName(String... classNames) {
        component.removeClassNames(classNames);
        return this;
    }

    public TextFieldFactory valueChangeListener(HasValue.ValueChangeListener<
            ? super AbstractField.ComponentValueChangeEvent<TextField, String>> listener) {
        component.addValueChangeListener(listener);
        return this;
    }

    public TextFieldFactory keyUpEnterListener(ComponentEventListener<KeyUpEvent> listener) {
        component.addKeyUpListener(Key.ENTER, listener);
        return this;
    }
}
