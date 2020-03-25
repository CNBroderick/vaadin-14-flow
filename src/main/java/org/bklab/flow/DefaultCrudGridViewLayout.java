/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-23 16:48:05
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.DefaultCrudGridViewLayout
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DefaultCrudGridViewLayout<T> extends VerticalLayout {

    protected CrudGridView<T> crudGridView;

    public DefaultCrudGridViewLayout() {
        setSizeFull();
        setMargin(false);
        setSpacing(false);
        setPadding(false);
        add(new CrudGridView<T>().peek(v -> crudGridView = v));
    }

    protected void adjustGrid(Grid<T> grid) {
        grid.getColumns().forEach(c -> c.setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true));
        grid.setSizeFull();
        grid.setMultiSort(false);
    }
}
