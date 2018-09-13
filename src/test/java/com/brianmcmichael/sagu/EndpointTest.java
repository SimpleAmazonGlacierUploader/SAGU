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

import static com.brianmcmichael.sagu.Endpoint.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EndpointTest {

    @Test
    public void shouldReturnCorrectInstanceByIndex() {
        assertThat(Endpoint.getByIndex(0), is(US_EAST_NVIRGINIA));
        assertThat(Endpoint.getByIndex(1), is(US_WEST_OREGON));
        assertThat(Endpoint.getByIndex(2), is(US_WEST_NCALIFORNIA));
        assertThat(Endpoint.getByIndex(3), is(EU_WEST_IRELAND));
        assertThat(Endpoint.getByIndex(4), is(AP_NORTHEAST_TOKYO));
        assertThat(Endpoint.getByIndex(5), is(AP_SOUTHEAST_SYDNEY));
        assertThat(Endpoint.getByIndex(6), is(EU_CENTRAL_FRANKFURT));
        assertThat(Endpoint.getByIndex(7), is(EU_WEST_LONDON));
        assertThat(Endpoint.getByIndex(8), is(EU_WEST_PARIS));
        assertThat(Endpoint.getByIndex(9), is(AP_NORTHEAST_SEOUL));
        assertThat(Endpoint.getByIndex(10), is(AP_NORTHEAST_OSAKA_LOCAL));
        assertThat(Endpoint.getByIndex(11), is(AP_SOUTH_MUMBAI));
        assertThat(Endpoint.getByIndex(12), is(AP_SOUTHEAST_SINGAPORE));
        assertThat(Endpoint.getByIndex(13), is(CN_NORTH_BEIJING));
        assertThat(Endpoint.getByIndex(14), is(CN_NORTHWEST_NINGXIA));
        assertThat(Endpoint.getByIndex(15), is(CA_CENTRAL));
        assertThat(Endpoint.getByIndex(16), is(US_EAST_OHIO));
        assertThat(Endpoint.getByIndex(17), is(US_GOV_WEST));
    }

    @Test
    public void getRegionTitleShouldReturnCorrectValues() {
        assertThat(getTitleByIndex(0), is("US East (Northern Virginia)"));
        assertThat(getTitleByIndex(1), is("US West (Oregon)"));
        assertThat(getTitleByIndex(2), is("US West (Northern California)"));
        assertThat(getTitleByIndex(3), is("EU (Ireland)"));
        assertThat(getTitleByIndex(4), is("Asia Pacific (Tokyo)"));
        assertThat(getTitleByIndex(5), is("Asia Pacific (Sydney)"));
        assertThat(getTitleByIndex(6), is("EU (Frankfurt)"));
        assertThat(getTitleByIndex(7), is("EU (London)"));
        assertThat(getTitleByIndex(8), is("EU (Paris)"));
        assertThat(getTitleByIndex(9), is("Asia Pacific (Seoul)"));
        assertThat(getTitleByIndex(10), is("Asia Pacific (Osaka-Local)"));
        assertThat(getTitleByIndex(11), is("Asia Pacific (Mumbai)"));
        assertThat(getTitleByIndex(12), is("Asia Pacific (Singapore)"));
        assertThat(getTitleByIndex(13), is("China (Beijing)"));
        assertThat(getTitleByIndex(14), is("China (Ningxia)"));
        assertThat(getTitleByIndex(15), is("Canada (Central)"));
        assertThat(getTitleByIndex(16), is("US East (Ohio)"));
        assertThat(getTitleByIndex(17), is("AWS GovCloud (US)"));
    }

    @Test
    public void shouldPopulateComboBoxWithValuesInCorrectOrder() {
        final JComboBox<String> comboBox = new JComboBox<>();
        assertThat(comboBox.getItemCount(), is(0));

        Endpoint.populateComboBox(comboBox);
        assertThat(comboBox.getItemCount(), is(18));
        assertThat(comboBox.getItemAt(0), is("US East (Northern Virginia)"));
        assertThat(comboBox.getItemAt(1), is("US West (Oregon)"));
        assertThat(comboBox.getItemAt(2), is("US West (Northern California)"));
        assertThat(comboBox.getItemAt(3), is("EU (Ireland)"));
        assertThat(comboBox.getItemAt(4), is("Asia Pacific (Tokyo)"));
        assertThat(comboBox.getItemAt(5), is("Asia Pacific (Sydney)"));
        assertThat(comboBox.getItemAt(6), is("EU (Frankfurt)"));
        assertThat(comboBox.getItemAt(7), is("EU (London)"));
        assertThat(comboBox.getItemAt(8), is("EU (Paris)"));
        assertThat(comboBox.getItemAt(9), is("Asia Pacific (Seoul)"));
        assertThat(comboBox.getItemAt(10), is("Asia Pacific (Osaka-Local)"));
        assertThat(comboBox.getItemAt(11), is("Asia Pacific (Mumbai)"));
        assertThat(comboBox.getItemAt(12), is("Asia Pacific (Singapore)"));
        assertThat(comboBox.getItemAt(13), is("China (Beijing)"));
        assertThat(comboBox.getItemAt(14), is("China (Ningxia)"));
        assertThat(comboBox.getItemAt(15), is("Canada (Central)"));
        assertThat(comboBox.getItemAt(16), is("US East (Ohio)"));
        assertThat(comboBox.getItemAt(17), is("AWS GovCloud (US)"));
    }

    @Test
    public void getGlacierEndpointShouldReturnCorrectAddress() {
        assertThat(US_EAST_NVIRGINIA.getGlacierEndpoint(), is("https://glacier.us-east-1.amazonaws.com/"));
        assertThat(US_WEST_OREGON.getGlacierEndpoint(), is("https://glacier.us-west-2.amazonaws.com/"));
        assertThat(US_WEST_NCALIFORNIA.getGlacierEndpoint(), is("https://glacier.us-west-1.amazonaws.com/"));
        assertThat(EU_WEST_IRELAND.getGlacierEndpoint(), is("https://glacier.eu-west-1.amazonaws.com/"));
        assertThat(AP_NORTHEAST_TOKYO.getGlacierEndpoint(), is("https://glacier.ap-northeast-1.amazonaws.com/"));
        assertThat(AP_SOUTHEAST_SYDNEY.getGlacierEndpoint(), is("https://glacier.ap-southeast-2.amazonaws.com/"));
        assertThat(EU_CENTRAL_FRANKFURT.getGlacierEndpoint(), is("https://glacier.eu-central-1.amazonaws.com/"));
        assertThat(EU_WEST_LONDON.getGlacierEndpoint(), is("https://glacier.eu-west-2.amazonaws.com/"));
        assertThat(EU_WEST_PARIS.getGlacierEndpoint(), is("https://glacier.eu-west-3.amazonaws.com/"));
        assertThat(AP_NORTHEAST_SEOUL.getGlacierEndpoint(), is("https://glacier.ap-northeast-2.amazonaws.com/"));
        assertThat(AP_NORTHEAST_OSAKA_LOCAL.getGlacierEndpoint(), is("https://glacier.ap-northeast-3.amazonaws.com/"));
        assertThat(AP_SOUTH_MUMBAI.getGlacierEndpoint(), is("https://glacier.ap-south-1.amazonaws.com/"));
        assertThat(AP_SOUTHEAST_SINGAPORE.getGlacierEndpoint(), is("https://glacier.ap-southeast-1.amazonaws.com/"));
        assertThat(CN_NORTH_BEIJING.getGlacierEndpoint(), is("https://glacier.cn-north-1.amazonaws.com/"));
        assertThat(CN_NORTHWEST_NINGXIA.getGlacierEndpoint(), is("https://glacier.cn-northwest-1.amazonaws.com/"));
        assertThat(CA_CENTRAL.getGlacierEndpoint(), is("https://glacier.ca-central-1.amazonaws.com/"));
        assertThat(US_EAST_OHIO.getGlacierEndpoint(), is("https://glacier.us-east-2.amazonaws.com/"));
        assertThat(US_GOV_WEST.getGlacierEndpoint(), is("https://glacier.us-gov-west-1.amazonaws.com/"));
    }

    @Test
    public void getSQSEndpointShouldReturnCorrectAddress() {
        assertThat(US_EAST_NVIRGINIA.getSQSEndpoint(), is("https://sqs.us-east-1.amazonaws.com/"));
        assertThat(US_WEST_OREGON.getSQSEndpoint(), is("https://sqs.us-west-2.amazonaws.com/"));
        assertThat(US_WEST_NCALIFORNIA.getSQSEndpoint(), is("https://sqs.us-west-1.amazonaws.com/"));
        assertThat(EU_WEST_IRELAND.getSQSEndpoint(), is("https://sqs.eu-west-1.amazonaws.com/"));
        assertThat(AP_NORTHEAST_TOKYO.getSQSEndpoint(), is("https://sqs.ap-northeast-1.amazonaws.com/"));
        assertThat(AP_SOUTHEAST_SYDNEY.getSQSEndpoint(), is("https://sqs.ap-southeast-2.amazonaws.com/"));
        assertThat(EU_CENTRAL_FRANKFURT.getSQSEndpoint(), is("https://sqs.eu-central-1.amazonaws.com/"));
        assertThat(EU_WEST_LONDON.getSQSEndpoint(), is("https://sqs.eu-west-2.amazonaws.com/"));
        assertThat(EU_WEST_PARIS.getSQSEndpoint(), is("https://sqs.eu-west-3.amazonaws.com/"));
        assertThat(AP_NORTHEAST_SEOUL.getSQSEndpoint(), is("https://sqs.ap-northeast-2.amazonaws.com/"));
        assertThat(AP_NORTHEAST_OSAKA_LOCAL.getSQSEndpoint(), is("https://sqs.ap-northeast-3.amazonaws.com/"));
        assertThat(AP_SOUTH_MUMBAI.getSQSEndpoint(), is("https://sqs.ap-south-1.amazonaws.com/"));
        assertThat(AP_SOUTHEAST_SINGAPORE.getSQSEndpoint(), is("https://sqs.ap-southeast-1.amazonaws.com/"));
        assertThat(CN_NORTH_BEIJING.getSQSEndpoint(), is("https://sqs.cn-north-1.amazonaws.com/"));
        assertThat(CN_NORTHWEST_NINGXIA.getSQSEndpoint(), is("https://sqs.cn-northwest-1.amazonaws.com/"));
        assertThat(CA_CENTRAL.getSQSEndpoint(), is("https://sqs.ca-central-1.amazonaws.com/"));
        assertThat(US_EAST_OHIO.getSQSEndpoint(), is("https://sqs.us-east-2.amazonaws.com/"));
        assertThat(US_GOV_WEST.getSQSEndpoint(), is("https://sqs.us-gov-west-1.amazonaws.com/"));
    }

    @Test
    public void getSNSEndpointShouldReturnCorrectAddress() {
        assertThat(US_EAST_NVIRGINIA.getSNSEndpoint(), is("https://sns.us-east-1.amazonaws.com/"));
        assertThat(US_WEST_OREGON.getSNSEndpoint(), is("https://sns.us-west-2.amazonaws.com/"));
        assertThat(US_WEST_NCALIFORNIA.getSNSEndpoint(), is("https://sns.us-west-1.amazonaws.com/"));
        assertThat(EU_WEST_IRELAND.getSNSEndpoint(), is("https://sns.eu-west-1.amazonaws.com/"));
        assertThat(AP_NORTHEAST_TOKYO.getSNSEndpoint(), is("https://sns.ap-northeast-1.amazonaws.com/"));
        assertThat(AP_SOUTHEAST_SYDNEY.getSNSEndpoint(), is("https://sns.ap-southeast-2.amazonaws.com/"));
        assertThat(EU_CENTRAL_FRANKFURT.getSNSEndpoint(), is("https://sns.eu-central-1.amazonaws.com/"));
        assertThat(EU_WEST_LONDON.getSNSEndpoint(), is("https://sns.eu-west-2.amazonaws.com/"));
        assertThat(EU_WEST_PARIS.getSNSEndpoint(), is("https://sns.eu-west-3.amazonaws.com/"));
        assertThat(AP_NORTHEAST_SEOUL.getSNSEndpoint(), is("https://sns.ap-northeast-2.amazonaws.com/"));
        assertThat(AP_NORTHEAST_OSAKA_LOCAL.getSNSEndpoint(), is("https://sns.ap-northeast-3.amazonaws.com/"));
        assertThat(AP_SOUTH_MUMBAI.getSNSEndpoint(), is("https://sns.ap-south-1.amazonaws.com/"));
        assertThat(AP_SOUTHEAST_SINGAPORE.getSNSEndpoint(), is("https://sns.ap-southeast-1.amazonaws.com/"));
        assertThat(CN_NORTH_BEIJING.getSNSEndpoint(), is("https://sns.cn-north-1.amazonaws.com/"));
        assertThat(CN_NORTHWEST_NINGXIA.getSNSEndpoint(), is("https://sns.cn-northwest-1.amazonaws.com/"));
        assertThat(CA_CENTRAL.getSNSEndpoint(), is("https://sns.ca-central-1.amazonaws.com/"));
        assertThat(US_EAST_OHIO.getSNSEndpoint(), is("https://sns.us-east-2.amazonaws.com/"));
        assertThat(US_GOV_WEST.getSNSEndpoint(), is("https://sns.us-gov-west-1.amazonaws.com/"));
    }
}