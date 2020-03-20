/*
 * Class: org.bklab.flow.component.SchemaCustomField
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import dataq.core.data.schema.DataType;
import dataq.core.data.schema.Field;
import dataq.core.data.schema.Schema;
import org.bklab.flow.TmbView;
import org.bklab.flow.factory.ButtonFactory;
import org.bklab.flow.factory.NumberFieldFactory;
import org.bklab.flow.factory.TextFieldFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SchemaCustomField extends CustomField<Schema> {

    private final List<Field> fields = new ArrayList<>();
    private final Grid<Field> grid = createFieldGrid();
    private final NumberField skipLines = new NumberFieldFactory().min(0d).step(1d).lumoAlignCenter().lumoSmall().setLabel("跳过行数").get();
    private final NumberField csvHeaderLine = new NumberFieldFactory().min(0d).step(1d).lumoAlignCenter().lumoSmall().setLabel("CSV跳过行数").get();
    private final Select<DataType> dataType = new Select<>(DataType.values());
    private final Button createFieldButton = new ButtonFactory().text("添加").minWidth("100px").icon(VaadinIcon.PLUS.create())
            .lumoTertiaryInline().width("100px").clickListener(e -> createField()).get();
    private final Checkbox enable = new Checkbox("设置Schema");
    private final TmbView<TmbView.DefaultTmbView> main = TmbView.create();


    private final Schema schema;

    public SchemaCustomField() {
        this(new Schema());
    }

    public SchemaCustomField(Schema schema) {
        super(schema);
        this.schema = schema;
        init();
        setPresentationValue(schema);

    }

    private void init() {
        dataType.setLabel("数据类型");
        dataType.setValue(DataType.STRING);
        dataType.getElement().setProperty("theme", "small").setAttribute("theme", "small");
        dataType.setEmptySelectionAllowed(false);
        dataType.setItemLabelGenerator(DataType::name);
        enable.setValue(true);
        csvHeaderLine.setVisible(false);

        setSizeFull();
        setMinHeight("300px");
        add(main);
        setLabel("Schema");
        main.addToolBarLeft(enable).addToolBarRight(skipLines, csvHeaderLine, dataType, createFieldButton).setContent(grid);
        main.getToolBarRight().setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        main.getToolBarLeft().setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        createFieldButton.getStyle().set("margin-top", "auto");

//        enable.addValueChangeListener(e -> setEnabled(e.getValue()));
    }

    @Override
    protected Schema generateModelValue() {
        if (!enable.getValue()) return null;
        Schema schema = new Schema();
        schema.setSkiplines(skipLines.getValue().intValue());
        schema.setCSVHeaderLine(csvHeaderLine.getValue().intValue());
        fields.forEach(schema::addField);
        return schema;
    }

    @Override
    protected void setPresentationValue(Schema newPresentationValue) {
        fields.addAll(Arrays.asList(schema.fields()));
        skipLines.setValue((double) newPresentationValue.getSkiplines());
        csvHeaderLine.setValue((double) newPresentationValue.getCSVHeaderLine());
        grid.setItems(fields);
    }

    private void createField() {
        Field field = new Field("name " + (fields.size() + 1), dataType.getValue());
        fields.add(field);
        grid.setItems(fields);
    }

    private Grid<Field> createFieldGrid() {
        TextField nameField = new TextFieldFactory().lumoSmall().widthFull().get();
        TextField captionField = new TextFieldFactory().lumoSmall().widthFull().get();
        TextField descriptionField = new TextFieldFactory().lumoSmall().widthFull().get();
        TextField aliasesField = new TextFieldFactory().lumoSmall().widthFull().get();
        List<Button> editButtons = new ArrayList<>();
        Grid<Field> grid = new Grid<>();
        Editor<Field> editor = grid.getEditor();

        grid.addColumn(Field::getName).setEditorComponent(nameField).setHeader("名称");
        grid.addColumn(Field::getCaption).setEditorComponent(captionField).setHeader("中文名");
        grid.addColumn(field -> field.getDataType().name()).setHeader("类型");
        grid.addColumn(Field::getDescription).setEditorComponent(descriptionField).setHeader("描述");
        grid.addColumn(Field::getAliases).setEditorComponent(aliasesField).setHeader("别名");

        grid.addComponentColumn(f -> {
            Button editButton = new ButtonFactory().lumoTertiaryInline().lumoSmall().text("修改").clickListener(e -> {
                editor.editItem(f);
                nameField.focus();
            }).get();
            editButton.setEnabled(!editor.isOpen());
            editButtons.add(editButton);

            Button upButton = new ButtonFactory().lumoTertiaryInline().lumoSmall().text("上移").clickListener(e -> {
                int i = fields.indexOf(f);
                if (i > 0) {
                    fields.remove(f);
                    fields.add(i - 1, f);
                    grid.setItems(fields);
                }
                e.getSource().setEnabled(fields.indexOf(f) > 0);
            }).get();
            upButton.setEnabled(fields.indexOf(f) > 0);

            Button downButton = new ButtonFactory().lumoTertiaryInline().lumoSmall().text("下移").clickListener(e -> {
                int i = fields.indexOf(f);
                if (i < fields.size() - 1) {
                    fields.remove(f);
                    fields.add(i + 1, f);
                    grid.setItems(fields);
                }
                e.getSource().setEnabled(fields.indexOf(f) < fields.size() - 1);
            }).get();
            downButton.setEnabled(fields.indexOf(f) < fields.size() - 1);

            Button deleteButton = new ButtonFactory().lumoTertiaryInline().lumoSmall().lumoError().text("删除").clickListener(e -> {
                fields.remove(f);
                grid.setItems(f);
                grid.setItems(fields);
            }).get();

            Span span = new Span(editButton, upButton, downButton, deleteButton);
            span.getChildren().forEach(s -> s.getElement().getStyle().set("margin-right", "1em"));
            return span;
        }).setHeader("操作");

        Button save = new ButtonFactory().lumoTertiaryInline().lumoSmall().lumoPrimary().clickListener(e -> editor.save()).text("保存").get();
        Button cancel = new ButtonFactory().lumoTertiaryInline().lumoSmall().lumoError().clickListener(e -> editor.cancel()).text("取消").get();

        editor.setBuffered(false);
        Binder<Field> binder = new Binder<>();
        editor.setBinder(binder);

        binder.forField(nameField).asRequired()
                .withValidator(n -> fields.stream().filter(f -> f.getName().equals(n)).count() <= 1, "名称不能重复")
                .bind(Field::getName, Field::setName);
        binder.forField(captionField).bind(Field::getCaption, Field::setCaption);
        binder.forField(descriptionField).asRequired().bind(Field::getDescription, Field::setDescription);
        binder.forField(aliasesField).asRequired().bind(Field::getAliases, Field::setAliases);
        editor.addSaveListener(e -> {
            if (binder.isValid()) {
                binder.writeBeanIfValid(e.getItem());
                grid.getDataProvider().refreshItem(e.getItem());
            }
        });
        editor.addOpenListener(e -> {
            binder.readBean(e.getItem());
            editButtons.forEach(button -> button.setEnabled(!editor.isOpen()));
        });
        editor.addCloseListener(e -> editButtons.forEach(button -> button.setEnabled(!editor.isOpen())));
        grid.setSizeFull();
        grid.setMinWidth("100%");
        grid.setMinHeight("10em");
        grid.setHeightByRows(true);
        return grid;
    }

}
