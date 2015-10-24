/*
 * Simple Amazon Glacier Uploader - GUI upload and log for Amazon Glacier
 * Copyright (C) 2012 Brian L. McMichael <brian@brianmcmichael.com>
 */

package com.brianmcmichael.sagu.ui;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.ItemListener;

import static java.awt.Color.WHITE;
import static java.lang.String.format;

/**
 * Holder of log types.
 */
public class LogTypes {

    private JRadioButtonMenuItem logRadio = new JRadioButtonMenuItem(".log");
    private JRadioButtonMenuItem txtRadio = new JRadioButtonMenuItem(".txt");
    private JRadioButtonMenuItem csvRadio = new JRadioButtonMenuItem(".csv");
    private JRadioButtonMenuItem yamlRadio = new JRadioButtonMenuItem(".yaml");

    /**
     * Initializes log type menu items and selects one by index.
     *
     * @param menu          menu to which are log type items added
     * @param selectedIndex index of the log type to be selected
     */
    public LogTypes(final JMenu menu, final int selectedIndex) {
        final ButtonGroup logFileGroup = new ButtonGroup();
        logFileGroup.add(logRadio);
        logFileGroup.add(txtRadio);
        logFileGroup.add(csvRadio);
        logFileGroup.add(yamlRadio);

        menu.add(logRadio);
        menu.add(txtRadio);
        menu.add(csvRadio);
        menu.add(yamlRadio);

        logRadio.setBackground(WHITE);
        txtRadio.setBackground(WHITE);
        csvRadio.setBackground(WHITE);
        yamlRadio.setBackground(WHITE);

        if (selectedIndex < 0 || selectedIndex > 3) {
            throw new RuntimeException(format("Log type index %s out of bounds", selectedIndex));
        }
        selectLogType(selectedIndex);
    }

    /**
     * @return the index of selected log type
     */
    public int getSelectedIndex() {
        if (logRadio.isSelected()) {
            return 0;
        }
        if (txtRadio.isSelected()) {
            return 1;
        }
        if (csvRadio.isSelected()) {
            return 2;
        }
        if (yamlRadio.isSelected()) {
            return 3;
        } else {
            throw new RuntimeException("No log type selected");
        }
    }

    /**
     * Selects the log type by index
     *
     * @param index the index of the log type to be selected
     */
    void selectLogType(int index) {
        switch (index) {
            case 0:
                logRadio.setSelected(true);
                break;
            case 1:
                txtRadio.setSelected(true);
                break;
            case 2:
                csvRadio.setSelected(true);
                break;
            case 3:
                yamlRadio.setSelected(true);
                break;
            default:
                throw new RuntimeException(format("Log type index %s out of bounds", index));
        }
    }

    /**
     * Finds out if the source of focus event was some item of log types
     *
     * @param event focus event
     * @return true if the source of focus event was some item of log types, false otherwise
     */
    public boolean isEventSource(final FocusEvent event) {
        final Object source = event.getSource();
        return source == logRadio || source == txtRadio || source == csvRadio || source == yamlRadio;
    }

    /**
     * Adds the specified item listener to all log types
     *
     * @param listener listener to be added
     */
    public void addItemListener(final ItemListener listener) {
        logRadio.addItemListener(listener);
        txtRadio.addItemListener(listener);
        csvRadio.addItemListener(listener);
        yamlRadio.addItemListener(listener);
    }

    JRadioButtonMenuItem getLogRadio() {
        return logRadio;
    }

    JRadioButtonMenuItem getTxtRadio() {
        return txtRadio;
    }

    JRadioButtonMenuItem getCsvRadio() {
        return csvRadio;
    }

    JRadioButtonMenuItem getYamlRadio() {
        return yamlRadio;
    }
}
