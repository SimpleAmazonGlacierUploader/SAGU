//Copyright 2012 Brian L. McMichael 
//               brianmcmichael.com
//v0.1		Initial launch - basic upload functionality
//v0.2		Added upload logging
//v0.3		Right click context menus
//v0.4		Delete button. Save Preferences.

package com.brianmcmichael.SimpleGlacierUploader;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import java.awt.*;
import java.awt.event.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Properties;
//import java.util.prefs.Preferences;


import javax.swing.JFileChooser;


//import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.auth.PropertiesCredentials;

//import com.amazonaws.services.glacier.*;

import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;

public class SimpleGlacierUploader extends Frame implements ActionListener
{

	Properties applicationProps = new Properties();
	
	//static identfiers
	private static final long serialVersionUID = 1L;
	private static final String versionNumber = "0.4";
	private static final String fileName = "Glacier.log";
	private static final String fileProperties = "SAGU.properties";
	
	//parts for progress bar  v0.4
	static JFrame frmMain;
	static Container pane;
	static JButton btnDo;
	static JProgressBar barDo;

	//Data handling variables
	DataOutputStream output;
	
	//Right mouse click context listener
	ContextMenuMouseListener rmb = new ContextMenuMouseListener();
	
	
	Font f3= new Font("Helvetica",Font.BOLD,20);
	Font f4= new Font("Helvetica",Font.PLAIN,11);
	
	
	File uploadFile = null;

	
	Panel titlePanel = new Panel();
		Label titleLabel = new Label("Simple Amazon Glacier Uploader "+versionNumber);
	
	Panel inputPanel = new Panel();
		//Label accessLabel = new Label("AWS Access Key: ");  //v0.2
		JHyperlinkLabel accessLabel = new JHyperlinkLabel("AWS Access Key: ");  //v0.3
		TextField accessField = new TextField(25);
		Label secretLabel = new Label("AWS Secret Key: ");
		TextField secretField = new TextField(50);
		//Label vaultName = new Label("Vault Name: ");  //v0.2
		JHyperlinkLabel vaultName = new JHyperlinkLabel("Vault Name: ");  //v0.3
		TextField vaultField = new TextField(25);
		Label locationName = new Label("Upload Location: ");
		Choice locationChoice = new Choice();
		Label fileLabel = new Label("File to upload: ");
		Button selectFile = new Button("Select File");
		Label selectedLabel = new Label("");
		Label blankLabel1 = new Label(" ");
		Button uploadButton = new Button("Upload File");
		Label blankLabel2 = new Label(" ");
		Button deleteButton = new Button("Delete Archive");
		
		
		
	JFileChooser fc = new JFileChooser();
		
	
	Panel copyrightPanel = new Panel();
		Label copyrightLabel = new Label("©2012 brian@brianmcmichael.com");
		JHyperlinkLabel updateLink = new JHyperlinkLabel("\tCheck for Update");
		
	Panel logPanel = new Panel();
		JCheckBox logCheck = new JCheckBox("Log?");
		JHyperlinkLabel viewLog = new JHyperlinkLabel(" View Log");
	
	public SimpleGlacierUploader()
	{
		this.setLayout(new BorderLayout());
			titlePanel.setLayout(new FlowLayout());
			inputPanel.setLayout(new GridLayout(8,2,20,20));
			copyrightPanel.setLayout(new FlowLayout());
			logPanel.setLayout(new FlowLayout());
			
		titlePanel.add(titleLabel);
			titleLabel.setFont(f3);
		
		logPanel.add(logCheck);
		logPanel.add(viewLog);
			logCheck.setSelected(true);
		
		inputPanel.add(accessLabel);
		inputPanel.add(accessField);
			accessField.addMouseListener(rmb); 
		inputPanel.add(secretLabel);
		inputPanel.add(secretField);
			secretField.addMouseListener(rmb); 
		inputPanel.add(vaultName);
		inputPanel.add(vaultField);
			vaultField.addMouseListener(rmb); 
		inputPanel.add(locationName);
		inputPanel.add(locationChoice);
			locationChoice.add("US East (Northern Virginia) Region");
			locationChoice.add("US West (Oregon) Region");
			locationChoice.add("US West (Northern California) Region");
			locationChoice.add("EU (Ireland) Region");
			locationChoice.add("Asia Pacific (Tokyo) Region");
		inputPanel.add(fileLabel);
		inputPanel.add(selectFile);
			selectFile.addActionListener(this);
		inputPanel.add(selectedLabel);
		inputPanel.add(uploadButton);
			uploadButton.addActionListener(this);
			//uploadButton.addActionListener(new btnDoAction());
		inputPanel.add(blankLabel1);
		inputPanel.add(logPanel);
		inputPanel.add(blankLabel2);
		inputPanel.add(deleteButton);
			deleteButton.addActionListener(this);
		
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			
		copyrightPanel.add(copyrightLabel);
			copyrightLabel.setFont(f4);
		copyrightPanel.add(updateLink);
			updateLink.setFont(f4);
			
		//add panels to frame
		add(titlePanel, BorderLayout.NORTH);
		add(inputPanel, BorderLayout.CENTER);
		add(copyrightPanel, BorderLayout.SOUTH);
	
		addWindowListener(
				new WindowAdapter()
				{
					public void windowClosing(WindowEvent e)
					{
						System.exit(0);
					}
				}
			);

		// Load properties from last invocation
		FileInputStream in;
		try 
		{
			in = new FileInputStream("SAGU.properties");
			applicationProps.load(in);
			accessField.setText(applicationProps.getProperty("accessKey"));
			secretField.setText(applicationProps.getProperty("secretKey"));
			vaultField.setText(applicationProps.getProperty("vaultKey"));
			locationChoice.select(Integer.parseInt(applicationProps.getProperty("locationSet")));
			in.close();
		} catch (FileNotFoundException e1) {			
		} catch (IOException e1) {			
		}
			
	}
	
	//Main class
	public static void main(String[] args) throws Exception
	{
		SimpleGlacierUploader g = new SimpleGlacierUploader();
		g.setBounds(300,300,600,475);
		g.setTitle("Simple Glacier Uploader");
		g.setVisible(true);
	} //end of main
	
	public void clearFile()
	{
		uploadFile = null;
        selectedLabel.setText("");
	}
	
	public static String getFilename()
	{
		return fileName;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String accessString = accessField.getText().trim();
		String vaultString = vaultField.getText().trim();
		String secretString = secretField.getText().trim();
				
		if (e.getSource() == selectFile)
		{
			int returnVal = fc.showOpenDialog(SimpleGlacierUploader.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                uploadFile = fc.getSelectedFile();
                selectedLabel.setText(uploadFile.toString());
                
                //This is where a real application would open the file.
                
            } else {
                //log.append("Open command cancelled by user." + newline);
            }
            //log.setCaretPosition(log.getDocument().getLength());
		}
		
		if (e.getSource() == deleteButton)
		{
			
			if ((accessString.equals("")) || vaultString.equals("") || (secretString.equals("")))
			{
				JOptionPane.showMessageDialog(null,"You must complete all fields.", "Error", JOptionPane.ERROR_MESSAGE);
				if ((accessField.getText().trim().equals("")))
				{
					accessField.setFocusable(true);
					accessField.requestFocus();
				}
				else if ((secretField.getText().trim().equals("")))
				{
					secretField.setFocusable(true);
					secretField.requestFocus();
				}
				else if ((vaultField.getText().trim().equals("")))
				{
					vaultField.setFocusable(true);
					vaultField.requestFocus();
				}
			}
			else
			{
				AmazonGlacierClient client;
			    BasicAWSCredentials credentials = new BasicAWSCredentials(accessField.getText().trim(),secretField.getText().trim());	        
			    client = new AmazonGlacierClient(credentials);
		        if(locationChoice.getSelectedIndex() == 0)
		        {
		        	client.setEndpoint("https://glacier.us-east-1.amazonaws.com/");		        	
		        }
		        if(locationChoice.getSelectedIndex() == 1)
		        {
		        	client.setEndpoint("https://glacier.us-west-2.amazonaws.com/");		        	
		        }
		        if(locationChoice.getSelectedIndex() == 2)
		        {
		        	client.setEndpoint("https://glacier.us-west-1.amazonaws.com/");		        	
		        }
		        if(locationChoice.getSelectedIndex() == 3)
		        {
		        	client.setEndpoint("https://glacier.eu-west-1.amazonaws.com/");
		        }
		        if(locationChoice.getSelectedIndex() == 4)
		        {
		        	client.setEndpoint("https://glacier.ap-northeast-1.amazonaws.com/");
		        }
		        String vaultName = vaultField.getText().trim();
			    DeleteArchiveFrame delete = new DeleteArchiveFrame(client, vaultName);
			    delete.setVisible(true);
			}
		}
		
		if (e.getSource() == uploadButton)
		{
			if ((accessField.getText().trim().equals("")) || vaultField.getText().trim().equals("") || (secretField.getText().trim().equals("")))
			{
				JOptionPane.showMessageDialog(null,"You must complete all fields.", "Error", JOptionPane.ERROR_MESSAGE);
				if ((accessField.getText().trim().equals("")))
				{
					accessField.setFocusable(true);
					accessField.requestFocus();
				}
				else if ((secretField.getText().trim().equals("")))
				{
					secretField.setFocusable(true);
					secretField.requestFocus();
				}
				else if ((vaultField.getText().trim().equals("")))
				{
					vaultField.setFocusable(true);
					vaultField.requestFocus();
				}
			}
			else if (selectedLabel.getText().equals("") == true)
			{
				JOptionPane.showMessageDialog(null,"Please select a file.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				
					String vaultName = vaultField.getText().trim();

				    AmazonGlacierClient client;
				    BasicAWSCredentials credentials = new BasicAWSCredentials(accessField.getText().trim(),secretField.getText().trim());	        
				    client = new AmazonGlacierClient(credentials);
				    String locationUpped = ""; 
				        
				        if(locationChoice.getSelectedIndex() == 0)
				        {
				        	client.setEndpoint("https://glacier.us-east-1.amazonaws.com/");
				        	locationUpped = "USEASTNVA";
				        }
				        if(locationChoice.getSelectedIndex() == 1)
				        {
				        	client.setEndpoint("https://glacier.us-west-2.amazonaws.com/");
				        	locationUpped = "USWESTOR";
				        }
				        if(locationChoice.getSelectedIndex() == 2)
				        {
				        	client.setEndpoint("https://glacier.us-west-1.amazonaws.com/");
				        	locationUpped = "USWESTNCA";
				        }
				        if(locationChoice.getSelectedIndex() == 3)
				        {
				        	client.setEndpoint("https://glacier.eu-west-1.amazonaws.com/");
				        	locationUpped = "EUIRELAND";
				        }
				        if(locationChoice.getSelectedIndex() == 4)
				        {
				        	client.setEndpoint("https://glacier.ap-northeast-1.amazonaws.com/");
				        	locationUpped = "APTOKYO";
				        }
				        
				        //Save Current Settings to properties
				        FileOutputStream out;
						try 
						{
							out = new FileOutputStream(fileProperties);
							applicationProps.setProperty("accessKey", accessString);
							applicationProps.setProperty("secretKey", secretString);
							applicationProps.setProperty("vaultKey", vaultString);
							applicationProps.setProperty("locationSet", convertSimple(locationChoice.getSelectedIndex()));
							applicationProps.store(out, "Properties");
							out.close();
						} 
						catch (FileNotFoundException e1) {						} 
						catch (IOException e1) {						}
				     
				        try {
				            ArchiveTransferManager atm = new ArchiveTransferManager(client, credentials);
				            
				            UploadResult result = atm.upload(vaultName, "-", uploadFile);
				            
				            //write to file
				            if(logCheck.isSelected())
				            {
				            	try
				                {
				                	output = new DataOutputStream(new FileOutputStream(fileName, true));
				                }
				                catch(IOException ex)
				                {
				                	JOptionPane.showMessageDialog(null, "There was an error creating the log.","IO Error",JOptionPane.ERROR_MESSAGE);
				                	System.exit(1);
				                }
				            	
				            	try
				    			{
				            		
				            		Date d = new Date();
				            		
				            		String thisResult = result.getArchiveId();
				            		thisResult.replaceAll("Š", "");
				            		String thisFile = selectedLabel.getText();
				            		
				            		output.writeUTF("ArchiveID: " + thisResult);
				    				output.writeUTF(" | File: " + thisFile);
				    				output.writeUTF(" | Vault: " +vaultName);
				    				output.writeUTF(" | Location: " + locationUpped);
				    				output.writeUTF(" | Date: "+d.toString()+"\n\n");	    				
				    				
				    				JOptionPane.showMessageDialog(null,"Upload Complete! Archive ID logged to " + fileName + "\nArchive ID: " + thisResult+"\nAmazon updates their inventory every 24 hours.", "Uploaded", JOptionPane.INFORMATION_MESSAGE);
						            
				    			}
				    			catch(IOException c)
				    			{
				    				JOptionPane.showMessageDialog(null, "There was an error writing to the log.","IO Error",JOptionPane.ERROR_MESSAGE);
				    				System.exit(1);
				    			}
				            }
				            else
				            {
				            	JOptionPane.showMessageDialog(null,"Upload Complete!\nArchive ID: " + result.getArchiveId()+"\nIt may take some time for Amazon to update the inventory.", "Uploaded", JOptionPane.INFORMATION_MESSAGE);
					            
				            }
				            
				            clearFile();
				            
				            
				        } catch (Exception h)
				        {
				        	JOptionPane.showMessageDialog(null,""+h, "Error", JOptionPane.ERROR_MESSAGE);
				        }
				}
				
			}
			
		}
	
	public static String convertSimple(int i) {
	    return "" + i;
	}
	
	//The action
	public static class btnDoAction implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			new Thread(new thread1()).start(); //Start the thread
		}
	}

    
	public static class thread1 implements Runnable
	{
    	public void run()
    	{
    		for (int i=0; i<=100; i++)
    		{ //Progressively increment variable i
    			barDo.setValue(i); //Set value
    			barDo.repaint(); //Refresh graphics
    			try
    			{
    				Thread.sleep(50);
    			} //Sleep 50 milliseconds
    			catch (InterruptedException err)
    			{
    			}
    	    }
    	}
	}
	
}