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
import java.nio.file.Path;
import java.util.List;

import static com.brianmcmichael.sagu.LogWriter.getLogFile;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LogWriterTest {

    @Test
    public void getLogFilenamePathShouldConstructCorrectPath() throws Exception {
        final AppProperties properties = mock(AppProperties.class);
        final Path tempDir = Files.createTempDirectory("sagu");
        final String separator = System.getProperty("file.separator");
        when(properties.getDir()).thenReturn(tempDir.toString());

        assertThat(getLogFile(0, properties).getPath(), is(tempDir.toString() + separator + "Glacier.log"));
        assertThat(getLogFile(1, properties).getPath(), is(tempDir.toString() + separator + "Glacier.txt"));
        assertThat(getLogFile(2, properties).getPath(), is(tempDir.toString() + separator + "Glacier.csv"));
        assertThat(getLogFile(3, properties).getPath(), is(tempDir.toString() + separator + "Glacier.yaml"));
        assertThat(getLogFile(4, properties).getPath(),
                is(tempDir.toString() + separator + "GlacierErrors.txt"));
        assertThat(getLogFile(5, properties).getPath(), is(tempDir.toString() + separator + "Glacier.log"));
    }

    @Test
    public void logUploadedFileShouldWriteCorrectLogs() throws Exception {
        final AppProperties properties = mock(AppProperties.class);
        final Path tempDir = Files.createTempDirectory("sagu");
        when(properties.getDir()).thenReturn(tempDir.toString());

        final LogWriter logWriter = new LogWriter(properties);
        logWriter.logUploadedFile("test \"vault\"", "test_region", "test_file", "test_length", "test_hash", "test_id");

        final List<String> logLines = Files.readAllLines(getLogFile(0, properties).toPath());
        assertThat(logLines.size(), is(3));
        assertThat(logLines.get(0), is(""));
        assertThat(logLines.get(1), is(" | ArchiveID: test_id "));
        assertThat(logLines.get(2).matches(
                        " \\| File: test_file  \\| Bytes: test_length  \\| Vault: test \"vault\"  \\| Location: test_region  \\| Date: .*  \\| Hash: test_hash "),
                is(true));

        final List<String> txtLines = Files.readAllLines(getLogFile(1, properties).toPath());
        assertThat(txtLines.size(), is(3));
        assertThat(txtLines.get(0), is(""));
        assertThat(txtLines.get(1), is(" | ArchiveID: test_id "));
        assertThat(txtLines.get(2).matches(
                        " \\| File: test_file  \\| Bytes: test_length  \\| Vault: test \"vault\"  \\| Location: test_region  \\| Date: .*  \\| Hash: test_hash "),
                is(true));

        final List<String> csvLines = Files.readAllLines(getLogFile(2, properties).toPath());
        assertThat(csvLines.size(), is(1));
        assertThat(csvLines.get(0).matches(
                "\"test_id\",\"test_file\",\"test_length\",\"test \"\"vault\"\"\",\"test_region\",\".*\",\"test_hash\""),
                is(true));

        final List<String> yamlLines = Files.readAllLines(getLogFile(3, properties).toPath());
        assertThat(yamlLines.size(), is(8));
        assertThat(yamlLines.get(0), is(""));
        assertThat(yamlLines.get(1), is("-  ArchiveID: \"test_id\""));
        assertThat(yamlLines.get(2), is("   File:      \"test_file\""));
        assertThat(yamlLines.get(3), is("   Bytes:     \"test_length\""));
        assertThat(yamlLines.get(4), is("   Vault:     \"test \\\"vault\\\"\""));
        assertThat(yamlLines.get(5), is("   Location:  \"test_region\""));
        assertThat(yamlLines.get(6).matches("   Date:      \".*\""), is(true));
        assertThat(yamlLines.get(7), is("   Hash:      \"test_hash\""));
    }
}