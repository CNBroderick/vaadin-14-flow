/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-27 13:40:59
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.element.HasExceptionConsumer
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.element;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public interface HasExceptionConsumer<T extends HasExceptionConsumer<T>> {

    /*
     * please add flow code to your extends class:
     * private final List<Consumer<Exception>> exceptionConsumers = new ArrayList<>();
     * <p>
     * public List<Consumer<Exception>> getExceptionConsumers() {
     * return exceptionConsumers;
     * }
     *
     * @return exception consumers
     */
    List<Consumer<Exception>> getExceptionConsumers();

    default T addExceptionConsumers(Consumer<Exception> exceptionConsumer) {
        getExceptionConsumers().add(exceptionConsumer);
        return (T) this;
    }

    default T addExceptionConsumers(Collection<Consumer<Exception>> exceptionConsumers) {
        getExceptionConsumers().addAll(exceptionConsumers);
        return (T) this;
    }

    default T callExceptionConsumers(Exception e) {
        getExceptionConsumers().forEach(exceptionConsumer -> exceptionConsumer.accept(e));
        return (T) this;
    }

}
