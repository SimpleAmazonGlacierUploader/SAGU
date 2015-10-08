///////////////////////////////////////////////////////////////////////////////////
//    Simple Amazon Glacier Uploader - GUI upload and log for Amazon Glacier 
//    Copyright (C) 2012 Brian L. McMichael <brian@brianmcmichael.com>
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.
//////////////////////////////////////////////////////////////////////////////////

package com.brianmcmichael.sagu;

import javax.swing.*;

/**
 * Amazon Endpoint(Region) helper enum. It preserves stable indexes of it's values.
 */
public enum Endpoint {

    US_EAST_NVIRGINIA("us-east-1", "US East (Northern Virginia)"),
    US_WEST_OREGON("us-west-2", "US West (Oregon)"),
    US_WEST_NCALIFORNIA("us-west-1", "US West (Northern California)"),
    EU_WEST_IRELAND("eu-west-1", "EU (Ireland)"),
    AP_NORTHEAST_TOKYO("ap-northeast-1", "Asia Pacific (Tokyo)"),
    AP_SOUTHEAST_SYDNEY("ap-southeast-2", "Asia Pacific (Sydney)"),
    EU_CENTRAL_FRANKFURT("eu-central-1", "EU (Frankfurt)");

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
