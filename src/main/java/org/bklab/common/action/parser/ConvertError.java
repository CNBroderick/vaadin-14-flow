/*
 * Class: org.bklab.common.action.parser.ConvertError
 * Modify date: 2020/3/20 上午10:58
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action.parser;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;
import java.util.StringJoiner;

public class ConvertError {
    private final String source;
    private final String message;
    private final Throwable throwable;

    public ConvertError(String source, String message, Throwable throwable) {
        this.source = source;
        this.message = message;
        this.throwable = throwable;
    }

    public String getSource() {
        return Objects.toString(source, "");
    }

    public String getMessage() {
        return Objects.toString(message, "");
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String getThrowableString() {
        if (throwable == null) return "";
        StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConvertError.class.getSimpleName() + "{", "\n}")
                .add("\n\tsource: '" + source + "'")
                .add("\n\tmessage: '" + message + "'")
                .add("\n\tthrowable: " + throwable)
                .toString();
    }
}
