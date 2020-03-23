/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-23 13:38:44
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.FlowFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory;

import com.vaadin.flow.component.*;
import org.bklab.flow.factory.base.IComponentFactory;

import java.util.function.Consumer;

public class FlowFactory<T extends Component, E extends FlowFactory<T, E>> implements IComponentFactory<T> {

    protected final T component;

    public FlowFactory(T component) {
        this.component = component;
    }

    public <A> A access(Class<A> a) {
        if (a.isInstance(component)) {
            return (A) component;
        }
        throw new ClassCastException(component.getClass().getName() + "未实现类" + a.getName());
    }

    public E width(String width) {
        access(HasSize.class).setWidth(width);
        return (E) this;
    }

    public E minWidth(String minWidth) {
        access(HasSize.class).setMinWidth(minWidth);
        return (E) this;
    }

    public E minWidth100px() {
        access(HasSize.class).setMinWidth("100px");
        return (E) this;
    }

    public E maxWidth(String maxWidth) {
        access(HasSize.class).setMaxWidth(maxWidth);
        return (E) this;
    }

    public E height(String height) {
        access(HasSize.class).setHeight(height);
        return (E) this;
    }

    public E minHeight(String minHeight) {
        access(HasSize.class).setMinHeight(minHeight);
        return (E) this;
    }

    public E maxHeight(String maxHeight) {
        access(HasSize.class).setMaxHeight(maxHeight);
        return (E) this;
    }

    public E sizeFull() {
        access(HasSize.class).setSizeFull();
        return (E) this;
    }

    public E widthFull() {
        access(HasSize.class).setWidthFull();
        return (E) this;
    }

    public E heightFull() {
        access(HasSize.class).setHeightFull();
        return (E) this;
    }

    public E sizeUndefined() {
        access(HasSize.class).setSizeUndefined();
        return (E) this;
    }

    public E peek(Consumer<T> consumer) {
        consumer.accept(component);
        return (E) this;
    }

    public E errorMessage(String errorMessage) {
        access(HasValidation.class).setErrorMessage(errorMessage);
        return (E) this;
    }

    public E invalid(boolean invalid) {
        access(HasValidation.class).setInvalid(invalid);
        return (E) this;
    }

    public E addClassName(String className) {
        access(HasStyle.class).addClassName(className);
        return (E) this;
    }

    public E setClassName(String className) {
        access(HasStyle.class).setClassName(className);
        return (E) this;
    }

    public E setClassName(String className, boolean set) {
        access(HasStyle.class).setClassName(className, set);
        return (E) this;
    }

    public E addClassNames(String... classNames) {
        access(HasStyle.class).addClassNames(classNames);
        return (E) this;
    }

    public E removeClassNames(String... classNames) {
        access(HasStyle.class).removeClassNames(classNames);
        return (E) this;
    }

    public E enabled(boolean enabled) {
        access(HasEnabled.class).setEnabled(enabled);
        return (E) this;
    }

    public E readOnly(boolean readOnly) {
        access(HasValueAndElement.class).setReadOnly(readOnly);
        return (E) this;
    }

    public E readOnly() {
        access(HasValueAndElement.class).setReadOnly(true);
        return (E) this;
    }

    public E style(String name, String value) {
        access(HasStyle.class).getStyle().set(name, value);
        return (E) this;
    }

    public E addDetachListener(ComponentEventListener<DetachEvent> listener) {
        component.addDetachListener(listener);
        return (E) this;
    }

    public E addAttachListener(ComponentEventListener<AttachEvent> listener) {
        component.addAttachListener(listener);
        return (E) this;
    }

    @Override
    public T get() {
        return component;
    }
}
