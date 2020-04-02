/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-04-02 13:19:56
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.ButtonFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.Style;

import java.util.function.Consumer;

public class ButtonFactory extends FlowFactory<Button, ButtonFactory> {

    public ButtonFactory() {
        this(new Button());
    }

    public ButtonFactory(Button button) {
        super(button);
    }

    public ButtonFactory icon(Component icon) {
        component.setIcon(icon);
        return this;
    }

    public ButtonFactory icon(IconFactory icon) {
        component.setIcon(icon.create());
        return this;
    }

    public ButtonFactory clickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        if (listener != null) component.addClickListener(listener);
        return this;
    }

    public ButtonFactory title(String title) {
        component.getElement().setAttribute("title", title);
        return this;
    }

    public ButtonFactory title() {
        component.getElement().setAttribute("title", component.getText());
        return this;
    }

    @SafeVarargs
    public final ButtonFactory clickListener(ComponentEventListener<ClickEvent<Button>>... listeners) {
        if (listeners != null) {
            for (ComponentEventListener<ClickEvent<Button>> listener : listeners) {
                if (listener != null) component.addClickListener(listener);
            }
        }
        return this;
    }

    public ButtonFactory width(String width) {
        component.setWidth(width);
        return this;
    }

    public ButtonFactory maxWidth(String width) {
        component.setMaxWidth(width);
        return this;
    }

    public ButtonFactory minWidth(String width) {
        component.setMinWidth(width);
        return this;
    }

    public ButtonFactory height(String height) {
        component.setHeight(height);
        return this;
    }

    public ButtonFactory maxHeight(String height) {
        component.setMaxHeight(height);
        return this;
    }

    public ButtonFactory minHeight(String height) {
        component.setMinHeight(height);
        return this;
    }

    public ButtonFactory text(String text) {
        component.setText(text);
        return this;
    }

    public ButtonFactory disableOnClick() {
        component.setDisableOnClick(true);
        return this;
    }

    public ButtonFactory disableOnClick(boolean disableOnClick) {
        component.setDisableOnClick(disableOnClick);
        return this;
    }

    public ButtonFactory lumoSmall() {
        component.addThemeVariants(ButtonVariant.LUMO_SMALL);
        return this;
    }

    public ButtonFactory lumoLarge() {
        component.addThemeVariants(ButtonVariant.LUMO_LARGE);
        return this;
    }

    public ButtonFactory lumoTertiary() {
        component.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return this;
    }

    public ButtonFactory lumoTertiaryInline() {
        component.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        return this;
    }

    public ButtonFactory lumoPrimary() {
        component.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return this;
    }

    public ButtonFactory lumoSuccess() {
        component.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        return this;
    }

    public ButtonFactory lumoError() {
        component.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return this;
    }

    public ButtonFactory lumoContrast() {
        component.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        return this;
    }

    public ButtonFactory lumoIcon() {
        component.addThemeVariants(ButtonVariant.LUMO_ICON);
        return this;
    }

    public ButtonFactory clearLumoColor() {
        component.removeThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        return this;
    }

    public ButtonFactory materialContained() {
        component.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        return this;
    }

    public ButtonFactory materialOutlined() {
        component.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
        return this;
    }

    public ButtonFactory element(Consumer<Element> elementConsumer) {
        elementConsumer.accept(component.getElement());
        return this;
    }

    public ButtonFactory style(Consumer<Style> styleConsumer) {
        styleConsumer.accept(component.getStyle());
        return this;
    }

    public ButtonFactory advance(Consumer<Button> buttonConsumer) {
        buttonConsumer.accept(component);
        return this;
    }

    @Override
    public Button get() {
        return component;
    }
}
