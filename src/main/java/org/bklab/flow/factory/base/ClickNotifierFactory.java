/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-23 13:39:25
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.base.ClickNotifierFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory.base;

import com.vaadin.flow.component.*;

public interface ClickNotifierFactory<E extends ClickNotifierFactory<E, T>, T extends Component & ClickNotifier<T>> extends IComponentFactory<T> {
    default E addClickListener(ComponentEventListener<ClickEvent<T>> listener) {
        get().addClickListener(listener);
        return (E) this;
    }

    default E addClickShortcut(Key key, KeyModifier... keyModifiers) {
        get().addClickShortcut(key, keyModifiers);
        return (E) this;
    }
}
