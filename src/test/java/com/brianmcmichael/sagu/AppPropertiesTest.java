/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;

import static java.util.Collections.singleton;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class AppPropertiesTest {

    private String propertiesResourcePath;
    private String resourceDir;

    @BeforeClass
    public void setUpClass() throws Exception {
        propertiesResourcePath = AppPropertiesTest.class.getResource("/SAGU.properties").getPath();
        resourceDir = Paths.get(propertiesResourcePath).getParent().toString();
    }

    @Test
    public void constructorShouldLoadPropertiesFile() throws Exception {
        final AppProperties properties = new AppProperties(resourceDir);
        assertThat(properties.getAccessKey(), is("TEST_ACCESS"));
        assertThat(properties.getSecretKey(), is("TEST_SECRET"));
        assertThat(properties.getVaultKey(), is("TEST_VAULT"));
        assertThat(properties.getLogTypeIndex(), is(1));
        assertThat(properties.getLocationIndex(), is(3));
    }

    @Test
    public void constructorShouldUseWorkingDirIfPropertiesPresentThere() throws Exception {
        final AppProperties properties = new AppProperties(resourceDir, "");
        assertThat(properties.getAccessKey(), is("TEST_ACCESS"));
    }

    @Test
    public void constructorShouldUseHomeDirIfPropertiesNotInWorkingDir() throws Exception {
        final Path working = Files.createTempDirectory("working");
        final Path home = Files.createTempDirectory("home");
        final Path saguHome = Paths.get(home.toString(), ".sagu");
        Files.createDirectory(saguHome);
        final Path propFile = Files.createFile(Paths.get(saguHome.toString(), "SAGU.properties"));
        Files.write(propFile, singleton("accessKey=TEST"));

        final AppProperties properties = new AppProperties(working.toString(), saguHome.toString());
        assertThat(properties.getAccessKey(), is("TEST"));
    }

    @Test
    public void getFilePropertiesPathShouldConcatDirAndFileName() throws Exception {
        final AppProperties properties = new AppProperties(resourceDir);
        assertThat(properties.getFilePropertiesPath().getAbsolutePath(), is(propertiesResourcePath));
    }

    @Test
    public void gettersShouldHaveReturnSensibleDefault() throws Exception {
        final String tempDir = Files.createTempDirectory("sagu-test-").toString();
        final AppProperties emptyProperties = new AppProperties(tempDir);

        assertThat(emptyProperties.getLogTypeIndex(), is(0));
        assertThat(emptyProperties.getLocationIndex(), is(0));
        assertThat(emptyProperties.getVaultKey(), is(nullValue()));
        assertThat(emptyProperties.getSecretKey(), is(nullValue()));
        assertThat(emptyProperties.getAccessKey(), is(nullValue()));
    }

    @Test
    public void settersShouldChangeRightValuesAndReturnChangedFlag() throws Exception {
        final AppProperties properties = new AppProperties(resourceDir);

        assertThat(properties.setAccessKey("AC"), is(true));
        assertThat(properties.setSecretKey("SE".toCharArray()), is(true));
        assertThat(properties.setVaultKey("VA"), is(true));
        assertThat(properties.setLocationIndex(1), is(true));
        assertThat(properties.setLogTypeIndex(2), is(true));

        assertThat(properties.getAccessKey(), is("AC"));
        assertThat(properties.getSecretKey(), is("SE"));
        assertThat(properties.getVaultKey(), is("VA"));
        assertThat(properties.getLocationIndex(), is(1));
        assertThat(properties.getLogTypeIndex(), is(2));

        assertThat(properties.setAccessKey("AC"), is(false));
        assertThat(properties.setSecretKey("SE".toCharArray()), is(false));
        assertThat(properties.setVaultKey("VA"), is(false));
        assertThat(properties.setLocationIndex(1), is(false));
        assertThat(properties.setLogTypeIndex(2), is(false));
    }

    @Test
    public void settersShouldSanitizeNulls() throws Exception {
        final AppProperties properties = new AppProperties(resourceDir);

        assertThat(properties.setAccessKey(null), is(true));
        assertThat(properties.setSecretKey(null), is(true));
        assertThat(properties.setVaultKey(null), is(true));

        assertThat(properties.getAccessKey(), is(""));
        assertThat(properties.getSecretKey(), is(""));
        assertThat(properties.getVaultKey(), is(""));

        assertThat(properties.setAccessKey(null), is(false));
        assertThat(properties.setSecretKey(null), is(false));
        assertThat(properties.setVaultKey(null), is(false));
    }

    @Test
    public void settersShouldTrimStrings() throws Exception {
        final AppProperties properties = new AppProperties(resourceDir);

        assertThat(properties.setAccessKey(" AC "), is(true));
        assertThat(properties.setSecretKey(" SE ".toCharArray()), is(true));
        assertThat(properties.setVaultKey(" VA "), is(true));

        assertThat(properties.getAccessKey(), is("AC"));
        assertThat(properties.getSecretKey(), is("SE"));
        assertThat(properties.getVaultKey(), is("VA"));

        assertThat(properties.setAccessKey("AC"), is(false));
        assertThat(properties.setSecretKey("SE".toCharArray()), is(false));
        assertThat(properties.setVaultKey("VA"), is(false));
    }

    @Test
    public void savePropertiesShouldWriteThemToFile() throws Exception {
        final String tempDir = Files.createTempDirectory("sagu-test-").toString();
        final AppProperties tempProperties = new AppProperties(tempDir);
        tempProperties.setAccessKey("AC");
        tempProperties.setSecretKey("SE".toCharArray());
        tempProperties.setVaultKey("VA");
        tempProperties.setLocationIndex(3);
        tempProperties.setLogTypeIndex(4);
        tempProperties.saveProperties();

        final Properties savedProp = new Properties();
        savedProp.load(new FileInputStream(tempDir + System.getProperty("file.separator") + "SAGU.properties"));
        assertThat(savedProp.getProperty("accessKey"), is("AC"));
        assertThat(savedProp.getProperty("secretKey"), is("SE"));
        assertThat(savedProp.getProperty("vaultKey"), is("VA"));
        assertThat(savedProp.getProperty("locationSet"), is("3"));
        assertThat(savedProp.getProperty("logType"), is("4"));
    }

}