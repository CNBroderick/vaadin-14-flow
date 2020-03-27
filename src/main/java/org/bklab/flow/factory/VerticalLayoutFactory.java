/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-27 09:46:21
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.VerticalLayoutFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.bklab.flow.factory.base.*;

public class VerticalLayoutFactory extends FlowFactory<VerticalLayout, VerticalLayoutFactory> implements
        FlexComponentFactory<VerticalLayoutFactory, VerticalLayout>,
        HasComponentFactory<VerticalLayoutFactory, VerticalLayout>,
        HasStyleFactory<VerticalLayoutFactory, VerticalLayout>,
        ThemableLayoutFactory<VerticalLayoutFactory, VerticalLayout>,
        ClickNotifierFactory<VerticalLayoutFactory, VerticalLayout> {

    public VerticalLayoutFactory() {
        super(new VerticalLayout());
    }

    public VerticalLayoutFactory(VerticalLayout component) {
        super(component);
    }

    public VerticalLayoutFactory(Component... components) {
        super(new VerticalLayout(components));
    }

    public VerticalLayoutFactory compress() {
        component.setPadding(false);
        component.setMargin(false);
        return this;
    }

    public VerticalLayoutFactory spacing(boolean spacing) {
        component.setSpacing(spacing);
        return this;
    }

    public VerticalLayoutFactory padding(boolean padding) {
        component.setPadding(padding);
        return this;
    }

    public VerticalLayoutFactory horizontalComponentAlignment(FlexComponent.Alignment alignment, Component... componentsToAlign) {
        component.setHorizontalComponentAlignment(alignment, componentsToAlign);
        return this;
    }

    public VerticalLayoutFactory defaultHorizontalComponentAlignment(FlexComponent.Alignment alignment) {
        component.setDefaultHorizontalComponentAlignment(alignment);
        return this;
    }

    public VerticalLayoutFactory alignItems(FlexComponent.Alignment alignment) {
        component.setAlignItems(alignment);
        return this;
    }

    public VerticalLayoutFactory alignSelf(FlexComponent.Alignment alignment, HasElement... elementContainers) {
        component.setAlignSelf(alignment, elementContainers);

        return this;
    }

    public VerticalLayoutFactory addAndExpand(Component... components) {
        component.addAndExpand(components);
        return this;
    }

    public VerticalLayoutFactory expand(Component... componentsToExpand) {
        component.expand(componentsToExpand);
        return this;
    }
}
