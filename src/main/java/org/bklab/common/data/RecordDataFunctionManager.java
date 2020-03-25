/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-25 15:10:19
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.common.data.RecordDataFunctionManager
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.data;

import dataq.core.data.schema.Record;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class RecordDataFunctionManager implements IRecordDataFunction<RecordDataFunctionManager> {
    private final List<Consumer<Exception>> exceptionConsumers = new ArrayList<>();

    private final Collection<IRecordDataFunction<? extends IRecordDataFunction<?>>> instance = List.of(
            new CsvRecordDataFunction(),
            new ExcelRecordDataFunction()
    );

    @Override
    public boolean isSupport(Path path) {
        return instance.stream().anyMatch(p -> p.isSupport(path));
    }

    @Override
    public List<Record> parseData(Path path) throws Exception {
        IRecordDataFunction<? extends IRecordDataFunction<?>> function =
                instance.stream().filter(p -> p.isSupport(path)).findFirst().orElse(null);
        if (function == null) {
            callExceptionConsumers(new UnsupportedOperationException("不支持的格式数据"));
            return new ArrayList<>();
        }
        return function.apply(path);
    }

    @Override
    public List<Consumer<Exception>> getExceptionConsumers() {
        return null;
    }
}
