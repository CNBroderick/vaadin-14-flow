/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-04-03 16:42:16
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.util.RunningTime
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.util;

public class RunningTime {

    private static final long ONE_DAY = 86400000;
    private static final long ONE_HOUR = 3600000;
    private static final long ONE_MINUTE = 60000;
    private static final long ONE_SECOND = 1000;
    private long t0;

    public RunningTime() {
        this.t0 = System.currentTimeMillis();
    }

    public RunningTime reset() {
        this.t0 = System.currentTimeMillis();
        return this;
    }

    public long getMillis() {
        return System.currentTimeMillis() - t0;
    }

    public double getSeconds() {
        return getMillis() / 1000d;
    }

    public double getMinutes() {
        return getSeconds() / 60;
    }

    public double getHours() {
        return getMinutes() / 60;
    }

    public double getDays() {
        return getHours() / 24;
    }

    public String time() {
        long millis = getMillis();
        StringBuilder b = new StringBuilder();

        if (millis > ONE_DAY) {
            b.append(millis / ONE_DAY).append("天 ");
            millis = millis % ONE_DAY;
        }

        if (millis > ONE_HOUR) {
            b.append(millis / ONE_HOUR).append("时 ");
            millis = millis % ONE_HOUR;
        }

        if (millis > ONE_MINUTE) {
            b.append(millis / ONE_MINUTE).append("分 ");
            millis = millis % ONE_MINUTE;
        }

        if (millis > ONE_SECOND) {
            b.append(millis / ONE_SECOND).append("秒 ");
            millis = millis % ONE_SECOND;
        }

        b.append(millis);
        return b.append("毫秒").toString();
    }

    public RunningTime print() {
        StackTraceElement stack = (new Throwable()).getStackTrace()[1];
        System.err.println(stack.getClassName() + ':' +
                stack.getMethodName() + '\t' +
                stack.getLineNumber() + "()" + ':' + System.lineSeparator() + '\t' +
                "当前用时：" + time() + System.lineSeparator());
        return this;
    }
}
