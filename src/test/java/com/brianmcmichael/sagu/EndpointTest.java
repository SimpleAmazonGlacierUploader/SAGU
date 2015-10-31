/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu;

import org.testng.annotations.Test;

import javax.swing.*;

import static com.brianmcmichael.sagu.Endpoint.AP_NORTHEAST_TOKYO;
import static com.brianmcmichael.sagu.Endpoint.AP_SOUTHEAST_SYDNEY;
import static com.brianmcmichael.sagu.Endpoint.EU_CENTRAL_FRANKFURT;
import static com.brianmcmichael.sagu.Endpoint.EU_WEST_IRELAND;
import static com.brianmcmichael.sagu.Endpoint.US_EAST_NVIRGINIA;
import static com.brianmcmichael.sagu.Endpoint.US_WEST_NCALIFORNIA;
import static com.brianmcmichael.sagu.Endpoint.US_WEST_OREGON;
import static com.brianmcmichael.sagu.Endpoint.getTitleByIndex;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EndpointTest {

    @Test
    public void shouldReturnCorrectInstanceByIndex() throws Exception {
        assertThat(Endpoint.getByIndex(0), is(US_EAST_NVIRGINIA));
        assertThat(Endpoint.getByIndex(1), is(US_WEST_OREGON));
        assertThat(Endpoint.getByIndex(2), is(US_WEST_NCALIFORNIA));
        assertThat(Endpoint.getByIndex(3), is(EU_WEST_IRELAND));
        assertThat(Endpoint.getByIndex(4), is(AP_NORTHEAST_TOKYO));
        assertThat(Endpoint.getByIndex(5), is(AP_SOUTHEAST_SYDNEY));
        assertThat(Endpoint.getByIndex(6), is(EU_CENTRAL_FRANKFURT));
    }

    @Test
    public void getRegionTitleShouldReturnCorrectValues() throws Exception {
        assertThat(getTitleByIndex(0), is("US East (Northern Virginia)"));
        assertThat(getTitleByIndex(1), is("US West (Oregon)"));
        assertThat(getTitleByIndex(2), is("US West (Northern California)"));
        assertThat(getTitleByIndex(3), is("EU (Ireland)"));
        assertThat(getTitleByIndex(4), is("Asia Pacific (Tokyo)"));
        assertThat(getTitleByIndex(5), is("Asia Pacific (Sydney)"));
        assertThat(getTitleByIndex(6), is("EU (Frankfurt)"));
    }

    @Test
    public void shouldPopulateComboBoxWithValuesInCorrectOrder() throws Exception {
        final JComboBox<String> comboBox = new JComboBox<String>();
        assertThat(comboBox.getItemCount(), is(0));

        Endpoint.populateComboBox(comboBox);
        assertThat(comboBox.getItemCount(), is(7));
        assertThat(comboBox.getItemAt(0), is("US East (Northern Virginia)"));
        assertThat(comboBox.getItemAt(1), is("US West (Oregon)"));
        assertThat(comboBox.getItemAt(2), is("US West (Northern California)"));
        assertThat(comboBox.getItemAt(3), is("EU (Ireland)"));
        assertThat(comboBox.getItemAt(4), is("Asia Pacific (Tokyo)"));
        assertThat(comboBox.getItemAt(5), is("Asia Pacific (Sydney)"));
        assertThat(comboBox.getItemAt(6), is("EU (Frankfurt)"));
    }

    @Test
    public void getGlacierEndpointShouldReturnCorrectAddress() throws Exception {
        assertThat(US_EAST_NVIRGINIA.getGlacierEndpoint(), is("https://glacier.us-east-1.amazonaws.com/"));
        assertThat(US_WEST_OREGON.getGlacierEndpoint(), is("https://glacier.us-west-2.amazonaws.com/"));
        assertThat(US_WEST_NCALIFORNIA.getGlacierEndpoint(), is("https://glacier.us-west-1.amazonaws.com/"));
        assertThat(EU_WEST_IRELAND.getGlacierEndpoint(), is("https://glacier.eu-west-1.amazonaws.com/"));
        assertThat(AP_NORTHEAST_TOKYO.getGlacierEndpoint(), is("https://glacier.ap-northeast-1.amazonaws.com/"));
        assertThat(AP_SOUTHEAST_SYDNEY.getGlacierEndpoint(), is("https://glacier.ap-southeast-2.amazonaws.com/"));
        assertThat(EU_CENTRAL_FRANKFURT.getGlacierEndpoint(), is("https://glacier.eu-central-1.amazonaws.com/"));
    }

    @Test
    public void getSQSEndpointShouldReturnCorrectAddress() throws Exception {
        assertThat(US_EAST_NVIRGINIA.getSQSEndpoint(), is("https://sqs.us-east-1.amazonaws.com/"));
        assertThat(US_WEST_OREGON.getSQSEndpoint(), is("https://sqs.us-west-2.amazonaws.com/"));
        assertThat(US_WEST_NCALIFORNIA.getSQSEndpoint(), is("https://sqs.us-west-1.amazonaws.com/"));
        assertThat(EU_WEST_IRELAND.getSQSEndpoint(), is("https://sqs.eu-west-1.amazonaws.com/"));
        assertThat(AP_NORTHEAST_TOKYO.getSQSEndpoint(), is("https://sqs.ap-northeast-1.amazonaws.com/"));
        assertThat(AP_SOUTHEAST_SYDNEY.getSQSEndpoint(), is("https://sqs.ap-southeast-2.amazonaws.com/"));
        assertThat(EU_CENTRAL_FRANKFURT.getSQSEndpoint(), is("https://sqs.eu-central-1.amazonaws.com/"));
    }

    @Test
    public void getSNSEndpointShouldReturnCorrectAddress() throws Exception {
        assertThat(US_EAST_NVIRGINIA.getSNSEndpoint(), is("https://sns.us-east-1.amazonaws.com/"));
        assertThat(US_WEST_OREGON.getSNSEndpoint(), is("https://sns.us-west-2.amazonaws.com/"));
        assertThat(US_WEST_NCALIFORNIA.getSNSEndpoint(), is("https://sns.us-west-1.amazonaws.com/"));
        assertThat(EU_WEST_IRELAND.getSNSEndpoint(), is("https://sns.eu-west-1.amazonaws.com/"));
        assertThat(AP_NORTHEAST_TOKYO.getSNSEndpoint(), is("https://sns.ap-northeast-1.amazonaws.com/"));
        assertThat(AP_SOUTHEAST_SYDNEY.getSNSEndpoint(), is("https://sns.ap-southeast-2.amazonaws.com/"));
        assertThat(EU_CENTRAL_FRANKFURT.getSNSEndpoint(), is("https://sns.eu-central-1.amazonaws.com/"));
    }
}