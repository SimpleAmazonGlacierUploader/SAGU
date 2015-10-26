/*
 * Simple Amazon Glacier Uploader - GUI upload and log for Amazon Glacier
 * Copyright (C) 2012 Brian L. McMichael <brian@brianmcmichael.com>
 */

package com.brianmcmichael.sagu;

import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.brianmcmichael.sagu.SAGU.getLogFilenamePath;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void getLogFilenamePathShouldConstructCorrectPath() throws Exception {
        final AppProperties properties = mock(AppProperties.class);
        final Path tempDir = Files.createTempDirectory("sagu");
        final String separator = System.getProperty("file.separator");
        when(properties.getDir()).thenReturn(tempDir.toString());

        assertThat(getLogFilenamePath(0, properties).getPath(), is(tempDir.toString() + separator + "Glacier.log"));
        assertThat(getLogFilenamePath(1, properties).getPath(), is(tempDir.toString() + separator + "Glacier.txt"));
        assertThat(getLogFilenamePath(2, properties).getPath(), is(tempDir.toString() + separator + "Glacier.csv"));
        assertThat(getLogFilenamePath(3, properties).getPath(), is(tempDir.toString() + separator + "Glacier.yaml"));
        assertThat(getLogFilenamePath(4, properties).getPath(), is(tempDir.toString() + separator + "GlacierErrors.txt"));
        assertThat(getLogFilenamePath(5, properties).getPath(), is(tempDir.toString() + separator + "Glacier.log"));
    }
}