/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-25 15:10:19
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.common.data.IRecordDataFunction
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.data;

import dataq.core.data.schema.Record;
import org.bklab.element.HasExceptionConsumer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface IRecordDataFunction<T extends IRecordDataFunction<T>> extends HasExceptionConsumer<T>, Function<Path, List<Record>> {

    boolean isSupport(Path path);

    List<Record> parseData(Path path) throws Exception;

    @Override
    default List<Record> apply(Path path) {
        try {
            return parseData(path);
        } catch (Exception e) {
            callExceptionConsumers(e);
            return new ArrayList<>();
        }
    }
}
