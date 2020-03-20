/*
 * Class: org.bklab.common.action.ActionType
 * Modify date: 2020/3/20 上午10:58
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.common.action;

public enum ActionType {

    SHELL命令,
    SHELL脚本,
    SQL命令,
    SQL脚本,
    HTTP请求,
    ;

    @Override
    public String toString() {
        return name();
    }
}
