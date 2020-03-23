/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-23 13:39:25
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.RearrangeTest
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RearrangeTest<T> {
    private final Map<String, Supplier<Object>> parameterMap = new HashMap<>();
    private final int singlePageSize = 20;
    private final PagingList<T> pagingList = new PagingList<>(singlePageSize);
    private final HorizontalPageBar<T> pageBar = new HorizontalPageBar<>(pagingList);

    public RearrangeTest() {
    }

    public static class HorizontalPageBar<T> {
        public HorizontalPageBar(PagingList<T> pagingList) {
        }
    }

    public static class PagingList<T> {
        public PagingList(int singlePageSize) {
        }
    }
}
