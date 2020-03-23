/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-23 13:58:14
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.chartjs.ClickEvent
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.chartjs;

import com.vaadin.flow.component.ComponentEvent;

/**
 * Represents dataset click event.
 * Holds information which dataset values was clicked.
 */
public class ClickEvent extends ComponentEvent<ChartJs> {
    private final String label;
    private final String value;
    private final String datasetLabel;

    public ClickEvent(ChartJs source, boolean fromClient, String label, String datasetLabel, String value) {
        super(source, fromClient);

        this.label = label;
        this.value = value;
        this.datasetLabel = datasetLabel;
    }

    /**
     * Corresponds to value on x axis of vertical chart.
     *
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     * Corresponds to value on y axis of vertical chart.
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * Label the dataset, it;s value corresponds to values in the legend.
     *
     * @return
     */
    public String getDatasetLabel() {
        return datasetLabel;
    }
}
