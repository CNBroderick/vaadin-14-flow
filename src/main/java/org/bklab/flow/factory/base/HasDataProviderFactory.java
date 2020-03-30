/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-30 13:14:54
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.base.HasDataProviderFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory.base;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.HasDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

import java.util.Arrays;
import java.util.Collection;

public interface HasDataProviderFactory<E extends HasDataProviderFactory<E, C, T>,
        C extends Component & HasDataProvider<T>, T> extends IComponentFactory<C> {

    default E dataProvider(DataProvider<T, ?> dataProvider) {
        get().setDataProvider(dataProvider);
        return (E) this;
    }

    default E items(Collection<T> items) {
        get().setItems(items);
        return (E) this;
    }

    @SuppressWarnings("unchecked")
    default E items(T... items) {
        get().setItems(Arrays.asList(items));
        return (E) this;
    }
}
