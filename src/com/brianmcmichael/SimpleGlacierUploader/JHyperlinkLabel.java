///////////////////////////////////////////////////////////////////////////////////
//    Simple Amazon Glacier Uploader v0.51 - GUI upload and log for Amazon Glacier 
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
//		v0.1		Initial launch - basic upload functionality
//		v0.2		Added upload logging
//		v0.3		Right click context menus
//		v0.4		Delete button. Save Preferences.
//		v0.5		Cleaned up logs. Multifile upload.
//		v0.51		Better multifile upload. Better error handling.
//////////////////////////////////////////////////////////////////////////////////

package com.brianmcmichael.SimpleGlacierUploader;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

public class JHyperlinkLabel extends JLabel {
  private Color underlineColor = null;

  public JHyperlinkLabel(String label) {
    super(label);

    setForeground(Color.BLUE.darker());
    setCursor(new Cursor(Cursor.HAND_CURSOR));
    addMouseListener(new HyperlinkLabelMouseAdapter());
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    g.setColor(underlineColor == null ? getForeground() : underlineColor);

    Insets insets = getInsets();

    int left = insets.left;
    if (getIcon() != null)
      left += getIcon().getIconWidth() + getIconTextGap();

    g.drawLine(left, getHeight() - 1 - insets.bottom, (int) getPreferredSize().getWidth()
        - insets.right, getHeight() - 1 - insets.bottom);
  }

  public class HyperlinkLabelMouseAdapter extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      //System.out.println(getText());
    	
    	if (getText().equals("AWS Access Key: "))
    	{
    		OpenURI("https://portal.aws.amazon.com/gp/aws/securityCredentials");    		
    	}
    	else if (getText().equals("Vault Name: "))
    	{
    		OpenURI("https://console.aws.amazon.com/glacier/home");    		
    	}
    	else if (getText().equals(" View Log"))
    	{
    		//OpenURI(""+SimpleGlacierUploader.getLogFilenamePath(getLogFileType()).toURI());    		
    	}
    	else if (getText().equals("Check for Update"))
    	{
    		OpenURI("http://simpleglacieruploader.brianmcmichael.com/");    		
    	}
    }
  }

  	public static void OpenURI(String url) 
  	{

	        if( !java.awt.Desktop.isDesktopSupported() ) {

	            System.err.println( "Desktop is not supported (fatal)" );
	            System.exit( 1 );
	        }

	        if ( url.length() == 0 ) {

	            System.out.println( "Usage: OpenURI [URI [URI ... ]]" );
	            System.exit( 0 );
	        }

	        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

	        if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {

	            System.err.println( "Desktop doesn't support the browse action (fatal)" );
	            System.exit( 1 );
	        }

            try {

                java.net.URI uri = new java.net.URI( url );
                desktop.browse( uri );
            }
            catch ( Exception e ) {

                System.err.println( e.getMessage() );
            }
	        
	    }


  
  public Color getUnderlineColor() {
    return underlineColor;
  }

  public void setUnderlineColor(Color underlineColor) {
    this.underlineColor = underlineColor;
  }
}