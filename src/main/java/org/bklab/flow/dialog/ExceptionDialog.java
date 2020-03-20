/*
 * Class: org.bklab.flow.dialog.ExceptionDialog
 * Modify date: 2020/3/20 上午10:14
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.dialog;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextAreaVariant;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;
import java.util.function.Consumer;

public class ExceptionDialog extends ConfirmedDialog {

    public ExceptionDialog(Throwable throwable) {
        VerticalLayout layout = new VerticalLayout();

        setTitle("发生错误");
        Button close = new Button("关闭", VaadinIcon.CLOSE.create());
        close.addClickListener(e -> close());

        addToolBarRight(close);

        layout.add(createBriefArea(throwable.getLocalizedMessage()));

        StringWriter messageWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(messageWriter));
        Details details = new Details("详细信息", createDetailArea(messageWriter.toString()));
        details.getElement().getStyle().set("width", "100%");
        details.addThemeVariants(DetailsVariant.SMALL);
        layout.add(details);

        setContent(layout);
        layout.setMinWidth("40vw");
    }

    public ExceptionDialog(String message, String subMessage) {
        Details details = new Details("详细信息", createDetailArea(subMessage));
        details.addThemeVariants(DetailsVariant.SMALL);
        details.getElement().getStyle().set("width", "100%");
        setTitle("发生错误").redTitle().setContent(
                new VerticalLayout(createBriefArea(message), details));
    }

    public ExceptionDialog(String message) {
        setTitle("发生错误").redTitle().setContent(redText(new H5(message)));
    }

    public static <T extends Exception> Consumer<T> create(String title) {
        return e -> new ExceptionDialog(e).title(title).open();
    }

    private <T extends HasStyle> T redText(T component) {
        component.getStyle().set("color", "red");
        return component;
    }

    private TextArea createBriefArea(String message) {
        TextArea area = new TextArea("主要内容");
        area.setWidthFull();
        area.setValue(Objects.toString(message, ""));
        area.setReadOnly(true);
        area.getStyle().set("color", "red");
        return area;
    }

    private TextArea createDetailArea(String message) {
        TextArea area = new TextArea();
        area.setSizeFull();
        area.setValue(Objects.toString(message, ""));
        area.addThemeVariants(TextAreaVariant.LUMO_SMALL);
        area.setReadOnly(true);
        area.setMaxHeight("50vh");
        area.getStyle().set("color", "red");
        return area;
    }

}
