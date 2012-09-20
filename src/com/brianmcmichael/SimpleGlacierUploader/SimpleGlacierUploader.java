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
//		v0.1		Initial launch - basic upload functionality
//		v0.2		Added upload logging
//		v0.3		Right click context menus
//		v0.4		Delete button. Save Preferences.
//		v0.5		Cleaned up logs. Multifile upload.
//		v0.51		Better multifile upload. Better error handling.
//		v0.52		Bug fixes.
//////////////////////////////////////////////////////////////////////////////////

package com.brianmcmichael.SimpleGlacierUploader;

//import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;


import java.awt.*;
import java.awt.event.*;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;



import javax.swing.JFileChooser;

import com.amazonaws.auth.BasicAWSCredentials;


import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;

public class SimpleGlacierUploader extends Frame implements ActionListener
{
	

	//Set Colors
	Color wc = Color.WHITE;

	//Set ints
	int width = 200;
	int height = 170;
	int fileInt = 0;
	
	//set longs
	long max_file_size = 4294967296L;
	
	//Call properties
	Properties applicationProps = new Properties();
	
	//static identfiers
	private static final long serialVersionUID = 1L;
	private static final String versionNumber = "0.52";
	private static final String logFileName = "Glacier.log";
	private static final String fileProperties = "SAGU.properties";
	public static final String curDir = System.getProperty("user.dir");
	
	//Error messages
	private static final String NO_DIRECTORIES_ERROR = "Directories, folders, and packages are not supported. \nPlease compress this into a single archive (such as a .zip) and try uploading again.";
	private static final String LOG_CREATION_ERROR = "There was an error creating the log.";
	private static final String FILE_TOO_BIG_ERROR = "Files over 4GB are currently unsuppoted. \nYou may want to split your upload into multiple archives. \nAmazon recommends files of 100mb at a time.";
	private static final String LOG_WRITE_ERROR = "There was an error writing to the log.";
	
	//parts for progress bar
	static JFrame frmMain;
	static Container pane;
	static JButton btnDo;
	static JProgressBar barDo;

	//Data handling variables
	DataOutputStream output;
	
	//Call Amazon Client
	AmazonGlacierClient client;
		
	//Right mouse click context listener
	ContextMenuMouseListener rmb = new ContextMenuMouseListener();
	
	//File array for multiupload
	File[] multiFiles;
	
	Font f3= new Font("Helvetica",Font.BOLD,20);
	Font f4= new Font("Helvetica",Font.PLAIN,11);
	
	File uploadFile = null;
	
	JPanel titlePanel = new JPanel();
		JLabel titleLabel = new JLabel("Simple Amazon Glacier Uploader "+versionNumber);
		
	JPanel selectionsPanel = new JPanel();
		Button selectFileButton = new Button("Select File");
		Button selectMultiButton = new Button("Multiple Files");
		
	
	JPanel inputPanel = new JPanel();
		//Label accessLabel = new Label("AWS Access Key: ");  //v0.2
		JHyperlinkLabel accessLabel = new JHyperlinkLabel("AWS Access Key: ");  //v0.3
		JTextField accessField = new JTextField(25);
		JLabel secretLabel = new JLabel("AWS Secret Key: ");
		JTextField secretField = new JTextField(50);
		//Label vaultName = new Label("Vault Name: ");  //v0.2
		JHyperlinkLabel vaultName = new JHyperlinkLabel("Vault Name: ");  //v0.3
		JTextField vaultField = new JTextField(25);
		JLabel locationName = new JLabel("Upload Location: ");
		Choice locationChoice = new Choice();
		JLabel fileLabel = new JLabel("File to upload: ");
		JLabel selectedLabel = new JLabel("");
		JLabel blankLabel1 = new JLabel(" ");
		Button uploadButton = new Button("Upload File");
		JLabel blankLabel2 = new JLabel(" ");
		Button deleteButton = new Button("Delete Archive");
		
		
		
	
	//Create dumb progressbar
    JFrame uploadFrame = new JFrame("Uploading"); {
    uploadFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    final JProgressBar dumJProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
    //aJProgressBar.setStringPainted(true);
    dumJProgressBar.setIndeterminate(true);
    uploadFrame.add(dumJProgressBar, BorderLayout.NORTH);
    uploadFrame.setSize(300, 50);}
    
    
	JFileChooser fc = new JFileChooser();
		
	
	JPanel copyrightPanel = new JPanel();
		JLabel copyrightLabel = new JLabel("©2012 brian@brianmcmichael.com");
		JHyperlinkLabel updateLink = new JHyperlinkLabel("\tCheck for Update");
		
	JPanel logPanel = new JPanel();
		JCheckBox logCheck = new JCheckBox("Log?");
		JHyperlinkLabel viewLog = new JHyperlinkLabel(" View Log");
		
	//Create Filedrop Components
	javax.swing.JFrame frame = new javax.swing.JFrame( "Drag and drop files here" );
    //javax.swing.border.TitledBorder dragBorder = new javax.swing.border.TitledBorder( "Drop files here:" );
    final javax.swing.JTextArea ddText = new javax.swing.JTextArea();
    final javax.swing.JButton ddClearButton = new javax.swing.JButton("Clear");
    final javax.swing.JButton ddButton = new javax.swing.JButton("OK");
	
            
        
           
        
	public SimpleGlacierUploader()
	{
		this.setLayout(new BorderLayout());
			titlePanel.setLayout(new FlowLayout());
			inputPanel.setLayout(new GridLayout(8,2,20,20));
			//inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
			copyrightPanel.setLayout(new FlowLayout());
			logPanel.setLayout(new FlowLayout());
			selectionsPanel.setLayout(new GridLayout(1,2,5,5));
			
			
		titlePanel.add(titleLabel);
			titleLabel.setFont(f3);
		titlePanel.setBackground(wc);
		
		logPanel.add(logCheck);
			logCheck.setBackground(wc);
		logPanel.add(viewLog);
			logCheck.setSelected(true);
		logPanel.setBackground(wc);
		
		selectionsPanel.add(selectFileButton);
			selectFileButton.addActionListener(this);
			selectFileButton.setBackground(wc);
		selectionsPanel.add(selectMultiButton);
			selectMultiButton.addActionListener(this);	
			selectMultiButton.setBackground(wc);
		selectionsPanel.setBackground(wc);
			
		
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
		inputPanel.add(selectionsPanel);
			
		//inputPanel.add(selectFile);
			//selectFile.addActionListener(this);
		inputPanel.add(selectedLabel);
		inputPanel.add(uploadButton);
			uploadButton.addActionListener(this);
			uploadButton.setBackground(wc);
		inputPanel.add(blankLabel1);
		inputPanel.add(logPanel);
		inputPanel.add(blankLabel2);
		inputPanel.add(deleteButton);
			deleteButton.addActionListener(this);
			deleteButton.setBackground(wc);
		inputPanel.setBackground(wc);
			
		
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			
		copyrightPanel.add(copyrightLabel);
			copyrightLabel.setFont(f4);
		copyrightPanel.add(updateLink);
			updateLink.setFont(f4);
		copyrightPanel.setBackground(wc);
			
		//add panels to frame
		add(titlePanel, BorderLayout.NORTH);
		add(inputPanel, BorderLayout.CENTER);
		add(copyrightPanel, BorderLayout.SOUTH);
		
		//Build FileDropComponents
		frame.getContentPane().add( 
            new javax.swing.JScrollPane( ddText ), 
            java.awt.BorderLayout.CENTER );
		ddText.setEditable(false);
		frame.getContentPane().add( 
	             ddClearButton , 
	            java.awt.BorderLayout.NORTH );
		ddClearButton.addActionListener(this);
		ddClearButton.setBackground(wc);
        frame.getContentPane().add( 
	             ddButton , 
	            java.awt.BorderLayout.SOUTH );
        ddButton.addActionListener(this);
        ddButton.setBackground(wc);
        
	
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
			in = new FileInputStream(getFilePropertiesPath());
			applicationProps.load(in);
			accessField.setText(applicationProps.getProperty("accessKey"));
			secretField.setText(applicationProps.getProperty("secretKey"));
			vaultField.setText(applicationProps.getProperty("vaultKey"));
			locationChoice.select(Integer.parseInt(applicationProps.getProperty("locationSet")));
			in.close();
		} catch (FileNotFoundException e1) {			
		} catch (IOException e1) {			
		}
		
		
		
		
		pack();
        if( width < getWidth())				// prevent setting width too small
 		   width = getWidth();
 		if(height < getHeight())			// prevent setting height too small
 			height = getHeight();
 		centerOnScreen(width, height); 
			
	}
	
	//Main class
	public static void main(String[] args) throws Exception
	{
		SimpleGlacierUploader g = new SimpleGlacierUploader();
		g.setBounds(300,300,600,475);
		g.setTitle("Simple Glacier Uploader");
		g.setVisible(true);
		g.setIconImage(Toolkit.getDefaultToolkit().getImage("img\\glaciericon.png"));
		
	} //end of main
	
	
	
	public static String getLogFilename()
	{
		return "" + logFileName;
	}
	
	public static File getLogFilenamePath()
	{
		File logFile = new File(curDir + System.getProperty("file.separator") + logFileName);
		return logFile;
	}
	
	public static String getFileProperties()
	{
		return "" + fileProperties;
	}
	
	public static File getFilePropertiesPath()
	{
		File propsFile = new File(curDir  + System.getProperty("file.separator") + fileProperties);
		return propsFile;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String accessString = getAccessField();
		String vaultString = getVaultField();
		String secretString = getSecretField();
				
		if (e.getSource() == selectFileButton)
		{
			clearFile();
			int returnVal = fc.showOpenDialog(SimpleGlacierUploader.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
            	if (fc.getSelectedFile().isFile() == true)
            	{	
            		if (fc.getSelectedFile().length() > max_file_size == true)
            		{
            			multiFiles = null;
            			ddText.setText("");            			
            			JOptionPane.showMessageDialog(null, FILE_TOO_BIG_ERROR,"Error",JOptionPane.ERROR_MESSAGE);
	                	clearFile();
            		}
            		else
            		{
            			File[] thisFile = new File[1];
                    	thisFile[0] = fc.getSelectedFile();
                    	multiFiles = thisFile;
                        selectedLabel.setText(thisFile[0].toString());
            		}
                }
                else {
                	JOptionPane.showMessageDialog(null, NO_DIRECTORIES_ERROR,"Error",JOptionPane.ERROR_MESSAGE);
                	
                }
            } else {
            }
            
		}			
		
		if (e.getSource() == selectMultiButton)
		{
			
            //File Drop for Multi Uploads	
			
			        new FileDrop( System.out, ddText, /*dragBorder,*/ new FileDrop.Listener()
			        {   
			        	
			        	public void filesDropped( java.io.File[] files )
			        	{
			        		boolean badFiles = false;
			        		
			        		{	
			        			
				        		for( int i = 0; i < files.length; i++ )
				                {   
				        			if (files[i].isDirectory() == true)
				        			{
				        				ddText.setText("");
				        				clearFile();
				        				multiFiles = null;
				        				badFiles = true;
				        				JOptionPane.showMessageDialog(null, NO_DIRECTORIES_ERROR,"Error",JOptionPane.ERROR_MESSAGE);
					                	break;
				        			}
				        			else if (files[i].length() > max_file_size == true)
				        			{
				        				ddText.setText("");
				        				clearFile();
				        				multiFiles = null;
				        				badFiles = true;
				        				JOptionPane.showMessageDialog(null, FILE_TOO_BIG_ERROR,"Error",JOptionPane.ERROR_MESSAGE);
					                	break;
				        			}
				        			else
				        			{
				        				try
					                    {   ddText.append( files[i].getCanonicalPath() + "\n" ); 			                    	
					                    }   // end try
					                    catch( java.io.IOException e ) {}
				        			}				        			
				                }   // end for: through each dropped file
			        		}
			            if (multiFiles != null)
			            {
			            	multiFiles = concatFileArray(multiFiles,files);
			            }
			            else
			            {
			            	multiFiles = files;
			            }
			        		
			            
			            if (badFiles == true)
			            {
			            	clearFile();
			            	multiFiles = null;
			            }
			            else if (multiFiles.length == 0)
			            {			  
			            	selectedLabel.setText("");
			            }
			            else if(multiFiles.length == 1)
			            {
			            	try
			            	{   	selectedLabel.setText(files[0].getCanonicalPath());
			            	}   // end try
		                    catch( java.io.IOException e ) {}
			            }
			            else if(multiFiles.length > 1)
			            {
			            	selectedLabel.setText("Multiple files selected");
			            	uploadButton.setLabel("Upload Multiple");
			            }
			        		
			            }   // end filesDropped
			        }); // end FileDrop.Listener
			        
			        frame.setBounds( 100, 100, 300, 400 );
			        //frame.setDefaultCloseOperation( frame.EXIT_ON_CLOSE );
			        frame.setVisible(true);
			        
				    
            
		}
		if (e.getSource() == ddButton)
        {
        	frame.dispose();
        }
		
		if (e.getSource() == ddClearButton)
        {
        	ddText.setText("");
        	selectedLabel.setText("");
        	uploadButton.setLabel("Upload File");
        	multiFiles = null;
        }
		
		if (e.getSource() == deleteButton)
		{
			if(checkAllFields() == true)
			{
				//AmazonGlacierClient client;
			    BasicAWSCredentials credentials = new BasicAWSCredentials(accessString,secretString);	        
			    client = new AmazonGlacierClient(credentials);
			    int locInt = locationChoice.getSelectedIndex();
			    Endpoints ep = new Endpoints();
			    String endpointUrl = ep.Endpoint(locInt);
			    client.setEndpoint(endpointUrl);
			    
		        String vaultName = vaultField.getText().trim();
			    DeleteArchiveFrame delete = new DeleteArchiveFrame(client, vaultName);
			    delete.setVisible(true);
			}
		}
		
		if (e.getSource() == uploadButton)
		{
			if(checkAllFields() == true && checkForFile() == true)
			{
				String vaultName = getVaultField();
								
				DummyProgress(true);
				try { Thread.sleep(500L);} catch (InterruptedException e1) {e1.printStackTrace();}
			    BasicAWSCredentials credentials = new BasicAWSCredentials(accessString,secretString);	        
			    client = new AmazonGlacierClient(credentials);
			    int locInt = locationChoice.getSelectedIndex();
			    Endpoints ep = new Endpoints();
			    String endpointUrl = ep.Endpoint(locInt);
			    client.setEndpoint(endpointUrl);
			    String locationUpped = ep.Location(locInt);

			    
			    if (multiFiles.length > 0)
			    {
			    	SaveCurrentProperties(accessString, secretString, vaultString, locationChoice.getSelectedIndex());
			    	ArrayList<String> uploadList = new ArrayList<String>();
			    	
			    	
			    	for( int i = 0; i < multiFiles.length; i++ )
	                {   
			    		//Save Current Settings to properties
			    		
				        try {
				            ArchiveTransferManager atm = new ArchiveTransferManager(client, credentials);
				            
				            String thisFile = multiFiles[i].getCanonicalPath();
				            
				            String fileLength = Long.toString(multiFiles[i].length());
				            
				            UploadResult result = atm.upload(vaultName, thisFile, multiFiles[i]);
				            
				            Writer plainOutput = null;
				            
				            //write to file
				            if(logCheck.isSelected())
				            {
				            	try
				                {	
				            		
				            		plainOutput = new BufferedWriter(new FileWriter(getLogFilenamePath(), true));
				            		
				            		//v0.4 method and earlier
				                	//output = new DataOutputStream(new FileOutputStream(logFileName, true));
				                }
				                catch(IOException ex)
				                {
				                	JOptionPane.showMessageDialog(null, LOG_CREATION_ERROR,"IO Error",JOptionPane.ERROR_MESSAGE);
				                	System.exit(1);
				                }
				            	
				            	try
				    			{
				            		
				            		Date d = new Date();
				            		
				            		String thisResult = result.getArchiveId();
				            					            		
				            		plainOutput.write(System.getProperty( "line.separator" ));
				            		plainOutput.write(" | ArchiveID: " + thisResult + " ");
				            		plainOutput.write(System.getProperty( "line.separator" ));
				            		plainOutput.write(" | File: " + thisFile + " ");
				            		plainOutput.write(" | Bytes: " + fileLength + " ");
				            		plainOutput.write(" | Vault: " +vaultName + " ");
				            		plainOutput.write(" | Location: " + locationUpped + " ");
				            		plainOutput.write(" | Date: "+d.toString()+ " ");
				            		plainOutput.write(System.getProperty( "line.separator" ));
				            		plainOutput.close();
				            		
				            		uploadList.add("Successfully uploaded " + thisFile + " to vault " + vaultName + " at " + locationUpped + ". Bytes: " + fileLength + ". ArchiveID Logged.\n");
				            		
				            		//v0.4 logging code
				            		//output.writeUTF("ArchiveID: " + thisResult + " ");
				            		//output.writeUTF(" | File: " + thisFile + " ");
				    				//output.writeUTF(" | Vault: " +vaultName + " ");
				    				//output.writeUTF(" | Location: " + locationUpped + " ");
				    				//output.writeUTF(" | Date: "+d.toString()+"\n\n");	  
				    				
				    				
				    			}
				    			catch(IOException c)
				    			{
				    				JOptionPane.showMessageDialog(null, LOG_WRITE_ERROR,"IO Error",JOptionPane.ERROR_MESSAGE);
				    				System.exit(1);
				    			}
				            }
				            else
				            {
				            	JOptionPane.showMessageDialog(null,"Upload Complete!\nArchive ID: " + result.getArchiveId()+"\nIt may take some time for Amazon to update the inventory.", "Uploaded", JOptionPane.INFORMATION_MESSAGE);
					            multiFiles = null;
					            DummyProgress(false);
				            }
				            
				            clearFile();
				            
				            
				        } catch (Exception h)
				        {
				        	JOptionPane.showMessageDialog(null,""+h, "Error", JOptionPane.ERROR_MESSAGE);
				        	DummyProgress(false);
				        }
				        
	                }
			    	StringBuilder sb = new StringBuilder();
			    	for( int j = 0; j < multiFiles.length; j++ ){
			    		sb.append(uploadList.get(j));
			    		}			    	
			    	DummyProgress(false);
			    	JOptionPane.showMessageDialog(null,"Upload Complete! \n" + sb,"Uploaded", JOptionPane.INFORMATION_MESSAGE);
			        //Close the JProgressBar
			    	multiFiles = null;
			    	clearFile();
			    }
			    else
			    {
			    	JOptionPane.showMessageDialog(null,"This wasn't supposed to happen.", "Bug!", JOptionPane.ERROR_MESSAGE);			        
			    	DummyProgress(false);
			    }
			       
			}
			
		}
	}
			
		
			
		
	
		
	
	private void SaveCurrentProperties(String accessString,
			String secretString, String vaultString, int selectedIndex) {

		FileOutputStream out;
		try 
		{
			out = new FileOutputStream(getFilePropertiesPath());
			
			applicationProps.setProperty("accessKey", accessString);
			applicationProps.setProperty("secretKey", secretString);
			applicationProps.setProperty("vaultKey", vaultString);
			applicationProps.setProperty("locationSet", Integer.toString(selectedIndex));
			applicationProps.store(out, "Properties");
			out.close();
		} 
		catch (FileNotFoundException e1) {						} 
		catch (IOException e1) {						}
		
	}

	public static String convertSimple(int i) {
	    return "" + i;
	}
	
	public boolean checkAllFields()
	{
		boolean passBool = false;
		
		if ((accessField.getText().trim().equals("")) || vaultField.getText().trim().equals("") || (secretField.getText().trim().equals("")))
		{
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
			JOptionPane.showMessageDialog(null,"You must complete all fields.", "Error", JOptionPane.ERROR_MESSAGE);
			passBool = false;
		}
		else if ((accessField.getText().trim().length() != 20) || (secretField.getText().trim().length() != 40))
		{
			if (accessField.getText().trim().length() != 20)
			{
				accessField.setFocusable(true);
				accessField.requestFocus();
				JOptionPane.showMessageDialog(null,"Your AWS Access Key does not appear to be valid.", "Error", JOptionPane.ERROR_MESSAGE);
				passBool = false;
			}
			else if (secretField.getText().trim().length() != 40) 
			{
				secretField.setFocusable(true);
				secretField.requestFocus();
				JOptionPane.showMessageDialog(null,"Your AWS Secret Key does not appear to be valid.", "Error", JOptionPane.ERROR_MESSAGE);
				passBool = false;
			}
		}
		else{
			passBool = true; 
		}
		return passBool;
	}
	
	public boolean checkForFile()
	{
		boolean passBool;
		if (selectedLabel.getText().equals("") == true)
		{
			JOptionPane.showMessageDialog(null,"Please select a file.", "Error", JOptionPane.ERROR_MESSAGE);
			passBool = false;
		}
		else{
			passBool = true; 
		}
		return passBool;
	}
	
	public String getVaultField()
	{
		return vaultField.getText().trim();
	}
	
	public String getAccessField()
	{
		return accessField.getText().trim();
	}
	
	public String getSecretField()
	{
		return secretField.getText().trim();
	}
	
	public void clearFile()
	{
		
		uploadFile = null;
        selectedLabel.setText("");
        uploadButton.setLabel("Upload File(s)");
        ddText.setText("");
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
	
	
	void centerFrame (JFrame f) {
	    // Need the toolkit to get info on system.
	    Toolkit tk = Toolkit.getDefaultToolkit ();

	    // Get the screen dimensions.
	    Dimension screen = tk.getScreenSize ();

	    // Make the frame 1/4th size of screen.
	    int fw =  (int) (screen.getWidth ()/4);
	    int fh =  (int) (screen.getWidth ()/4);
	    f.setSize (fw,fh);

	    // And place it in center of screen.
	    int lx =  (int) (screen.getWidth ()  * 3/8);
	    int ly =  (int) (screen.getHeight () * 3/8);
	    f.setLocation (lx,ly);
	  } // centerFrame
	
	void centerDefineFrame (JFrame f, int width, int height) {
	    // Need the toolkit to get info on system.
	    Toolkit tk = Toolkit.getDefaultToolkit ();

	    // Get the screen dimensions.
	    Dimension screen = tk.getScreenSize ();

	    // Make the frame 1/4th size of screen.
	    //int fw =  (int) (screen.getWidth ()/4);
	    //int fh =  (int) (screen.getWidth ()/4);
	    
	    //Set frame size
	    f.setSize (width,height);

	    // And place it in center of screen.
	    int lx =  (int) (screen.getWidth ()  * 3/8);
	    int ly =  (int) (screen.getHeight () * 3/8);
	    f.setLocation (lx,ly);
	  } // centerFrame
	
	public void DummyProgress(Boolean b)
	{   
		centerDefineFrame(uploadFrame, 300, 50);
	    if(b == true)
	    {
	    	uploadFrame.setVisible(b);
	    }
	    else
	    {
	    	uploadFrame.dispose();
	    }
	    
	}
	public File[] concatFileArray(File[] A, File[] B) {
		   File[] C= new File[A.length+B.length];
		   System.arraycopy(A, 0, C, 0, A.length);
		   System.arraycopy(B, 0, C, A.length, B.length);

		   return C;
		}
}


	
	
	
	