/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-20 13:37:26
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.TmbView
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.bklab.flow.component.HorizontalRule;

@SuppressWarnings("unchecked")
public class TmbView<T extends TmbView<T>> extends VerticalLayout {

    private final HorizontalRule topHr = new HorizontalRule();

    private final HorizontalLayout title = new HorizontalLayout();

    private final HorizontalLayout toolBarLeft = new HorizontalLayout(title);
    private final HorizontalLayout toolBarMiddle = new HorizontalLayout();
    private final HorizontalLayout toolBarRight = new HorizontalLayout();

    private final VerticalLayout content = new VerticalLayout();

    private final HorizontalLayout footerBarLeft = new HorizontalLayout();
    private final HorizontalLayout footerBarMiddle = new HorizontalLayout();
    private final HorizontalLayout footerBarRight = new HorizontalLayout();
    private final HorizontalLayout footerBar = new HorizontalLayout(footerBarLeft, footerBarMiddle, footerBarRight);
    private final HorizontalRule bottomHr = new HorizontalRule();

    public TmbView() {
        HorizontalLayout toolBar = new HorizontalLayout(toolBarLeft, toolBarMiddle, toolBarRight);
        toolBar.expand(toolBarMiddle);
        toolBar.setWidthFull();
        toolBarMiddle.setMaxWidth("100%");

        toolBar.setMargin(false);
        toolBarLeft.setMargin(false);
        toolBarMiddle.setMargin(false);
        toolBarRight.setMargin(false);

        content.setMargin(false);
        content.getStyle().set("margin-top", "0");
        content.setPadding(false);
        content.setSpacing(false);
        content.setSizeFull();

        footerBarLeft.setMargin(false);
        footerBarMiddle.setMargin(false);
        footerBarRight.setMargin(false);
        footerBar.setMargin(false);
        footerBar.getStyle().set("margin-top", "0");

        toolBarLeft.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        toolBarMiddle.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        toolBarRight.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        toolBarLeft.setJustifyContentMode(JustifyContentMode.START);
        toolBarMiddle.setJustifyContentMode(JustifyContentMode.CENTER);
        toolBarRight.setJustifyContentMode(JustifyContentMode.END);

        footerBar.expand(footerBarMiddle);
        footerBar.setWidthFull();
        footerBarMiddle.setMaxWidth("100%");

        footerBarLeft.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        footerBarMiddle.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        footerBarRight.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        footerBarLeft.setJustifyContentMode(JustifyContentMode.START);
        footerBarMiddle.setJustifyContentMode(JustifyContentMode.CENTER);
        footerBarRight.setJustifyContentMode(JustifyContentMode.END);


        add(toolBar, topHr);
        addAndExpand(content);
        add(bottomHr, footerBar);

        setSizeFull();
        setMargin(false);

        setAlignSelf(Alignment.START, toolBar, content);
        setAlignSelf(Alignment.END, footerBar);
    }

    public static TmbView<DefaultTmbView> create() {
        return new DefaultTmbView();
    }

    public T title(String text) {
        title.removeAll();
        H3 h3 = new H3(text);
        h3.getStyle().set("margin-top", "0px");
        title.add(h3);
        return (T) this;
    }

    public T title(Component... component) {
        title.removeAll();
        title.add(component);
        return (T) this;
    }

    public T addToolBarLeft(Component... components) {
        toolBarLeft.add(components);
        return (T) this;
    }

    public T addToolBarLeft(String text) {
        toolBarLeft.add(text);
        return (T) this;
    }

    public T addToolBarMiddle(Component... components) {
        toolBarMiddle.add(components);
        return (T) this;
    }

    public T addToolBarMiddle(String text) {
        toolBarMiddle.add(text);
        return (T) this;
    }

    public T addToolBarRight(Component... components) {
        toolBarRight.add(components);
        return (T) this;
    }

    public T addToolBarRight(String text) {
        toolBarRight.add(text);
        return (T) this;
    }

    public T addFooterBarLeft(Component... components) {
        footerBarLeft.add(components);
        return (T) this;
    }

    public T addFooterBarLeft(String text) {
        footerBarLeft.add(text);
        return (T) this;
    }

    public T addFooterBarMiddle(Component... components) {
        footerBarMiddle.add(components);
        return (T) this;
    }

    public T addFooterBarMiddle(String text) {
        footerBarMiddle.add(text);
        return (T) this;
    }

    public T addFooterBarRight(Component... components) {
        footerBarRight.add(components);
        return (T) this;
    }

    public T addFooterBarRight(String text) {
        footerBarRight.add(text);
        return (T) this;
    }

    public T addContent(Component... components) {
        content.add(components);
        return (T) this;
    }

    public T addContent(String text) {
        content.add(text);
        return (T) this;
    }

    public HorizontalLayout getToolBarLeft() {
        return toolBarLeft;
    }

    public HorizontalLayout getToolBarMiddle() {
        return toolBarMiddle;
    }

    public HorizontalLayout getToolBarRight() {
        return toolBarRight;
    }

    public VerticalLayout getContent() {
        return content;
    }

    public T setContent(Component... components) {
        content.removeAll();
        content.add(components);
        return (T) this;
    }

    public HorizontalLayout getFooterBarLeft() {
        return footerBarLeft;
    }

    public HorizontalLayout getFooterBarMiddle() {
        return footerBarMiddle;
    }

    public HorizontalLayout getFooterBarRight() {
        return footerBarRight;
    }

    public HorizontalLayout getFooterBar() {
        return footerBar;
    }

    public T noTopHr() {
        topHr.setVisible(false);
        return (T) this;
    }

    public T noBottomHr() {
        bottomHr.setVisible(false);
        return (T) this;
    }

    public T noFooter() {
        bottomHr.setVisible(false);
        footerBar.setVisible(false);
        return (T) this;
    }

    public static class DefaultTmbView extends TmbView<DefaultTmbView> {

    }

}
