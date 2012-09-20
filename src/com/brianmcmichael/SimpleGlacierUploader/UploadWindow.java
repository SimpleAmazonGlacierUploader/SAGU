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
