/*
 * Class: org.bklab.flow.component.HorizontalPageBar
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import org.bklab.util.DigitalFormatter;
import org.bklab.util.PagingList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HorizontalPageBar<T> extends HorizontalLayout {

    private final Text totalText = new Text("");
    private final Button firstButton = createButton(VaadinIcon.FAST_BACKWARD.create(), "第一页");
    private final Button beforeButton = createButton(VaadinIcon.STEP_BACKWARD.create(), "前一页");
    private final Button nextButton = createButton(VaadinIcon.STEP_FORWARD.create(), "下一页");
    private final Button lastButton = createButton(VaadinIcon.FAST_FORWARD.create(), "最后一页");
    private final PagingList<T> pagingList;
    private final List<Consumer<List<T>>> dataConsumers = new ArrayList<>();
    private final List<Registration> registrations = new ArrayList<>();
    private int onePageSize = 15;
    private final Select<Integer> onePageSizeBox = createOnePageSizeBox();
    private Function<Integer, String> totalDataSizeFormatter = i -> "共" + i + "条数据";

    public HorizontalPageBar(PagingList<T> pagingList) {
        this.pagingList = pagingList;
        this.onePageSize = pagingList.getSinglePageSize();
        setWidthFull();
        setDefaultVerticalComponentAlignment(Alignment.CENTER);
        setMaxHeight("80px");
        getElement().setProperty("color", "var(--lumo-body-text-color)");
    }

    public final HorizontalPageBar<T> addDataConsumer(Consumer<List<T>> dataConsumer) {
        this.dataConsumers.add(dataConsumer);
        return this;
    }

    public final HorizontalPageBar<T> setDataConsumer(Consumer<List<T>> dataConsumer) {
        this.dataConsumers.clear();
        this.dataConsumers.add(dataConsumer);
        return this;
    }

    public HorizontalPageBar<T> build() {
        removeAll();
        HorizontalLayout middle = new HorizontalLayout();
        middle.setMaxWidth("100%");
        H6 totalText = new H6(totalDataSizeFormatter.apply(pagingList.dataLength()));


        NumberField current = new NumberField();
        current.setMinWidth("60px");
        current.setValueChangeMode(ValueChangeMode.ON_CHANGE);
        current.setStep(1);
        current.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_CENTER);
        current.addValueChangeListener(e -> dataConsumers.forEach(c -> c.accept(pagingList.apply(e.getValue().intValue()))));
        current.setValue(1d);

        final H6 totalPageNumber = new H6("共 " + new DigitalFormatter(pagingList.length()) + " 页");

        bindButtonAction(current, totalPageNumber);


        H6 onePageSizeBoxPrefix = new H6("每页");
        H6 onePageSizeBoxSuffix = new H6("条数据");
        HorizontalLayout right = new HorizontalLayout(onePageSizeBoxPrefix, onePageSizeBox, onePageSizeBoxSuffix);
        right.setMinWidth("200px");
        right.setDefaultVerticalComponentAlignment(Alignment.AUTO);

        totalText.getStyle().set("line-height", "0.5em");
        totalPageNumber.getStyle().set("line-height", "0.5em");
        HorizontalLayout left = new HorizontalLayout(totalText, firstButton, beforeButton, current, totalPageNumber, nextButton, lastButton);
        left.setMinWidth("425px");
        left.setDefaultVerticalComponentAlignment(Alignment.AUTO);

        add(left);
        addAndExpand(middle);
        add(right);

        setAlignSelf(Alignment.START, left);
        setAlignSelf(Alignment.CENTER, middle);
        setAlignSelf(Alignment.END, right);

        return this;
    }

    private void bindButtonAction(NumberField current, H6 totalPageNumber) {
        registrations.forEach(Registration::remove);
        registrations.clear();

        current.setMin(1);
        current.setMax(pagingList.length());

        Consumer<Integer> pageChangingListener = i -> {
            firstButton.setEnabled(i > 1);
            beforeButton.setEnabled(i > 1);
            nextButton.setEnabled(i < pagingList.length());
            lastButton.setEnabled(i < pagingList.length());
            firstButton.setDisableOnClick(!firstButton.isEnabled());
            beforeButton.setDisableOnClick(!beforeButton.isEnabled());
            nextButton.setDisableOnClick(!nextButton.isEnabled());
            lastButton.setDisableOnClick(!lastButton.isEnabled());
        };

        registrations.add(current.addValueChangeListener(e -> pageChangingListener.accept(e.getValue().intValue())));
        onePageSizeBox.setValue(pagingList.getSinglePageSize());
        registrations.add(onePageSizeBox.addValueChangeListener(e -> {
            registrations.forEach(Registration::remove);
            registrations.clear();
            T t = pagingList.apply(e.getOldValue()).stream().findFirst().orElse(null);
            pagingList.setSinglePageSize(e.getValue());
            totalPageNumber.setText("共 " + new DigitalFormatter(pagingList.length()) + " 页");
            Integer currentPage = t != null ? pagingList.inPage(t) : 1;
            current.setValue(currentPage.doubleValue());
            List<T> list = pagingList.apply(currentPage);
            dataConsumers.forEach(d -> d.accept(list));
            bindButtonAction(current, totalPageNumber);
        }));

        registrations.add(firstButton.addClickListener(e -> current.setValue(1d)));
        registrations.add(beforeButton.addClickListener(e -> current.setValue((current.getValue().intValue() - 1d))));
        registrations.add(nextButton.addClickListener(e -> current.setValue((current.getValue().intValue() + 1d))));
        registrations.add(lastButton.addClickListener(e -> current.setValue((double) pagingList.length())));

        pageChangingListener.accept(current.getValue().intValue());
    }

    public HorizontalPageBar<T> setTotalDataSizeFormatter(Function<Integer, String> totalDataSizeFormatter) {
        this.totalDataSizeFormatter = totalDataSizeFormatter;
        return this;
    }

    public HorizontalPageBar setOnePageSize(int onePageSize) {
        this.onePageSize = onePageSize;
        return this;
    }

    private Button createButton(Component icon, String title) {
        Button button = new Button(icon);
        button.addThemeVariants(
                ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_CONTRAST,
                ButtonVariant.LUMO_SMALL
        );
        button.getElement().setProperty("title", title);
        return button;
    }

    private Select<Integer> createOnePageSizeBox() {
        Select<Integer> box = new Select<>();
        List<Integer> sorted = Stream.of(onePageSize, 15, 20, 30, 50, 100).filter(i -> i > 0)
                .distinct().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        box.setItems(sorted);
        box.setValue(
                sorted.contains(onePageSize)
                        ? Integer.valueOf(onePageSize)
                        : sorted.stream().findFirst().orElse(20)
        );
        box.setMaxWidth("75px");
        return box;
    }
}
