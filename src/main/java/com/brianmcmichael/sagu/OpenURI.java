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

import java.awt.Desktop;
import java.net.URI;

public class OpenURI {

    public static void open(String url) {
        if (!Desktop.isDesktopSupported()) {
            System.err.println("Desktop is not supported (fatal)");
            System.exit(1);
        }

        if (url.length() == 0) {
            System.out.println("Usage: OpenURI [URI [URI ... ]]");
            System.exit(0);
        }

        Desktop desktop = Desktop.getDesktop();

        if (!desktop.isSupported(Desktop.Action.BROWSE)) {
            System.err.println("Desktop doesn't support the browse action (fatal)");
            System.exit(1);
        }

        try {
            URI uri = new URI(url);
            desktop.browse(uri);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
