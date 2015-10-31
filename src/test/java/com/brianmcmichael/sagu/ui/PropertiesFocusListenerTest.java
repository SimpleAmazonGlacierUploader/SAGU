/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu.ui;

import com.brianmcmichael.sagu.AppProperties;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.swing.*;
import java.awt.event.FocusEvent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PropertiesFocusListenerTest {

    public static final String TEXT = "blah";
    @Mock
    private AppProperties properties;
    @Mock
    private JTextField accessField;
    @Mock
    private JPasswordField secretField;
    @Mock
    private JTextField vaultField;
    @Mock
    private JComboBox<String> locationChoice;
    @Mock
    private LogTypes logTypes;

    private PropertiesFocusListener listener;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        listener = new PropertiesFocusListener(properties, accessField, secretField, vaultField, locationChoice
        );
    }

    @Test
    public void shouldSaveAllChangedProperties() throws Exception {
        when(accessField.getText()).thenReturn(TEXT);
        when(secretField.getPassword()).thenReturn(TEXT.toCharArray());
        when(vaultField.getText()).thenReturn(TEXT);
        when(locationChoice.getSelectedIndex()).thenReturn(1);
        when(properties.setAccessKey(TEXT)).thenReturn(true);
        when(properties.setSecretKey(TEXT.toCharArray())).thenReturn(true);
        when(properties.setVaultKey(TEXT)).thenReturn(true);
        when(properties.setLocationIndex(1)).thenReturn(true);

        listener.focusLost(mock(FocusEvent.class));

        verify(properties).setAccessKey(TEXT);
        verify(properties).setSecretKey(TEXT.toCharArray());
        verify(properties).setVaultKey(TEXT);
        verify(properties).setLocationIndex(1);
        verify(properties).saveProperties();
    }

    @Test
    public void shouldNotSavePropertiesWhenNoValueChanged() throws Exception {
        when(accessField.getText()).thenReturn(TEXT);
        when(properties.setAccessKey(TEXT)).thenReturn(false);
        when(secretField.getPassword()).thenReturn(TEXT.toCharArray());
        when(properties.setSecretKey(TEXT.toCharArray())).thenReturn(false);
        when(vaultField.getText()).thenReturn(TEXT);
        when(properties.setVaultKey(TEXT)).thenReturn(false);
        when(locationChoice.getSelectedIndex()).thenReturn(1);
        when(properties.setLocationIndex(1)).thenReturn(false);

        listener.focusLost(mock(FocusEvent.class));

        verify(properties, never()).saveProperties();
    }

}