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
//		v0.6		AWS SDK Updated, Redesign UI. Add Vault. Basic Download.
//////////////////////////////////////////////////////////////////////////////////

package com.brianmcmichael.SimpleGlacierUploader;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


import java.awt.*;
import java.awt.event.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;



import javax.swing.JFileChooser;

import com.amazonaws.auth.BasicAWSCredentials;


import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.CreateVaultRequest;
import com.amazonaws.services.glacier.model.CreateVaultResult;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;

public class SimpleGlacierUploader extends Frame implements ActionListener
{
	
	//Call properties
	Properties applicationProps = new Properties();
	
	//static identfiers
	private static final long serialVersionUID = 1L;
	private static final String versionNumber = "0.61";
	private static final String logFileNamelog = "Glacier.log";
	private static final String logFileNametxt = "Glacier.txt";
	private static final String logFileNamecsv = "Glacier.csv";	
	private static final String fileProperties = "SAGU.properties";
	
	//Server Region Strings
	private static final String regionOne = "US East (Northern Virginia)";
	private static final String regionTwo = "US West (Oregon)";
	private static final String regionThree = "US West (Northern California)";
	private static final String regionFour = "EU (Ireland)";
	private static final String regionFive = "Asia Pacific (Tokyo)";
	
	public static final String curDir = System.getProperty("user.dir");
	
	//Error messages
	private static final String NO_DIRECTORIES_ERROR = "Directories, folders, and packages are not supported. \nPlease compress this into a single archive (such as a .zip) and try uploading again.";
	private static final String LOG_CREATION_ERROR = "There was an error creating the log.";
	private static final String FILE_TOO_BIG_ERROR = "Files over 4GB are currently unsuppoted. \nYou may want to split your upload into multiple archives. \nAmazon recommends files of 100mb at a time.";
	private static final String LOG_WRITE_ERROR = "There was an error writing to the log.";
	
	//Other Strings
	public static final String DOWNLOAD_STRING = "Download (Experimental)";
	public static final String COPYRIGHT_STRING = "Simple Amazon Glacier Uploader v"+versionNumber+"\n ©2012 brian@brianmcmichael.com";
	public static final String UPDATE_STRING = "Check for Update";
	public static final String UPDATE_SITE_STRING = "http://simpleglacieruploader.brianmcmichael.com/";
	public static final String ABOUT_WINDOW_STRING = ""+COPYRIGHT_STRING+"\n\nReport errors or direct correspondence to: brian@brianmcmichael.com\n\nSimple Amazon Glacier Uploader is free and always will be. \n I'm currently studying software development and I am hand coding this \nin Java to improve my skills and develop my portfolio.\n Your feedback is appreciated.\nThis program is not any way affiliated with Amazon Web Services or Amazon.com.";
	public static final String DOWNLOAD_NOTICE = "!!!!!!EXPERIMENTAL!!!!!!! \nThis feature is not yet threaded and is provided here for convenience and necessity only.\n\nIn addition to your AWS keys, you must assure that the region and vault name match\nthe location of the file you are trying to download. \nThis information can be found in your Glacier.log file.\n\n-Insert the ArchiveID and hit the download button. \n-You will then need to select a location and enter a filename for the download. \n-You should name the file to the same name as the filename in your log. \n-Click to accept.\n\n---IMPORTANT---\nThe program will appear to freeze at this point. This is normal.\n\nAfter Amazon retrieves the files (about four hours) the download will begin automatically \nand save the data to the location and filename that you selected. \n\nWhen the download is complete, you will receive a notice and the program will resume normal operation.\nIf you wish to download another file or upload files during this period you will \nneed to open another instance of the program.";
	public static final String URL_STRING = "http://simpleglacieruploader.brianmcmichael.com/";
	
	//Set Colors
	Color wc = Color.WHITE;
	Color rc = Color.RED;
	Color bc = Color.BLACK;
	
	//Set ints
	int width = 200;
	int height = 170;
	int fileInt = 0;
		
	//set longs
	long max_file_size = 4294967296L;
	
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
	
	//Set Fonts	
	Font f3= new Font("Helvetica",Font.BOLD,20);
	Font f4= new Font("Helvetica",Font.PLAIN,11);
	
	//Set Graphics
	ImageIcon xIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("img\\smallx.png"));
	ImageIcon downIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("img\\ArrowDown.png"));
	ImageIcon exitIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("img\\powerButton.png"));
	ImageIcon logIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("img\\logkey.png"));
	ImageIcon toolsIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("img\\tools.png"));
	
	File uploadFile = null;
	
	JPanel mainPanel = new JPanel();
	
	JPanel o1 = new JPanel();
	
	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();
	JPanel p3 = new JPanel();
	
	JPanel q1 = new JPanel();
	
	JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
			JMenuItem saveFileMnu = new JMenuItem("Export Log");
			JMenuItem exitApplicationMnu = new JMenuItem("Exit", exitIcon);
		JMenu retreiveMenu = new JMenu("Retreive");
			JMenuItem downloadFileMnu = new JMenuItem(DOWNLOAD_STRING, downIcon);
		JMenu viewMenu = new JMenu("View");
			JMenuItem viewLog = new JMenuItem("View Log", logIcon);
			JCheckBoxMenuItem logCheckMenuItem = new JCheckBoxMenuItem("Logging");
			JRadioButtonMenuItem logLogRadio = new JRadioButtonMenuItem(".log");
			JRadioButtonMenuItem logTxtRadio = new JRadioButtonMenuItem(".txt");
			JRadioButtonMenuItem logCsvRadio = new JRadioButtonMenuItem(".csv");
				ButtonGroup logFileGroup = new ButtonGroup();
		JMenu deleteMenu = new JMenu("Delete");
			JMenuItem deleteArchiveMnu = new JMenuItem("Delete Archive", xIcon);
		JMenu helpMenu = new JMenu("Help");
			JMenuItem updateMnu = new JMenuItem(UPDATE_STRING);
			JMenuItem aboutMnu = new JMenuItem("About", toolsIcon);	
		
		
	JPanel titlePanel = new JPanel();
		JLabel titleLabel = new JLabel("Simple Amazon Glacier Uploader "+versionNumber);
	
	JPanel credentialsPanel = new JPanel(new GridLayout(4,1,10,10));
		JHyperlinkLabel accessLabel = new JHyperlinkLabel("Access Key: ");  //v0.3
		JTextField accessField = new JTextField(21);
		JLabel secretLabel = new JLabel("Secret Key: ");
		JTextField secretField = new JTextField(41);
		
	JPanel locationPanel = new JPanel();
		JLabel locationName = new JLabel("Upload Location: ");
		JComboBox locationChoice = new JComboBox();
		
	
	JPanel vaultPanel = new JPanel();
		JHyperlinkLabel vaultName = new JHyperlinkLabel("Vault Name: ");  //v0.3
		JTextField vaultField = new JTextField(15);
		JButton newVaultButton = new JButton("Create Vault");
		
				
	JPanel logPanel = new JPanel();
		JButton logButton = new JButton("View Log");
		JButton downloadRequestButton = new JButton(DOWNLOAD_STRING);
		JButton checkUpdateButton = new JButton(UPDATE_STRING);
		
	
	JPanel selectionsPanel = new JPanel();
	JButton selectFileButton = new JButton("Select File");
		JButton clearButton = new JButton("Clear");
		
		

		
	JPanel fileDropPanel = new JPanel();
		JTextArea ddText = new JTextArea();
		JScrollPane ddScroll = new JScrollPane(ddText);
		
		
		
		FileDrop fileDropHere = new FileDrop( ddText, /*dragBorder,*/ new FileDrop.Listener()
		{       	
			//File[] goodFiles = null;
			File thisFile = null;
			public void filesDropped( java.io.File[] files )
			{
				//boolean badFiles = false;
				File[] goodFiles = new File[files.length];
    			{   
    				
    				int j = 0;
					for( int i = 0; i < files.length; i++ )
					{   
						if (files[i].isDirectory() == true)
	        			{
							try
		                    {   
								//ddText.setForeground(rc);
								ddText.append( "Unable to upload: " + files[i].getCanonicalPath() + "\n" ); 			                    	
		                    }   // end try
		                    catch( java.io.IOException e ) {}
	        				JOptionPane.showMessageDialog(null, NO_DIRECTORIES_ERROR,"Error",JOptionPane.ERROR_MESSAGE);
		                	files[i] = null;
	        			}
	        			else if (files[i].length() > max_file_size == true)
	        			{
	        				try
		                    {   
								//ddText.setForeground(rc);
								ddText.append( "Unable to upload: " + files[i].getCanonicalPath() + "\n" ); 			                    	
		                    }   // end try
		                    catch( java.io.IOException e ) {}
	        				JOptionPane.showMessageDialog(null, FILE_TOO_BIG_ERROR,"Error",JOptionPane.ERROR_MESSAGE);
		                	files[i] = null;
	        			}	
	        			else
	        			{
	        				try
		                    {   
	        					ddText.append( files[i].getCanonicalPath() + "\n" );
		                    }   // end try
		                    catch( java.io.IOException e ) {}
	        			}				        			
					}   // end for: through each dropped file
    			}
    			files = removeNullFile(files);
    			if (multiFiles != null)
		        {
		        	multiFiles = concatFileArray(multiFiles,files);
		        }
		        else
		        {
		        	multiFiles = files;
		        }		    		
		        
    			if (multiFiles.length == 0)
		        {	
		        	
		            uploadButton.setText("Select File(s)");
		        }
		        else if(multiFiles.length == 1)
		        {
		        	uploadButton.setText("Upload File");
		        }
		        else if(multiFiles.length > 1)
		        {
		        	uploadButton.setText("Upload Files");
		        }
			}
    	});   // end filesDropped
    //}); // end FileDrop.Listener 
	
	JButton uploadButton = new JButton("Upload");
		
	JPanel copyrightPanel = new JPanel();
		JLabel copyrightLabel = new JLabel(COPYRIGHT_STRING);
		JHyperlinkLabel updateLink = new JHyperlinkLabel("\tCheck for Update");
		
	
	
	//Create dumb progressbar
    JFrame uploadFrame = new JFrame("Uploading"); {
    uploadFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    final JProgressBar dumJProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
    //aJProgressBar.setStringPainted(true);
    dumJProgressBar.setIndeterminate(true);
    uploadFrame.add(dumJProgressBar, BorderLayout.NORTH);
    uploadFrame.setSize(300, 50);}
    
    //Set FileChooser
	JFileChooser fc = new JFileChooser();
		
		
	//Create Filedrop Components
	//javax.swing.JFrame frame = new javax.swing.JFrame( "Drag and drop files here" );
    //javax.swing.border.TitledBorder dragBorder = new javax.swing.border.TitledBorder( "Drop files here:" );
    //final javax.swing.JTextArea ddText = new javax.swing.JTextArea();
    //final javax.swing.JButton ddClearButton = new javax.swing.JButton("Clear");
    //final javax.swing.JButton ddButton = new javax.swing.JButton("OK");
	
           
        
           
        
	public SimpleGlacierUploader()
	{
		this.setLayout(new BorderLayout());
		
			mainPanel.setLayout(new BorderLayout());
			o1.setLayout(new GridLayout(1,3,10,10));
			p1.setLayout(new GridLayout(2,1,20,20));
			q1.setLayout(new GridLayout(2,1,10,10));
			//p2.setLayout(new GridLayout(8,1,10,10));	
			p2.setLayout(new BorderLayout());
			p3.setLayout(new BorderLayout());
			
		
		titlePanel.setBackground(wc);
		titlePanel.add(titleLabel);
			titleLabel.setFont(f3);
				
		credentialsPanel.setBackground(wc);
		credentialsPanel.setBorder(BorderFactory.createTitledBorder("AWS Credentials"));
		credentialsPanel.add(accessLabel);
		credentialsPanel.add(accessField);
			accessField.addMouseListener(rmb);
		credentialsPanel.add(secretLabel);
		credentialsPanel.add(secretField);
			secretField.addMouseListener(rmb);
		
		locationPanel.setBackground(wc);
		locationPanel.setBorder(BorderFactory.createTitledBorder("Server Location"));
		locationPanel.add(locationChoice);
			locationChoice.setBackground(wc);
			locationChoice.addItem(regionOne);
			locationChoice.addItem(regionTwo);
			locationChoice.addItem(regionThree);
			locationChoice.addItem(regionFour);
			locationChoice.addItem(regionFive);
			locationChoice.addActionListener(this);
		
		vaultPanel.setBackground(wc);
		vaultPanel.setBorder(BorderFactory.createTitledBorder("Vault Selection"));
		vaultPanel.add(vaultField);
		vaultPanel.add(newVaultButton);
			newVaultButton.addActionListener(this);
			newVaultButton.setBackground(wc);
		
		logPanel.setBackground(wc);
		logPanel.setBorder(BorderFactory.createTitledBorder("Options"));
		logPanel.add(logButton);
			logButton.setBackground(wc);
			logButton.addActionListener(this);
		logPanel.add(downloadRequestButton);
			downloadRequestButton.setBackground(wc);
			downloadRequestButton.addActionListener(this);
		logPanel.add(checkUpdateButton);
			checkUpdateButton.setBackground(wc);
			checkUpdateButton.addActionListener(this);
			
			
		selectionsPanel.setBackground(wc);
		selectionsPanel.add(selectFileButton);
			selectFileButton.setBackground(wc);
			selectFileButton.addActionListener(this);
			//fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		selectionsPanel.add(clearButton);
			clearButton.setBackground(wc);
			clearButton.addActionListener(this);
		
		fileDropPanel.setBackground(wc);
		fileDropPanel.setLayout(new BorderLayout());
		fileDropPanel.setBorder(BorderFactory.createTitledBorder("Drag and Drop Files"));	
		fileDropPanel.add(ddScroll, BorderLayout.CENTER);
			ddScroll.setSize(180, 300);
		
		q1.setBackground(wc);
		q1.add(locationPanel);
		q1.add(vaultPanel);
		
		p1.setBackground(wc);
		p1.add(credentialsPanel);
		p1.add(q1);
		//p1.add(vaultPanel);
		
			
		p2.setBackground(wc);
		p2.add(logPanel);
		
		p3.setBackground(wc);
		p3.add(selectionsPanel, BorderLayout.NORTH);
		p3.add(fileDropPanel, BorderLayout.CENTER);
		p3.add(uploadButton, BorderLayout.SOUTH);
			uploadButton.setBackground(wc);
			uploadButton.addActionListener(this);
		p3.setBorder(BorderFactory.createTitledBorder("Uploads"));
		
		
		
		o1.setBackground(wc);
		o1.add(p1);
		o1.add(p2);
		o1.add(p3);
		
		mainPanel.add(o1, BorderLayout.CENTER);	
		mainPanel.setBackground(wc);
		mainPanel.add(menuBar, BorderLayout.NORTH);
		mainPanel.add(copyrightPanel, BorderLayout.SOUTH);
		
		menuBar.setBackground(wc);
		menuBar.add(fileMenu);
			fileMenu.add(saveFileMnu);
				saveFileMnu.addActionListener(this);
			fileMenu.add(exitApplicationMnu);
				exitApplicationMnu.addActionListener(this);
		menuBar.add(retreiveMenu);
			retreiveMenu.add(downloadFileMnu);
				downloadFileMnu.addActionListener(this);		
		menuBar.add(viewMenu);
			viewMenu.add(viewLog);
				viewLog.addActionListener(this);
			viewMenu.add(logCheckMenuItem);
				logCheckMenuItem.setSelected(true);
			viewMenu.addSeparator();
			viewMenu.add(logLogRadio);
				logLogRadio.setSelected(true);				
				logFileGroup.add(logLogRadio);				
			viewMenu.add(logTxtRadio);
				logFileGroup.add(logTxtRadio);
			viewMenu.add(logCsvRadio);
				logFileGroup.add(logCsvRadio);
		menuBar.add(deleteMenu);
			deleteMenu.add(deleteArchiveMnu);
				deleteArchiveMnu.addActionListener(this);
		menuBar.add(helpMenu);
			helpMenu.add(updateMnu);
				updateMnu.addActionListener(this);
			helpMenu.add(aboutMnu);
				aboutMnu.addActionListener(this);
				
		
		add(mainPanel, BorderLayout.CENTER);
		pack();

	
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
			locationChoice.setSelectedIndex(Integer.parseInt(applicationProps.getProperty("locationSet")));
			if(applicationProps.getProperty("logType") == null)
			{
				setLogFileType(0);
			}
			else
			{
				setLogFileType(Integer.parseInt(applicationProps.getProperty("logType")));
			}			
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
	
	public void setLogFileType(int intype)
	{
		if((intype == 0) == true)
		{
			logLogRadio.setSelected(true);
		}
		else if((intype == 1) == true)
		{
			logTxtRadio.setSelected(true);
		}
		else if((intype == 0) == true)
		{
			logCsvRadio.setSelected(true);
		}
		else
		{
			logLogRadio.setSelected(true);
		}		
	}
	
	public int getLogFileType()
	{
		if(logLogRadio.isSelected() == true)
		{
			return 0;
		}
		if(logTxtRadio.isSelected() == true)
		{
			return 1;
		}
		if(logCsvRadio.isSelected() == true)
		{
			return 2;
		}
		else
		{
			return 0;
		}		
	}
	
	public static String getLogFilename(int filename)
	{
		if(filename == 0)
		{
			return ""+logFileNamelog;
		}
		if(filename == 1)
		{
			return ""+logFileNametxt;
		}
		if(filename == 2)
		{
			return ""+logFileNamecsv;
		}
		else
		{
			return ""+logFileNamelog;
		}
	}
	
	public static File getLogFilenamePath(int filepath)
	{
		if(filepath == 0)
		{
			File logFile = new File(curDir + System.getProperty("file.separator") + logFileNamelog);
			return logFile;
		}
		if(filepath == 1)
		{
			File logFile = new File(curDir + System.getProperty("file.separator") + logFileNametxt);
			return logFile;
		}
		if(filepath == 2)
		{
			File logFile = new File(curDir + System.getProperty("file.separator") + logFileNamecsv);
			return logFile;
		}
		else
		{
			File logFile = new File(curDir + System.getProperty("file.separator") + logFileNamelog);
			return logFile;
		}		
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
			applicationProps.setProperty("logType", convertSimple(getLogFileType()));
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

		if((multiFiles.length == 0) == true)
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
	
	public int getServerRegion()
	{
		return locationChoice.getSelectedIndex();
	}
	
	public void clearFile()
	{		
		uploadFile = null;
        uploadButton.setText("Upload File(s)");
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
	
	
	public static String getRegion(int reg)
	{
		String regString;
		
		switch (reg) 
		{
			case 0: 	regString = regionOne;
				break;
			
			case 1:		regString = regionTwo;
				break;
				
			case 2:		regString = regionThree;
				break;
				
			case 3:		regString = regionFour;
				break;
				
			case 4:		regString = regionFive;
				break;
				
			default:	regString = regionOne;
				break;
		
		}
		return regString; 
	}
	
	public File[] removeNullFile(File[] a) {
		   ArrayList<File> removed = new ArrayList<File>();
		   for (File fle : a)
		      if (fle != null)
		         removed.add(fle);
		   return removed.toArray(new File[0]);
		}
	
	public File[] concatFileArray(File[] A, File[] B) {
		   File[] C= new File[A.length+B.length];
		   System.arraycopy(A, 0, C, 0, A.length);
		   System.arraycopy(B, 0, C, A.length, B.length);

		   return C;
		}
	
	public AmazonGlacierClient makeClient(String accessorString, String secretiveString, int region)
	{
		
		//AmazonGlacierClient client;
	    BasicAWSCredentials credentials = new BasicAWSCredentials(accessorString,secretiveString);	        
	    client = new AmazonGlacierClient(credentials);
	    //int locInt = locationChoice.getSelectedIndex();
	    Endpoints ep = new Endpoints();
	    String endpointUrl = ep.Endpoint(region);
	    client.setEndpoint(endpointUrl);
		
		return client;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String accessString = getAccessField();
		String vaultString = getVaultField();
		String secretString = getSecretField();
		int regionInt = getServerRegion();
		
		if(e.getSource() == newVaultButton && checkAllFields() == true)
		{
			AmazonGlacierClient newVaultClient = new AmazonGlacierClient();
			newVaultClient = makeClient(accessString, secretString, regionInt);
			AddVaultFrame avf = new AddVaultFrame(newVaultClient, regionInt);
			avf.setVisible(true);
			//TODO add vault selection
			//getVaults();
			//System.out.println(""+avf.getVault());
		}
		if(e.getSource() == exitApplicationMnu)
		{
			System.exit(0);
		}
		if(e.getSource() == updateMnu || e.getSource() == checkUpdateButton)
		{
			JHyperlinkLabel.OpenURI(URL_STRING);
		}
		if(e.getSource() == saveFileMnu)
		{
			//JFileChooser saveChooser = new JFileChooser();
			FileDialog fd = new FileDialog(new Frame(), "Save...", FileDialog.SAVE);
		    fd.setFile("Glacier.txt");
		    fd.setDirectory(curDir);
		    fd.setLocation(50, 50);
		    fd.setVisible(true);
		    //fd.show();
		    String filePath = ""+fd.getDirectory()+System.getProperty("file.separator")+fd.getFile();
		    		
		    		
			File outFile = new File(filePath);
			//System.out.println(outFile.toString());
			
			if ((outFile.equals("") == false) && (outFile.equals("null")) == false)
			{
				
            	try 
            	{
            		FileReader fr = new FileReader(getLogFilenamePath(getLogFileType()));
            		BufferedReader br = new BufferedReader(fr);
            		
            		FileWriter saveFile = new FileWriter(outFile.toString());
            		
					int count = 0;
					boolean moreLines = true;
					
					String ln1 ="";
					String ln2 ="";
					String ln3 ="";
					
					while(moreLines == true)
					{
						ln1 = br.readLine();
						ln2 = br.readLine();
						ln3 = br.readLine();
						
						if (ln1 == null) { ln1 = ""; }
						if (ln2 == null) { ln2 = ""; }
						if (ln3 == null) { ln3 = ""; }
								
						saveFile.write(ln1);
						saveFile.write("\r\n");
						saveFile.write(ln2);
						saveFile.write("\r\n");
						saveFile.write(ln3);
						saveFile.write("\r\n");
						
						count++;
						
						if (ln3.equals(""))
						{
							moreLines = false;
							br.close();
							saveFile.close();
							JOptionPane.showMessageDialog(null,"Successfully exported " + count + " archive records to " + outFile.toString(), "Export", JOptionPane.INFORMATION_MESSAGE);				            
						}
					}
				} 
            	catch (FileNotFoundException e1) 
				{					
            		JOptionPane.showMessageDialog(null, "Unable to locate Glacier.log","Error",JOptionPane.ERROR_MESSAGE);
        			e1.printStackTrace();
				} catch (IOException e1) 
				{					
					e1.printStackTrace();
				}		
        	}
		}
            
		
		
		if(e.getSource() == viewLog || e.getSource() == logButton)
		{
			JHyperlinkLabel.OpenURI(""+SimpleGlacierUploader.getLogFilenamePath(getLogFileType()).toURI());
		}
		if(e.getSource() == deleteArchiveMnu)
		{
			AmazonGlacierClient newDeleteClient = new AmazonGlacierClient();
			newDeleteClient = makeClient(accessString, secretString, regionInt);
			DeleteArchiveFrame adf = new DeleteArchiveFrame(newDeleteClient, vaultString, regionInt);
			adf.setVisible(true);
		}
		
		if(e.getSource() == downloadRequestButton || e.getSource() == downloadFileMnu)
		{	
			JOptionPane.showMessageDialog(null,DOWNLOAD_NOTICE, "Download Warning", JOptionPane.INFORMATION_MESSAGE);
            
			AmazonGlacierClient newDownloadClient = new AmazonGlacierClient();
			newDownloadClient = makeClient(accessString, secretString, regionInt);
			BasicAWSCredentials credentials = new BasicAWSCredentials(accessString,secretString);
			AmazonDownloadRequest adr = new AmazonDownloadRequest(newDownloadClient, vaultString, regionInt, credentials);
			adr.setVisible(true);			
		}
		
		if (e.getSource() == aboutMnu)
        {
			JOptionPane.showMessageDialog(null,ABOUT_WINDOW_STRING, "About", JOptionPane.INFORMATION_MESSAGE);
        }
		
		if (e.getSource() == clearButton)
        {
        	ddText.setText("");
        	uploadButton.setText("Select Files");
        	multiFiles = null;
        }
		
		if (e.getSource() == selectFileButton)
		{
			int returnVal = fc.showOpenDialog(SimpleGlacierUploader.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
            	if (fc.getSelectedFile().isFile() == true)
            	{	
            		if (fc.getSelectedFile().length() > max_file_size == true)
            		{
            			JOptionPane.showMessageDialog(null, FILE_TOO_BIG_ERROR,"Error",JOptionPane.ERROR_MESSAGE);
            			try
	                    {   
							//ddText.setForeground(rc);
							ddText.append( "Unable to upload: " + fc.getSelectedFile().getCanonicalPath() + "\n" ); 			                    	
	                    }   // end try
	                    catch( java.io.IOException g ) {}
            		}
            		else
            		{
            			File[] thisFile = new File[1];
            			thisFile[0] = fc.getSelectedFile();
                    	try
	                    {   
        					ddText.append( thisFile[0].getCanonicalPath() + "\n" );
	                    }   // end try
	                    catch( java.io.IOException f ) {}
                    	if (multiFiles != null)
                    	{
                    		multiFiles = concatFileArray(multiFiles,thisFile);
                    	}
                    	else
                    	{
                    		multiFiles = thisFile;
                    	}
                    	       
                    	
            		}
                }
                else 
                {
                	JOptionPane.showMessageDialog(null, NO_DIRECTORIES_ERROR,"Error",JOptionPane.ERROR_MESSAGE);
                }
            } else 
            {
            }
            
		}
		
		if (e.getSource() == uploadButton)
		{
			if((checkAllFields() == true) && (checkForFile() == true))
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
				            if(logCheckMenuItem.isSelected())
				            {
				            	try
				                {	
				            		
				            		plainOutput = new BufferedWriter(new FileWriter(getLogFilenamePath(getLogFileType()), true));
				            		
				            		//v0.4 method and earlier
				                	//output = new DataOutputStream(new FileOutputStream(logFileName, true));
				                }
				                catch(IOException ex)
				                {
				                	JOptionPane.showMessageDialog(null, LOG_CREATION_ERROR,"IO Error",JOptionPane.ERROR_MESSAGE);
				                	System.exit(1);
				                }
				            	
				            	if (logLogRadio.isSelected() == true || logTxtRadio.isSelected() == true)
				            	{
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
					    			}
					            		
					            		//v0.4 logging code
					            		//output.writeUTF("ArchiveID: " + thisResult + " ");
					            		//output.writeUTF(" | File: " + thisFile + " ");
					    				//output.writeUTF(" | Vault: " +vaultName + " ");
					    				//output.writeUTF(" | Location: " + locationUpped + " ");
					    				//output.writeUTF(" | Date: "+d.toString()+"\n\n");	  
				            		catch(IOException c)
					    			{
					    				JOptionPane.showMessageDialog(null, LOG_WRITE_ERROR,"IO Error",JOptionPane.ERROR_MESSAGE);
					    				System.exit(1);
					    			}
					    		}
					            else if (logCsvRadio.isSelected() == true)
					            {
					            	try
					    			{
					            		
					            		Date d = new Date();
					            		
					            		String thisResult = result.getArchiveId();
					            					            		
					            		plainOutput.write(""+thisResult+",");
					            		plainOutput.write(""+thisFile+",");
					            		plainOutput.write(""+fileLength+",");
					            		plainOutput.write(""+vaultName +",");
					            		plainOutput.write(""+locationUpped+",");
					            		plainOutput.write(""+d.toString()+",");
					            		plainOutput.write(System.getProperty( "line.separator" ));
					            		plainOutput.close();
					            		
					            		uploadList.add("Successfully uploaded " + thisFile + " to vault " + vaultName + " at " + locationUpped + ". Bytes: " + fileLength + ". ArchiveID Logged.\n");
					    			}	
				            		catch(IOException c)
					    			{
					    				JOptionPane.showMessageDialog(null, LOG_WRITE_ERROR,"IO Error",JOptionPane.ERROR_MESSAGE);
					    				System.exit(1);
					    			}
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
		else
		{
			
		}
			
	
	}
	
	public class UseFileDialog {
		 
		  public String loadFile
		      (Frame f, String title, String defDir, String fileType) {
		    FileDialog fd = new FileDialog(f, title, FileDialog.LOAD);
		    fd.setFile(fileType);
		    fd.setDirectory(defDir);
		    fd.setLocation(50, 50);
		    fd.setVisible(true);
		    //fd.show();
		    return fd.getFile();
		    }
		 
		  public String saveFile
		      (Frame f, String title, String defDir, String fileType) {
		    FileDialog fd = new FileDialog(f, title,    FileDialog.SAVE);
		    fd.setFile(fileType);
		    fd.setDirectory(defDir);
		    fd.setLocation(50, 50);
		    fd.setVisible(true);
		    //fd.show();
		    return fd.getFile();
		    }
		 
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Main Class
	public static void main(String[] args) throws Exception
	{
		SimpleGlacierUploader g = new SimpleGlacierUploader();
		g.setBounds(300,300,650,475);
		g.setTitle("Simple Amazon Glacier Uploader "+versionNumber);
		g.setVisible(true);
		g.setIconImage(Toolkit.getDefaultToolkit().getImage("img\\glaciericon.png"));
		
	} //end of main
}


