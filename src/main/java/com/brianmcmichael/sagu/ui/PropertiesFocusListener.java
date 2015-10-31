/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu.ui;

import com.brianmcmichael.sagu.AppProperties;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * The listener for saving of properties in case some property value changes and the field which is holding it looses
 * focus.
 */
public class PropertiesFocusListener implements FocusListener {

    private final AppProperties appProperties;
    private final JTextField accessField;
    private final JPasswordField secretField;
    private final JTextField vaultField;
    private final JComboBox<String> locationChoice;

    /**
     * Initializes focus listener to be able to check all properties fields and to save them when change and loose focus
     *
     * @param appProperties  holder of properties used for saving
     * @param accessField    access key field
     * @param secretField    secret key field
     * @param vaultField     vault field
     * @param locationChoice server location field
     */
    public PropertiesFocusListener(final AppProperties appProperties, final JTextField accessField,
                                   final JPasswordField secretField, final JTextField vaultField,
                                   final JComboBox<String> locationChoice) {
        this.appProperties = appProperties;
        this.accessField = accessField;
        this.secretField = secretField;
        this.vaultField = vaultField;
        this.locationChoice = locationChoice;
    }

    @Override
    public void focusGained(final FocusEvent e) {
        //no-op
    }

    @Override
    public void focusLost(final FocusEvent e) {
        final boolean accessKeyChanged = appProperties.setAccessKey(accessField.getText());
        final boolean secretKeyChanged = appProperties.setSecretKey(secretField.getPassword());
        final boolean vaultKeyChanged = appProperties.setVaultKey(vaultField.getText());
        final boolean locationChoiceChanged = appProperties.setLocationIndex(locationChoice.getSelectedIndex());

        if (accessKeyChanged || secretKeyChanged || vaultKeyChanged || locationChoiceChanged) {
            appProperties.saveProperties();
        }
    }

}
