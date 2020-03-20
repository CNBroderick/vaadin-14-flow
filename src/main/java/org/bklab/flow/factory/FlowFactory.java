/*
 * Class: org.bklab.flow.factory.FlowFactory
 * Modify date: 2020/3/20 上午10:14
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory;

import com.vaadin.flow.component.*;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class FlowFactory<T extends Component, E extends FlowFactory<T, E>> implements Supplier<T> {

    protected final T component;

    public FlowFactory(T component) {
        this.component = component;
    }

    public <A> A checked(Class<A> a) {
        if (a.isInstance(component)) {
            return (A) component;
        }
        throw new ClassCastException(component.getClass().getName() + "未实现类" + a.getName());
    }

    public E width(String width) {
        checked(HasSize.class).setWidth(width);
        return (E) this;
    }

    public E minWidth(String minWidth) {
        checked(HasSize.class).setMinWidth(minWidth);
        return (E) this;
    }

    public E minWidth100px() {
        checked(HasSize.class).setMinWidth("100px");
        return (E) this;
    }

    public E maxWidth(String maxWidth) {
        checked(HasSize.class).setMaxWidth(maxWidth);
        return (E) this;
    }

    public E height(String height) {
        checked(HasSize.class).setHeight(height);
        return (E) this;
    }

    public E minHeight(String minHeight) {
        checked(HasSize.class).setMinHeight(minHeight);
        return (E) this;
    }

    public E maxHeight(String maxHeight) {
        checked(HasSize.class).setMaxHeight(maxHeight);
        return (E) this;
    }

    public E sizeFull() {
        checked(HasSize.class).setSizeFull();
        return (E) this;
    }

    public E widthFull() {
        checked(HasSize.class).setWidthFull();
        return (E) this;
    }

    public E heightFull() {
        checked(HasSize.class).setHeightFull();
        return (E) this;
    }

    public E sizeUndefined() {
        checked(HasSize.class).setSizeUndefined();
        return (E) this;
    }

    public E peek(Consumer<T> consumer) {
        consumer.accept(component);
        return (E) this;
    }

    public E errorMessage(String errorMessage) {
        checked(HasValidation.class).setErrorMessage(errorMessage);
        return (E) this;
    }

    public E invalid(boolean invalid) {
        checked(HasValidation.class).setInvalid(invalid);
        return (E) this;
    }

    public E addClassName(String className) {
        checked(HasStyle.class).addClassName(className);
        return (E) this;
    }

    public E setClassName(String className) {
        checked(HasStyle.class).setClassName(className);
        return (E) this;
    }

    public E setClassName(String className, boolean set) {
        checked(HasStyle.class).setClassName(className, set);
        return (E) this;
    }

    public E addClassNames(String... classNames) {
        checked(HasStyle.class).addClassNames(classNames);
        return (E) this;
    }

    public E removeClassNames(String... classNames) {
        checked(HasStyle.class).removeClassNames(classNames);
        return (E) this;
    }

    public E enabled(boolean enabled) {
        checked(HasEnabled.class).setEnabled(enabled);
        return (E) this;
    }

    public E readOnly(boolean readOnly) {
        checked(HasValueAndElement.class).setReadOnly(readOnly);
        return (E) this;
    }

    public E readOnly() {
        checked(HasValueAndElement.class).setReadOnly(true);
        return (E) this;
    }

    public E style(String name, String value) {
        checked(HasStyle.class).getStyle().set(name, value);
        return (E) this;
    }

//    public <V> E addValueChangeListener(HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<T, V>> listener) {
//        checked(HasValue.class).addValueChangeListener(listener);
//        return (E) this;
//    }


    @Override
    public T get() {
        return component;
    }
}
