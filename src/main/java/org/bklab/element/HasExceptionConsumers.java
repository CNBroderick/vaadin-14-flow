/*
 * Class: org.bklab.element.HasExceptionConsumers
 * Modify date: 2020/3/20 上午11:01
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class HasExceptionConsumers<T extends HasExceptionConsumers<T>> {

    protected final List<Consumer<Exception>> exceptionConsumers = new ArrayList<>();

    public List<Consumer<Exception>> getExceptionConsumers() {
        return exceptionConsumers;
    }

    public T addExceptionConsumers(Consumer<Exception> exceptionConsumer) {
        this.exceptionConsumers.add(exceptionConsumer);
        return (T) this;
    }

    public T addExceptionConsumers(Consumer<Exception>... exceptionConsumers) {
        this.exceptionConsumers.addAll(Arrays.asList(exceptionConsumers));
        return (T) this;
    }

    protected T callExceptionConsumers(Exception e) {
        this.exceptionConsumers.forEach(exceptionConsumer -> exceptionConsumer.accept(e));
        return (T) this;
    }

}
