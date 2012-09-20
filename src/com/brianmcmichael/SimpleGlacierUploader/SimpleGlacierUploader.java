//Copyright 2012 Brian L. McMichael 
//               brianmcmichael.com
//v0.1		Initial launch - basic upload functionality
//v0.2		Added upload logging
//v0.3		Right click context menus


package com.brianmcmichael.SimpleGlacierUploader;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Date;


import javax.swing.JFileChooser;


import com.amazonaws.auth.BasicAWSCredentials;

//import com.amazonaws.services.glacier.*;

import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;

public class SimpleGlacierUploader extends Frame implements ActionListener
{

	private static final long serialVersionUID = 1L;
	private static final String versionNumber = "0.3";
	private static final String fileName = "Glacier.log";
	
	

	DataOutputStream output;
	
	ContextMenuMouseListener rmb = new ContextMenuMouseListener();
	
	
	Font f3= new Font("Helvetica",Font.BOLD,20);
	Font f4= new Font("Helvetica",Font.PLAIN,10);
	
	
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
		Button uploadButton = new Button("Upload");
		Label blankLabel = new Label("");
		
		
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
			inputPanel.setLayout(new GridLayout(7,2,20,20));
			copyrightPanel.setLayout(new FlowLayout());
			logPanel.setLayout(new FlowLayout());
			
		titlePanel.add(titleLabel);
			titleLabel.setFont(f3);
		
		logPanel.add(logCheck);
		logPanel.add(viewLog);
			logCheck.setSelected(true);
		
		inputPanel.add(accessLabel);
		inputPanel.add(accessField);
			accessField.addMouseListener(new ContextMenuMouseListener()); 
		inputPanel.add(secretLabel);
		inputPanel.add(secretField);
			secretField.addMouseListener(new ContextMenuMouseListener()); 
		inputPanel.add(vaultName);
		inputPanel.add(vaultField);
			vaultField.addMouseListener(new ContextMenuMouseListener()); 
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
		inputPanel.add(blankLabel);
		inputPanel.add(logPanel);
		
		
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
			
	}
	
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

	private static void open(URI uri) {
	    if (Desktop.isDesktopSupported()) {
	      try {
	        Desktop.getDesktop().browse(uri);
	      } catch (IOException e) { /* TODO: error handling */ }
	    } else { /* TODO: error handling */ }
	  }
	
	public static String getFilename()
	{
		return fileName;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
				
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
		
		if (e.getSource() == uploadButton)
		{
			if ((accessField.getText().trim().equals("")) || vaultField.getText().trim().equals("") || (secretField.getText().trim().equals("")))
			{
				JOptionPane.showMessageDialog(null,"You must complete all fields.", "Error", JOptionPane.ERROR_MESSAGE);
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
				        //AWSCredentials credentials = new PropertiesCredentials(
				        //        SimpleGlacierUploader.class.getResourceAsStream("AwsCredentials.properties"));
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
				                	JOptionPane.showMessageDialog(null, "There was an error creating the log.","IO Error",JOptionPane.INFORMATION_MESSAGE);
				                	System.exit(1);
				                }
				            	
				            	try
				    			{
				            		
				            		Date d = new Date();
				            		
				            		output.writeUTF("ArchiveID: ");
				    				output.writeUTF(result.getArchiveId());
				    				output.writeUTF(" |  File: ");
				    				output.writeUTF(selectedLabel.getText());
				    				output.writeUTF(" | Vault: ");
				    				output.writeUTF(vaultName);
				    				output.writeUTF(" | Date: ");
				    				output.writeUTF(d.toString() +"\n\n");				    				
				    				
				    				JOptionPane.showMessageDialog(null,"Upload Complete! Archive ID logged to " + fileName + "\nArchive ID: " + result.getArchiveId()+"\n Amazon updates their inventory every 24 hours.", "Uploaded", JOptionPane.INFORMATION_MESSAGE);
						            
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
		
	}
