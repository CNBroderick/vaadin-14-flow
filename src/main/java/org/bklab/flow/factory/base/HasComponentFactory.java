/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-23 10:32:43
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.base.HasComponentFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory.base;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;

@SuppressWarnings("unchecked")
public interface HasComponentFactory<E extends HasComponentFactory<E, T>, T extends Component & HasComponents> extends IComponentFactory<T> {
    default E add(Component... components) {
        get().add(components);
        return (E) this;
    }

    default E add(String text) {
        get().add(text);
        return (E) this;
    }

    default E remove(Component... components) {
        get().remove(components);
        return (E) this;
    }

    default E removeAll() {
        get().removeAll();
        return (E) this;
    }

    default E addComponentAtIndex(int index, Component component) {
        get().addComponentAtIndex(index, component);
        return (E) this;
    }

    default E addComponentAsFirst(Component component) {
        get().addComponentAsFirst(component);
        return (E) this;
    }
}
