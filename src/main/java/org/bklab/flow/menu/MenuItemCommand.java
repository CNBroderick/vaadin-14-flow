/*
 * Class: org.bklab.flow.menu.MenuItemCommand
 * Modify date: 2020/3/20 上午10:51
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.menu;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;

import java.util.function.Consumer;

public interface MenuItemCommand<T> {

    String getName();

    void apply(T entity, GridMenuItemFactory<T> itemFactory, Consumer<T> whenSuccess);

    default Component getComponent() {
        return null;
    }

    default boolean isEnable(T entity) {
        return true;
    }

    default boolean isVisible(T entity) {
        return true;
    }

    default void peek(GridMenuItem<T> menuItem) {
    }
}
