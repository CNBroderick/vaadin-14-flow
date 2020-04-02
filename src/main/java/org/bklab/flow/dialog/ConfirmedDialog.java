/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-04-02 11:52:13
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.dialog.ConfirmedDialog
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.dialog;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.bklab.flow.component.HorizontalRule;
import org.bklab.flow.factory.ButtonFactory;

import java.util.function.Supplier;

public class ConfirmedDialog extends Dialog {

    private final H4 title = new H4("提示");
    private final HorizontalLayout topBarMiddle = new HorizontalLayout();
    private final HorizontalLayout topBarRight = new HorizontalLayout();
    private final VerticalLayout content = new VerticalLayout();
    private final HorizontalLayout footerBar = new HorizontalLayout();
    private final HorizontalRule topHr = new HorizontalRule();
    private final HorizontalRule footerHr = new HorizontalRule();

    public ConfirmedDialog() {
        final HorizontalLayout topBar = new HorizontalLayout(title, topBarMiddle, topBarRight);
        topBar.setAlignSelf(FlexComponent.Alignment.START, title);
        topBar.setAlignSelf(FlexComponent.Alignment.CENTER, topBarMiddle);
        topBar.setAlignSelf(FlexComponent.Alignment.END, topBarRight);

//        topBarMiddle.setWidthFull();
        topBar.expand(topBarMiddle);

        VerticalLayout main = new VerticalLayout(topBar, topHr, content, footerHr, footerBar);

        content.setMargin(false);
        content.setPadding(false);
        content.setSpacing(false);
        content.getStyle().set("margin-top", "0");
//        content.setMaxHeight("100%");
        content.setSizeFull();
        main.expand(content);

        topBar.setWidthFull();
        footerBar.setWidthFull();
        topBar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        footerBar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        footerBar.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        main.setSizeFull();
        add(main);

        setCloseOnOutsideClick(true);
    }

    public ConfirmedDialog closeOnOutsideClick() {
        this.setCloseOnOutsideClick(true);
        return this;
    }

    public ConfirmedDialog disableCloseOnOutsideClick() {
        this.setCloseOnOutsideClick(false);
        return this;
    }

    public ConfirmedDialog closeOnEsc() {
        this.setCloseOnEsc(true);
        return this;
    }

    public ConfirmedDialog disableCloseOnEsc() {
        this.setCloseOnEsc(false);
        return this;
    }

    public final ConfirmedDialog hasCustomButton(Button button) {
        footerBar.add(button);
        return this;
    }

    public final ConfirmedDialog hasCustomButton(boolean add, Button button) {
        return add ? hasCustomButton(button) : this;
    }

    @SafeVarargs
    public final ConfirmedDialog hasSaveButton(ComponentEventListener<ClickEvent<Button>>... listeners) {
        footerBar.add(new ButtonFactory().icon(VaadinIcon.CHECK_CIRCLE_O.create()).text("保存").minWidth("100px")
                .clickListener(listeners).clickListener(e -> {
                    if (isOpened()) close();
                }).lumoSmall().lumoPrimary().get());
        return this;
    }

    @SafeVarargs
    public final ConfirmedDialog hasSaveButton(boolean add, ComponentEventListener<ClickEvent<Button>>... listeners) {
        return add ? hasSaveButton(listeners) : this;
    }

    @SafeVarargs
    public final ConfirmedDialog hasSaveButtonWithoutClose(ComponentEventListener<ClickEvent<Button>>... listeners) {
        footerBar.add(new ButtonFactory().icon(VaadinIcon.CHECK_CIRCLE_O.create()).text("保存").minWidth("100px")
                .clickListener(listeners).lumoSmall().lumoPrimary().get());
        return this;
    }

    @SafeVarargs
    public final ConfirmedDialog hasUpdateButton(ComponentEventListener<ClickEvent<Button>>... listeners) {
        footerBar.add(new ButtonFactory().icon(VaadinIcon.EDIT.create()).text("修改").minWidth("100px")
                .clickListener(listeners).lumoSmall().lumoPrimary().get());
        return this;
    }

    @SafeVarargs
    public final ConfirmedDialog hasUpdateButtonWithClose(ComponentEventListener<ClickEvent<Button>>... listeners) {
        footerBar.add(new ButtonFactory().icon(VaadinIcon.EDIT.create()).text("修改").minWidth("100px")
                .clickListener(listeners).clickListener(e -> close()).lumoSmall().lumoPrimary().get());
        return this;
    }

    @SafeVarargs
    public final ConfirmedDialog hasUpdateButton(boolean add, ComponentEventListener<ClickEvent<Button>>... listeners) {
        return add ? hasUpdateButton(listeners) : this;
    }

    @SafeVarargs
    public final ConfirmedDialog hasCancelButton(ComponentEventListener<ClickEvent<Button>>... listeners) {
        footerBar.add(new ButtonFactory().icon(VaadinIcon.CLOSE_CIRCLE_O.create()).text("取消").minWidth("100px")
                .clickListener(listeners).clickListener(e -> close()).lumoSmall().get());
        return this;
    }

    @SafeVarargs
    public final ConfirmedDialog hasCancelButton(boolean add, ComponentEventListener<ClickEvent<Button>>... listeners) {
        return add ? hasCancelButton(listeners) : this;
    }

    @SafeVarargs
    public final ConfirmedDialog hasCloseButton(ComponentEventListener<ClickEvent<Button>>... listeners) {
        footerBar.add(new ButtonFactory().icon(VaadinIcon.CLOSE_CIRCLE_O.create()).text("关闭").lumoPrimary().minWidth("100px")
                .clickListener(listeners).clickListener(e -> close()).lumoSmall().get());
        return this;
    }

    @SafeVarargs
    public final ConfirmedDialog hasCloseButtonOnTopRight(ComponentEventListener<ClickEvent<Button>>... listeners) {
        topBarRight.add(new ButtonFactory().icon(VaadinIcon.CLOSE_CIRCLE_O.create()).text("关闭").lumoPrimary().minWidth("100px")
                .clickListener(listeners).clickListener(e -> close()).lumoSmall().get());
        return this;
    }

    public final ConfirmedDialog hasCloseButtonOnTopRight() {
        topBarRight.add(new ButtonFactory().icon(VaadinIcon.CLOSE_CIRCLE_O.create()).text("关闭").lumoPrimary().minWidth("100px")
                .clickListener(e -> close()).lumoSmall().get());
        return this;
    }

    @SafeVarargs
    public final ConfirmedDialog hasCloseButton(boolean add, ComponentEventListener<ClickEvent<Button>>... listeners) {
        return add ? hasCloseButton(listeners) : this;
    }

    public final ConfirmedDialog hasCancelButton() {
        footerBar.add(new ButtonFactory().icon(VaadinIcon.CLOSE_CIRCLE_O.create()).minWidth("100px")
                .text("取消").clickListener(e -> close()).lumoSmall().get());
        return this;
    }

    public final ConfirmedDialog hasCloseButton() {
        footerBar.add(new ButtonFactory().icon(VaadinIcon.CLOSE_CIRCLE_O.create()).minWidth("100px")
                .text("关闭").clickListener(e -> close()).lumoSmall().lumoPrimary().get());
        return this;
    }

    public ConfirmedDialog addFooterBar(Component... components) {
        this.footerBar.add(components);
        return this;
    }

    public ConfirmedDialog addFooterBar(String text) {
        this.footerBar.add(text);
        return this;
    }

    public ConfirmedDialog addToolBarMiddle(Component... components) {
        topBarMiddle.add(components);
        return this;
    }

    public ConfirmedDialog addToolBarMiddle(String text) {
        topBarMiddle.add(text);
        return this;
    }

    public ConfirmedDialog addToolBarRight(Component... components) {
        topBarRight.add(components);
        return this;
    }

    public ConfirmedDialog addToolBarRight(String text) {
        topBarRight.add(text);
        return this;
    }

    public ConfirmedDialog withSize(String width, String height) {
        if (width != null) setWidth(width);
        if (height != null) setHeight(height);
        return this;
    }

    public ConfirmedDialog withMaxSize(String width, String height) {
        if (width != null) setMaxWidth(width);
        if (height != null) setMaxHeight(height);
        return this;
    }

    public ConfirmedDialog withMinSize(String width, String height) {
        if (width != null) setMinWidth(width);
        if (height != null) setMinHeight(height);
        return this;
    }

    public ConfirmedDialog title(String title) {
        if (title != null) this.title.setText(title);
        return this;
    }

    public ConfirmedDialog title(String pattern, Object... args) {
        this.title.setText(String.format(pattern, args));
        return this;
    }

    public ConfirmedDialog errorTitle() {
        this.title.getStyle().set("color", "red");
        return this;
    }

    public H4 getTitle() {
        return title;
    }

    public ConfirmedDialog setTitle(String title) {
        this.title.setText(title);
        return this;
    }

    public HorizontalLayout getTopBarRight() {
        return topBarRight;
    }

    public HorizontalLayout getTopBarMiddle() {
        return topBarMiddle;
    }

    public VerticalLayout getContent() {
        return content;
    }

    public ConfirmedDialog setContent(Component... components) {
        this.content.removeAll();
        this.content.add(components);
        return this;
    }

    public ConfirmedDialog setContent(Supplier<Component> supplier) {
        this.content.removeAll();
        this.content.add(supplier.get());
        return this;
    }

    public ConfirmedDialog setContent(String message) {
        this.content.removeAll();
        this.content.add(new Text(message));
        return this;
    }

    public ConfirmedDialog setContent(String pattern, Object... args) {
        this.content.removeAll();
        this.content.add(new Text(String.format(pattern, args)));
        return this;
    }

    public HorizontalLayout getFooterBar() {
        return footerBar;
    }

    public ConfirmedDialog redTitle() {
        this.title.getStyle().set("color", "red");
        return this;
    }

    public ConfirmedDialog hideTopHr() {
        topHr.setVisible(false);
        return this;
    }

    public ConfirmedDialog hideFooterHr() {
        footerHr.setVisible(false);
        return this;
    }

    public ConfirmedDialog hideFooterBar() {
        footerBar.setVisible(false);
        return this;
    }

    public ConfirmedDialog closeOnOutsideClick(boolean close) {
        setCloseOnOutsideClick(close);
        return this;
    }

    public ConfirmedDialog closeOnEsc(boolean close) {
        setCloseOnEsc(close);
        return this;
    }
}
