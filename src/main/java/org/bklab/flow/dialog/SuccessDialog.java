/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-31 20:41:29
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.dialog.SuccessDialog
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.dialog;

import org.bklab.image.ImageBase;

public class SuccessDialog extends ConfirmedDialog {

    private String title = "成功";
    private String message = "";
    private boolean hasCreated = false;

    public SuccessDialog() {
    }

    public SuccessDialog title(String title) {
        this.title = title;
        return this;
    }

    public SuccessDialog message(String message) {
        this.message = message;
        return this;
    }

    public SuccessDialog message(String pattern, Object... args) {
        this.message = String.format(pattern, args);
        return this;
    }

    public SuccessDialog create() {
        setContent(createLayout()).hasCloseButton().title(title);
        hasCreated = true;
        return this;
    }

    public SimpleDialogLayout createLayout() {
        return new SimpleDialogLayout(ImageBase.getResource("ok.svg"), message);
    }

    @Override
    public void open() {
        if (!hasCreated) create();
        super.open();
    }
}
