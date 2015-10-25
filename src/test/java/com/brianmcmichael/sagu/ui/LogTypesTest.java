/*
 * Simple Amazon Glacier Uploader - GUI upload and log for Amazon Glacier
 * Copyright (C) 2012 Brian L. McMichael <brian@brianmcmichael.com>
 */

package com.brianmcmichael.sagu.ui;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.ItemListener;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogTypesTest {

    @Mock
    private JMenu menu;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void constructorShouldRegisterAllTypes() throws Exception {
        final LogTypes logTypes = new LogTypes(menu, 0);

        assertThat(logTypes.getSelectedIndex(), is(0));
        verify(menu, times(4)).add(Matchers.<JMenuItem>any());
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void constructorShouldFailWhenIndexOutOfBounds() throws Exception {
        new LogTypes(menu, 4);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void constructorShouldFailWhenIndexLessThenZero() throws Exception {
        new LogTypes(menu, -1);
    }

    @Test
    public void getSelectedIndexShouldReturnTheRightIndex() throws Exception {
        LogTypes logTypes = new LogTypes(menu, 0);
        assertThat(logTypes.getSelectedIndex(), is(0));

        logTypes = new LogTypes(menu, 1);
        assertThat(logTypes.getSelectedIndex(), is(1));

        logTypes = new LogTypes(menu, 2);
        assertThat(logTypes.getSelectedIndex(), is(2));

        logTypes = new LogTypes(menu, 3);
        assertThat(logTypes.getSelectedIndex(), is(3));
    }

    @Test
    public void selectLogTypeShouldSetTheRightIndex() throws Exception {
        final LogTypes logTypes = new LogTypes(menu, 0);

        logTypes.selectLogType(1);
        assertThat(logTypes.getSelectedIndex(), is(1));
        logTypes.selectLogType(2);
        assertThat(logTypes.getSelectedIndex(), is(2));
        logTypes.selectLogType(3);
        assertThat(logTypes.getSelectedIndex(), is(3));
        logTypes.selectLogType(0);
        assertThat(logTypes.getSelectedIndex(), is(0));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void selectLogTypeShouldFailWhenIndexOutOfBounds() throws Exception {
        final LogTypes logTypes = new LogTypes(menu, 0);

        logTypes.selectLogType(4);
    }

    @Test
    public void isEventSourceShouldWork() throws Exception {
        final LogTypes logTypes = new LogTypes(menu, 0);
        final FocusEvent event = mock(FocusEvent.class);

        assertThat(logTypes.isEventSource(event), is(false));
        when(event.getSource()).thenReturn(logTypes.getLogRadio());
        assertThat(logTypes.isEventSource(event), is(true));
        when(event.getSource()).thenReturn(logTypes.getTxtRadio());
        assertThat(logTypes.isEventSource(event), is(true));
        when(event.getSource()).thenReturn(logTypes.getCsvRadio());
        assertThat(logTypes.isEventSource(event), is(true));
        when(event.getSource()).thenReturn(logTypes.getYamlRadio());
        assertThat(logTypes.isEventSource(event), is(true));
    }

    @Test
    public void addItemListenerShouldAddItToAllTypes() throws Exception {
        final LogTypes logTypes = new LogTypes(menu, 0);
        final ItemListener listener = mock(ItemListener.class);

        assertThat(logTypes.getLogRadio().getItemListeners().length, is(0));
        assertThat(logTypes.getTxtRadio().getItemListeners().length, is(0));
        assertThat(logTypes.getCsvRadio().getItemListeners().length, is(0));
        assertThat(logTypes.getYamlRadio().getItemListeners().length, is(0));

        logTypes.addItemListener(listener);

        assertThat(logTypes.getLogRadio().getItemListeners().length, is(1));
        assertThat(logTypes.getTxtRadio().getItemListeners().length, is(1));
        assertThat(logTypes.getCsvRadio().getItemListeners().length, is(1));
        assertThat(logTypes.getYamlRadio().getItemListeners().length, is(1));
    }
}