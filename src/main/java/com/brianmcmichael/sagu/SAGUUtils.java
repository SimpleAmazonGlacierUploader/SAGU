/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * Helper methods.
 */
public class SAGUUtils {

    private SAGUUtils() {
    }

    /**
     * Load version number string from `version.properties` resource file updated automatically by Maven.
     *
     * @return version number string
     */
    static String loadVersionNumber() {
        final Properties properties = new Properties();
        final InputStream resourceIS = SAGU.class.getResourceAsStream("/version.properties");
        try {
            properties.load(resourceIS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty("version");
    }

    /**
     * Converts pathname of the file to the string description for the archive to be uploaded.
     *
     * @return description created from pathname
     */
    static String pathToDescription(final String pathname) {
        return pathname.replaceAll("[^a-zA-Z0-9_\\-\\.]", "");
    }

    /**
     * Removes null elements form array of {@link File}s.
     *
     * @return array with files from input array but without null elements
     */
    static File[] removeNullFiles(final File[] input) {
        return Arrays.asList(input).stream().filter(file -> file != null).toArray(File[]::new);
    }

    /**
     * Concatenates two arrays of {@link File}s to one.
     *
     * @return new array with all elements from two input arrays
     */
    static File[] concatFileArrays(final File[] first, final File[] second) {
        File[] result = new File[first.length + second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
