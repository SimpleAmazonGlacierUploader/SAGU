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
