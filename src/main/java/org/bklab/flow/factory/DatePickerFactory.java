/*
 * Class: org.bklab.flow.factory.DatePickerFactory
 * Modify date: 2020/3/20 上午10:14
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datepicker.GeneratedVaadinDatePicker;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;

public class DatePickerFactory extends FlowFactory<DatePicker, DatePickerFactory> {

    public DatePickerFactory(DatePicker component) {
        super(component);
    }

    public DatePickerFactory() {
        this(new DatePicker(null, Locale.SIMPLIFIED_CHINESE));
    }

    public DatePickerFactory min(LocalDate min) {
        this.component.setMin(min);
        return this;
    }

    public DatePickerFactory max(LocalDate max) {
        this.component.setMax(max);
        return this;
    }

    public DatePickerFactory locale(Locale locale) {
        this.component.setLocale(locale);
        return this;
    }

    public DatePickerFactory i18n(DatePicker.DatePickerI18n i18n) {
        this.component.setI18n(i18n);
        return this;
    }

    public DatePickerFactory i18n() {
        this.component.setI18n(
                new DatePicker.DatePickerI18n()
                        .setCalendar("日历")
                        .setCancel("取消")
                        .setClear("清除")
                        .setFirstDayOfWeek(7)
                        .setMonthNames(Arrays.asList(
                                "一月",
                                "二月",
                                "三月",
                                "四月",
                                "五月",
                                "六月",
                                "七月",
                                "八月",
                                "九月",
                                "十月",
                                "十一月",
                                "十二月"
                        )).setToday("今天")
                        .setWeek("星期")
                        .setWeekdays(Arrays.asList(
                                "星期一",
                                "星期二",
                                "星期三",
                                "星期四",
                                "星期五",
                                "星期六",
                                "星期日"
                        ))
                        .setWeekdaysShort(Arrays.asList(
                                "一",
                                "二",
                                "三",
                                "四",
                                "五",
                                "六",
                                "七"
                        ))
        );
        return this;
    }

    public DatePickerFactory errorMessage(String errorMessage) {
        this.component.setErrorMessage(errorMessage);
        return this;
    }

    public DatePickerFactory invalid(boolean invalid) {
        this.component.setInvalid(invalid);
        return this;
    }

    public DatePickerFactory clearButtonVisible(boolean clearButtonVisible) {
        this.component.setClearButtonVisible(clearButtonVisible);
        return this;
    }

    public DatePickerFactory label(String label) {
        this.component.setLabel(label);
        return this;
    }

    public DatePickerFactory readOnly() {
        component.setReadOnly(true);
        return this;
    }

    public DatePickerFactory placeholder(String placeholder) {
        this.component.setPlaceholder(placeholder);
        return this;
    }

    public DatePickerFactory initialPosition(LocalDate initialPosition) {
        this.component.setInitialPosition(initialPosition);
        return this;
    }

    public DatePickerFactory required(boolean required) {
        this.component.setRequired(required);
        return this;
    }

    public DatePickerFactory requiredIndicatorVisible(boolean requiredIndicatorVisible) {
        this.component.setRequiredIndicatorVisible(requiredIndicatorVisible);
        return this;
    }

    public DatePickerFactory weekNumbersVisible(boolean weekNumbersVisible) {
        this.component.setWeekNumbersVisible(weekNumbersVisible);
        return this;
    }

    public DatePickerFactory opened(boolean opened) {
        this.component.setOpened(opened);
        return this;
    }

    public DatePickerFactory open() {
        this.component.open();
        return this;
    }

    public DatePickerFactory name(String name) {
        this.component.setName(name);
        return this;
    }

    public DatePickerFactory openedChangeListener(
            ComponentEventListener<GeneratedVaadinDatePicker.OpenedChangeEvent<DatePicker>> listener) {
        this.component.addOpenedChangeListener(listener);
        return this;
    }

    public DatePickerFactory invalidChangeListener(
            ComponentEventListener<GeneratedVaadinDatePicker.InvalidChangeEvent<DatePicker>> listener) {
        this.component.addInvalidChangeListener(listener);
        return this;
    }


}
