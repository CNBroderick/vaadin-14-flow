/*
 * Class: org.bklab.flow.dialog.ErrorDialog
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.dialog;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.bklab.flow.factory.ButtonFactory;
import org.bklab.flow.factory.TextAreaFactory;
import org.bklab.image.ImageBase;

public class ErrorDialog extends ConfirmedDialog {

    private final String message;
    private String title;
    private Throwable throwable;
    private boolean hasCreate = false;

    {
        super.title("错误");
    }

    public ErrorDialog(String message) {
        this.message = message;
    }

    public ErrorDialog(String pattern, Object... args) {
        this.message = String.format(pattern, args);
    }

    public ErrorDialog(Exception ex) {
        this.message = ex.getLocalizedMessage();
        this.throwable = ex;
    }

    public ErrorDialog throwable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    @Override
    public ErrorDialog title(String title) {
        return (ErrorDialog) super.title(title);
    }

    public ErrorDialog create() {
        Image image = ImageBase.getImage("error.svg");
        TextArea area = new TextAreaFactory().value((message == null || message.isBlank())
                ? "出现位置错误" : message).readOnly().maxWidth("40vw").widthFull().get();

        HorizontalLayout layout = new HorizontalLayout(image, area);
        layout.expand(area);
        image.setMaxWidth("10vw");
        area.setMinWidth("30vw");
        layout.setMaxWidth("52vw");
        layout.setMaxHeight("9em");

        if (throwable != null)
            hasCustomButton(new ButtonFactory().lumoPrimary().icon(VaadinIcon.BOOK.create()).text("详细")
                    .lumoSmall().clickListener(e -> new ExceptionDialog(throwable).open()).minWidth("100px").get());

        setContent(layout).hasCloseButton();
        hasCreate = true;
        return this;
    }

    @Override
    public void open() {
        if (!hasCreate) create();
        super.open();
    }
}
