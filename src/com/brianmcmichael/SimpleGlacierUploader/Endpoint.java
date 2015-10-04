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

package com.brianmcmichael.SimpleGlacierUploader;

public enum Endpoint {
    USEASTNVA("us-east-1"),
    USWESTOR("us-west-2"),
    USWESTNCA("us-west-1"),
    EUIRELAND("eu-west-1"),
    APTOKYO("ap-northeast-1");

    Endpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    private final String endpoint;

    public String getGlacerEndpoint() {
        return "https://glacier." + endpoint + ".amazonaws.com/";
    }

    public String getSQSEndpoint() {
        return "https://sqs." + endpoint + ".amazonaws.com/";
    }

    public String getSNSEndpoint() {
        return "https://sns." + endpoint + ".amazonaws.com/";
    }
}
