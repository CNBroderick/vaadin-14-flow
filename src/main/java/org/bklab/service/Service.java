/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-23 16:58:30
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.service.Service
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.service;

import dataq.core.operation.AbstractOperation;
import dataq.core.operation.OperationResult;
import org.bklab.element.HasAbstractOperation;
import org.bklab.flow.dialog.ExceptionDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class Service<T extends Service<T>> {
    private final List<Consumer<Exception>> exceptionConsumers = new ArrayList<>();
    private final List<Consumer<OperationResult>> successConsumers = new ArrayList<>();
    protected Consumer<Exception> exceptionConsumer = e -> exceptionConsumers.forEach(x -> x.accept(e));
    protected Consumer<OperationResult> successConsumer = e -> successConsumers.forEach(s -> s.accept(e));
    private Supplier<String> currentUserSupplier = () -> null;

    {
        exceptionConsumers.add(Throwable::printStackTrace);
    }


    public T uiExceptionConsumer() {
        exceptionConsumers.add(e -> new ExceptionDialog(e).open());
        return (T) this;
    }

    public T uiExceptionConsumer(String title) {
        exceptionConsumers.add(e -> new ExceptionDialog(e).title(title).open());
        return (T) this;
    }

    public T addExceptionConsumer(Consumer<Exception> exceptionConsumer) {
        if (exceptionConsumer != null) this.exceptionConsumers.add(exceptionConsumer);
        return (T) this;
    }

    public T addSuccessConsumer(Consumer<OperationResult> successConsumer) {
        if (successConsumer != null) this.successConsumers.add(successConsumer);
        return (T) this;
    }


    protected void executeOperation(HasAbstractOperation operation, Map<String, Object> params) {
        insertParams(operation, params).execute().ifSuccess(successConsumer)
                .ifException(exceptionConsumer);
    }

    protected <E> E queryList(HasAbstractOperation operation) {
        return queryList(operation, new AtomicReference<>(), new HashMap<>());
    }

    protected <E> E queryList(HasAbstractOperation operation, AtomicReference<E> reference, Map<String, Object> params) {
        insertParams(operation, params).execute().ifOK(reference::set).ifException(exceptionConsumer);
        return reference.get();
    }

    protected <E> E queryEntity(HasAbstractOperation operation, Map<String, Object> params) {
        AtomicReference<List<E>> reference = new AtomicReference<>();
        insertParams(operation, params).execute().ifOK(reference::set).ifException(exceptionConsumer);
        return Stream.ofNullable(reference.get()).findFirst().flatMap(a -> a.stream().findFirst()).orElse(null);
    }

    protected <E> E queryObject(HasAbstractOperation operation) {
        AtomicReference<E> reference = new AtomicReference<>();
        insertParams(operation, new HashMap<>()).execute().ifOK(reference::set).ifException(exceptionConsumer);
        return reference.get();
    }

    protected <E> E queryObject(HasAbstractOperation operation, E defaultValue, Map<String, Object> params) {
        AtomicReference<E> reference = new AtomicReference<>(defaultValue);
        insertParams(operation, params).execute().ifOK(reference::set).ifException(exceptionConsumer);
        return reference.get();
    }

    protected AbstractOperation insertParams(HasAbstractOperation hasAbstractOperation, Map<String, Object> params) {
        AbstractOperation operation = hasAbstractOperation.createAbstractOperation();
        operation.setParam("opr", currentUserSupplier.get());
        if (params != null) params.forEach(operation::setParam);
        return operation;
    }

    public Service<T> setCurrentUserSupplier(Supplier<String> currentUserSupplier) {
        this.currentUserSupplier = currentUserSupplier;
        return this;
    }
}
