/*
 * Class: org.bklab.flow.convertor.IntegerToDoubleConverter
 * Modify date: 2020/3/20 上午10:14
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.convertor;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class IntegerToDoubleConverter implements Converter<Double, Integer> {
    @Override
    public Result<Integer> convertToModel(Double value, ValueContext context) {
        return Result.ok(value.intValue());
    }

    @Override
    public Double convertToPresentation(Integer value, ValueContext context) {
        return value.doubleValue();
    }
}
