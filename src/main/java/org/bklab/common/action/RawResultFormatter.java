/*
 * Class: org.bklab.common.action.RawResultFormatter
 * Modify date: 2020/3/20 上午10:58
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action;

public interface RawResultFormatter {

    String format(String content);

    String getName();
}
