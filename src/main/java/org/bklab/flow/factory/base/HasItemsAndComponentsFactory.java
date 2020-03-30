/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-30 12:38:13
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.base.HasItemsAndComponentsFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory.base;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.HasItemsAndComponents;

public interface HasItemsAndComponentsFactory<E extends HasItemsAndComponentsFactory<E, C, T>,
        C extends Component & HasItemsAndComponents<T>, T> extends IComponentFactory<C> {

    default E addComponents(T afterItem, Component... components) {
        get().addComponents(afterItem, components);
        return (E) this;
    }

    default E prependComponents(T beforeItem, Component... components) {
        get().prependComponents(beforeItem, components);
        return (E) this;
    }

}
