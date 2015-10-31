/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Helper methods.
 */
public class SAGUUtils {

    private SAGUUtils() {
    }

    /**
     * Load version number string from `version.properties` resource file updated automatically by Maven.
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
}
