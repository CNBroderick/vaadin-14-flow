/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-30 13:00:30
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.base.HasValidationFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory.base;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValidation;

public interface HasValidationFactory<E extends HasValidationFactory<E, C>,
        C extends Component & HasValidation> extends IComponentFactory<C> {

    default E invalid() {
        get().setInvalid(true);
        return (E) this;
    }

    default E invalid(boolean invalid) {
        get().setInvalid(invalid);
        return (E) this;
    }

    default E errorMessage() {
        get().setErrorMessage(null);
        return (E) this;
    }

    default E errorMessage(String errorMessage) {
        get().setErrorMessage(errorMessage);
        return (E) this;
    }
}
