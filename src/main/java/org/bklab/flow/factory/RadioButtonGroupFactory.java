/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-30 13:18:43
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.RadioButtonGroupFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializablePredicate;
import org.bklab.flow.factory.base.HasDataProviderFactory;
import org.bklab.flow.factory.base.HasItemsAndComponentsFactory;
import org.bklab.flow.factory.base.HasValidationFactory;
import org.bklab.flow.factory.base.HasValueAndElementFactory;

public class RadioButtonGroupFactory<T>
        extends FlowFactory<RadioButtonGroup<T>, RadioButtonGroupFactory<T>>
        implements HasItemsAndComponentsFactory<RadioButtonGroupFactory<T>, RadioButtonGroup<T>, T>,
        HasValueAndElementFactory<RadioButtonGroupFactory<T>, RadioButtonGroup<T>>,
        HasDataProviderFactory<RadioButtonGroupFactory<T>, RadioButtonGroup<T>, T>,
        HasValidationFactory<RadioButtonGroupFactory<T>, RadioButtonGroup<T>> {

    public RadioButtonGroupFactory() {
        super(new RadioButtonGroup<>());
    }

    public RadioButtonGroupFactory(RadioButtonGroup<T> component) {
        super(component);
    }

    public RadioButtonGroupFactory<T> dataProvider(DataProvider<T, ?> dataProvider) {
        component.setDataProvider(dataProvider);
        return this;
    }

    public RadioButtonGroupFactory<T> itemEnabledProvider(SerializablePredicate<T> itemEnabledProvider) {
        component.setItemEnabledProvider(itemEnabledProvider);
        return this;
    }

    public RadioButtonGroupFactory<T> renderer(ComponentRenderer<? extends Component, T> renderer) {
        component.setRenderer(renderer);
        return this;
    }

    public RadioButtonGroupFactory<T> onEnabledStateChanged(boolean enabled) {
        component.onEnabledStateChanged(enabled);
        return this;
    }

    public RadioButtonGroupFactory<T> readOnly(boolean readOnly) {
        component.setReadOnly(readOnly);
        return this;
    }

    public RadioButtonGroupFactory<T> required(boolean required) {
        component.setRequired(required);
        return this;
    }

    public RadioButtonGroupFactory<T> errorMessage(String errorMessage) {
        component.setErrorMessage(errorMessage);
        return this;
    }

    public RadioButtonGroupFactory<T> label(String label) {
        component.setLabel(label);
        return this;
    }

    public RadioButtonGroupFactory<T> invalid(boolean invalid) {
        component.setInvalid(invalid);
        return this;
    }

    public RadioButtonGroupFactory<T> value(T value) {
        component.setValue(value);
        return this;
    }

    public RadioButtonGroupFactory<T> lumoVertical() {
        component.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        return this;
    }

    public RadioButtonGroupFactory<T> materialVertical() {
        component.addThemeVariants(RadioGroupVariant.MATERIAL_VERTICAL);
        return this;
    }

    public RadioButtonGroupFactory<T> lumoSmall() {
        component.getElement().setAttribute("theme", "small");
        return this;
    }
}
