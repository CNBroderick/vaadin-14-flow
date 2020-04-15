/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-04-13 18:29:09
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.menu.GridMenuBuilder
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.menu;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.grid.contextmenu.GridSubMenu;
import org.bklab.flow.CrudGridView;
import org.bklab.flow.component.HorizontalRule;
import org.bklab.flow.dialog.ExceptionDialog;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GridMenuBuilder<T> {

    private final Consumer<T> whenVoid = t -> {
    };
    protected T entity;
    protected GridContextMenu<T> gridMenu;
    protected GridSubMenu<T> subMenu;
    protected List<Consumer<T>> whenCreates = new ArrayList<>();
    private final Consumer<T> whenCreate = t -> whenCreates.forEach(e -> e.accept(t));
    protected List<Consumer<T>> whenUpdates = new ArrayList<>();
    private final Consumer<T> whenUpdate = t -> whenUpdates.forEach(e -> e.accept(t));
    protected List<Consumer<T>> whenDeletes = new ArrayList<>();
    private final Consumer<T> whenDelete = t -> whenDeletes.forEach(e -> e.accept(t));

    public GridMenuBuilder(GridContextMenu<T> menu, T entity) {
        this.gridMenu = menu;
        this.entity = entity;
    }

    public GridMenuBuilder(GridSubMenu<T> menu, T entity) {
        this.subMenu = menu;
        this.entity = entity;
    }

    public static <T> BiConsumer<GridContextMenu<T>, T> createContextMenuManager(CrudGridView<T> view, Class<? extends IGridMenuManager> manager) {
        return (m, e) -> new GridMenuBuilder<>(m, e).whenCreate(view::insertEntity)
                .whenUpdate(view::updateEntity).whenDelete(view::deleteEntity).fromManager(manager);
    }

    public static <T> BiConsumer<GridSubMenu<T>, T> createSubMenuManager(CrudGridView<T> view, Class<? extends IGridMenuManager> manager) {
        return (m, e) -> new GridMenuBuilder<>(m, e).whenCreate(view::insertEntity)
                .whenUpdate(view::updateEntity).whenDelete(view::deleteEntity).fromManager(manager);
    }

    public GridMenuBuilder<T> whenGridView(CrudGridView<T> view) {
        return this.whenCreate(view::insertEntity).whenUpdate(view::updateEntity).whenDelete(view::deleteEntity);
    }

    public GridMenuBuilder<T> whenCreate(Consumer<T> whenNew) {
        if (whenNew != null) this.whenCreates.add(whenNew);
        return this;
    }

    public GridMenuBuilder<T> whenUpdate(Consumer<T> whenUpdate) {
        if (whenUpdate != null) this.whenUpdates.add(whenUpdate);
        return this;
    }

    public GridMenuBuilder<T> whenDelete(Consumer<T> whenDelete) {
        if (whenDelete != null) this.whenDeletes.add(whenDelete);
        return this;
    }

    public GridMenuBuilder<T> add(MenuItemCommand<T> command) {
        return add(command, whenVoid);
    }

    public GridMenuBuilder<T> addHorizontalRule() {
        GridMenuItem<T> item = null;
        if (gridMenu != null) item = gridMenu.addItem(new HorizontalRule());
        if (subMenu != null) item = subMenu.addItem(new HorizontalRule());
        if (item != null) new GridMenuItemFactory<>(item).enabled(false)
                .peek(i -> i.getElement().getStyle().set("min-height", "1px").set("max-height", "1px"))
                .peek(i -> i.setId("HorizontalRule"))
                ;
        return this;
    }

    public GridMenuBuilder<T> add(MenuItemCommand<T> command, Consumer<T> consumer) {
        GridMenuItemFactory<T> menuItem = createMenuItem(command);
        menuItem.enabled(command.isEnable(entity)).visible(command.isVisible(entity))
                .clickListener(e -> {
                    try {
                        command.apply(entity, menuItem, consumer);
                    } catch (Exception exception) {
                        new ExceptionDialog(exception).open();
                    }
                }).peek(command::peek);
        return this;
    }

    public GridMenuBuilder<T> addCreateItem(MenuItemCommand<T> command) {
        return add(command, whenCreate);
    }

    public GridMenuBuilder<T> addUpdateItem(MenuItemCommand<T> command) {
        return add(command, whenUpdate);
    }

    public GridMenuBuilder<T> addDeleteItem(MenuItemCommand<T> command) {
        return add(command, whenDelete);
    }

    private <C extends MenuItemCommand<T>> GridMenuItemFactory<T> createMenuItem(C command) {
        String name = command.getName();
        Component component = command.getComponent();
        if (gridMenu != null) {
            if (name != null) return new GridMenuItemFactory<>(gridMenu.addItem(name));
            if (component != null) return new GridMenuItemFactory<>(gridMenu.addItem(component));
        }

        if (subMenu != null) {
            if (name != null) return new GridMenuItemFactory<>(subMenu.addItem(name));
            if (component != null) return new GridMenuItemFactory<>(subMenu.addItem(component));
        }

        throw new RuntimeException("无法创建右键菜单项，name、component 均为空：" + command.getClass().getName());
    }

    public void fromManager(Class<? extends IGridMenuManager> manager) {
        try {
            IGridMenuManager iGridMenuManager = manager.getDeclaredConstructor().newInstance();
            manager.getMethod("build", this.getClass(), entity.getClass())
                    .invoke(iGridMenuManager, this, entity);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void finish() {
    }
}
