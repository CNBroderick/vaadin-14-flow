/*
 * Class: org.bklab.flow.component.TabLayout
 * Modify date: 2020/3/20 上午10:14
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TabLayout extends VerticalLayout {

    private final Tabs tabs = new Tabs();
    private final Div content = new Div();
    private final Map<Tab, Supplier<Component>> map = new LinkedHashMap<>();


    public TabLayout() {
        setSizeFull();
        add(tabs, content);
        setAlignSelf(Alignment.START, tabs);
        setAlignSelf(Alignment.END, content);
        content.setSizeFull();
        content.getStyle().set("border", "1px solid var(--lumo-contrast-30pct)");

        tabs.addSelectedChangeListener(e -> {
            content.removeAll();
            content.add(map.getOrDefault(e.getSelectedTab(), Span::new).get());
        });
        tabs.setAutoselect(true);
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
    }

    public TabLayout addTab(String tabName, Supplier<Component> componentSupplier) {
        return addTab(new Tab(tabName), componentSupplier);
    }

    public TabLayout addTab(Tab tab, Supplier<Component> componentSupplier) {
        tabs.add(tab);
        if (componentSupplier == null) componentSupplier = Span::new;
        map.put(tab, componentSupplier);
        return this;
    }

    public TabLayout build() {
        map.entrySet().stream().findFirst().ifPresent(e -> {
            tabs.setSelectedTab(e.getKey());
            content.removeAll();
            content.add(e.getValue().get());
        });
        return this;
    }

    public Tabs getTabs() {
        return tabs;
    }

    public Div getContent() {
        return content;
    }
}
