/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-31 19:18:28
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.dialog.InformationDialog
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.dialog;

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
        setContent(new SimpleDialogLayout(ImageBase.getImage("information.svg"), message)).hasCloseButton().title(title);
    }

}
