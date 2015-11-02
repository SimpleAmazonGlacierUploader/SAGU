/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu;

import org.testng.annotations.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class SAGUUtilsTest {

    @Test
    public void loadVersionNumberShouldReturnVersionString() throws Exception {
        final String version = SAGUUtils.loadVersionNumber();
        assertThat(version, is(notNullValue()));
        assertThat(version.matches("\\d+\\.\\d+\\.\\d+(-SNAPSHOT)?"), is(true));
    }

    @Test
    public void pathToDescriptionShouldReplaceInvalidChars() throws Exception {
        assertThat(SAGUUtils.pathToDescription("/Dir/File With Spaces.test"), is("DirFileWithSpaces.test"));
    }

    @Test
    public void shouldRemoveNullFiles() throws Exception {
        final File file = new File("");
        final File[] result = SAGUUtils.removeNullFiles(new File[]{null, file, null});

        assertThat(result.length, is(1));
        assertThat(result[0], is(file));
    }

    @Test
    public void shouldConcatFileArrays() throws Exception {
        final File file1 = new File("");
        final File file2 = new File("");
        final File[] result = SAGUUtils.concatFileArrays(new File[]{file1}, new File[]{file2});

        assertThat(result.length, is(2));
        assertThat(result, is(new File[]{file1, file2}));
    }
}