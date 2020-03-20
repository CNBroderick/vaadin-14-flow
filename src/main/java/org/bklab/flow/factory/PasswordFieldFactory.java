/*
 * Class: org.bklab.flow.factory.PasswordFieldFactory
 * Modify date: 2020/3/20 上午10:14
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;

public class PasswordFieldFactory extends FlowFactory<PasswordField, PasswordFieldFactory> {

    public PasswordFieldFactory() {
        super(new PasswordField());
    }

    public PasswordFieldFactory(PasswordField textField) {
        super(textField);
    }

    public PasswordFieldFactory setValueChangeMode(ValueChangeMode valueChangeMode) {
        component.setValueChangeMode(valueChangeMode);
        return this;
    }

    public PasswordFieldFactory setValueChangeTimeout(int valueChangeTimeout) {
        component.setValueChangeTimeout(valueChangeTimeout);
        return this;
    }

    public PasswordFieldFactory setErrorMessage(String errorMessage) {
        component.setErrorMessage(errorMessage);
        return this;
    }

    public PasswordFieldFactory setInvalid(boolean invalid) {
        component.setInvalid(invalid);
        return this;
    }

    public PasswordFieldFactory setLabel(String label) {
        component.setLabel(label);
        return this;
    }

    public PasswordFieldFactory setPlaceholder(String placeholder) {
        component.setPlaceholder(placeholder);
        return this;
    }

    public PasswordFieldFactory setAutoselect(boolean autoselect) {
        component.setAutoselect(autoselect);
        return this;
    }

    public PasswordFieldFactory setClearButtonVisible(boolean clearButtonVisible) {
        component.setClearButtonVisible(clearButtonVisible);
        return this;
    }

    public PasswordFieldFactory setAutofocus(boolean autofocus) {
        component.setAutofocus(autofocus);
        return this;
    }

    public PasswordFieldFactory setMaxLength(int maxLength) {
        component.setMaxLength(maxLength);
        return this;
    }

    public PasswordFieldFactory setMinLength(int minLength) {
        component.setMinLength(minLength);
        return this;
    }

    public PasswordFieldFactory setRequired(boolean required) {
        component.setRequired(required);
        return this;
    }

    public PasswordFieldFactory setPreventInvalidInput(boolean preventInvalidInput) {
        component.setPreventInvalidInput(preventInvalidInput);
        return this;
    }

    public PasswordFieldFactory setPattern(String pattern) {
        component.setPattern(pattern);
        return this;
    }

    public PasswordFieldFactory setTitle(String title) {
        component.setTitle(title);
        return this;
    }

    public PasswordFieldFactory setValue(String value) {
        component.setValue(value);
        return this;
    }

    public PasswordFieldFactory setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        component.setRequiredIndicatorVisible(requiredIndicatorVisible);
        component.addThemeVariants();
        return this;
    }

    public PasswordFieldFactory lumoSmall() {
        component.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        return this;
    }

    public PasswordFieldFactory lumoAlignCenter() {
        component.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        return this;
    }

    public PasswordFieldFactory lumoAlignRight() {
        component.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        return this;
    }

    public PasswordFieldFactory materialAlwaysFloatLabel() {
        component.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        return this;
    }

    public PasswordFieldFactory addClassName(String className) {
        component.addClassName(className);
        return this;
    }

    public PasswordFieldFactory addClassName(String... classNames) {
        component.addClassNames(classNames);
        return this;
    }

    public PasswordFieldFactory removeClassName(String... classNames) {
        component.removeClassNames(classNames);
        return this;
    }

    public PasswordFieldFactory addValueChangeListener(HasValue.ValueChangeListener<
            ? super AbstractField.ComponentValueChangeEvent<PasswordField, String>> listener) {
        component.addValueChangeListener(listener);
        return this;
    }

    public PasswordFieldFactory widthFull() {
        component.setWidthFull();
        return this;
    }

    public PasswordFieldFactory width(String width) {
        component.setWidth(width);
        return this;
    }

    public PasswordFieldFactory keyUpEnterListener(ComponentEventListener<KeyUpEvent> listener) {
        component.addKeyUpListener(Key.ENTER, listener);
        return this;
    }
}
