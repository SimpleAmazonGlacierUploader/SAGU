//Copyright 2012 Brian L. McMichael 
//               brianmcmichael.com

package com.brianmcmichael.SimpleGlacierUploader;

import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.Date;

import javax.swing.JFileChooser;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.glacier.*;
import com.amazonaws.services.glacier.model.UploadArchiveRequest;
import com.amazonaws.services.glacier.model.UploadArchiveResult;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;

public class SimpleGlacierUploader extends Frame implements ActionListener
{
	File uploadFile = null;
	
	Panel titlePanel = new Panel();
		Label titleLabel = new Label("Simple Amazon Glacier Uploader 1.0");
	
	Panel inputPanel = new Panel();
		Label accessLabel = new Label("AWS Access Key: ");
		TextField accessField = new TextField(25);
		Label secretLabel = new Label("AWS Secret Key: ");
		TextField secretField = new TextField(50);
		Label vaultName = new Label("Vault Name: ");
		TextField vaultField = new TextField(25);
		Label fileLabel = new Label("File to upload: ");
		Button selectFile = new Button("Select File");
		Label selectedLabel = new Label("");
		Button uploadButton = new Button("Upload");
		
	JFileChooser fc = new JFileChooser();
		
	
	Panel copyrightPanel = new Panel();
		Label copyrightLabel = new Label("©2012 brian@brianmcmichael.com");
	
	public SimpleGlacierUploader()
	{
		this.setLayout(new BorderLayout());
			titlePanel.setLayout(new FlowLayout());
			inputPanel.setLayout(new GridLayout(5,2,40,40));
			copyrightPanel.setLayout(new FlowLayout());
			
		titlePanel.add(titleLabel);
		
		inputPanel.add(accessLabel);
		inputPanel.add(accessField);
		inputPanel.add(secretLabel);
		inputPanel.add(secretField);
		inputPanel.add(vaultName);
		inputPanel.add(vaultField);
		inputPanel.add(fileLabel);
		inputPanel.add(selectFile);
		selectFile.addActionListener(this);
		inputPanel.add(selectedLabel);
		inputPanel.add(uploadButton);
		uploadButton.addActionListener(this);
		
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			
		copyrightPanel.add(copyrightLabel);	
			
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
		g.setBounds(300,300,600,400);
		g.setTitle("Simple Glacier Uploader");
		g.setVisible(true);
	} //end of main

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
				    String archiveToUpload = uploadFile.toString();
				    
				    AmazonGlacierClient client;
				    
				    BasicAWSCredentials credentials = new BasicAWSCredentials(accessField.getText().trim(),secretField.getText().trim());	        
				        //AWSCredentials credentials = new PropertiesCredentials(
				        //        SimpleGlacierUploader.class.getResourceAsStream("AwsCredentials.properties"));
				        client = new AmazonGlacierClient(credentials);
				        
				        client.setEndpoint("https://glacier.us-east-1.amazonaws.com/");

				        try {
				            ArchiveTransferManager atm = new ArchiveTransferManager(client, credentials);
				            
				            UploadResult result = atm.upload(vaultName, "-", uploadFile);
				            //UploadResult result = atm.upload(vaultName, "my archive " + (new Date()), new File(archiveToUpload));
				            //System.out.println("Archive ID: " + result.getArchiveId());
				            JOptionPane.showMessageDialog(null,"Archive ID: " + result.getArchiveId(), "Uploaded", JOptionPane.ERROR_MESSAGE);
				            
				        } catch (Exception h)
				        {
				        	JOptionPane.showMessageDialog(null,""+h, "Error", JOptionPane.ERROR_MESSAGE);
				        }
				   
					

					
				}
				
			}
			
		}
		
	}
