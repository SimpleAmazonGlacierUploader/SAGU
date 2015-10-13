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

public class S {

    //static identfiers
    private static final long serialVersionUID = 11041980L;
    private static final String versionNumber = "0.74.5";
    private static final String logFileNamelog = "Glacier.log";
    private static final String logFileNametxt = "Glacier.txt";
    private static final String logFileNamecsv = "Glacier.csv";
    private static final String logFileNameerr = "GlacierErrors.txt";
    private static final String fileProperties = "SAGU.properties";

    //Server Region Strings
    private static final String regionOne = "US East (Northern Virginia)";
    private static final String regionTwo = "US West (Oregon)";
    private static final String regionThree = "US West (Northern California)";
    private static final String regionFour = "EU (Ireland)";
    private static final String regionFive = "Asia Pacific (Tokyo)";

    public static final String curDir = System.getProperty("user.dir");

    //Error messages
    private static final String NO_DIRECTORIES_ERROR = "Directories, folders, and packages are not supported. \nPlease compress this into a single archive (such as a .zip) and try uploading again.";
    private static final String LOG_CREATION_ERROR = "There was an error creating the log.";
    private static final String LOG_WRITE_ERROR = "There was an error writing to the log.";

    //Sites
    public static final String GET_AWS_CREDENTIALS = "https://portal.aws.amazon.com/gp/aws/securityCredentials";
    public static final String BLM_URL_STRING = "http://simpleglacieruploader.brianmcmichael.com/";

    //Other Strings
    public static final String DOWNLOAD_STRING = "Download Archive";
    public static final String INVENTORY_REQUEST_STRING = "Request Inventory";
    public static final String COPYRIGHT_STRING = "Simple Amazon Glacier Uploader\nVersion " + versionNumber + "\n ©2012-2013 brian@brianmcmichael.com";
    public static final String UPDATE_STRING = "Check for Update";
    public static final String UPDATE_SITE_STRING = "http://simpleglacieruploader.brianmcmichael.com/";
    public static final String ABOUT_WINDOW_STRING = "" + COPYRIGHT_STRING + "\n\nReport errors or direct correspondence to: brian@brianmcmichael.com\n\nSimple Amazon Glacier Uploader is free and always will be. \nYour feedback is appreciated.\nThis program is not any way affiliated with Amazon Web Services or Amazon.com.";
    public static final String AWS_SITE_STRING = "Get AWS Credentials";
    public static final String ACCESS_LABEL = "Access Key: ";


}
