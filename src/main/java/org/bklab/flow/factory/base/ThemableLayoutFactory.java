/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-27 09:46:01
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.base.ThemableLayoutFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory.base;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.ThemableLayout;

public interface ThemableLayoutFactory<E extends ThemableLayoutFactory<E, T>, T extends Component & ThemableLayout> extends IComponentFactory<T> {

    default E hasMargin() {
        get().setMargin(true);
        return (E) this;
    }

    default E hasPadding() {
        get().setPadding(true);
        return (E) this;
    }

    default E hasSpacing() {
        get().setSpacing(true);
        return (E) this;
    }

    default E noMargin() {
        get().setMargin(false);
        return (E) this;
    }

    default E noPadding() {
        get().setPadding(false);
        return (E) this;
    }

    default E noSpacing() {
        get().setSpacing(false);
        return (E) this;
    }

    default E boxSizingUndefined() {
        get().setBoxSizing(BoxSizing.UNDEFINED);
        return (E) this;
    }

    default E boxSizingContentBox() {
        get().setBoxSizing(BoxSizing.CONTENT_BOX);
        return (E) this;
    }

    default E boxSizingBorderBox() {
        get().setBoxSizing(BoxSizing.BORDER_BOX);
        return (E) this;
    }


}
