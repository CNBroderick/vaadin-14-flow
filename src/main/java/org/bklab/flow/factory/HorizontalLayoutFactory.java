/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-27 09:46:01
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.factory.HorizontalLayoutFactory
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.factory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.bklab.flow.factory.base.ClickNotifierFactory;
import org.bklab.flow.factory.base.FlexComponentFactory;
import org.bklab.flow.factory.base.HasComponentFactory;
import org.bklab.flow.factory.base.ThemableLayoutFactory;

public class HorizontalLayoutFactory extends FlowFactory<HorizontalLayout, HorizontalLayoutFactory> implements
        FlexComponentFactory<HorizontalLayoutFactory, HorizontalLayout>,
        HasComponentFactory<HorizontalLayoutFactory, HorizontalLayout>,
        ThemableLayoutFactory<HorizontalLayoutFactory, HorizontalLayout>,
        ClickNotifierFactory<HorizontalLayoutFactory, HorizontalLayout> {

    public HorizontalLayoutFactory() {
        super(new HorizontalLayout());
    }

    public HorizontalLayoutFactory(HorizontalLayout component) {
        super(component);
    }

    public HorizontalLayoutFactory(Component... components) {
        super(new HorizontalLayout(components));
    }

    public HorizontalLayoutFactory compress() {
        component.setPadding(false);
        component.setMargin(false);
        return this;
    }

    public HorizontalLayoutFactory spacing(boolean spacing) {
        component.setSpacing(spacing);
        return this;
    }

    public HorizontalLayoutFactory padding(boolean padding) {
        component.setPadding(padding);
        return this;
    }

    public HorizontalLayoutFactory verticalComponentAlignment(FlexComponent.Alignment alignment, Component... componentsToAlign) {
        component.setVerticalComponentAlignment(alignment, componentsToAlign);
        return this;
    }

    public HorizontalLayoutFactory defaultVerticalComponentAlignment(FlexComponent.Alignment alignment) {
        component.setDefaultVerticalComponentAlignment(alignment);
        return this;
    }

    public HorizontalLayoutFactory alignItems(FlexComponent.Alignment alignment) {
        component.setAlignItems(alignment);
        return this;
    }

    public HorizontalLayoutFactory alignSelf(FlexComponent.Alignment alignment, HasElement... elementContainers) {
        component.setAlignSelf(alignment, elementContainers);
        return this;
    }

    public HorizontalLayoutFactory addAndExpand(Component... components) {
        component.addAndExpand(components);
        return this;
    }

    public HorizontalLayoutFactory expand(Component... componentsToExpand) {
        component.expand(componentsToExpand);
        return this;
    }
}
