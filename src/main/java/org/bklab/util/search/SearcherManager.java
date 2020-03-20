/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-20 14:16:17
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.util.search.SearcherManager
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.util.search;

import dataq.core.xml.XmlObject;
import org.bklab.util.search.common.KeyWordSearcher;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SearcherManager<T> {

    private final Map<String, Searcher<T>> searcherMap = new LinkedHashMap<>();
    private final XmlObject xml = XmlObject.fromURL(SearcherManager.class.getResource("searcher-config.xml"));

    public SearcherManager() {
    }

    public Map<String, Searcher<T>> create(Class<T> classT) {
        return create(classT.getName());
    }

    public Map<String, Searcher<T>> create(String className) {
        List<XmlObject> o = xml.children("searches").stream()
                .filter(c -> c.get("entity").equals(className))
                .findFirst()
                .map(x -> x.children("search"))
                .orElse(new ArrayList<>());
        Map<String, Searcher<T>> map = new LinkedHashMap<>();

        map.put("全部", createDefault());
        for (XmlObject object : o) {
            try {
                String name = object.getString("name");
                //noinspection unchecked
                map.put(name, new Searcher<>(name, object.getString("description", ""),
                        (IKeywordSearcher<T>) Class.forName(object.getString("class")).getDeclaredConstructor().newInstance())
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private Searcher<T> createDefault() {
        return new Searcher<>("全部", "", new DefaultKeywordSearcher<>());
    }

    private static class DefaultKeywordSearcher<T> implements IKeywordSearcher<T> {
        @Override
        public boolean matchKeyword(T entity, String keyword) {
            return new KeyWordSearcher<>(entity).match(keyword);
        }
    }
}
