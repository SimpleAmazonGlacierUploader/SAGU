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
	
	public String Endpoint(int end)
	{
		String endString = "";
		
        if(end == 0)
        {
        	endString = "https://glacier.us-east-1.amazonaws.com/";		        	
        }
        if(end == 1)
        {
        	endString = "https://glacier.us-west-2.amazonaws.com/";		        	
        }
        if(end == 2)
        {
        	endString = "https://glacier.us-west-1.amazonaws.com/";		        	
        }
        if(end == 3)
        {
        	endString = "https://glacier.eu-west-1.amazonaws.com/";
        }
        if(end == 4)
        {
        	endString = "https://glacier.ap-northeast-1.amazonaws.com/";
        }        
        
        return endString;
	}
	
	public String Location(int loc)
	{
		String locString = "";
		
        if(loc == 0)
        {
        	locString = "USEASTNVA";		        	
        }
        if(loc == 1)
        {
        	locString = "USWESTOR";		        	
        }
        if(loc == 2)
        {
        	locString = "USWESTNCA";		        	
        }
        if(loc == 3)
        {
        	locString = "EUIRELAND";
        }
        if(loc == 4)
        {
        	locString = "APTOKYO";
        }        
        
        return locString;
	}

}
