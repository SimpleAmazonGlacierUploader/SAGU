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

    static String loadVersionNumber() {
        final Properties properties = new Properties();
        final InputStream resourceIS = SimpleGlacierUploader.class.getResourceAsStream("/version.properties");
        try {
            properties.load(resourceIS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty("version");
    }
}
