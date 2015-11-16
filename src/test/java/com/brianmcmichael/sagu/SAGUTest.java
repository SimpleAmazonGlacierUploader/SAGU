/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu;

import org.testng.annotations.Test;

import java.nio.file.Files;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SAGUTest {

    @Test
    public void shouldUseWorkingDirForPropertiesAsDefault() throws Exception {
        String saguHomeDir = System.getProperty("user.home") + System.getProperty("file.separator") + ".sagu";
        SAGU.main(null);
        assertThat(SAGU.sagu.getAppProperties().getDir(), is(saguHomeDir));
    }

    @Test
    public void shouldUseDirFromParamWhenPassed() throws Exception {
        final String tempDir = Files.createTempDirectory("sagu").toString();
        SAGU.main(new String[]{"--properties-dir", tempDir});
        assertThat(SAGU.sagu.getAppProperties().getDir(), is(tempDir));
    }

    @Test
    public void getSecretKeyShouldReturnTrimmedString() throws Exception {
        SAGU.main(null);
        SAGU.sagu.secretField.setText(" SEC ");
        assertThat(SAGU.sagu.getSecretKey(), is("SEC"));
    }

    @Test
    public void getAccessKeyShouldReturnTrimmedString() throws Exception {
        SAGU.main(null);
        SAGU.sagu.accessField.setText(" ACC ");
        assertThat(SAGU.sagu.getAccessKey(), is("ACC"));
    }

    @Test
    public void getVaultNameShouldReturnTrimmedString() throws Exception {
        SAGU.main(null);
        SAGU.sagu.vaultField.setText(" VAU ");
        assertThat(SAGU.sagu.getVaultName(), is("VAU"));
    }

}
