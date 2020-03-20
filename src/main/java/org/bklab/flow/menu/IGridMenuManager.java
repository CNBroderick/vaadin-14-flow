/*
 * Class: org.bklab.flow.menu.IGridMenuManager
 * Modify date: 2020/3/20 上午10:51
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.menu;

public interface IGridMenuManager {

    default void build(GridMenuBuilder<Object> builder, Object t) {
        throw new UnsupportedOperationException("暂不支持类[" + t.getClass().getName() + "]的右键菜单");
    }
}
