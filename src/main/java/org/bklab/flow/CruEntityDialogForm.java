/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-25 18:41:12
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.CruEntityDialogForm
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.binder.Binder;
import org.bklab.flow.dialog.ConfirmedDialog;
import org.bklab.flow.dialog.ErrorDialog;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public abstract class CruEntityDialogForm<T> {
    protected final ConfirmedDialog dialog = new ConfirmedDialog();
    private final Map<String, Object> property = new ConcurrentHashMap<>();
    protected T entity;
    protected Boolean updateMode = Boolean.FALSE;
    protected Boolean readMode = Boolean.FALSE;
    protected String title = "";
    protected List<Consumer<T>> saveListeners = new ArrayList<>();

    private CruEntityDialogForm() {
    }

    public CruEntityDialogForm(T entity) {
        this.entity = entity;
        this.updateMode = Boolean.TRUE;
    }

    public CruEntityDialogForm(Class<T> t) {
        try {
            this.entity = t.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public CruEntityDialogForm<T> readMode() {
        this.updateMode = Boolean.FALSE;
        this.readMode = Boolean.TRUE;
        return this;
    }

    protected CruEntityDialogForm<T> addProperty(String name, Object property) {
        this.property.put(name, property);
        return this;
    }

    protected <E> E getProperty(String name) {
        return (E) this.property.getOrDefault(name, null);
    }

    protected <E> E getProperty(String name, E defaultValue) {
        return (E) this.property.getOrDefault(name, defaultValue);
    }


    protected CruEntityDialogForm<T> updateTitle(String title) {
        if (updateMode) this.title = title;
        return this;
    }

    protected CruEntityDialogForm<T> readTitle(String title) {
        if (readMode) this.title = title;
        return this;
    }

    protected CruEntityDialogForm<T> createTitle(String title) {
        if (!updateMode) this.title = title;
        return this;
    }

    protected String generateTitle(String create, String update, String query) {
        if (updateMode) this.title = update;
        if (!updateMode) this.title = create;
        if (readMode) this.title = query;
        return title;
    }

    public Dialog create() {
        try {
            return createDialog();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorDialog("打开对话框失败：" + e.getLocalizedMessage()).throwable(e).create();
        }
    }

    public CruEntityDialogForm<T> addSaveListener(Consumer<T> saveListener) {
        if (saveListener != null) this.saveListeners.add(saveListener);
        return this;
    }

    protected Binder<T> createBinder() {
        Binder<T> binder = new Binder<>();
        binder.setBean(entity);
        return binder;
    }

    protected void callSaveListeners() {
        this.saveListeners.forEach(t -> t.accept(entity));
    }

    abstract protected Dialog createDialog();
}
