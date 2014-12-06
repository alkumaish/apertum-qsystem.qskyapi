/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsky.web;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.PieModel;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import org.apache.commons.lang.StringUtils;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PiePlot;
import org.zkoss.zkex.zul.impl.JFreeChartEngine;
import org.zkoss.zul.Chart;

import org.zkoss.zul.SimplePieModel;

public class PieChartVM {

    private PieChartEngine engine;
    PieModel model;
    boolean threeD = false;
    String message;

    @Init
    public void init() {
        // prepare chart data
        engine = new PieChartEngine();

        model = PieChartData.getModel();
    }

    public PieChartEngine getEngine() {
        return engine;
    }

    public PieModel getModel() {
        return model;
    }

    public boolean isThreeD() {
        return threeD;
    }

    public String getMessage() {
        return message;
    }

    @Command("showMessage")
    @NotifyChange("message")
    public void onShowMessage(@BindingParam("msg") String message) {
        this.message = message;
    }

    @GlobalCommand("dataChanged")
    @NotifyChange("model")
    public void onDataChanged(@BindingParam("category") String category, @BindingParam("num") Number num) {
        model.setValue(category, num);
    }

    @GlobalCommand("configChanged")
    @NotifyChange({"threeD", "engine"})
    public void onConfigChanged(@BindingParam("threeD") boolean threeD, @BindingParam("exploded") boolean exploded) {
        this.threeD = threeD;
        engine.setExplode(exploded);
    }

    public static class PieChartEngine extends JFreeChartEngine {

        private boolean explode = false;

        @Override
        public boolean prepareJFreeChart(JFreeChart jfchart, Chart chart) {
            jfchart.setBackgroundPaint(Color.white);

            PiePlot piePlot = (PiePlot) jfchart.getPlot();
            piePlot.setLabelBackgroundPaint(ChartColors.COLOR_4);

            //override some default colors
            Paint[] colors = new Paint[]{ChartColors.COLOR_1, ChartColors.COLOR_2, ChartColors.COLOR_3, ChartColors.COLOR_4};
            DefaultDrawingSupplier defaults = new DefaultDrawingSupplier();
            piePlot.setDrawingSupplier(new DefaultDrawingSupplier(colors, new Paint[]{defaults.getNextFillPaint()}, new Paint[]{defaults.getNextOutlinePaint()},
                    new Stroke[]{defaults.getNextStroke()}, new Stroke[]{defaults.getNextOutlineStroke()}, new Shape[]{defaults.getNextShape()}));

            piePlot.setShadowPaint(null);

            piePlot.setSectionOutlinesVisible(false);

            piePlot.setExplodePercent("Java", explode ? 0.2 : 0);

            return false;
        }

        public void setExplode(boolean explode) {
            this.explode = explode;
        }
    }

    private static class PieChartData {

        public static PieModel getModel() {
            PieModel model = new SimplePieModel();
            model.setValue("C#", 21.2);
            model.setValue("VB", 10.2);
            model.setValue("Java", 10.4);
            model.setValue("Java1", 5);
            model.setValue("Java2", 5);
            model.setValue("Java3", 5);
            model.setValue("Java4", 5);
            model.setValue("Java5", 5);
            model.setValue("Java6", 5);
            model.setValue("PHP", 28.2);
            return model;
        }
    }

    private static class ChartColors {

        //main colors
        public static Color COLOR_1 = new Color(0x3E454C);
        public static Color COLOR_2 = new Color(0x2185C5);
        public static Color COLOR_3 = new Color(0x7ECEFD);
        public static Color COLOR_4 = new Color(0xFFF6E5);
        public static Color COLOR_5 = new Color(0xFF7F66);
        //additional colors
        public static Color COLOR_6 = new Color(0x98D9FF);
        public static Color COLOR_7 = new Color(0x4689B1);
        public static Color COLOR_8 = new Color(0xB17C35);
        public static Color COLOR_9 = new Color(0xFDC77E);
        public static Color COLOR_10 = new Color(0xF9997E);

        public static String toHtmlColor(Color color) {
            return "#" + toHexColor(color);
        }

        public static String toHexColor(Color color) {
            return StringUtils.leftPad(Integer.toHexString(color.getRGB() & 0xFFFFFF), 6, '0');
        }

    }
}
