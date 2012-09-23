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

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

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
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.swing.JFileChooser;

import com.amazonaws.auth.BasicAWSCredentials;

import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.CreateVaultRequest;
import com.amazonaws.services.glacier.model.CreateVaultResult;
import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.amazonaws.services.glacier.model.ListVaultsRequest;
import com.amazonaws.services.glacier.model.ListVaultsResult;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;

public class SimpleGlacierUploader extends Frame implements ActionListener
{
	
	//Call properties
	Properties applicationProps = new Properties();
	
	//static identfiers
	private static final long serialVersionUID = 1L;
	private static final String versionNumber = "0.74";
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
	//private static final String FILE_TOO_BIG_ERROR = "Files over 4GB are currently unsuppoted. \nYou may want to split your upload into multiple archives. \nAmazon recommends files of 100mb at a time.";
	private static final String LOG_WRITE_ERROR = "There was an error writing to the log.";
	
	//Other Strings
	public static final String DOWNLOAD_STRING = "Download Archive";
	public static final String INVENTORY_REQUEST_STRING = "Request Inventory";
	public static final String COPYRIGHT_STRING = "Simple Amazon Glacier Uploader\nVersion "+versionNumber+"\n ©2012 brian@brianmcmichael.com";
	public static final String UPDATE_STRING = "Check for Update";
	public static final String UPDATE_SITE_STRING = "http://simpleglacieruploader.brianmcmichael.com/";
	public static final String ABOUT_WINDOW_STRING = ""+COPYRIGHT_STRING+"\n\nReport errors or direct correspondence to: brian@brianmcmichael.com\n\nSimple Amazon Glacier Uploader is free and always will be. \nYour feedback is appreciated.\nThis program is not any way affiliated with Amazon Web Services or Amazon.com.";
	public static final String URL_STRING = "http://simpleglacieruploader.brianmcmichael.com/";
	public static final String AWS_SITE_STRING = "Get AWS Credentials";
	public static final String ACCESS_LABEL = "Access Key: ";
	
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
	
	//Set dimension
	Dimension buttonDimension = new Dimension(180, 27);
	
	//Set Graphics
	URL xIconUrl = getClass().getResource("/smallx.png"); 
	ImageIcon xIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(xIconUrl));
	URL downIconUrl = getClass().getResource("/arrowDown.png");
	ImageIcon downIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(downIconUrl));
	URL exitIconUrl = getClass().getResource("/powerButton.png");
	ImageIcon exitIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(exitIconUrl));
	URL logIconUrl = getClass().getResource("/logKey.png");
	ImageIcon logIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(logIconUrl));
	URL toolsIconUrl = getClass().getResource("/tools.png");
	ImageIcon toolsIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(toolsIconUrl));
	URL saveIconUrl = getClass().getResource("/floppy.png");
	ImageIcon saveIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(saveIconUrl));
	URL logViewIconUrl = getClass().getResource("/logView.png");
	ImageIcon logViewIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(logViewIconUrl));
	URL updateIconUrl = getClass().getResource("/paper.png");
	ImageIcon updateIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(updateIconUrl));
	URL userUrl = getClass().getResource("/littleguy.png");
	ImageIcon userIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(userUrl));
	URL logoUrl = getClass().getResource("/SAGU.png");
	JLabel logoLabel = new JLabel(new ImageIcon( Toolkit.getDefaultToolkit().getImage(logoUrl) ));
		
	File uploadFile = null;
	
	JPanel mainPanel = new JPanel();
	
	JPanel o1 = new JPanel();
	
	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();
	JPanel p3 = new JPanel();
	
	JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
			JMenuItem saveFileMnu = new JMenuItem("Export Log", saveIcon);
			JMenuItem exitApplicationMnu = new JMenuItem("Exit", exitIcon);
		JMenu retrieveMenu = new JMenu("retrieve");
			JMenuItem getAWSCredentialsLinkMnu = new JMenuItem(AWS_SITE_STRING, userIcon);
			JMenuItem downloadFileMnu = new JMenuItem(DOWNLOAD_STRING, downIcon);
		JMenu viewMenu = new JMenu("View");
			JMenuItem viewLog = new JMenuItem("View Log", logViewIcon);
			JCheckBoxMenuItem logCheckMenuItem = new JCheckBoxMenuItem("Logging On/Off", logIcon);
			JRadioButtonMenuItem logLogRadio = new JRadioButtonMenuItem(".log");
			JRadioButtonMenuItem logTxtRadio = new JRadioButtonMenuItem(".txt");
			JRadioButtonMenuItem logCsvRadio = new JRadioButtonMenuItem(".csv");
				ButtonGroup logFileGroup = new ButtonGroup();
		JMenu deleteMenu = new JMenu("Delete");
			JMenuItem deleteArchiveMnu = new JMenuItem("Delete Archive", xIcon);
		JMenu helpMenu = new JMenu("Help");
			JMenuItem updateMnu = new JMenuItem(UPDATE_STRING, updateIcon);
			JMenuItem aboutMnu = new JMenuItem("About", toolsIcon);	
		
		
	JPanel titlePanel = new JPanel();
		JLabel titleLabel = new JLabel("Simple Amazon Glacier Uploader "+versionNumber);
	
	JPanel credentialsPanel = new JPanel(new GridLayout(4,1,10,10));
		JHyperlinkLabel accessLabel = new JHyperlinkLabel(ACCESS_LABEL);  //v0.3
		JTextField accessField = new JTextField(21);
		JLabel secretLabel = new JLabel("Secret Key: ");
		JPasswordField secretField = new JPasswordField(41);
		
	JPanel locationPanel = new JPanel();
		JLabel locationName = new JLabel("Upload Location: ");
		JComboBox locationChoice = new JComboBox();
		JButton loginButton = new JButton("Refresh Vaults");
		
	
	JPanel vaultPanel = new JPanel();
		JHyperlinkLabel vaultSelectLabel = new JHyperlinkLabel("Select Existing:");  //v0.3
		JComboBox vaultSelector = new JComboBox();	
		JHyperlinkLabel vaultName = new JHyperlinkLabel("Vault Name: ");  //v0.3
		JTextField vaultField = new JTextField(15);
		JButton newVaultButton = new JButton("Create Vault");
		
	JPanel logoPanel = new JPanel();
				
	JPanel logPanel = new JPanel();
		JButton logButton = new JButton("View Log");
		JButton downloadRequestButton = new JButton(DOWNLOAD_STRING);
		JButton inventoryRequestButton = new JButton(INVENTORY_REQUEST_STRING);
		JButton checkUpdateButton = new JButton(UPDATE_STRING);
		
	
	JPanel selectionsPanel = new JPanel();
	JButton selectFileButton = new JButton("Select File");
		JButton clearButton = new JButton("Clear");
		
	JPanel fileDropPanel = new JPanel();
		JTextArea ddText = new JTextArea();
		JScrollPane ddScroll = new JScrollPane(ddText);
		
		FileDrop fileDropHere = new FileDrop( ddText, /*dragBorder,*/ new FileDrop.Listener()
		{    
			
			File thisFile = null;
			public void filesDropped( java.io.File[] files )
			{
				ddText.setEditable(false);
				File[] goodFiles = new File[files.length];
    			{   
    				
    				int j = 0;
					for( int i = 0; i < files.length; i++ )
					{   
						if (files[i].isDirectory() == true)
	        			{
							try
		                    {   
								ddText.append( "Unable to upload: " + files[i].getCanonicalPath() + "\n" ); 			                    	
		                    }   // end try
		                    catch( java.io.IOException e ) {}
	        				JOptionPane.showMessageDialog(null, NO_DIRECTORIES_ERROR,"Error",JOptionPane.ERROR_MESSAGE);
		                	files[i] = null;
	        			}
	        			/*    Removed filesize limitation with 0.72. Upgraded AWS SDK to 1.3.19
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
	        			*/	
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
   
	
	JButton uploadButton = new JButton("Upload");
		
	JPanel copyrightPanel = new JPanel();
		JLabel copyrightLabel = new JLabel(COPYRIGHT_STRING);
		JHyperlinkLabel updateLink = new JHyperlinkLabel("\tCheck for Update");
		
	
    //Set FileChooser
	JFileChooser fc = new JFileChooser();
		
	       
        
	public SimpleGlacierUploader()
	{
		this.setLayout(new BorderLayout());
		
			mainPanel.setLayout(new BorderLayout());
			o1.setLayout(new GridLayout(1,3,10,10));
			p1.setLayout(new GridLayout(3,1,3,3));
			
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
			accessField.setPreferredSize(buttonDimension);
		credentialsPanel.add(secretLabel);
		credentialsPanel.add(secretField);
			secretField.addMouseListener(rmb);
			secretField.setPreferredSize(buttonDimension);

		
		locationPanel.setBackground(wc);
		locationPanel.setBorder(BorderFactory.createTitledBorder("Server Location"));
		locationPanel.add(locationChoice);
			locationChoice.setPreferredSize(buttonDimension);
			locationChoice.setBackground(wc);
			locationChoice.addItem(regionOne);
			locationChoice.addItem(regionTwo);
			locationChoice.addItem(regionThree);
			locationChoice.addItem(regionFour);
			locationChoice.addItem(regionFive);
			locationChoice.addActionListener(this);
		locationPanel.add(loginButton);
			loginButton.addActionListener(this);
			loginButton.setBackground(wc);
			loginButton.setPreferredSize(buttonDimension);
		
		
		vaultPanel.setBackground(wc);
		vaultPanel.setBorder(BorderFactory.createTitledBorder("Vault Selection"));
		vaultPanel.add(vaultSelector);
			vaultSelector.setBackground(wc);
			vaultSelector.addActionListener(this);
			vaultSelector.setPreferredSize(buttonDimension);
		vaultPanel.add(vaultField);
			vaultField.addActionListener(this);
			vaultField.setPreferredSize(buttonDimension);			
		vaultPanel.add(newVaultButton);
			newVaultButton.addActionListener(this);
			newVaultButton.setBackground(wc);
			newVaultButton.setPreferredSize(buttonDimension);
		
		logoPanel.setBackground(wc);	
		logoPanel.add(logoLabel);
		
		logPanel.setBackground(wc);
		logPanel.setBorder(BorderFactory.createTitledBorder("Options"));
		logPanel.add(logButton);
			logButton.setBackground(wc);
			logButton.addActionListener(this);
			logButton.setPreferredSize(buttonDimension);
		logPanel.add(downloadRequestButton);
			downloadRequestButton.setBackground(wc);
			downloadRequestButton.addActionListener(this);
			downloadRequestButton.setPreferredSize(buttonDimension);
		logPanel.add(inventoryRequestButton);
			inventoryRequestButton.setBackground(wc);
			inventoryRequestButton.addActionListener(this);
			inventoryRequestButton.setPreferredSize(buttonDimension);
		logPanel.add(checkUpdateButton);
			checkUpdateButton.setBackground(wc);
			checkUpdateButton.addActionListener(this);
			checkUpdateButton.setPreferredSize(buttonDimension);
			
			
		selectionsPanel.setBackground(wc);
		selectionsPanel.add(selectFileButton);
			selectFileButton.setBackground(wc);
			selectFileButton.addActionListener(this);
			selectFileButton.setPreferredSize(new Dimension(110,27));
			//fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		selectionsPanel.add(clearButton);
			clearButton.setBackground(wc);
			clearButton.addActionListener(this);
			clearButton.setPreferredSize(new Dimension(70,27));
		
		fileDropPanel.setBackground(wc);
		fileDropPanel.setLayout(new BorderLayout());
		fileDropPanel.setBorder(BorderFactory.createTitledBorder("Drag and Drop Files"));	
		fileDropPanel.add(ddScroll, BorderLayout.CENTER);
			ddText.setEditable(false);
			ddScroll.setSize(180, 300);
		
		
		p1.setBackground(wc);
		p1.add(credentialsPanel);
		p1.add(locationPanel);
		p1.add(vaultPanel);
		
			
		p2.setBackground(wc);
		p2.add(logoPanel, BorderLayout.NORTH);
		p2.add(logPanel, BorderLayout.CENTER);
		
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
			fileMenu.setBackground(wc);
			fileMenu.add(saveFileMnu);
				saveFileMnu.setBackground(wc);
				saveFileMnu.addActionListener(this);
			fileMenu.addSeparator();
			fileMenu.add(exitApplicationMnu);
				exitApplicationMnu.setBackground(wc);
				exitApplicationMnu.addActionListener(this);
		menuBar.add(retrieveMenu);
			retrieveMenu.setBackground(wc);
			retrieveMenu.add(getAWSCredentialsLinkMnu);
				getAWSCredentialsLinkMnu.setBackground(wc);
				getAWSCredentialsLinkMnu.addActionListener(this);
			retrieveMenu.add(downloadFileMnu);
				downloadFileMnu.setBackground(wc);
				downloadFileMnu.addActionListener(this);		
		menuBar.add(viewMenu);
			viewMenu.setBackground(wc);
			viewMenu.add(viewLog);
				viewLog.setBackground(wc);
				viewLog.addActionListener(this);
			viewMenu.add(logCheckMenuItem);
				logCheckMenuItem.setBackground(wc);
				logCheckMenuItem.setSelected(true);
			viewMenu.addSeparator();
			viewMenu.add(logLogRadio);
				logLogRadio.setBackground(wc);
				logLogRadio.setSelected(true);				
				logFileGroup.add(logLogRadio);				
			viewMenu.add(logTxtRadio);
				logFileGroup.add(logTxtRadio);
				logTxtRadio.setBackground(wc);
			viewMenu.add(logCsvRadio);
				logCsvRadio.setBackground(wc);
				logFileGroup.add(logCsvRadio);
		menuBar.add(deleteMenu);
			deleteMenu.add(deleteArchiveMnu);
				deleteArchiveMnu.setBackground(wc);
				deleteArchiveMnu.addActionListener(this);
		menuBar.add(helpMenu);
			helpMenu.setBackground(wc);
			helpMenu.add(updateMnu);
				updateMnu.setBackground(wc);
				updateMnu.addActionListener(this);
			helpMenu.add(aboutMnu);
				aboutMnu.setBackground(wc);
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
	
	public boolean checkAWSFields()
	{
		boolean passBool = false;
		
		if ((accessField.getText().trim().equals("")) || (secretField.getText().trim().equals("")))
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
			
			JOptionPane.showMessageDialog(null,"You must enter your AWS credentials.", "Error", JOptionPane.ERROR_MESSAGE);
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

		if((multiFiles == null) == true)
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
	
	public void repopulateVaults(String accessString, String secretString, int regionInt)
	{

		int newLoc = locationChoice.getSelectedIndex();
		
		if (((accessField.getText().trim().equals("")) == true) || (secretField.getText().trim().equals("")) == true) {}
		else
		{
			AmazonGlacierClient newVaultCheckClient = new AmazonGlacierClient();
			newVaultCheckClient = makeClient(accessString, secretString, newLoc);
			BasicAWSCredentials credentials = new BasicAWSCredentials(accessString,secretString);
			
			String marker = null;
			do
			{
				ListVaultsRequest lv = new ListVaultsRequest()
					.withMarker(marker);
				ListVaultsResult lvr = newVaultCheckClient.listVaults(lv);
				ArrayList<DescribeVaultOutput> vList = new ArrayList<DescribeVaultOutput>(lvr.getVaultList());
				
				vaultSelector.removeAllItems();
				vaultSelector.addItem("Select Existing:");
				
				for(DescribeVaultOutput vault: vList)
				{
					vaultSelector.addItem(vault.getVaultName());
				}
			}
			while (marker != null);				
		}
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
		String secretString = getSecretField();
		String vaultString = getVaultField();
		int regionInt = getServerRegion();
		
		if(e.getSource() == newVaultButton && (checkAWSFields() == true))
		{
				AmazonGlacierClient newVaultClient = new AmazonGlacierClient();
				newVaultClient = makeClient(accessString, secretString, regionInt);
				AddVaultFrame avf = new AddVaultFrame(newVaultClient, regionInt);
				avf.setVisible(true);
				
		}
		if(e.getSource() == vaultSelector)
		{
			if (vaultSelector.getSelectedItem() != null)
			{
				if (vaultSelector.getSelectedIndex() == 0)
				{
					vaultField.setText("");
				}
				else
				{
					vaultField.setText(vaultSelector.getSelectedItem().toString());
				}
			}
			else{}
		}
		if(e.getSource() == loginButton)
		{
			repopulateVaults(accessString, secretString, regionInt);
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
			FileDialog fd = new FileDialog(new Frame(), "Save...", FileDialog.SAVE);
		    fd.setFile("Glacier.txt");
		    fd.setDirectory(curDir);
		    fd.setLocation(50, 50);
		    fd.setVisible(true);
		    //fd.show();
		    String filePath = ""+fd.getDirectory()+System.getProperty("file.separator")+fd.getFile();
		    		
		    		
			File outFile = new File(filePath);
			
			if ((outFile.equals("") == false) && (outFile.equals("null") == false) && ((outFile == null) == false))
			{
				
            	try 
            	{
            		FileReader fr = new FileReader(getLogFilenamePath(0));
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
			if (checkAllFields() == true)
			{
				AmazonGlacierClient newDeleteClient = new AmazonGlacierClient();
				newDeleteClient = makeClient(accessString, secretString, regionInt);
				DeleteArchiveFrame daf = new DeleteArchiveFrame(newDeleteClient, vaultString, regionInt);
				daf.setVisible(true);
			}
		}
		if (e.getSource() == inventoryRequestButton)
		{
			if (checkAllFields() == true)
			{
				AmazonGlacierClient newInventoryClient = new AmazonGlacierClient();
				newInventoryClient = makeClient(accessString, secretString, regionInt);
				InventoryRequest ir = new InventoryRequest(newInventoryClient, vaultString, regionInt);
				ir.setVisible(true);
			}
		}
		if(e.getSource() == downloadRequestButton || e.getSource() == downloadFileMnu)
		{	
	        if (checkAllFields() == true)
	        {
				AmazonGlacierClient newDownloadClient = new AmazonGlacierClient();
				newDownloadClient = makeClient(accessString, secretString, regionInt);
				BasicAWSCredentials credentials = new BasicAWSCredentials(accessString,secretString);
				AmazonDownloadRequest adr = new AmazonDownloadRequest(newDownloadClient, vaultString, regionInt, credentials);
				adr.setVisible(true);
	        }
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
		
		if (e.getSource() == locationChoice)
		{
			repopulateVaults(accessString, secretString, regionInt);					
		}
		
		if (e.getSource() == selectFileButton)
		{
			int returnVal = fc.showOpenDialog(SimpleGlacierUploader.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
            	if (fc.getSelectedFile().isFile() == true)
            	{	
            		/* Removed for v. 0.72
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
            		*/
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
                    	       
                    	
            		//}
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
				
				SaveCurrentProperties(accessString, secretString, vaultString, locationChoice.getSelectedIndex());
					    	
				SwingWorker uploadWorker = new SwingWorker() {
		    		
					@Override
					protected Object doInBackground() throws Exception {
						String accessString = getAccessField();		
						String secretString = getSecretField();
						String vaultString = getVaultField();
						String vaultName = getVaultField();
						File[] uploadFileBatch = multiFiles;
						int locInt = locationChoice.getSelectedIndex();
						multiFiles = null;
						clearFile();
						UploadWindow uw = new UploadWindow();
						uw.uploadFrame.setVisible(true);
					    
						
					    if (uploadFileBatch.length > 0)
					    {
					    	
					    	ArrayList<String> uploadList = new ArrayList<String>();
					    			
					    	for( int i = 0; i < uploadFileBatch.length; i++ )
			                {   
					    		//Save Current Settings to properties
					    		
								
								try { Thread.sleep(100L);} catch (InterruptedException e1) {e1.printStackTrace();}
							    BasicAWSCredentials credentials = new BasicAWSCredentials(accessString,secretString);	        
							    client = new AmazonGlacierClient(credentials);
							    Endpoints ep = new Endpoints();
							    String endpointUrl = ep.Endpoint(locInt);
							    client.setEndpoint(endpointUrl);
							    String locationUpped = ep.Location(locInt);
								
						        try {
						            ArchiveTransferManager atm = new ArchiveTransferManager(client, credentials);
						            
						            String thisFile = uploadFileBatch[i].getCanonicalPath();
						            
						            String fileLength = Long.toString(uploadFileBatch[i].length());
						            
						            uw.uploadFrame.setTitle("("+(i+1)+"/"+uploadFileBatch.length+")"+" Uploading: "+thisFile);
						            
						            UploadResult result = atm.upload(vaultName, thisFile, uploadFileBatch[i]);
						            
						            uw.uploadText.append("Done: "+thisFile+"\n");
						            Writer plainOutputLog = null;
						            Writer plainOutputTxt = null;
						            Writer plainOutputCsv = null;
						            
						            //write to file
						            if(logCheckMenuItem.isSelected())
						            {
						            	try
						                {							            		
						            		plainOutputLog = new BufferedWriter(new FileWriter(getLogFilenamePath(0), true));
						            		plainOutputTxt = new BufferedWriter(new FileWriter(getLogFilenamePath(1), true));
						            		plainOutputCsv = new BufferedWriter(new FileWriter(getLogFilenamePath(2), true));
						            		
						                }
						                catch(IOException ex)
						                {
						                	JOptionPane.showMessageDialog(null, LOG_CREATION_ERROR,"IO Error",JOptionPane.ERROR_MESSAGE);
						                	System.exit(1);
						                	uw.destroyUploadWindow();
						               
						                }
						            	
					            		try
						    			{
						            		
						            		Date d = new Date();
						            		
						            		String thisResult = result.getArchiveId();
						            					            		
						            		plainOutputLog.write(System.getProperty( "line.separator" ));
						            		plainOutputLog.write(" | ArchiveID: " + thisResult + " ");
						            		plainOutputLog.write(System.getProperty( "line.separator" ));
						            		plainOutputLog.write(" | File: " + thisFile + " ");
						            		plainOutputLog.write(" | Bytes: " + fileLength + " ");
						            		plainOutputLog.write(" | Vault: " +vaultName + " ");
						            		plainOutputLog.write(" | Location: " + locationUpped + " ");
						            		plainOutputLog.write(" | Date: "+d.toString()+ " ");
						            		plainOutputLog.write(System.getProperty( "line.separator" ));
						            		plainOutputLog.close();
						            		
						            		plainOutputTxt.write(System.getProperty( "line.separator" ));
						            		plainOutputTxt.write(" | ArchiveID: " + thisResult + " ");
						            		plainOutputTxt.write(System.getProperty( "line.separator" ));
						            		plainOutputTxt.write(" | File: " + thisFile + " ");
						            		plainOutputTxt.write(" | Bytes: " + fileLength + " ");
						            		plainOutputTxt.write(" | Vault: " +vaultName + " ");
						            		plainOutputTxt.write(" | Location: " + locationUpped + " ");
						            		plainOutputTxt.write(" | Date: "+d.toString()+ " ");
						            		plainOutputTxt.write(System.getProperty( "line.separator" ));
						            		plainOutputTxt.close();
						            		
						            		plainOutputCsv.write(""+thisResult+",");
						            		plainOutputCsv.write(""+thisFile+",");
						            		plainOutputCsv.write(""+fileLength+",");
						            		plainOutputCsv.write(""+vaultName +",");
						            		plainOutputCsv.write(""+locationUpped+",");
						            		plainOutputCsv.write(""+d.toString()+",");
						            		plainOutputCsv.write(System.getProperty( "line.separator" ));
						            		plainOutputCsv.close();
						            		
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
						    				
						    				uw.destroyUploadWindow();						    				
						    			} 	
							            
						            }
						            else
						            {
						            	JOptionPane.showMessageDialog(null,"Upload Complete!\nArchive ID: " + result.getArchiveId()+"\nIt may take some time for Amazon to update the inventory.", "Uploaded", JOptionPane.INFORMATION_MESSAGE);
							            multiFiles = null;
							            uw.destroyUploadWindow();
							            
						            }
						            
						            clearFile();
						            
						            
						        } catch (Exception h)
						        {
						        	JOptionPane.showMessageDialog(null,""+h, "Error", JOptionPane.ERROR_MESSAGE);
						        	uw.destroyUploadWindow();
						        	
						        }
						        
			                }
					    	StringBuilder sb = new StringBuilder();
					    	for( int j = 0; j < uploadFileBatch.length; j++ ){
					    		sb.append(uploadList.get(j));
					    		}			    	
					    	uw.destroyUploadWindow();
					    	
					    	JOptionPane.showMessageDialog(null,"Upload Complete! \n" + sb,"Uploaded", JOptionPane.INFORMATION_MESSAGE);
					        //Close the JProgressBar
					    	multiFiles = null;
					    	clearFile();
					    }
					    else
					    {
					    	JOptionPane.showMessageDialog(null,"This wasn't supposed to happen.", "Bug!", JOptionPane.ERROR_MESSAGE);			        
					    	uw.destroyUploadWindow();
					    	
					    }
						
						return null;
					}
		    		
		    	};
		    	uploadWorker.execute();
				
				
				
			       
			}
			
		}
		else
		{			
		}
	}
	
	
	public class UseFileDialog 
	{	 
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
		g.setIconImage(Toolkit.getDefaultToolkit().getImage("/glaciericon.png"));
		g.setVisible(true);
		
		
	} //end of main


	
}


