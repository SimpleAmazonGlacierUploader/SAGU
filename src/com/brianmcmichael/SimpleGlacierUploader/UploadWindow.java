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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UploadWindow {
	
	JTextArea uploadText = new JTextArea();
	JScrollPane uploadScroll = new JScrollPane(uploadText);
	JFrame uploadFrame = new JFrame("Uploading"); 
	{		
		uploadFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		final JProgressBar dumJProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
			dumJProgressBar.setIndeterminate(true);
		uploadFrame.add(dumJProgressBar, BorderLayout.NORTH);
		uploadFrame.add(uploadScroll, BorderLayout.CENTER);
		uploadFrame.setSize(500, 400);
		uploadText.setEditable(false);
		centerDefineFrame(uploadFrame, 500, 400);
	}
	
	public UploadWindow()
	{
		uploadFrame.setVisible(true);
	}
	
    public void destroyUploadWindow()
    {
    	uploadFrame.dispose();
    }
    
    void centerDefineFrame (JFrame f, int width, int height) 
    {
	    
	    Toolkit tk = Toolkit.getDefaultToolkit ();

	    // Get the screen dimensions.
	    Dimension screen = tk.getScreenSize ();

	    //Set frame size
	    f.setSize (width,height);

	    // And place it in center of screen.
	    int lx =  (int) (screen.getWidth ()  * 3/8);
	    int ly =  (int) (screen.getHeight () * 3/8);
	    f.setLocation (lx,ly);
	  } // centerFrame

}
