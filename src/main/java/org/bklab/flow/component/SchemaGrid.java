/*
 * Class: org.bklab.flow.component.SchemaGrid
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.component;

import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.ColumnPathRenderer;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.server.StreamResource;
import dataq.core.data.schema.Field;
import dataq.core.data.schema.Record;
import dataq.core.data.schema.Schema;
import org.bklab.flow.dialog.ErrorDialog;
import org.bklab.flow.factory.TextAreaFactory;
import org.bklab.util.DigitalFormatter;
import org.bklab.util.PagingList;

import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SchemaGrid extends VerticalLayout {

    private final Grid<Record> grid = new Grid<>();
    private final PagingList<Record> pagingList = new PagingList<>(20);
    private final HorizontalPageBar<Record> pageBar = new HorizontalPageBar<>(pagingList);
    private int singlePageSize = 20;
    private Function<Integer, String> totalDataSizeFormatter = i -> "共" + i + "条数据";

    public SchemaGrid(Schema schema) {
        BiFunction<String, String, String> formatIfAvailable = (source, pattern) -> String.format(pattern, source);
        for (Field field : schema.fields()) {
            Grid.Column<Record> recordColumn = grid.addColumn(createValueProvider(field)).setAutoWidth(true).setTextAlign(ColumnTextAlign.CENTER)
                    .setHeader(createHeader(field));
            Comparator<Record> comparable = createComparable(field);
            if (comparable != null) recordColumn.setComparator(comparable).setSortable(true);
        }
        grid.setPageSize(singlePageSize);
        grid.setSizeFull();

        setSizeFull();
        add(grid, new HorizontalRule(), pageBar);
        setAlignSelf(Alignment.START, grid);
        setAlignSelf(Alignment.END, pageBar);
        setPadding(false);
        setMargin(false);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    public SchemaGrid setTotalDataSizeFormatter(Function<Integer, String> totalDataSizeFormatter) {
        this.totalDataSizeFormatter = totalDataSizeFormatter;
        return this;
    }

    public SchemaGrid setSinglePageSize(int singlePageSize) {
        this.singlePageSize = singlePageSize;
        return this;
    }

    public Grid<Record> getGrid() {
        return grid;
    }

    public SchemaGrid setRecords(List<Record> records) {
        if (records == null) {
            records = new ArrayList<>();
        }
        this.pagingList.update(records, singlePageSize);
        this.pageBar.setOnePageSize(singlePageSize).setTotalDataSizeFormatter(totalDataSizeFormatter).build();
        return this;
    }

    private Span createHeader(Field field) {
        Span span = new Span(field.getCaption());
        String title = String.format("名称：%s\n描述：%s\n类型：%s\n别名：%s",
                field.getName(), field.getDescription(), field.getDataType().name(), field.getAliases());
        span.getElement().setAttribute("title", title);
        return span;
    }

    //    private ValueProvider<Record, ?> createValueProvider(Field field) {
    private Renderer<Record> createValueProvider(Field field) {

        switch (field.getDataType()) {
            case STRING:
                return new ColumnPathRenderer<>(field.getName(), r -> r.getString(field.getName()));

            case INT:
            case LONG:
                return new ColumnPathRenderer<>(field.getName(), r -> new DigitalFormatter(r.getDouble(field.getName())).toInteger());

            case FLOAT:
            case DOUBLE:
                return new ColumnPathRenderer<>(field.getName(), r -> new DigitalFormatter(r.getDouble(field.getName())).toFormatted());

            case BOOLEAN:
                return new ColumnPathRenderer<>(field.getName(), r -> ((boolean) r.getObject(field.getName())) ? "是" : "否");

            case DATE:
                return new ColumnPathRenderer<>(field.getName(), r -> r.getObject(field.getName()) == null ? ""
                        : DateTimeFormatter.ofPattern("yyyy-MM-dd").format((TemporalAccessor) r.getObject(field.getName())));
            case DATETIME:
                return new ColumnPathRenderer<>(field.getName(), r -> r.getObject(field.getName()) == null ? ""
                        : DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(((TemporalAccessor) r.getObject(field.getName()))));


            case BLOB:
                return new ComponentRenderer<>(r -> {
                    Details details = new Details();
                    details.setSummaryText("blob");
                    Blob blob = (Blob) r.getObject(field.getName());
                    details.addContent(new Anchor(new StreamResource(field.getName(), () -> {
                        try {
                            return blob.getBinaryStream();
                        } catch (SQLException e) {
                            new ErrorDialog(e).open();
                            return null;
                        }
                    }), "下载"));
                    try {
                        details.addContent(new TextAreaFactory().label("详细信息").value(
                                new String(blob.getBinaryStream().readAllBytes())).readOnly().get());
                    } catch (Exception e) {
                        new ErrorDialog(e).open();
                    }
                    return details;
                });


            default:
                return new ColumnPathRenderer<>(field.getName(), r -> r.getObject(field.getName()));
        }
    }


    private Comparator<Record> createComparable(Field field) {
        switch (field.getDataType()) {
            case INT:
                return Comparator.comparingInt(r -> r.getInt(field.getName()));
            case LONG:
                return Comparator.comparingLong(r -> r.getLong(field.getName()));
            case FLOAT:
            case DOUBLE:
                return Comparator.comparingDouble(r -> r.getDouble(field.getName()));
            case STRING:
                return Comparator.comparing(r -> r.getString(field.getName()));

            case DATE:
                return Comparator.comparing(r -> (LocalDate) r.getObject(field.getName()));
            case DATETIME:
                return Comparator.comparing(r -> (LocalDateTime) r.getObject(field.getName()));
            default:
                return null;
        }

    }

    private boolean canSort(Field field) {
        switch (field.getDataType()) {
            case BOOLEAN:
            case DATE:
            case DATETIME:
            case FLOAT:
            case LONG:
            case INT:
            case DOUBLE:
            case STRING:
                return true;
            default:
                return false;
        }
    }
}
