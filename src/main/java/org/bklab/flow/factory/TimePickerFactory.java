/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-27 12:27:40
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.TimePickerFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.timepicker.GeneratedVaadinTimePicker;
import com.vaadin.flow.component.timepicker.TimePicker;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;

public class TimePickerFactory extends FlowFactory<TimePicker, TimePickerFactory> {

    public TimePickerFactory(TimePicker component) {
        super(component);
        component.setLocale(Locale.CHINA);
    }

    public TimePickerFactory() {
        this(new TimePicker());
    }

    public TimePickerFactory label(String label) {
        component.setLabel(label);
        return this;
    }

    public TimePickerFactory value(LocalTime value) {
        component.setValue(value);
        return this;
    }

    public TimePickerFactory value(LocalDateTime value) {
        if (value != null && value.toLocalTime() != null) component.setValue(value.toLocalTime());
        return this;
    }

    public TimePickerFactory errorMessage(String errorMessage) {
        component.setErrorMessage(errorMessage);
        return this;
    }

    public TimePickerFactory invalid(boolean invalid) {
        component.setInvalid(invalid);
        return this;
    }

    public TimePickerFactory placeholder(String placeholder) {
        component.setPlaceholder(placeholder);
        return this;
    }

    public TimePickerFactory required(boolean required) {
        component.setRequired(required);
        return this;
    }

    public TimePickerFactory requiredIndicatorVisible(boolean requiredIndicatorVisible) {
        component.setRequiredIndicatorVisible(requiredIndicatorVisible);
        return this;
    }

    public TimePickerFactory readOnly() {
        component.setReadOnly(true);
        return this;
    }

    public TimePickerFactory step(Duration step) {
        component.setStep(step);
        return this;
    }

    public TimePickerFactory locale(Locale locale) {
        component.setLocale(locale);
        return this;
    }

    public TimePickerFactory min(String min) {
        component.setMin(min);
        return this;
    }

    public TimePickerFactory max(String max) {
        component.setMax(max);
        return this;
    }

    public TimePickerFactory clearButtonVisible(boolean clearButtonVisible) {
        component.setClearButtonVisible(clearButtonVisible);
        return this;
    }

    public TimePickerFactory invalidChangeListener(ComponentEventListener<GeneratedVaadinTimePicker.InvalidChangeEvent<TimePicker>> listener) {
        component.addInvalidChangeListener(listener);
        return this;
    }

}
