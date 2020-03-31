/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-31 17:22:18
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.SelectFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializablePredicate;

import java.util.Collection;
import java.util.stream.Stream;

public class SelectFactory<T> extends FlowFactory<Select<T>, SelectFactory<T>> {

    public SelectFactory() {
        this(new Select<>());
    }

    public SelectFactory(Select<T> component) {
        super(component);
    }

    public SelectFactory<T> items(Collection<T> collection) {
        component.setItems(collection);
        return this;
    }

    @SafeVarargs
    public final SelectFactory<T> items(T... collections) {
        component.setItems(collections);
        return this;
    }

    public SelectFactory<T> autoSelectItems(Collection<T> collection) {
        component.setItems(collection);
        collection.stream().findFirst().ifPresent(component::setValue);
        return this;
    }

    @SafeVarargs
    public final SelectFactory<T> autoSelectItems(T... collections) {
        component.setItems(collections);
        Stream.of(collections).findFirst().ifPresent(component::setValue);
        return this;
    }

    public SelectFactory<T> value(T value) {
        component.setValue(value);
        return this;
    }

    public SelectFactory<T> renderer(ComponentRenderer<? extends Component, T> renderer) {
        component.setRenderer(renderer);
        return this;
    }

    public SelectFactory<T> textRenderer(ItemLabelGenerator<T> itemLabelGenerator) {
        component.setTextRenderer(itemLabelGenerator);
        return this;
    }

    public SelectFactory<T> emptySelectionAllowed(boolean emptySelectionAllowed) {
        component.setEmptySelectionAllowed(emptySelectionAllowed);
        return this;
    }

    public SelectFactory<T> emptySelectionCaption(String emptySelectionCaption) {
        component.setEmptySelectionCaption(emptySelectionCaption);
        return this;
    }

    public SelectFactory<T> itemEnabledProvider(SerializablePredicate<T> itemEnabledProvider) {
        component.setItemEnabledProvider(itemEnabledProvider);
        return this;
    }

    public SelectFactory<T> itemLabelGenerator(ItemLabelGenerator<T> itemLabelGenerator) {
        component.setItemLabelGenerator(t -> t == null ? component.getEmptySelectionCaption() : itemLabelGenerator.apply(t));
        return this;
    }

    public SelectFactory<T> placeholder(String placeholder) {
        component.setPlaceholder(placeholder);
        return this;
    }

    public SelectFactory<T> label(String label) {
        component.setLabel(label);
        return this;
    }

    public SelectFactory<T> autofocus(boolean autofocus) {
        component.setAutofocus(autofocus);
        return this;
    }

    public SelectFactory<T> dataProvider(DataProvider<T, ?> dataProvider) {
        component.setDataProvider(dataProvider);
        return this;
    }

    public SelectFactory<T> onEnabledStateChanged(boolean enabled) {
        component.onEnabledStateChanged(enabled);
        return this;
    }

    public SelectFactory<T> requiredIndicatorVisible(boolean requiredIndicatorVisible) {
        component.setRequiredIndicatorVisible(requiredIndicatorVisible);
        return this;
    }

    public SelectFactory<T> errorMessage(String errorMessage) {
        component.setErrorMessage(errorMessage);
        return this;
    }

    public SelectFactory<T> invalid(boolean invalid) {
        component.setInvalid(invalid);
        return this;
    }

    public SelectFactory<T> add(Component... components) {
        component.add(components);
        return this;
    }

    public SelectFactory<T> addComponents(T afterItem, Component... components) {
        component.addComponents(afterItem, components);
        return this;
    }

    public SelectFactory<T> prependComponents(T beforeItem, Component... components) {
        component.prependComponents(beforeItem, components);
        return this;
    }

    public SelectFactory<T> addComponentAtIndex(int index, Component component) {
        super.component.addComponentAtIndex(index, component);
        return this;
    }

    public SelectFactory<T> addComponentAsFirst(Component component) {
        super.component.addComponentAsFirst(component);
        return this;
    }

    public SelectFactory<T> addToPrefix(Component... components) {
        component.addToPrefix(components);
        return this;
    }

    public SelectFactory<T> valueChangeListener(HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<Select<T>, T>> listener) {
        component.addValueChangeListener(listener);
        return this;
    }

    public SelectFactory<T> remove(Component... components) {
        component.remove(components);
        return this;
    }

    public SelectFactory<T> removeAll() {
        component.removeAll();
        return this;
    }

    public SelectFactory<T> lumoSmall() {
        component.getElement().setProperty("theme", "small").setAttribute("theme", "small");
        return this;
    }
}
