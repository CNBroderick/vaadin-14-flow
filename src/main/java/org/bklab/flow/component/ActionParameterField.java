/*
 * Class: org.bklab.flow.component.ActionParameterField
 * Modify date: 2020/3/20 下午1:13
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import org.bklab.common.action.ActionParameter;
import org.bklab.flow.TmbView;
import org.bklab.flow.factory.ButtonFactory;
import org.bklab.flow.factory.TextFieldFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ActionParameterField extends CustomField<List<ActionParameter>> {

    private final Grid<ParameterComponents> grid = new Grid<>();
    private List<ParameterComponents> components = new ArrayList<>();

    public ActionParameterField() {
        setLabel("命令参数");

        TmbView<TmbView.DefaultTmbView> view = TmbView.create().noBottomHr().noFooter();
        grid.addComponentColumn(c -> c.name).setHeader("变量名称");
        grid.addComponentColumn(c -> c.caption).setHeader("中文别名");
        grid.addComponentColumn(c -> c.defaultValue).setHeader("默认值");
        grid.addComponentColumn(c -> c.description).setHeader("描述");
        grid.addComponentColumn(this::createDeleteButton).setHeader("描述");
        grid.setSizeFull();
        grid.setMinHeight("10em");
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        view.setContent(grid);

        view.addToolBarRight(
                new ButtonFactory().icon(VaadinIcon.PLUS.create()).clickListener(e -> {
                    this.components.add(new ParameterComponents());
                    grid.setItems(this.components);
                }).lumoTertiaryInline().lumoSmall().lumoPrimary().text("增加").get()
        );
        add(view);
    }

    private Button createDeleteButton(ParameterComponents components) {
        return new ButtonFactory().icon(VaadinIcon.CLOSE.create()).clickListener(e -> {
            this.components.remove(components);
            grid.setItems(this.components);
        }).lumoTertiaryInline().lumoSmall().lumoError().lumoIcon().get();
    }

    @Override
    protected List<ActionParameter> generateModelValue() {
        return components.stream().map(ParameterComponents::getActionParameter).collect(Collectors.toList());
    }

    @Override
    protected void setPresentationValue(List<ActionParameter> newPresentationValue) {
        Stream<ParameterComponents> stream = newPresentationValue.stream().map(ParameterComponents::new);
        components = stream.collect(Collectors.toList());
        grid.setItems(components);
    }

    private static class ParameterComponents {
        private final TextField name = createTextField();
        private final TextField caption = createTextField();
        private final TextField defaultValue = createTextField();
        private final TextField description = createTextField();

        public ParameterComponents() {
        }

        public ParameterComponents(ActionParameter actionParameter) {
            name.setValue(actionParameter.getName());
            caption.setValue(actionParameter.getCaption());
            defaultValue.setValue(actionParameter.getDefaultValue());
            description.setValue(actionParameter.getDescription());
        }

        private TextField createTextField() {
            return new TextFieldFactory().widthFull().lumoSmall().lumoAlignCenter().get();
        }

        private ActionParameter getActionParameter() {
            return new ActionParameter().setName(name.getValue())
                    .setCaption(caption.getValue())
                    .setDefaultValue(defaultValue.getValue())
                    .setDescription(description.getValue());
        }
    }
}
