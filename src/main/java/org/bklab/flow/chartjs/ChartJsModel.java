/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-23 13:45:48
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.chartjs.ChartJsModel
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow.chartjs;

import com.vaadin.flow.templatemodel.TemplateModel;

/**
 * Represents chart's necessary data to be created.
 * jsonChart aggregates jsonChartData and jsonChartOptions.
 */
public interface ChartJsModel extends TemplateModel {
    void setChartJs(String jsonChart);

    void setChartData(String jsonChartData);

    void setChartOptions(String jsonChartOptions);
}
