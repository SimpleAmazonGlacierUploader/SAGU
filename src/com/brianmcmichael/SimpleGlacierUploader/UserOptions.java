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

public class UserOptions {

	private String accessKey;
	private char[] secretKey;
	private Endpoint serverLocation;
	private String vault;
	
	public UserOptions(String accessKey, char[] secretKey, Endpoint serverLocation, String vaultName)
	{
		this.setAccessKey(accessKey);
		this.setSecretKey(secretKey);
		this.setServerLocation(serverLocation);
		this.setVault(vaultName);
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public char[] getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(char[] secretKey) {
		this.secretKey = secretKey;
	}

	public Endpoint getServerLocation() {
		return serverLocation;
	}

	public void setServerLocation(Endpoint serverLocation) {
		this.serverLocation = serverLocation;
	}

	public String getVault() {
		return vault;
	}

	public void setVault(String vault) {
		this.vault = vault;
	}
}
