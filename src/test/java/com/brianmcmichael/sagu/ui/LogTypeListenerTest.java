/*
 * Simple Amazon Glacier Uploader - GUI upload and log for Amazon Glacier
 * Copyright (C) 2012 Brian L. McMichael <brian@brianmcmichael.com>
 */

package com.brianmcmichael.sagu.ui;

import com.brianmcmichael.sagu.AppProperties;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.event.ItemEvent;

import static java.awt.event.ItemEvent.DESELECTED;
import static java.awt.event.ItemEvent.SELECTED;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogTypeListenerTest {

    @Mock
    private AppProperties properties;
    @Mock
    private LogTypes logTypes;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSaveWhenSelected() throws Exception {
        final LogTypeListener listener = new LogTypeListener(properties, logTypes);
        final ItemEvent event = mock(ItemEvent.class);
        when(event.getStateChange()).thenReturn(SELECTED);
        when(logTypes.getSelectedIndex()).thenReturn(2);

        listener.itemStateChanged(event);

        verify(logTypes).selectLogType(2);
        verify(properties).saveProperties();
    }

    @Test
    public void shouldNotSaveWhenDeselected() throws Exception {
        final LogTypeListener listener = new LogTypeListener(properties, logTypes);
        final ItemEvent event = mock(ItemEvent.class);
        when(event.getStateChange()).thenReturn(DESELECTED);

        listener.itemStateChanged(event);

        verify(properties, never()).saveProperties();
    }
}