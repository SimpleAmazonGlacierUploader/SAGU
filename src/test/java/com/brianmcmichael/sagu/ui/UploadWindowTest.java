/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu.ui;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class UploadWindowTest {

    // just for manual UI testing
    @Test(enabled = false)
    public void testUI() throws Exception {
        new UploadWindow();
        Thread.sleep(10000);
    }
}