/*
 * Simple Amazon Glacier Uploader - GUI upload and log for Amazon Glacier
 * Copyright (C) 2012 Brian L. McMichael <brian@brianmcmichael.com>
 */

package com.brianmcmichael.sagu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Application properties holder.
 * It's able to load them from file "SAGU.properties" and save them back.
 */
public class AppProperties {

    private static final String PROPERTIES_FILE_NAME = "SAGU.properties";

    private final Properties properties = new Properties();
    private final String dir;

    /**
     * Loads properties from default path if the file is present there.
     */
    public AppProperties() {
        this(System.getProperty("user.dir"));
    }

    /**
     * Loads properties from given directory if the file is present there.
     *
     * @param dir directory (path string) to load properties from
     */
    public AppProperties(final String dir) {
        this.dir = dir;

        try {
            final FileInputStream in = new FileInputStream(getFilePropertiesPath());
            properties.load(in);
            in.close();
        } catch (FileNotFoundException e1) {
        } catch (IOException e1) {
        }
    }

    public void saveProperties(final String accessString, final String secretString, final String vaultString,
                               final int locationIndex, final int logFileType) {
        properties.setProperty("accessKey", accessString);
        properties.setProperty("secretKey", secretString);
        properties.setProperty("vaultKey", vaultString);
        properties.setProperty("locationSet", Integer.toString(locationIndex));
        properties.setProperty("logType", Integer.toString(logFileType));
        FileOutputStream out;
        try {
            out = new FileOutputStream(getFilePropertiesPath());
            properties.store(out, "Properties");
            out.close();
        } catch (FileNotFoundException e1) {
        } catch (IOException e1) {
        }
    }

    /**
     * @return index of the log type
     */
    public int getLogTypeIndex() {
        if (properties.getProperty("logType") == null) {
            return 0;
        } else {
            return Integer.parseInt(properties.getProperty("logType"));
        }
    }

    /**
     * @return location index
     */
    public int getLocationIndex() {
        if (properties.getProperty("locationSet") == null) {
            return 0;
        } else {
            return Integer.parseInt(properties.getProperty("locationSet"));
        }
    }

    /**
     * @return vault key
     */
    public String getVaultKey() {
        return properties.getProperty("vaultKey");
    }

    /**
     * @return secret key
     */
    public String getSecretKey() {
        return properties.getProperty("secretKey");
    }

    /**
     * @return access key
     */
    public String getAccessKey() {
        return properties.getProperty("accessKey");
    }

    File getFilePropertiesPath() {
        return new File(dir + System.getProperty("file.separator") + PROPERTIES_FILE_NAME);
    }

}
