/*
 * Class: org.bklab.flow.dialog.InformationDialog
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.dialog;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.bklab.flow.factory.TextAreaFactory;
import org.bklab.image.ImageBase;

public class InformationDialog extends ConfirmedDialog {


    public InformationDialog(String message) {
        this("提示", message);
    }

    public InformationDialog(String pattern, Object... args) {
        this("提示", pattern, args);
    }

    public InformationDialog(String title, String pattern, Object... args) {
        this(title, String.format(pattern, args));
    }

    public InformationDialog(String title, String message) {

        Image image = ImageBase.getImage("information.svg");
        TextArea area = new TextAreaFactory().value(message).readOnly().maxWidth("40vw").widthFull().get();

        HorizontalLayout layout = new HorizontalLayout(image, area);
        layout.expand(area);
        image.setMaxWidth("10vw");
        area.setMinWidth("30vw");
        layout.setMaxWidth("52vw");
        layout.setMaxHeight("9em");

        setContent(layout).hasCloseButton().title(title);
    }

}
