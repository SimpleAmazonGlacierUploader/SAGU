/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

/**
 * Application properties holder.
 * It's able to load them from file "SAGU.properties" and save them back.
 */
public class AppProperties {

    private static final String PROPERTIES_FILE_NAME = "SAGU.properties";
    private static final String SAGU_DIR = ".sagu";
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String SAGU_HOME_DIR = System.getProperty("user.home") + SEPARATOR + SAGU_DIR;
    private static final String ACCESS_KEY = "accessKey";
    private static final String SECRET_KEY = "secretKey";
    private static final String VAULT_KEY = "vaultKey";
    private static final String LOCATION_INDEX = "locationSet";
    private static final String LOG_TYPE_INDEX = "logType";

    private final Properties properties = new Properties();
    private final String dir;

    /**
     * Loads properties from working dir if the file is present there. If not, creates '.sagu' dir in home dir and
     * loads/stores properties there.
     */
    public AppProperties() {
        this(System.getProperty("user.dir"), SAGU_HOME_DIR);
    }

    AppProperties(final String workingDir, final String saguHomeDir) {
        if (Files.exists(Paths.get(workingDir, PROPERTIES_FILE_NAME))) {
            dir = workingDir;
        } else {
            if (!Files.exists(Paths.get(saguHomeDir))) {
                try {
                    Files.createDirectory(Paths.get(saguHomeDir));
                } catch (IOException e) {
                    throw new RuntimeException("Cannot create directory '%s' for properties and logs.", e);
                }
            }
            dir = saguHomeDir;
        }
        loadProperties();
    }

    /**
     * Loads properties from given directory if the file is present there.
     *
     * @param dir directory (path string) to load properties from
     */
    public AppProperties(final String dir) {
        this.dir = dir;
        loadProperties();
    }

    private void loadProperties() {
        try {
            final FileInputStream in = new FileInputStream(getFilePropertiesPath());
            properties.load(in);
            in.close();
        } catch (FileNotFoundException e1) {
        } catch (IOException e1) {
        }
    }

    /**
     * Saves properties to file
     */
    public void saveProperties() {
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
        if (properties.getProperty(LOG_TYPE_INDEX) == null) {
            return 0;
        } else {
            return Integer.parseInt(properties.getProperty(LOG_TYPE_INDEX));
        }
    }

    /**
     * @return location index
     */
    public int getLocationIndex() {
        if (properties.getProperty(LOCATION_INDEX) == null) {
            return 0;
        } else {
            return Integer.parseInt(properties.getProperty(LOCATION_INDEX));
        }
    }

    /**
     * @return vault key
     */
    public String getVaultKey() {
        return properties.getProperty(VAULT_KEY);
    }

    /**
     * @return secret key
     */
    public String getSecretKey() {
        return properties.getProperty(SECRET_KEY);
    }

    /**
     * @return access key
     */
    public String getAccessKey() {
        return properties.getProperty(ACCESS_KEY);
    }

    /**
     * @param accessKey new access key to be set
     * @return true if the value changed (new value is different from previous one)
     */
    public boolean setAccessKey(final String accessKey) {
        return setProperty(getAccessKey(), accessKey, ACCESS_KEY);
    }

    /**
     * @param secretKey new secret key to be set
     * @return true if the value changed (new value is different from previous one)
     */
    public boolean setSecretKey(final char[] secretKey) {
        char[] sanitized = secretKey;
        if (secretKey == null) {
            sanitized = new char[0];
        }
        return setProperty(getSecretKey(), String.valueOf(sanitized), SECRET_KEY);
    }

    /**
     * @param vaultKey new vault key to be set
     * @return true if the value changed (new value is different from previous one)
     */
    public boolean setVaultKey(final String vaultKey) {
        return setProperty(getVaultKey(), vaultKey, VAULT_KEY);
    }

    /**
     * @param locationIndex new location index to be set
     * @return true if the value changed (new value is different from previous one)
     */
    public boolean setLocationIndex(final int locationIndex) {
        return setProperty(properties.getProperty(LOCATION_INDEX), Integer.toString(locationIndex), LOCATION_INDEX);
    }

    /**
     * @param logTypeIndex new log type index to be set
     * @return true if the value changed (new value is different from previous one)
     */
    public boolean setLogTypeIndex(final int logTypeIndex) {
        return setProperty(properties.getProperty(LOG_TYPE_INDEX), Integer.toString(logTypeIndex), LOG_TYPE_INDEX);
    }


    File getFilePropertiesPath() {
        return new File(dir + SEPARATOR + PROPERTIES_FILE_NAME);
    }

    String getDir() {
        return dir;
    }

    private boolean setProperty(final String oldValue, final String newValue, final String propertyKey) {
        if (isNullOrEmpty(oldValue) && !isNullOrEmpty(newValue) ||
                !isNullOrEmpty(oldValue) && !oldValue.equals(newValue)) {
            properties.setProperty(propertyKey, newValue == null ? "" : newValue.trim());
            return true;
        } else {
            return false;
        }
    }
}
