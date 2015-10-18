package com.brianmcmichael.sagu;

import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class SAGUUtilsTest {
    @Test
    public void loadVersionNumberShouldReturnVersionString() throws Exception {
        final String version = SAGUUtils.loadVersionNumber();
        assertThat(version, is(notNullValue()));
        assertThat(version.matches("\\d+\\.\\d+\\.\\d+(-SNAPSHOT)?"), is(true));
    }
}