/*
 * Class: org.bklab.flow.factory.NumberFieldFactory
 * Modify date: 2020/3/20 上午10:14
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.function.Consumer;

public class NumberFieldFactory extends FlowFactory<NumberField, NumberFieldFactory> {

    public NumberFieldFactory() {
        super(new NumberField());
    }

    public NumberFieldFactory(NumberField textField) {
        super(textField);
    }

    public NumberFieldFactory setValueChangeMode(ValueChangeMode valueChangeMode) {
        component.setValueChangeMode(valueChangeMode);
        return this;
    }

    public NumberFieldFactory setValueChangeTimeout(int valueChangeTimeout) {
        component.setValueChangeTimeout(valueChangeTimeout);
        return this;
    }

    public NumberFieldFactory setErrorMessage(String errorMessage) {
        component.setErrorMessage(errorMessage);
        return this;
    }

    public NumberFieldFactory setInvalid(boolean invalid) {
        component.setInvalid(invalid);
        return this;
    }

    public NumberFieldFactory setLabel(String label) {
        component.setLabel(label);
        return this;
    }

    public NumberFieldFactory setPlaceholder(String placeholder) {
        component.setPlaceholder(placeholder);
        return this;
    }

    public NumberFieldFactory setAutoselect(boolean autoselect) {
        component.setAutoselect(autoselect);
        return this;
    }

    public NumberFieldFactory setClearButtonVisible(boolean clearButtonVisible) {
        component.setClearButtonVisible(clearButtonVisible);
        return this;
    }

    public NumberFieldFactory setAutofocus(boolean autofocus) {
        component.setAutofocus(autofocus);
        return this;
    }

    public NumberFieldFactory setTitle(String title) {
        component.setTitle(title);
        return this;
    }

    public NumberFieldFactory setValue(Double value) {
        component.setValue(value);
        return this;
    }

    public NumberFieldFactory setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        component.setRequiredIndicatorVisible(requiredIndicatorVisible);
        component.addThemeVariants();
        return this;
    }

    public NumberFieldFactory lumoSmall() {
        component.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        return this;
    }

    public NumberFieldFactory lumoAlignCenter() {
        component.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        return this;
    }

    public NumberFieldFactory lumoAlignRight() {
        component.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        return this;
    }

    public NumberFieldFactory materialAlwaysFloatLabel() {
        component.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        return this;
    }

    public NumberFieldFactory addClassName(String className) {
        component.addClassName(className);
        return this;
    }

    public NumberFieldFactory addClassName(String... classNames) {
        component.addClassNames(classNames);
        return this;
    }

    public NumberFieldFactory removeClassName(String... classNames) {
        component.removeClassNames(classNames);
        return this;
    }

    public NumberFieldFactory addValueChangeListener(HasValue.ValueChangeListener<
            ? super AbstractField.ComponentValueChangeEvent<NumberField, Double>> listener) {
        component.addValueChangeListener(listener);
        return this;
    }

    public NumberFieldFactory widthFull() {
        component.setWidthFull();
        return this;
    }

    public NumberFieldFactory width(String width) {
        component.setWidth(width);
        return this;
    }

    public NumberFieldFactory keyUpEnterListener(ComponentEventListener<KeyUpEvent> listener) {
        component.addKeyUpListener(Key.ENTER, listener);
        return this;
    }

    public NumberFieldFactory max(Double max) {
        component.setMax(max);
        return this;
    }

    public NumberFieldFactory min(Double min) {
        component.setMin(min);
        return this;
    }

    public NumberFieldFactory step(Double step) {
        component.setStep(step);
        return this;
    }

    public NumberFieldFactory peek(Consumer<NumberField> consumer) {
        consumer.accept(component);
        return this;
    }

}
