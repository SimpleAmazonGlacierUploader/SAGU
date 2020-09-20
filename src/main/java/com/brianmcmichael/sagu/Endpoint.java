/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu;

import javax.swing.*;

/**
 * Amazon Endpoint(Region) helper enum. It preserves stable indexes of its values.
 * See https://docs.aws.amazon.com/general/latest/gr/rande.html#glacier_region for current list.
 */
public enum Endpoint {

    US_EAST_NVIRGINIA("us-east-1", "US East (Northern Virginia)"),
    US_WEST_OREGON("us-west-2", "US West (Oregon)"),
    US_WEST_NCALIFORNIA("us-west-1", "US West (Northern California)"),
    EU_WEST_IRELAND("eu-west-1", "EU (Ireland)"),
    AP_NORTHEAST_TOKYO("ap-northeast-1", "Asia Pacific (Tokyo)"),
    AP_SOUTHEAST_SYDNEY("ap-southeast-2", "Asia Pacific (Sydney)"),
    EU_CENTRAL_FRANKFURT("eu-central-1", "EU (Frankfurt)"),
    EU_WEST_LONDON("eu-west-2", "EU (London)"),
    EU_WEST_PARIS("eu-west-3", "EU (Paris)"),
    AP_NORTHEAST_SEOUL("ap-northeast-2", "Asia Pacific (Seoul)"),
    AP_NORTHEAST_OSAKA_LOCAL("ap-northeast-3", "Asia Pacific (Osaka-Local)"),
    AP_SOUTH_MUMBAI("ap-south-1", "Asia Pacific (Mumbai)"),
    AP_SOUTHEAST_SINGAPORE("ap-southeast-1", "Asia Pacific (Singapore)"),
    CN_NORTH_BEIJING("cn-north-1", "China (Beijing)"),
    CN_NORTHWEST_NINGXIA("cn-northwest-1", "China (Ningxia)"),
    CA_CENTRAL("ca-central-1", "Canada (Central)"),
    US_EAST_OHIO("us-east-2", "US East (Ohio)"),
    US_GOV_WEST("us-gov-west-1", "AWS GovCloud (US)"),
    SA_SAO_PAULO("sa-east-1","South America (São Paulo)");

    private final String id;
    private final String title;

    Endpoint(final String id, final String title) {
        this.id = id;
        this.title = title;
    }


    /**
     * Get Endpoint(Region) by its index.
     *
     * @param index index of the Endpoint(Region) to be returned
     * @return Endpoint(Region)
     */
    public static Endpoint getByIndex(final int index) {
        return values()[index];
    }

    /**
     * Get human readable title of Endpoint(Region) by its index.
     *
     * @param index index of the Endpoint(Region) to be returned
     * @return Endpoint(Region)
     */
    public static String getTitleByIndex(final int index) {
        return getByIndex(index).getTitle();
    }

    /**
     * Populate {@link JComboBox} with all Endpoints(Regions) preserving their indexes.
     *
     * @param comboBox ComboBox to be populated with values
     */
    public static void populateComboBox(final JComboBox<String> comboBox) {
        for (Endpoint endpoint : values()) {
            comboBox.addItem(endpoint.getTitle());
        }
    }

    /**
     * Get address of Glacier in this Endpoint(Region).
     *
     * @return address of Glacier in this Endpoint(Region)
     */
    public String getGlacierEndpoint() {
        return "https://glacier." + id + ".amazonaws.com/";
    }

    /**
     * Get address of SQS in this Endpoint(Region).
     *
     * @return address of SQS in this Endpoint(Region)
     */
    public String getSQSEndpoint() {
        return "https://sqs." + id + ".amazonaws.com/";
    }

    /**
     * Get address of SNS in this Endpoint(Region).
     *
     * @return address of SNS in this Endpoint(Region)
     */
    public String getSNSEndpoint() {
        return "https://sns." + id + ".amazonaws.com/";
    }

    /**
     * Get identifier of this Endpoint(Region).
     *
     * @return identifier of this Endpoint(Region)
     */
    public String getId() {
        return id;
    }

    /**
     * Get human readable title of this Endpoint(Region).
     *
     * @return human readable title of this Endpoint(Region)
     */
    public String getTitle() {
        return title;
    }
}
