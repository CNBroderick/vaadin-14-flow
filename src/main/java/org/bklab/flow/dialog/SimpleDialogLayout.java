/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-31 19:08:54
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.dialog.SimpleDialogLayout
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.dialog;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.server.AbstractStreamResource;
import org.bklab.flow.factory.TextAreaFactory;

public class SimpleDialogLayout extends HorizontalLayout {

    public SimpleDialogLayout(AbstractStreamResource resource, String message) {
        this(new Image(resource, ""), message);
    }

    public SimpleDialogLayout(AbstractStreamResource resource, String imageName, String message) {
        this(new Image(resource, imageName), message);
    }

    public SimpleDialogLayout(Image image, String message) {
        TextArea area = new TextAreaFactory().value(message).readOnly()
                .minWidth("30vw").maxWidth("40vw").widthFull().get();
        add(image, area);
        expand(area);
        image.setMaxWidth("10vw");
        setMaxWidth("52vw");
        setMaxHeight("9em");
        setDefaultVerticalComponentAlignment(Alignment.CENTER);
        setAlignSelf(Alignment.END, image);
        setAlignSelf(Alignment.START, area);
    }
}
