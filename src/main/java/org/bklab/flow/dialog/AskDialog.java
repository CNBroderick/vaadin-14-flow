/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-31 20:41:28
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.dialog.AskDialog
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.dialog;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.bklab.flow.factory.ButtonFactory;
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
        setContent(new SimpleDialogLayout(ImageBase.getImage("ask_question.svg"), message)).hasCustomButton(
                new ButtonFactory().icon(VaadinIcon.CHECK_CIRCLE_O.create()).minWidth("100px")
                        .text("确认").clickListener(ifYes).clickListener(e -> close()).lumoSmall().lumoPrimary().get()
        ).hasCancelButton().title(title);
    }

}
