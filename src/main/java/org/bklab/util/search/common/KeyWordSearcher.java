/*
 * Class: org.bklab.util.search.common.KeyWordSearcher
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.util.search.common;

import dataq.core.data.schema.Record;
import org.bklab.util.LocalDateTools;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.IntStream;

public class KeyWordSearcher<T> {

    private final T entity;

    public KeyWordSearcher(T entity) {
        this.entity = entity;
    }

    public boolean matchJava5(String keyword) {
        if (keyword == null || entity == null) return true;
        for (Field field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object o = field.get(entity);
                if (o != null && String.valueOf(o).toLowerCase().contains(keyword.toLowerCase()))
                    return true;
            } catch (IllegalAccessException ignore) {
            }
        }
        return false;
    }

    /**
     * 二级深度搜索
     */
    public boolean match(String keyword) {
        if (keyword == null || entity == null) return true;
        if (entity instanceof Record) return matchKeyword((Record) entity, keyword);
        return Arrays.stream(entity.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .anyMatch(field -> {
                    try {
                        Object o = field.get(entity);
                        if (o == null) return false;
                        SkipSearch skipSearch = field.getAnnotation(SkipSearch.class);
                        if (skipSearch != null && skipSearch.skip()) {
                            return false;
                        }
                        if (o instanceof String || o instanceof Number)
                            return String.valueOf(o).toLowerCase().contains(keyword.toLowerCase());
                        if (o instanceof Collection || o.getClass().isEnum())
                            return o.toString().toLowerCase().contains(keyword.toLowerCase());
                        if (o instanceof LocalDate) {
                            return LocalDateTools.toChineseString((LocalDate) o).contains(keyword)
                                    || LocalDateTools.toShortString((LocalDate) o).contains(keyword);
                        }
                        if (o instanceof LocalDateTime) {
                            return DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").format((LocalDateTime) o).contains(keyword)
                                    || DateTimeFormatter.ofPattern("uuuu 年 MM月 dd日 HH时 mm分 ss秒").format((LocalDateTime) o).contains(keyword);
                        }
                        if (o instanceof Record) return matchKeyword((Record) o, keyword);
                        return new KeyWordSearcher<>(o).matchDirectly(keyword);
                    } catch (IllegalAccessException e) {
                        return false;
                    }
                });
    }

    public boolean matchKeyword(Record r, String keyword) {
        return IntStream.range(0, r.getSchema().size()).mapToObj(r::getObject)
                .anyMatch(a -> Objects.toString(a, "").contains(keyword));
    }

    /**
     * 一级快速搜索
     */
    public boolean matchDirectly(String keyword) {
        if (keyword == null || entity == null) return true;
        return Arrays.stream(entity.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .anyMatch(field -> {
                    try {
                        Object o = field.get(entity);
                        return o != null && String.valueOf(o).toLowerCase().contains(keyword.toLowerCase());
                    } catch (IllegalAccessException e) {
                        return false;
                    }
                });
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface SkipSearch {
        boolean skip() default true;
    }
}
