/*
 * Class: org.bklab.common.action.ExecutionResultType
 * Modify date: 2020/3/20 上午10:58
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action;

import java.util.Arrays;

public enum ExecutionResultType {

    string,
    html,
    xml,
    json,
    schema,
    other,
    ;

    public static ExecutionResultType parse(String name) {
        return Arrays.stream(values()).filter(t -> t.name().equalsIgnoreCase(name)).findFirst().orElse(string);
    }

    public static void main(String[] args) {
        System.out.println(parse("html"));
    }

    @Override
    public String toString() {
        return name();
    }
}
