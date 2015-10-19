/*
 * Simple Amazon Glacier Uploader - GUI upload and log for Amazon Glacier
 * Copyright (C) 2012 Brian L. McMichael <brian@brianmcmichael.com>
 */

package com.brianmcmichael.sagu;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class AppPropertiesTest {

    private String propertiesResourcePath;
    private String resourceDir;
    private AppProperties properties;
    private AppProperties emptyProperties;

    @BeforeClass
    public void setUpClass() throws Exception {
        propertiesResourcePath = AppPropertiesTest.class.getResource("/SAGU.properties").getPath();
        resourceDir = Paths.get(propertiesResourcePath).getParent().toString();
        properties = new AppProperties(resourceDir);
        emptyProperties = new AppProperties();
    }

    @Test
    public void constructorShouldLoadPropertiesFile() throws Exception {
        assertThat(properties.getAccessKey(), is("TEST_ACCESS"));
        assertThat(properties.getSecretKey(), is("TEST_SECRET"));
        assertThat(properties.getVaultKey(), is("TEST_VAULT"));
        assertThat(properties.getLogTypeIndex(), is(1));
        assertThat(properties.getLocationIndex(), is(3));
    }

    @Test
    public void getFilePropertiesPathShouldConcatDirAndFileName() throws Exception {
        assertThat(properties.getFilePropertiesPath().getAbsolutePath(), is(propertiesResourcePath));
    }

    @Test
    public void getLogTypeIndexShouldHaveZeroAsDefault() throws Exception {
        assertThat(emptyProperties.getLogTypeIndex(), is(0));
    }

    @Test
    public void getLocationIndexShouldHaveZeroAsDefault() throws Exception {
        assertThat(emptyProperties.getLocationIndex(), is(0));
    }

    @Test
    public void getVaultKeyShouldReturnNullWhenNotFound() throws Exception {
        assertThat(emptyProperties.getVaultKey(), is(nullValue()));
    }

    @Test
    public void getSecretKeyShouldReturnNullWhenNotFound() throws Exception {
        assertThat(emptyProperties.getSecretKey(), is(nullValue()));
    }

    @Test
    public void getAccessKeyShouldReturnNullWhenNotFound() throws Exception {
        assertThat(emptyProperties.getAccessKey(), is(nullValue()));
    }

    @Test
    public void savePropertiesShouldWriteThemToFile() throws Exception {
        final String tempDir = Files.createTempDirectory("sagu-test-").toString();
        final AppProperties tempProperties = new AppProperties(tempDir);
        tempProperties.saveProperties("ACC", "SEC", "VAU", 1, 2);

        final Properties savedProp = new Properties();
        savedProp.load(new FileInputStream(tempDir + System.getProperty("file.separator") + "SAGU.properties"));
        assertThat(savedProp.getProperty("accessKey"), is("ACC"));
        assertThat(savedProp.getProperty("secretKey"), is("SEC"));
        assertThat(savedProp.getProperty("vaultKey"), is("VAU"));
        assertThat(savedProp.getProperty("locationSet"), is("1"));
        assertThat(savedProp.getProperty("logType"), is("2"));
    }
}