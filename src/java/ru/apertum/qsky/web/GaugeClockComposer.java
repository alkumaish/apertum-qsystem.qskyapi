/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsky.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

/**
 *
 * @author Evgeniy Egorov
 */
public class GaugeClockComposer extends SelectorComposer<Window> {

    @Wire
    Label chart;
    public static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";
    /**
     * Формат даты.
     */
    public final static DateFormat format_d = new SimpleDateFormat(DATE_FORMAT);

    // Move
    @Listen("onTimer = #timer")
    public void updateData() {
        chart.setValue(format_d.format(new Date()));
    }

}
