/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-04-15 12:04:49
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.CheckboxFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.checkbox.Checkbox;

public class CheckboxFactory extends FlowFactory<Checkbox, CheckboxFactory> {

    public CheckboxFactory() {
        super(new Checkbox());
    }

    public CheckboxFactory(Checkbox component) {
        super(component);
    }

    public CheckboxFactory label(String label) {
        component.setLabel(label);
        return this;
    }

    public CheckboxFactory valueChangeListener(HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<Checkbox, Boolean>> listener) {
        component.addValueChangeListener(listener);
        return this;
    }

    public CheckboxFactory blurListener(ComponentEventListener<BlurNotifier.BlurEvent<Checkbox>> listener) {
        component.addBlurListener(listener);
        return this;
    }

    public CheckboxFactory clickListener(ComponentEventListener<ClickEvent<Checkbox>> listener) {
        component.addClickListener(listener);
        return this;
    }

    public CheckboxFactory clickShortcut(Key key, KeyModifier... keyModifiers) {
        component.addClickShortcut(key, keyModifiers);
        return this;
    }

    public CheckboxFactory focusListener(ComponentEventListener<FocusNotifier.FocusEvent<Checkbox>> listener) {
        component.addFocusListener(listener);
        return this;
    }

    public CheckboxFactory focusShortcut(Key key, KeyModifier... keyModifiers) {
        component.addFocusShortcut(key, keyModifiers);
        return this;
    }

    public CheckboxFactory labelAsHtml(String htmlContent) {
        component.setLabelAsHtml(htmlContent);
        return this;
    }

    public CheckboxFactory ariaLabel(String ariaLabel) {
        component.setAriaLabel(ariaLabel);
        return this;
    }

    public CheckboxFactory autofocus(boolean autofocus) {
        component.setAutofocus(autofocus);
        return this;
    }

    public CheckboxFactory indeterminate(boolean indeterminate) {
        component.setIndeterminate(indeterminate);
        return this;
    }
}
