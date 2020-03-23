/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-23 10:29:14
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.base.FlexComponentFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory.base;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

@SuppressWarnings("unchecked")
public interface FlexComponentFactory<E extends FlexComponentFactory<E, T>, T extends Component & FlexComponent<T>> extends IComponentFactory<T> {
    default E setAlignItems(FlexComponent.Alignment alignment) {
        get().setAlignItems(alignment);
        return (E) this;
    }

    default E setAlignSelf(FlexComponent.Alignment alignment, HasElement... elementContainers) {
        get().setAlignSelf(alignment, elementContainers);
        return (E) this;
    }

    default E setFlexGrow(double flexGrow, HasElement... elementContainers) {
        get().setFlexGrow(flexGrow, elementContainers);
        return (E) this;
    }

    default E setJustifyContentMode(FlexComponent.JustifyContentMode justifyContentMode) {
        get().setJustifyContentMode(justifyContentMode);
        return (E) this;
    }

    default E expand(Component... componentsToExpand) {
        get().expand(componentsToExpand);
        return (E) this;
    }

    default E replace(Component oldComponent, Component newComponent) {
        get().replace(oldComponent, newComponent);
        return (E) this;
    }
}
