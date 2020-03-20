/*
 * Class: org.bklab.common.action.parser.ResultParser
 * Modify date: 2020/3/20 上午10:58
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action.parser;

import java.util.function.Function;

public interface ResultParser<T> extends Function<String, T> {
}
