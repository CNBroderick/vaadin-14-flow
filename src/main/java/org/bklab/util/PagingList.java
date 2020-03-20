/*
 * Class: org.bklab.util.PagingList
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PagingList<T> implements Function<Integer, List<T>> {

    private List<T> instance = new ArrayList<>();
    private int singlePageSize = 1024;

    private int lastPageNo = 1;
    private int lastSinglePageSize = 1024;

    public PagingList(List<T> instance, int singlePageSize) {
        this.instance = instance == null ? this.instance : instance;
        this.singlePageSize = singlePageSize == 0 ? this.singlePageSize : singlePageSize;
    }

    @SafeVarargs
    public PagingList(int singlePageSize, T... entities) {
        this.instance = entities == null ? this.instance : Arrays.asList(entities);
        this.singlePageSize = singlePageSize == 0 ? this.singlePageSize : singlePageSize;
    }

    public PagingList<T> update(List<T> instance, int singlePageSize) {
        this.instance = instance == null ? this.instance : instance;
        this.singlePageSize = singlePageSize == 0 ? this.singlePageSize : singlePageSize;
        return this;
    }

    @SafeVarargs
    public final PagingList<T> update(int singlePageSize, T... entities) {
        this.instance = entities == null ? this.instance : Arrays.asList(entities);
        this.singlePageSize = singlePageSize == 0 ? this.singlePageSize : singlePageSize;
        return this;
    }

    public PagingList<T> update(List<T> instance) {
        this.instance = instance == null ? this.instance : instance;
        return this;
    }

    @SafeVarargs
    public final PagingList<T> update(T... entities) {
        this.instance = entities == null ? this.instance : Arrays.asList(entities);
        return this;
    }

    public PagingList<T> reverse() {
        instance = instance.stream().collect(ArrayList::new, (a, b) -> a.add(0, b), (a, b) -> a.addAll(0, b));
        return this;
    }

    public PagingList<T> sorted(Comparator<? super T> comparator) {
        instance = instance.stream().sorted(comparator).collect(Collectors.toList());
        return this;
    }

    public int inPage(T t) {
        int n = instance.indexOf(t);
        return n < 0 ? n : n / singlePageSize + 1;
    }

    public int length() {
        return Double.valueOf(Math.ceil(1.0 * instance.size() / singlePageSize)).intValue();
    }

    public int dataLength() {
        return instance.size();
    }

    @Override
    public List<T> apply(Integer pageNo) {
        this.lastPageNo = pageNo;
        return instance.stream()
                .skip((Math.max(Math.min(pageNo == null ? 1 : pageNo, length()), 1) - 1) * singlePageSize)
                .limit(singlePageSize).collect(Collectors.toList());
    }

    public List<T> apply(Integer pageNo, Integer singlePageSize) {
        singlePageSize = singlePageSize == 0 ? this.singlePageSize : singlePageSize;
        this.lastSinglePageSize = singlePageSize;
        return instance.stream()
                .skip((Math.max(Math.min(pageNo == null ? 1 : pageNo, length()), 1) - 1) * singlePageSize)
                .limit(singlePageSize).collect(Collectors.toList());
    }

    public List<T> apply() {
        return apply(lastPageNo, lastSinglePageSize);
    }

    public int getSinglePageSize() {
        return singlePageSize;
    }

    public PagingList<T> setSinglePageSize(int singlePageSize) {
        this.singlePageSize = singlePageSize > 0 ? singlePageSize : this.singlePageSize;
        return this;
    }
}
