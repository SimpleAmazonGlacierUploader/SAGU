/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu.ui;

import com.brianmcmichael.sagu.AppProperties;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static java.awt.event.ItemEvent.SELECTED;

/**
 * The listener for saving of properties in case log type selected item changes.
 */
public class LogTypeListener implements ItemListener {

    private final AppProperties appProperties;
    private final LogTypes logTypes;

    /**
     * Initializes item listener to be able to save selected log type
     *
     * @param appProperties holder of properties used for saving
     * @param logTypes      log types holder
     */
    public LogTypeListener(final AppProperties appProperties, final LogTypes logTypes) {
        this.appProperties = appProperties;
        this.logTypes = logTypes;
    }

    @Override
    public void itemStateChanged(final ItemEvent e) {
        if (e.getStateChange() == SELECTED) {
            appProperties.setLogTypeIndex(logTypes.getSelectedIndex());
            appProperties.saveProperties();
        }
    }
}
