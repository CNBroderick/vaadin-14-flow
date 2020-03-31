/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-31 09:26:18
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.ComboBoxFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.GeneratedVaadinComboBox;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.SerializableFunction;

import java.util.Collection;

public class ComboBoxFactory<T> extends FlowFactory<ComboBox<T>, ComboBoxFactory<T>> {

    public ComboBoxFactory(ComboBox<T> component) {
        super(component);
    }

    public ComboBoxFactory() {
        super(new ComboBox<>());
    }

    public ComboBoxFactory<T> value(T value) {
        component.setValue(value);
        return this;
    }

    public ComboBoxFactory<T> renderer(Renderer<T> renderer) {
        component.setRenderer(renderer);
        return this;
    }

    public ComboBoxFactory<T> items(Collection<T> items) {
        component.setItems(items);
        return this;
    }

    public ComboBoxFactory<T> itemsAndFirstValue(Collection<T> items) {
        component.setItems(items);
        items.stream().findFirst().ifPresent(component::setValue);
        return this;
    }

    @SafeVarargs
    public final ComboBoxFactory<T> items(T... items) {
        component.setItems(items);
        return this;
    }

    public ComboBoxFactory<T> items(ComboBox.ItemFilter<T> itemFilter, Collection<T> items) {
        component.setItems(items);
        return this;
    }

    @SafeVarargs
    public final ComboBoxFactory<T> items(ComboBox.ItemFilter<T> itemFilter, T... items) {
        component.setItems(itemFilter, items);
        return this;
    }

    public ComboBoxFactory<T> dataProvider(DataProvider<T, String> dataProvider) {
        component.setDataProvider(dataProvider);
        return this;
    }

    public ComboBoxFactory<T> dataProvider(ListDataProvider<T> listDataProvider) {
        component.setDataProvider(listDataProvider);
        return this;
    }

    public ComboBoxFactory<T> dataProvider(ComboBox.FetchItemsCallback<T> fetchItems, SerializableFunction<String, Integer> filterConverter) {
        component.setDataProvider(fetchItems, filterConverter);
        return this;
    }

    public ComboBoxFactory<T> dataProvider(ComboBox.ItemFilter<T> itemFilter, ListDataProvider<T> provider) {
        component.setDataProvider(itemFilter, provider);
        return this;
    }

    public ComboBoxFactory<T> itemLabelGenerator(ItemLabelGenerator<T> itemLabelGenerator) {
        component.setItemLabelGenerator(itemLabelGenerator);
        return this;
    }

    public ComboBoxFactory<T> pageSize(int pageSize) {
        component.setPageSize(pageSize);
        return this;
    }

    public ComboBoxFactory<T> opened(boolean opened) {
        component.setOpened(opened);
        return this;
    }

    public ComboBoxFactory<T> invalid(boolean invalid) {
        component.setInvalid(invalid);
        return this;
    }

    public ComboBoxFactory<T> errorMessage(String errorMessage) {
        component.setErrorMessage(errorMessage);
        return this;
    }

    public ComboBoxFactory<T> allowCustomValue(boolean allowCustomValue) {
        component.setAllowCustomValue(allowCustomValue);
        return this;
    }

    public ComboBoxFactory<T> autofocus(boolean autofocus) {
        component.setAutofocus(autofocus);
        return this;
    }

    public ComboBoxFactory<T> preventInvalidInput(boolean preventInvalidInput) {
        component.setPreventInvalidInput(preventInvalidInput);
        return this;
    }

    public ComboBoxFactory<T> required(boolean required) {
        component.setRequired(required);
        return this;
    }

    public ComboBoxFactory<T> label(String label) {
        component.setLabel(label);
        return this;
    }

    public ComboBoxFactory<T> placeholder(String placeholder) {
        component.setPlaceholder(placeholder);
        return this;
    }

    public ComboBoxFactory<T> pattern(String pattern) {
        component.setPattern(pattern);
        return this;
    }

    public ComboBoxFactory<T> requiredIndicatorVisible(boolean requiredIndicatorVisible) {
        component.setRequiredIndicatorVisible(requiredIndicatorVisible);
        return this;
    }

    public ComboBoxFactory<T> clearButtonVisible(boolean clearButtonVisible) {
        component.setClearButtonVisible(clearButtonVisible);
        return this;
    }

    public ComboBoxFactory<T> customValueSetListener(ComponentEventListener<GeneratedVaadinComboBox.CustomValueSetEvent<ComboBox<T>>> listener) {
        component.addCustomValueSetListener(listener);
        return this;
    }

    public ComboBoxFactory<T> valueChangeListener(HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<ComboBox<T>, T>> listener) {
        component.addValueChangeListener(listener);
        return this;
    }

    public ComboBoxFactory<T> lumoSmall() {
        component.getElement().setProperty("theme", "small").setAttribute("theme", "small");
        return this;
    }

}
