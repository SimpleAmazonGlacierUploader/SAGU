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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.CreateVaultRequest;
import com.amazonaws.services.glacier.model.CreateVaultResult;

	class AddVaultFrame extends JFrame implements ActionListener, WindowListener
	{

		private static final long serialVersionUID = 1L;

		//define instance variables
	    String addCode;

	    JTextField jtfAddField;
	    JButton jbtAdd, jbtBack;
	    
	    AmazonGlacierClient addClient;
	    int locationChoice;
	    String addVault;
	    
	    ContextMenuMouseListener rmb = new ContextMenuMouseListener();
	    
	    //Constructor
	    public AddVaultFrame(AmazonGlacierClient client, int region)
	    {	
	    	super("Add Vault");
	    	
	    	AmazonGlacierClient newVaultClient = client;
	    	int thisRegion = region;
	    	
	    	int width = 200;
			int height = 170;
			
			Color wc = Color.WHITE;
	    	
			this.addClient = client;
			
			JLabel label1 = new JLabel("Name of Vault to add to " + SimpleGlacierUploader.getRegion(thisRegion) + ":");
	        jtfAddField = new JTextField(30);
	        jbtAdd = new JButton("Add");
	        jbtBack = new JButton("Back");
	    	
	        JPanel p1 = new JPanel();
		        p1.setLayout(new FlowLayout());
		    	p1.add(label1);
		    	p1.setBackground(wc);
		    	
	    	    	
	    	JPanel p2 = new JPanel();
	    		p2.setLayout(new FlowLayout());
	    		p2.add(jtfAddField);
	    			jtfAddField.addMouseListener(rmb);
	    			jtfAddField.setFocusable(true);
	    		p2.setBackground(wc);
	    			
	    	JPanel p3 = new JPanel();
	    		p3.setLayout(new FlowLayout());
	    		p3.add(jbtAdd);
					jbtAdd.addActionListener(this);
					jbtAdd.setBackground(wc);
	    		p3.add(jbtBack);	
	    			jbtBack.addActionListener(this);
	    			jbtBack.setBackground(wc);
	    		p3.setBackground(wc);
	    			
	    	JPanel p4 = new JPanel();
	    		p4.setLayout(new BorderLayout());
	    		p4.add(p1, BorderLayout.NORTH);
	    		p4.add(p2, BorderLayout.CENTER);
	    		p4.add(p3, BorderLayout.SOUTH);
	    	
	    	setContentPane(p4);
	    	
			// Prepare for display
			pack();
			if( width < getWidth())				// prevent setting width too small
			   width = getWidth();
			if(height < getHeight())			// prevent setting height too small
				height = getHeight();
			centerOnScreen(width, height);
		    jtfAddField.setText("");
		    jtfAddField.requestFocus();
		    
	   }
	    
	    
	    
	  	public void centerOnScreen(int width, int height)
	  	{
	  	  int top, left, x, y;

	  	  // Get the screen dimension
	  	  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	  	  // Determine the location for the top left corner of the frame
	  	  x = (screenSize.width - width)/2;
	  	  y = (screenSize.height - height)/2;
	  	  left = (x < 0) ? 0 : x;
	  	  top = (y < 0) ? 0 : y;

		  this.setBounds(left, top, width, height);
	  	}
	  	
	  	
	  	
	    	
	  	
	    
		@Override
		public void windowActivated(WindowEvent arg0) {
			
			
		}
		@Override
		public void windowClosed(WindowEvent arg0) {
			
			
		}
		@Override
		public void windowClosing(WindowEvent arg0) {
			
			
		}
		@Override
		public void windowDeactivated(WindowEvent arg0) {
			
			
		}
		@Override
		public void windowDeiconified(WindowEvent arg0) {
			
			
		}
		@Override
		public void windowIconified(WindowEvent arg0) {
			
			
		}
		@Override
		public void windowOpened(WindowEvent arg0) {
			jtfAddField.setText("");
			jtfAddField.requestFocus();
			
		}
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(e.getSource() == jbtAdd)
	        {
				if ((jtfAddField.getText().trim().equals("")))
				{
					JOptionPane.showMessageDialog(null,"Enter the name of the vault to add.", "Error", JOptionPane.ERROR_MESSAGE);				
				}
				else
				{
						
					try {
						String vaultToAdd = jtfAddField.getText().trim();
						
						//TODO Limit to valid chars
						
						// Add the archive.						
						
						CreateVaultRequest cvreq = new CreateVaultRequest(vaultToAdd);
						
						CreateVaultResult cvres = new CreateVaultResult();
						cvres = addClient.createVault(cvreq);

						JOptionPane.showMessageDialog(null, "Added vault successfully.","Success",JOptionPane.INFORMATION_MESSAGE);
	                	this.dispose();
						
			        } 
					catch (AmazonServiceException k)
					{
						JOptionPane.showMessageDialog(null,"The server returned an error.", "Error", JOptionPane.ERROR_MESSAGE);
					}
					catch (AmazonClientException i) 
					{
			        	JOptionPane.showMessageDialog(null,"Client Error. Check that all fields are correct. Archive not deleted.", "Error", JOptionPane.ERROR_MESSAGE);
			        	
			        }
					catch (Exception j) 
					{
			        	JOptionPane.showMessageDialog(null,"Vault not Added. Unspecified Error.", "Error", JOptionPane.ERROR_MESSAGE);
			        }

				jtfAddField.setText("");
				jtfAddField.requestFocus();
				}
		        
	        }
	        else if(e.getSource() == jbtBack)
	        {        	
	        	this.setVisible(false);
	      	  	dispose();
			}
			else
	        {
	        	JOptionPane.showMessageDialog(this, "Please choose a valid action.");
			}
			
		}
		
		
}