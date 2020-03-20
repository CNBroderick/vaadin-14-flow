/*
 * Class: org.bklab.flow.menu.GridMenuItemFactory
 * Modify date: 2020/3/20 上午10:51
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.menu;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.grid.contextmenu.GridSubMenu;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GridMenuItemFactory<T> implements Supplier<GridMenuItem<T>> {

    private final GridMenuItem<T> item;

    public GridMenuItemFactory(GridMenuItem<T> item) {
        this.item = item;
    }

    public GridMenuItemFactory<T> text(String text) {
        this.item.setText(text);
        return this;
    }

    public GridMenuItemFactory<T> add(String text) {
        this.item.add(text);
        return this;
    }

    public GridMenuItemFactory<T> add(Component component) {
        this.item.add(component);
        return this;
    }

    public GridMenuItemFactory<T> visible(boolean visible) {
        this.item.setVisible(visible);
        return this;
    }

    public GridMenuItemFactory<T> enabled(boolean enabled) {
        this.item.setEnabled(enabled);
        return this;
    }

    public GridMenuItemFactory<T> checked(boolean checked) {
        this.item.setChecked(checked);
        return this;
    }

    public GridMenuItemFactory<T> checkable(boolean checkable) {
        this.item.setCheckable(checkable);
        return this;
    }

    public GridMenuItemFactory<T> id(String id) {
        this.item.setId(id);
        return this;
    }

    public GridMenuItemFactory<T> clickListener(ComponentEventListener<GridContextMenu.GridContextMenuItemClickEvent<T>> clickListener) {
        this.item.addMenuItemClickListener(clickListener);
        return this;
    }

    public GridMenuItemFactory<T> subMenu(Consumer<GridSubMenu<T>> subMenu) {
        subMenu.accept(item.getSubMenu());
        return this;
    }

    public GridMenuItemFactory<T> peek(Consumer<GridMenuItem<T>> menu) {
        menu.accept(item);
        return this;
    }

    @Override
    public GridMenuItem<T> get() {
        return item;
    }
}
