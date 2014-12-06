/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsky.web;

import java.awt.Color;
import java.text.NumberFormat;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.DialModel;
import org.zkoss.zul.DialModelScale;

public class DialChartVM {

    DialModel celsiusModel;
    DialModel fahrenheitModel;

    public DialModel getCelsiusModel() {
        return celsiusModel;
    }

    public DialModel getFahrenheitModel() {
        return fahrenheitModel;
    }

    @Init
    public void init() {
        int celsius = 27;
        celsiusModel = ChartData.createCelsiusModel(celsius);
        fahrenheitModel = ChartData.createFahrenheitModel(ChartData.toFahrenhit(celsius));
    }

    @GlobalCommand("configChanged")
    public void onConfigChanged(@BindingParam("isCelsius") boolean isCelsius, @BindingParam("degree") int degree) {
        if (isCelsius) {
            celsiusModel.getScale(0).setValue(degree);
            fahrenheitModel.getScale(0).setValue(ChartData.toFahrenhit(degree));
           // ((StandardDialScale) celsiusModel.getScale(0)).setTickLabelFormatter(NumberFormat.getIntegerInstance());
        } else {
            celsiusModel.getScale(0).setValue(ChartData.toCelsius(degree));
            fahrenheitModel.getScale(0).setValue(degree);
        }
    }

    private static class ChartData {

        public static DialModel createCelsiusModel(int value) {
            DialModel model = new DialModel();
            DialModelScale scale = model.newScale(-10.0, 60.0, 230, -280, 10.0, 4);
            //scale's configuration data
            scale.setValue(value);
            scale.setText("Посетители");
            scale.newRange(-10, 0, ChartColors.toHtmlColor(Color.getHSBColor(0.55f, 0.8f, 1)), 0.61, 0.603);
            scale.newRange(0, 10, ChartColors.toHtmlColor(Color.getHSBColor(0.3f, 0.8f, 1)), 0.61, 0.603);
            scale.newRange(10, 20, ChartColors.toHtmlColor(Color.getHSBColor(0.18f, 0.8f, 1)), 0.61, 0.603);
            scale.newRange(20, 30, ChartColors.toHtmlColor(Color.getHSBColor(0.12f, 0.8f, 1)), 0.61, 0.603);
            scale.newRange(30, 40, ChartColors.toHtmlColor(Color.getHSBColor(0.08f, 0.8f, 1)), 0.61, 0.603);
            scale.newRange(40, 50, ChartColors.toHtmlColor(Color.getHSBColor(0.05f, 0.8f, 1)), 0.61, 0.603);
            scale.newRange(50, 60, ChartColors.toHtmlColor(Color.getHSBColor(0.0f, 0.8f, 1)), 0.61, 0.603);
            scale.setTickColor("#FFFFFF");
            scale.setNeedleType("pin");
            scale.setNeedleColor("#FF0000");

            model.setFrameFgColor("#808080");
            model.setFrameBgAlpha(255);
            model.setFrameBgColor("#DDDDDD");
            model.setFrameBgColor1("#777777");
            model.setFrameBgColor2("#777777");

            model.setCapRadius(0.1);

            model.setGradientDirection("vertical");

            return model;
        }

        public static DialModel createFahrenheitModel(int value) {
            DialModel model = new DialModel();
            DialModelScale scale = model.newScale(14, 140.0, 230, -280, 18.0, 8);

            //scale's configuration data
            scale.setValue(value);
            scale.setText("Ожидание");
            scale.newRange(14, 32, ChartColors.toHtmlColor(Color.getHSBColor(0.55f, 0.8f, 1)), 0.91, 0.903);
            scale.newRange(32, 50, ChartColors.toHtmlColor(Color.getHSBColor(0.3f, 0.8f, 1)), 0.91, 0.903);
            scale.newRange(50, 68, ChartColors.toHtmlColor(Color.getHSBColor(0.18f, 0.8f, 1)), 0.91, 0.903);
            scale.newRange(68, 86, ChartColors.toHtmlColor(Color.getHSBColor(0.12f, 0.8f, 1)), 0.91, 0.903);
            scale.newRange(86, 104, ChartColors.toHtmlColor(Color.getHSBColor(0.08f, 0.8f, 1)), 0.91, 0.903);
            scale.newRange(104, 122, ChartColors.toHtmlColor(Color.getHSBColor(0.05f, 0.8f, 1)), 0.91, 0.903);
            scale.newRange(122, 140, ChartColors.toHtmlColor(Color.getHSBColor(0.0f, 0.8f, 1)), 0.91, 0.903);
            scale.setTickColor("#000000");
            scale.setNeedleColor("#FF0000");

            model.setFrameFgColor("#505050");
            model.setFrameBgAlpha(0);
            model.setFrameBgColor("#DDDDDD");
            model.setFrameBgColor1("#FFFFFF");
            model.setFrameBgColor2("#FFFFFF");

            model.setCapRadius(0.06);

            model.setGradientDirection("vertical");

            return model;
        }

        public static int toFahrenhit(int celsius) {
            return Math.round(celsius * 9 / 5 + 32);
        }

        public static int toCelsius(int fahrenheit) {
            return Math.round((fahrenheit - 32) * 5 / 9);
        }
    }

    private static class ChartColors {

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

        public static String toHtmlColor(Color color) {
            return "#" + toHexColor(color);
        }

        public static String toHexColor(Color color) {
            return StringUtils.leftPad(Integer.toHexString(color.getRGB() & 0xFFFFFF), 6, '0');
        }
    }
}
