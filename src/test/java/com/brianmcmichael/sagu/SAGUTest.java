/*
 * Simple Amazon Glacier Uploader - GUI upload and log for Amazon Glacier
 * Copyright (C) 2012 Brian L. McMichael <brian@brianmcmichael.com>
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

}