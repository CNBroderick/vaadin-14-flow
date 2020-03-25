/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-25 17:10:18
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.menu.IGridMenuManager
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.menu;

public interface IGridMenuManager {

    default void build(GridMenuBuilder<Object> builder, Object t) {
        System.err.println("暂不支持类[" + t.getClass().getName() + "]的右键菜单");
    }
}
