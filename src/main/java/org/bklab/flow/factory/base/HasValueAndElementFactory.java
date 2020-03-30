/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-30 12:38:13
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.base.HasValueAndElementFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory.base;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValueAndElement;

public interface HasValueAndElementFactory<E extends HasValueAndElementFactory<E, T>,
        T extends Component & HasValueAndElement<?, ?>> extends IComponentFactory<T> {

    default E readOnly() {
        get().setReadOnly(true);
        return (E) this;
    }

    default E requiredIndicatorVisible() {
        get().setRequiredIndicatorVisible(true);
        return (E) this;
    }

    default E readOnly(boolean readOnly) {
        get().setReadOnly(readOnly);
        return (E) this;
    }

    default E requiredIndicatorVisible(boolean requiredIndicatorVisible) {
        get().setRequiredIndicatorVisible(requiredIndicatorVisible);
        return (E) this;
    }

}
