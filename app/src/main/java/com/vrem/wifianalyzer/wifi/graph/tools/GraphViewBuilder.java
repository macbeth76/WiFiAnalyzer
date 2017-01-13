/*
 * WiFi Analyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.vrem.wifianalyzer.wifi.graph.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;

public class GraphViewBuilder {
    public static final int MIN_Y = -100;
    public static final int MAX_Y = 0;

    static final int MAX_Y_DEFAULT = -20;
    static final float TEXT_SIZE_ADJUSTMENT = 0.80f;
    static final float AXIS_TEXT_SIZE_ADJUSMENT = 0.90f;

    private static final int MIN_Y_HALF = MIN_Y / 2;

    private final Context content;
    private final int numHorizontalLabels;
    private final int maximumY;
    private final LayoutParams layoutParams;
    private LabelFormatter labelFormatter;
    private String verticalTitle;
    private String horizontalTitle;

    public GraphViewBuilder(@NonNull Context content, int numHorizontalLabels, int maximumY) {
        this.content = content;
        this.numHorizontalLabels = numHorizontalLabels;
        this.maximumY = (maximumY > MAX_Y || maximumY < MIN_Y_HALF) ? MAX_Y_DEFAULT : maximumY;
        this.layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public GraphViewBuilder setLabelFormatter(@NonNull LabelFormatter labelFormatter) {
        this.labelFormatter = labelFormatter;
        return this;
    }

    public GraphViewBuilder setVerticalTitle(@NonNull String verticalTitle) {
        this.verticalTitle = verticalTitle;
        return this;
    }

    public GraphViewBuilder setHorizontalTitle(@NonNull String horizontalTitle) {
        this.horizontalTitle = horizontalTitle;
        return this;
    }

    public GraphView build() {
        GraphView graphView = new GraphView(content);
        setGraphView(graphView);
        setGridLabelRenderer(graphView);
        setViewPortY(graphView);
        return graphView;
    }

    LayoutParams getLayoutParams() {
        return layoutParams;
    }

    void setGraphView(@NonNull GraphView graphView) {
        graphView.setLayoutParams(layoutParams);
        graphView.setVisibility(View.GONE);
    }

    void setViewPortY(@NonNull GraphView graphView) {
        Viewport viewport = graphView.getViewport();
        viewport.setScrollable(true);
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(MIN_Y);
        viewport.setMaxY(getMaximumY());
        viewport.setXAxisBoundsManual(true);
    }

    void setGridLabelRenderer(@NonNull GraphView graphView) {
        GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();
        gridLabelRenderer.setHighlightZeroLines(false);
        gridLabelRenderer.setNumVerticalLabels(getNumVerticalLabels());
        gridLabelRenderer.setNumHorizontalLabels(numHorizontalLabels);
        gridLabelRenderer.setTextSize(gridLabelRenderer.getTextSize() * TEXT_SIZE_ADJUSTMENT);
        gridLabelRenderer.reloadStyles();

        if (labelFormatter != null) {
            gridLabelRenderer.setLabelFormatter(labelFormatter);
        }

        if (verticalTitle != null) {
            gridLabelRenderer.setVerticalAxisTitle(verticalTitle);
            gridLabelRenderer.setVerticalAxisTitleTextSize(gridLabelRenderer.getVerticalAxisTitleTextSize() * AXIS_TEXT_SIZE_ADJUSMENT);
            gridLabelRenderer.setVerticalLabelsVisible(true);
        } else {
            gridLabelRenderer.setVerticalLabelsVisible(false);
        }

        if (horizontalTitle != null) {
            gridLabelRenderer.setHorizontalAxisTitle(horizontalTitle);
            gridLabelRenderer.setHorizontalAxisTitleTextSize(gridLabelRenderer.getHorizontalAxisTitleTextSize() * AXIS_TEXT_SIZE_ADJUSMENT);
            gridLabelRenderer.setHorizontalLabelsVisible(true);
        } else {
            gridLabelRenderer.setHorizontalLabelsVisible(false);
        }
    }

    int getNumVerticalLabels() {
        return (getMaximumY() - MIN_Y) / 10 + 1;
    }

    int getMaximumY() {
        return maximumY;
    }

}
