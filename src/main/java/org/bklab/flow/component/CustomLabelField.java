/*
 * Class: org.bklab.flow.component.CustomLabelField
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.bklab.flow.factory.ButtonFactory;
import org.bklab.flow.factory.TextFieldFactory;

import java.util.LinkedHashSet;
import java.util.Set;

public class CustomLabelField extends CustomField<Set<String>> {

    private final TextField inputField = new TextFieldFactory().placeholder("新标签名称").widthFull().get();
    private final Button addLabelButton = new ButtonFactory().text("添加").icon(VaadinIcon.PLUS.create())
            .lumoIcon().lumoSmall().lumoTertiaryInline().get();
    private final FlexLayout contentFlexLayout = new FlexLayout();
    private final VerticalLayout main = new VerticalLayout();
    private final Set<String> existLabels = new LinkedHashSet<>();
    private final Set<String> dictionary = new LinkedHashSet<>();


    public CustomLabelField() {
        init();
    }

    public CustomLabelField(Set<String> defaultValue) {
        super(defaultValue);
        init();
        setPresentationValue(defaultValue);
    }

    private void init() {

        setLabel("标签");

        main.setPadding(false);
        main.setMargin(false);
        main.setSizeFull();

        contentFlexLayout.setWidthFull();
        contentFlexLayout.setWrapMode(FlexLayout.WrapMode.WRAP_REVERSE);
        contentFlexLayout.getStyle().set("border", "1px dashed var(--lumo-contrast-30pct)");
        contentFlexLayout.setMinHeight("4em");

        inputField.setSuffixComponent(addLabelButton);

        inputField.addKeyPressListener(Key.ENTER, e -> addLabel(inputField.getValue()));
        addLabelButton.addClickListener(e -> addLabel(inputField.getValue()));

        main.add(inputField, contentFlexLayout);
        setWidthFull();
        add(main);
    }

    private void addLabel(String text) {
        String t = text.strip();
        if (existLabels.contains(t) || t.isBlank()) {
            return;
        }

        Label label = new Label(t);
        label.getStyle().set("margin-right", "1em");
        label.setId(t);

        Button deleteButton = new ButtonFactory().lumoSmall().lumoTertiary().icon(VaadinIcon.CLOSE.create()).get();
        label.add(deleteButton);
        deleteButton.addClickListener(e -> {
            contentFlexLayout.remove(label);
            existLabels.remove(t);
        });
        contentFlexLayout.add(label);
        existLabels.add(t);
        inputField.setValue("");
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        inputField.setVisible(!readOnly);
    }

    @Override
    protected Set<String> generateModelValue() {
        return existLabels;
    }

    @Override
    protected void setPresentationValue(Set<String> newPresentationValue) {
        newPresentationValue.forEach(this::addLabel);
    }
}
