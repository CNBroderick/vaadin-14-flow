/*
 * Class: org.bklab.flow.dialog.AskDialog
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.dialog;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.bklab.flow.factory.ButtonFactory;
import org.bklab.flow.factory.TextAreaFactory;
import org.bklab.image.ImageBase;

public class AskDialog extends ConfirmedDialog {


    public AskDialog(String message, ComponentEventListener<ClickEvent<Button>> ifYes) {
        this(ifYes, "请确认", message);
    }

    public AskDialog(ComponentEventListener<ClickEvent<Button>> ifYes, String pattern, Object... args) {
        this("请确认", ifYes, pattern, args);
    }

    public AskDialog(String title, ComponentEventListener<ClickEvent<Button>> ifYes, String pattern, Object... args) {
        this(title, String.format(pattern, args), ifYes);
    }

    public AskDialog(String title, String message, ComponentEventListener<ClickEvent<Button>> ifYes) {

        Image image = ImageBase.getImage("ask_question.svg");
        TextArea area = new TextAreaFactory().value(message).readOnly().maxWidth("40vw").widthFull().get();

        HorizontalLayout layout = new HorizontalLayout(image, area);
        layout.expand(area);
        image.setMaxWidth("10vw");
        area.setMinWidth("30vw");
        layout.setMaxWidth("52vw");
        layout.setMaxHeight("9em");

        setContent(layout).hasCustomButton(
                new ButtonFactory().icon(VaadinIcon.CHECK_CIRCLE_O.create()).minWidth("100px")
                        .text("确认").clickListener(ifYes).clickListener(e -> close()).lumoSmall().lumoPrimary().get()
        ).hasCancelButton().title(title);
    }

}
