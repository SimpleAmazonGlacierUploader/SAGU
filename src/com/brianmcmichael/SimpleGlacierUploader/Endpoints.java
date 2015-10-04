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

public class Endpoints {

    private int endpt;

    /*
    public Endpoints()
    {
        this.setEndpt(0);
    }
    */
    public Endpoints(int currentEndpoint) {
        this.setEndpt(currentEndpoint);
    }

    public String Endpoint() {
        String endString = "";

        if (endpt == 0) {
            endString = "https://glacier.us-east-1.amazonaws.com/";
        }
        if (endpt == 1) {
            endString = "https://glacier.us-west-2.amazonaws.com/";
        }
        if (endpt == 2) {
            endString = "https://glacier.us-west-1.amazonaws.com/";
        }
        if (endpt == 3) {
            endString = "https://glacier.eu-west-1.amazonaws.com/";
        }
        if (endpt == 4) {
            endString = "https://glacier.ap-northeast-1.amazonaws.com/";
        }

        return endString;
    }

    public String Location() {
        String locString = "";

        if (endpt == 0) {
            locString = "USEASTNVA";
        }
        if (endpt == 1) {
            locString = "USWESTOR";
        }
        if (endpt == 2) {
            locString = "USWESTNCA";
        }
        if (endpt == 3) {
            locString = "EUIRELAND";
        }
        if (endpt == 4) {
            locString = "APTOKYO";
        }

        return locString;
    }

    public int getEndpt() {
        return endpt;
    }

    public void setEndpt(int endpt) {
        this.endpt = endpt;
    }

    public String sqsEndpoint() {
        String endString = "";

        if (endpt == 0) {
            endString = "https://sqs.us-east-1.amazonaws.com/";
        }
        if (endpt == 1) {
            endString = "https://sqs.us-west-2.amazonaws.com/";
        }
        if (endpt == 2) {
            endString = "https://sqs.us-west-1.amazonaws.com/";
        }
        if (endpt == 3) {
            endString = "https://sqs.eu-west-1.amazonaws.com/";
        }
        if (endpt == 4) {
            endString = "https://sqs.ap-northeast-1.amazonaws.com/";
        }

        return endString;
    }

    public String snsEndpoint() {
        String endString = "";

        if (endpt == 0) {
            endString = "https://sns.us-east-1.amazonaws.com/";
        }
        if (endpt == 1) {
            endString = "https://sns.us-west-2.amazonaws.com/";
        }
        if (endpt == 2) {
            endString = "https://sns.us-west-1.amazonaws.com/";
        }
        if (endpt == 3) {
            endString = "https://sns.eu-west-1.amazonaws.com/";
        }
        if (endpt == 4) {
            endString = "https://sns.ap-northeast-1.amazonaws.com/";
        }

        return endString;
    }

}
