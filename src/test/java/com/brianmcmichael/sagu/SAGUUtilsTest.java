/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu;

import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class SAGUUtilsTest {
    @Test
    public void loadVersionNumberShouldReturnVersionString() throws Exception {
        final String version = SAGUUtils.loadVersionNumber();
        assertThat(version, is(notNullValue()));
        assertThat(version.matches("\\d+\\.\\d+\\.\\d+(-SNAPSHOT)?"), is(true));
    }
}