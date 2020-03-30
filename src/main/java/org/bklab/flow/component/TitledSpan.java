/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-30 10:47:15
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.component.TitledSpan
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.component;

import com.vaadin.flow.component.html.Span;

public class TitledSpan extends Span {

    public TitledSpan(String text) {
        super(text);
        getElement().setAttribute("title", text);
    }
}
