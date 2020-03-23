/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-23 13:39:25
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.base.HasStyleFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory.base;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;


public interface HasStyleFactory<E extends HasComponentFactory<E, T>, T extends Component & HasStyle & HasComponents> extends IComponentFactory<T> {

    default E addClassName(String className) {
        get().addClassName(className);
        return (E) this;
    }

    default E setClassName(String className) {
        get().setClassName(className);
        return (E) this;
    }

    default E setClassName(String className, boolean set) {
        get().setClassName(className, set);
        return (E) this;
    }

    default E addClassNames(String... classNames) {
        get().addClassNames(classNames);
        return (E) this;
    }

    default E removeClassNames(String... classNames) {
        get().removeClassNames(classNames);
        return (E) this;
    }

}
