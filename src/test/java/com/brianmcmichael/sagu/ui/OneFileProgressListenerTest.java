/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu.ui;

import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.*;

public class OneFileProgressListenerTest {

    @Test
    public void getPercentsShouldReturnZeroWhenTransferredZero() throws Exception {
        final OneFileProgressListener listener = new OneFileProgressListener(mock(UploadWindow.class), 1000);
        assertThat(listener.getPercents(0), is(0));
    }

    @Test
    public void getPercentsShouldReturnFiftyWhenTransferredHalf() throws Exception {
        final OneFileProgressListener listener = new OneFileProgressListener(mock(UploadWindow.class), 1000);
        assertThat(listener.getPercents(500), is(50));
    }

    @Test
    public void getPercentsShouldReturnHundredWhenEverythingTransferred() throws Exception {
        final OneFileProgressListener listener = new OneFileProgressListener(mock(UploadWindow.class), 1000);
        assertThat(listener.getPercents(1000), is(100));
    }

    @Test
    public void getPercentsShouldReturnZeroWhenTotalZero() throws Exception {
        final OneFileProgressListener listener = new OneFileProgressListener(mock(UploadWindow.class), 0);
        assertThat(listener.getPercents(0), is(0));
    }

    @Test
    public void getPercentsShouldReturnZeroWhenTotalNegative() throws Exception {
        final OneFileProgressListener listener = new OneFileProgressListener(mock(UploadWindow.class), -1);
        assertThat(listener.getPercents(0), is(0));
    }
}