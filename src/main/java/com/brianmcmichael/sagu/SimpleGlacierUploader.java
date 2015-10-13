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

package com.brianmcmichael.sagu;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.TreeHashGenerator;
import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.amazonaws.services.glacier.model.ListVaultsRequest;
import com.amazonaws.services.glacier.model.ListVaultsResult;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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

import static java.awt.Color.*;

public class SimpleGlacierUploader extends Frame implements ActionListener {

    private static final String versionNumber = "0.74.7";
    private static final String logFileNameLog = "Glacier.log";
    private static final String logFileNameTxt = "Glacier.txt";
    private static final String logFileNameCsv = "Glacier.csv";
    private static final String logFileNameYaml = "Glacier.yaml";
    private static final String logFileNameErr = "GlacierErrors.txt";
    private static final String fileProperties = "SAGU.properties";

    // Error messages
    private static final String NO_DIRECTORIES_ERROR = "Directories, folders, and packages are not supported. \nPlease compress this into a single archive (such as a .zip) and try uploading again.";
    private static final String LOG_CREATION_ERROR = "There was an error creating the log.";
    private static final String LOG_WRITE_ERROR = "There was an error writing to the log.";

    // Other Strings
    private static final String DOWNLOAD_STRING = "Download Archive";
    private static final String INVENTORY_REQUEST_STRING = "Request Inventory";
    private static final String COPYRIGHT_STRING = "Simple Amazon Glacier Uploader\nVersion "
            + versionNumber + "\n �2012-2015 Brian McMichael";
    private static final String UPDATE_STRING = "Check for Update";
    private static final String ABOUT_WINDOW_STRING = ""
            + COPYRIGHT_STRING
            + "\n\nReport errors or direct correspondence to: brian@brianmcmichael.com\n\nSimple Amazon Glacier Uploader is free software. \nYour feedback is appreciated.\nThis program is not any way affiliated with Amazon Web Services or Amazon.com.";
    private static final String URL_STRING = "http://simpleglacieruploader.brianmcmichael.com/";
    private static final String AWS_SITE_STRING = "Get AWS Credentials";

    public static final String ACCESS_LABEL = "Access Key: ";

    private static final String curDir = System.getProperty("user.dir");

    // Config override
    private static final int SOCKET_TIMEOUT = 1000000;
    private static final int MAX_RETRIES = 6;

    private Properties applicationProps = new Properties();

    private int width = 200;
    private int height = 170;

    private AmazonGlacierClient client;

    // Right mouse click context listener
    private ContextMenuMouseListener rmb = new ContextMenuMouseListener();

    // File array for multiupload
    private File[] multiFiles;

    private Dimension buttonDimension = new Dimension(180, 27);

    // Set Graphics
    private URL xIconUrl = getClass().getResource("/smallx.png");
    private ImageIcon xIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(xIconUrl));
    private URL downIconUrl = getClass().getResource("/arrowDown.png");
    private ImageIcon downIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(downIconUrl));
    private URL exitIconUrl = getClass().getResource("/powerButton.png");
    private ImageIcon exitIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(exitIconUrl));
    private URL logIconUrl = getClass().getResource("/logKey.png");
    private ImageIcon logIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(logIconUrl));
    private URL toolsIconUrl = getClass().getResource("/tools.png");
    private ImageIcon toolsIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(toolsIconUrl));
    private URL saveIconUrl = getClass().getResource("/floppy.png");
    private ImageIcon saveIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(saveIconUrl));
    private URL logViewIconUrl = getClass().getResource("/logView.png");
    private ImageIcon logViewIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(logViewIconUrl));
    private URL updateIconUrl = getClass().getResource("/paper.png");
    private ImageIcon updateIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(updateIconUrl));
    private URL userUrl = getClass().getResource("/littleguy.png");
    private ImageIcon userIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(userUrl));
    private URL logoUrl = getClass().getResource("/SAGU.png");
    private JLabel logoLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(logoUrl)));

    private JPanel mainPanel = new JPanel();

    private JPanel o1 = new JPanel();

    private JPanel p1 = new JPanel();
    private JPanel p2 = new JPanel();
    private JPanel p3 = new JPanel();

    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem saveFileMnu = new JMenuItem("Export Log", saveIcon);
    private JMenuItem exitApplicationMnu = new JMenuItem("Exit", exitIcon);
    private JMenu retrieveMenu = new JMenu("Retrieve");
    private JMenuItem getAWSCredentialsLinkMnu = new JMenuItem(AWS_SITE_STRING, userIcon);
    private JMenuItem downloadFileMnu = new JMenuItem(DOWNLOAD_STRING, downIcon);
    private JMenu viewMenu = new JMenu("View");
    private JMenuItem viewLog = new JMenuItem("View Log", logViewIcon);
    private JCheckBoxMenuItem logCheckMenuItem = new JCheckBoxMenuItem("Logging On/Off", logIcon);
    private JRadioButtonMenuItem logLogRadio = new JRadioButtonMenuItem(".log");
    private JRadioButtonMenuItem logTxtRadio = new JRadioButtonMenuItem(".txt");
    private JRadioButtonMenuItem logCsvRadio = new JRadioButtonMenuItem(".csv");
    private JRadioButtonMenuItem logYamlRadio = new JRadioButtonMenuItem(".yaml");
    private ButtonGroup logFileGroup = new ButtonGroup();
    private JMenu deleteMenu = new JMenu("Delete");
    private JMenuItem deleteArchiveMnu = new JMenuItem("Delete Archive", xIcon);
    private JMenu helpMenu = new JMenu("Help");
    private JMenuItem updateMnu = new JMenuItem(UPDATE_STRING, updateIcon);
    private JMenuItem aboutMnu = new JMenuItem("About", toolsIcon);

    private JPanel titlePanel = new JPanel();
    private JLabel titleLabel = new JLabel("Simple Amazon Glacier Uploader " + versionNumber);

    private JPanel credentialsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
    private JHyperlinkLabel accessLabel = new JHyperlinkLabel(ACCESS_LABEL); // v0.3
    private JTextField accessField = new JTextField(21);
    private JLabel secretLabel = new JLabel("Secret Key: ");
    private JPasswordField secretField = new JPasswordField(41);

    private JPanel locationPanel = new JPanel();
    private JComboBox<String> locationChoice = new JComboBox<String>();
    private JButton loginButton = new JButton("Refresh Vaults");

    private JPanel vaultPanel = new JPanel();
    private JComboBox<String> vaultSelector = new JComboBox<String>();
    private JTextField vaultField = new JTextField(15);
    private JButton newVaultButton = new JButton("Create Vault");

    private JPanel logoPanel = new JPanel();

    private JPanel logPanel = new JPanel();
    private JButton logButton = new JButton("View Log");
    private JButton downloadRequestButton = new JButton(DOWNLOAD_STRING);
    private JButton inventoryRequestButton = new JButton(INVENTORY_REQUEST_STRING);
    private JButton checkUpdateButton = new JButton(UPDATE_STRING);

    private JPanel selectionsPanel = new JPanel();
    private JButton selectFileButton = new JButton("Select File");
    private JButton clearButton = new JButton("Clear");

    private JPanel fileDropPanel = new JPanel();
    private JTextArea ddText = new JTextArea();
    private JScrollPane ddScroll = new JScrollPane(ddText);

    private FileDrop fileDropHere = new FileDrop(ddText, new FileDrop.Listener() {

        public void filesDropped(java.io.File[] files) {
            ddText.setEditable(false);
            {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        try {
                            ddText.append("Unable to upload: " + files[i].getCanonicalPath() + "\n");
                        } catch (java.io.IOException e) {
                        }
                        JOptionPane.showMessageDialog(null,
                                NO_DIRECTORIES_ERROR, "Error",
                                JOptionPane.ERROR_MESSAGE);
                        files[i] = null;
                    } else {
                        try {
                            ddText.append(files[i].getCanonicalPath() + "\n");
                        } catch (java.io.IOException e) {
                        }
                    }
                } // end for: through each dropped file
            }
            files = removeNullFile(files);
            if (multiFiles != null) {
                multiFiles = concatFileArray(multiFiles, files);
            } else {
                multiFiles = files;
            }

            if (multiFiles.length == 0) {
                uploadButton.setText("Select File(s)");
            } else if (multiFiles.length == 1) {
                uploadButton.setText("Upload File");
            } else if (multiFiles.length > 1) {
                uploadButton.setText("Upload Files");
            }
        }
    });

    private JButton uploadButton = new JButton("Upload");

    private JPanel copyrightPanel = new JPanel();

    private JFileChooser fc = new JFileChooser();

    private SimpleGlacierUploader() {
        this.setLayout(new BorderLayout());

        mainPanel.setLayout(new BorderLayout());
        o1.setLayout(new GridLayout(1, 3, 10, 10));
        p1.setLayout(new GridLayout(3, 1, 3, 3));

        p2.setLayout(new BorderLayout());
        p3.setLayout(new BorderLayout());

        titlePanel.setBackground(WHITE);
        titlePanel.add(titleLabel);
        final Font f3 = new Font("Helvetica", Font.BOLD, 20);
        titleLabel.setFont(f3);

        credentialsPanel.setBackground(WHITE);
        credentialsPanel.setBorder(BorderFactory.createTitledBorder("AWS Credentials"));
        credentialsPanel.add(accessLabel);
        credentialsPanel.add(accessField);
        accessField.addMouseListener(rmb);
        accessField.setPreferredSize(buttonDimension);
        credentialsPanel.add(secretLabel);
        credentialsPanel.add(secretField);
        secretField.addMouseListener(rmb);
        secretField.setPreferredSize(buttonDimension);

        locationPanel.setBackground(WHITE);
        locationPanel.setBorder(BorderFactory.createTitledBorder("Server Location"));
        locationPanel.add(locationChoice);
        locationChoice.setPreferredSize(buttonDimension);
        locationChoice.setBackground(WHITE);
        Endpoint.populateComboBox(locationChoice);
        locationChoice.addActionListener(this);
        locationPanel.add(loginButton);
        loginButton.addActionListener(this);
        loginButton.setBackground(WHITE);
        loginButton.setPreferredSize(buttonDimension);

        vaultPanel.setBackground(WHITE);
        vaultPanel.setBorder(BorderFactory.createTitledBorder("Vault Selection"));
        vaultPanel.add(vaultSelector);
        vaultSelector.setBackground(WHITE);
        vaultSelector.addActionListener(this);
        vaultSelector.setPreferredSize(buttonDimension);
        vaultPanel.add(vaultField);
        vaultField.addActionListener(this);
        vaultField.setPreferredSize(buttonDimension);
        vaultPanel.add(newVaultButton);
        newVaultButton.addActionListener(this);
        newVaultButton.setBackground(WHITE);
        newVaultButton.setPreferredSize(buttonDimension);

        logoPanel.setBackground(WHITE);
        logoPanel.add(logoLabel);

        logPanel.setBackground(WHITE);
        logPanel.setBorder(BorderFactory.createTitledBorder("Options"));
        logPanel.add(logButton);
        logButton.setBackground(WHITE);
        logButton.addActionListener(this);
        logButton.setPreferredSize(buttonDimension);
        logPanel.add(downloadRequestButton);
        downloadRequestButton.setBackground(WHITE);
        downloadRequestButton.addActionListener(this);
        downloadRequestButton.setPreferredSize(buttonDimension);
        logPanel.add(inventoryRequestButton);
        inventoryRequestButton.setBackground(WHITE);
        inventoryRequestButton.addActionListener(this);
        inventoryRequestButton.setPreferredSize(buttonDimension);
        logPanel.add(checkUpdateButton);
        checkUpdateButton.setBackground(WHITE);
        checkUpdateButton.addActionListener(this);
        checkUpdateButton.setPreferredSize(buttonDimension);

        selectionsPanel.setBackground(WHITE);
        selectionsPanel.add(selectFileButton);
        selectFileButton.setBackground(WHITE);
        selectFileButton.addActionListener(this);
        selectFileButton.setPreferredSize(new Dimension(110, 27));
        selectionsPanel.add(clearButton);
        clearButton.setBackground(WHITE);
        clearButton.addActionListener(this);
        clearButton.setPreferredSize(new Dimension(70, 27));

        fileDropPanel.setBackground(WHITE);
        fileDropPanel.setLayout(new BorderLayout());
        fileDropPanel.setBorder(BorderFactory.createTitledBorder("Drag and Drop Files"));
        fileDropPanel.add(ddScroll, BorderLayout.CENTER);
        ddText.setEditable(false);
        ddScroll.setSize(180, 300);

        p1.setBackground(WHITE);
        p1.add(credentialsPanel);
        p1.add(locationPanel);
        p1.add(vaultPanel);

        p2.setBackground(WHITE);
        p2.add(logoPanel, BorderLayout.NORTH);
        p2.add(logPanel, BorderLayout.CENTER);

        p3.setBackground(WHITE);
        p3.add(selectionsPanel, BorderLayout.NORTH);
        p3.add(fileDropPanel, BorderLayout.CENTER);
        p3.add(uploadButton, BorderLayout.SOUTH);
        uploadButton.setBackground(WHITE);
        uploadButton.addActionListener(this);
        p3.setBorder(BorderFactory.createTitledBorder("Uploads"));

        o1.setBackground(WHITE);
        o1.add(p1);
        o1.add(p2);
        o1.add(p3);

        mainPanel.add(o1, BorderLayout.CENTER);
        mainPanel.setBackground(WHITE);
        mainPanel.add(menuBar, BorderLayout.NORTH);
        mainPanel.add(copyrightPanel, BorderLayout.SOUTH);

        menuBar.setBackground(WHITE);
        menuBar.add(fileMenu);
        fileMenu.setBackground(WHITE);
        fileMenu.add(saveFileMnu);
        saveFileMnu.setBackground(WHITE);
        saveFileMnu.addActionListener(this);
        fileMenu.addSeparator();
        fileMenu.add(exitApplicationMnu);
        exitApplicationMnu.setBackground(WHITE);
        exitApplicationMnu.addActionListener(this);
        menuBar.add(retrieveMenu);
        retrieveMenu.setBackground(WHITE);
        retrieveMenu.add(getAWSCredentialsLinkMnu);
        getAWSCredentialsLinkMnu.setBackground(WHITE);
        getAWSCredentialsLinkMnu.addActionListener(this);
        retrieveMenu.add(downloadFileMnu);
        downloadFileMnu.setBackground(WHITE);
        downloadFileMnu.addActionListener(this);
        menuBar.add(viewMenu);
        viewMenu.setBackground(WHITE);
        viewMenu.add(viewLog);
        viewLog.setBackground(WHITE);
        viewLog.addActionListener(this);
        viewMenu.add(logCheckMenuItem);
        logCheckMenuItem.setBackground(WHITE);
        logCheckMenuItem.setSelected(true);
        viewMenu.addSeparator();
        viewMenu.add(logLogRadio);
        logLogRadio.setBackground(WHITE);
        logLogRadio.setSelected(true);
        logFileGroup.add(logLogRadio);
        viewMenu.add(logTxtRadio);
        logFileGroup.add(logTxtRadio);
        logTxtRadio.setBackground(WHITE);
        viewMenu.add(logCsvRadio);
        logCsvRadio.setBackground(WHITE);
        logFileGroup.add(logCsvRadio);
        viewMenu.add(logYamlRadio);
        logYamlRadio.setBackground(WHITE);
        logFileGroup.add(logYamlRadio);
        menuBar.add(deleteMenu);
        deleteMenu.add(deleteArchiveMnu);
        deleteArchiveMnu.setBackground(WHITE);
        deleteArchiveMnu.addActionListener(this);
        menuBar.add(helpMenu);
        helpMenu.setBackground(WHITE);
        helpMenu.add(updateMnu);
        updateMnu.setBackground(WHITE);
        updateMnu.addActionListener(this);
        helpMenu.add(aboutMnu);
        aboutMnu.setBackground(WHITE);
        aboutMnu.addActionListener(this);

        add(mainPanel, BorderLayout.CENTER);
        pack();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Load properties from last invocation
        FileInputStream in;
        try {
            in = new FileInputStream(getFilePropertiesPath());
            applicationProps.load(in);
            accessField.setText(applicationProps.getProperty("accessKey"));
            secretField.setText(applicationProps.getProperty("secretKey"));
            vaultField.setText(applicationProps.getProperty("vaultKey"));
            locationChoice.setSelectedIndex(Integer.parseInt(applicationProps
                    .getProperty("locationSet")));
            if (applicationProps.getProperty("logType") == null) {
                setLogFileType(0);
            } else {
                setLogFileType(Integer.parseInt(applicationProps
                        .getProperty("logType")));
            }
            in.close();
        } catch (FileNotFoundException e1) {
        } catch (IOException e1) {
        }

        pack();
        if (width < getWidth()) { // prevent setting width too small
            width = getWidth();
        }
        if (height < getHeight()) { // prevent setting height too small
            height = getHeight();
        }
        centerOnScreen(width, height);
    }

    public void setLogFileType(int intype) {
        switch (intype) {
            case 0:
            default:
                logLogRadio.setSelected(true);
                break;
            case 1:
                logTxtRadio.setSelected(true);
                break;
            case 2:
                logCsvRadio.setSelected(true);
                break;
            case 3:
                logYamlRadio.setSelected(true);
                break;
        }
    }

    public int getLogFileType() {
        if (logLogRadio.isSelected()) {
            return 0;
        }
        if (logTxtRadio.isSelected()) {
            return 1;
        }
        if (logCsvRadio.isSelected()) {
            return 2;
        }
        if (logYamlRadio.isSelected()) {
            return 3;
        } else {
            return 0;
        }
    }

    public static File getLogFilenamePath(int filepath) {
        if (filepath == 0) {
            return new File(curDir + System.getProperty("file.separator") + logFileNameLog);
        }
        if (filepath == 1) {
            return new File(curDir + System.getProperty("file.separator") + logFileNameTxt);
        }
        if (filepath == 2) {
            return new File(curDir + System.getProperty("file.separator") + logFileNameCsv);
        }
        if (filepath == 3) {
            return new File(curDir + System.getProperty("file.separator") + logFileNameYaml);
        }
        if (filepath == 4) {
            return new File(curDir + System.getProperty("file.separator") + logFileNameErr);
        } else {
            return new File(curDir + System.getProperty("file.separator") + logFileNameLog);
        }
    }

    public static File getFilePropertiesPath() {
        return new File(curDir + System.getProperty("file.separator") + fileProperties);
    }

    private void SaveCurrentProperties(String accessString,
                                       String secretString, String vaultString, int selectedIndex) {

        FileOutputStream out;
        try {
            out = new FileOutputStream(getFilePropertiesPath());

            applicationProps.setProperty("accessKey", accessString);
            applicationProps.setProperty("secretKey", secretString);
            applicationProps.setProperty("vaultKey", vaultString);
            applicationProps.setProperty("locationSet", Integer.toString(selectedIndex));
            applicationProps.setProperty("logType", convertSimple(getLogFileType()));
            applicationProps.store(out, "Properties");
            out.close();
        } catch (FileNotFoundException e1) {
        } catch (IOException e1) {
        }
    }

    public static String convertSimple(int i) {
        return "" + i;
    }

    public boolean checkAWSFields() {
        boolean passBool = false;

        if ((accessField.getText().trim().equals(""))
                || (secretField.getText().trim().equals(""))) {
            if ((accessField.getText().trim().equals(""))) {
                accessField.setFocusable(true);
                accessField.requestFocus();
            } else if ((secretField.getText().trim().equals(""))) {
                secretField.setFocusable(true);
                secretField.requestFocus();
            }

            JOptionPane.showMessageDialog(null,
                    "You must enter your AWS credentials.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            passBool = false;
        } else if ((accessField.getText().trim().length() != 20)
                || (secretField.getText().trim().length() != 40)) {
            if (accessField.getText().trim().length() != 20) {
                accessField.setFocusable(true);
                accessField.requestFocus();
                JOptionPane.showMessageDialog(null,
                        "Your AWS Access Key does not appear to be valid.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                passBool = false;
            } else if (secretField.getText().trim().length() != 40) {
                secretField.setFocusable(true);
                secretField.requestFocus();
                JOptionPane.showMessageDialog(null,
                        "Your AWS Secret Key does not appear to be valid.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                passBool = false;
            }
        } else {
            passBool = true;
        }
        return passBool;
    }

    public boolean checkAllFields() {
        boolean passBool = false;

        if ((accessField.getText().trim().equals(""))
                || vaultField.getText().trim().equals("")
                || (secretField.getText().trim().equals(""))) {
            if ((accessField.getText().trim().equals(""))) {
                accessField.setFocusable(true);
                accessField.requestFocus();
            } else if ((secretField.getText().trim().equals(""))) {
                secretField.setFocusable(true);
                secretField.requestFocus();
            } else if ((vaultField.getText().trim().equals(""))) {
                vaultField.setFocusable(true);
                vaultField.requestFocus();
            }
            JOptionPane.showMessageDialog(null,
                    "You must complete all fields.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            passBool = false;
        } else if ((accessField.getText().trim().length() != 20)
                || (secretField.getText().trim().length() != 40)) {
            if (accessField.getText().trim().length() != 20) {
                accessField.setFocusable(true);
                accessField.requestFocus();
                JOptionPane.showMessageDialog(null,
                        "Your AWS Access Key does not appear to be valid.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                passBool = false;
            } else if (secretField.getText().trim().length() != 40) {
                secretField.setFocusable(true);
                secretField.requestFocus();
                JOptionPane.showMessageDialog(null,
                        "Your AWS Secret Key does not appear to be valid.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                passBool = false;
            }
        } else {
            passBool = true;
        }
        return passBool;
    }

    public boolean checkForFile() {
        boolean passBool;

        if (multiFiles == null) {
            JOptionPane.showMessageDialog(null, "Please select a file.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            passBool = false;
        } else {
            passBool = true;
        }
        return passBool;
    }

    public String getVaultField() {
        return vaultField.getText().trim();
    }

    public String getAccessField() {
        return accessField.getText().trim();
    }

    public String getSecretField() {
        return secretField.getText().trim();
    }

    public int getServerRegion() {
        return locationChoice.getSelectedIndex();
    }

    public void clearFile() {
        uploadButton.setText("Upload File(s)");
        ddText.setText("");
    }

    public void repopulateVaults(String accessString, String secretString) {

        int newLoc = locationChoice.getSelectedIndex();

        if (!(accessField.getText().trim().equals("") || secretField.getText().trim().equals(""))) {
            AmazonGlacierClient newVaultCheckClient = makeClient(accessString, secretString, newLoc);

            String marker = null;
            vaultSelector.removeAllItems();
            vaultSelector.addItem("Select Existing:");
            do {
                ListVaultsRequest lv = new ListVaultsRequest().withMarker(
                        marker).withLimit("1000");

                ListVaultsResult lvr = newVaultCheckClient.listVaults(lv);
                ArrayList<DescribeVaultOutput> vList = new ArrayList<DescribeVaultOutput>(
                        lvr.getVaultList());
                marker = lvr.getMarker();

                for (DescribeVaultOutput vault : vList) {
                    vaultSelector.addItem(vault.getVaultName());
                }

            } while (marker != null);
        }
    }

    public void centerOnScreen(int width, int height) {
        int top, left, x, y;

        // Get the screen dimension
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the location for the top left corner of the frame
        x = (screenSize.width - width) / 2;
        y = (screenSize.height - height) / 2;
        left = (x < 0) ? 0 : x;
        top = (y < 0) ? 0 : y;

        this.setBounds(left, top, width, height);
    }

    public File[] removeNullFile(File[] a) {
        ArrayList<File> removed = new ArrayList<File>();
        for (File fle : a)
            if (fle != null)
                removed.add(fle);
        return removed.toArray(new File[removed.size()]);
    }

    public File[] concatFileArray(File[] A, File[] B) {
        File[] C = new File[A.length + B.length];
        System.arraycopy(A, 0, C, 0, A.length);
        System.arraycopy(B, 0, C, A.length, B.length);

        return C;
    }

    public AmazonGlacierClient makeClient(String accessorString,
                                          String secretiveString, int regionIndex) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessorString, secretiveString);
        client = new AmazonGlacierClient(credentials);
        client.setEndpoint(Endpoint.getByIndex(regionIndex).getGlacierEndpoint());
        return client;
    }

    public static String regexClean(String statement) {
        String stmt = statement;
        String regex = "[^a-zA-Z0-9_\\-\\.]";
        String out = stmt.replaceAll(regex, "");
        return out;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String accessString = getAccessField();
        String secretString = getSecretField();
        String vaultString = getVaultField();
        int regionInt = getServerRegion();

        if (e.getSource() == newVaultButton && checkAWSFields()) {
            AmazonGlacierClient newVaultClient = makeClient(accessString, secretString, regionInt);
            AddVaultFrame avf = new AddVaultFrame(newVaultClient, regionInt);
            avf.setVisible(true);
        }
        if (e.getSource() == vaultSelector) {
            if (vaultSelector.getSelectedItem() != null) {
                if (vaultSelector.getSelectedIndex() == 0) {
                    vaultField.setText("");
                } else {
                    vaultField.setText(vaultSelector.getSelectedItem().toString());
                }
            }
        }
        if (e.getSource() == loginButton) {
            repopulateVaults(accessString, secretString);
        }
        if (e.getSource() == exitApplicationMnu) {
            System.exit(0);
        }
        if (e.getSource() == updateMnu || e.getSource() == checkUpdateButton) {
            JHyperlinkLabel.OpenURI(URL_STRING);
        }
        if (e.getSource() == saveFileMnu) {
            FileDialog fd = new FileDialog(new Frame(), "Save...", FileDialog.SAVE);
            fd.setFile("Glacier.txt");
            fd.setDirectory(curDir);
            fd.setLocation(50, 50);
            fd.setVisible(true);
            String filePath = "" + fd.getDirectory() + System.getProperty("file.separator") + fd.getFile();

            File outFile = new File(filePath);

            if (!outFile.equals("") && !outFile.equals("null")) {

                try {
                    FileReader fr = new FileReader(getLogFilenamePath(0));
                    BufferedReader br = new BufferedReader(fr);

                    FileWriter saveFile = new FileWriter(outFile.toString());

                    int count = 0;
                    boolean moreLines = true;

                    String ln1;
                    String ln2;
                    String ln3;

                    while (moreLines) {
                        ln1 = br.readLine();
                        ln2 = br.readLine();
                        ln3 = br.readLine();

                        if (ln1 == null) {
                            ln1 = "";
                        }
                        if (ln2 == null) {
                            ln2 = "";
                        }
                        if (ln3 == null) {
                            ln3 = "";
                        }

                        saveFile.write(ln1);
                        saveFile.write("\r\n");
                        saveFile.write(ln2);
                        saveFile.write("\r\n");
                        saveFile.write(ln3);
                        saveFile.write("\r\n");

                        count++;

                        if (ln3.equals("")) {
                            moreLines = false;
                            br.close();
                            saveFile.close();
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Successfully exported " + count
                                            + " archive records to "
                                            + outFile.toString(), "Export",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (FileNotFoundException e1) {
                    JOptionPane.showMessageDialog(null,
                            "Unable to locate Glacier.log", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        if (e.getSource() == viewLog || e.getSource() == logButton) {
            File f = SimpleGlacierUploader.getLogFilenamePath(getLogFileType());
            if (f.exists()) {
                JHyperlinkLabel.OpenURI("" + f.toURI());
            } else {
                JOptionPane.showMessageDialog(null,
                        "Log file " + f.getName() + " does not exist.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == deleteArchiveMnu) {
            if (checkAllFields()) {
                AmazonGlacierClient newDeleteClient = makeClient(accessString, secretString, regionInt);
                DeleteArchiveFrame daf = new DeleteArchiveFrame(newDeleteClient, vaultString, regionInt);
                daf.setVisible(true);
            }
        }
        if (e.getSource() == inventoryRequestButton) {
            if (checkAllFields()) {
                AmazonGlacierClient newInventoryClient = makeClient(accessString, secretString, regionInt);
                InventoryRequest ir = new InventoryRequest(newInventoryClient, vaultString, regionInt);
                ir.setVisible(true);
            }
        }
        if (e.getSource() == downloadRequestButton
                || e.getSource() == downloadFileMnu) {
            if (checkAllFields()) {
                AmazonGlacierClient newDownloadClient = makeClient(accessString, secretString, regionInt);
                BasicAWSCredentials credentials = new BasicAWSCredentials(accessString, secretString);
                AmazonDownloadRequest adr = new AmazonDownloadRequest(newDownloadClient, vaultString, regionInt, credentials);
                adr.setVisible(true);
            }
        }

        if (e.getSource() == aboutMnu) {
            JOptionPane.showMessageDialog(null, ABOUT_WINDOW_STRING, "About", JOptionPane.INFORMATION_MESSAGE);
        }

        if (e.getSource() == clearButton) {
            ddText.setText("");
            uploadButton.setText("Select Files");
            multiFiles = null;
        }

        if (e.getSource() == locationChoice) {
            repopulateVaults(accessString, secretString);
        }

        if (e.getSource() == selectFileButton) {
            int returnVal = fc.showOpenDialog(SimpleGlacierUploader.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                if (fc.getSelectedFile().isFile()) {
                    File[] thisFile = new File[1];
                    thisFile[0] = fc.getSelectedFile();
                    try {
                        ddText.append(thisFile[0].getCanonicalPath() + "\n");
                    } catch (java.io.IOException f) {
                    }
                    if (multiFiles != null) {
                        multiFiles = concatFileArray(multiFiles, thisFile);
                    } else {
                        multiFiles = thisFile;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, NO_DIRECTORIES_ERROR, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        }

        if (e.getSource() == uploadButton) {
            if ((checkAllFields()) && (checkForFile())) {

                SaveCurrentProperties(accessString, secretString, vaultString,
                        locationChoice.getSelectedIndex());

                SwingWorker uploadWorker = new SwingWorker() {

                    @Override
                    protected Object doInBackground() throws Exception {
                        String accessString = getAccessField();
                        String secretString = getSecretField();
                        String vaultName = getVaultField();
                        File[] uploadFileBatch = multiFiles;

                        // work out exactly how much we are going to upload
                        // so we can support a second total upload progress bar
                        long totalSize = 0;
                        long uploadedSize = 0;
                        for (File f : uploadFileBatch) {
                            totalSize += f.length();
                        }

                        int locInt = locationChoice.getSelectedIndex();
                        multiFiles = null;
                        clearFile();
                        UploadWindow uw = new UploadWindow();

                        if (uploadFileBatch.length > 0) {

                            ArrayList<String> uploadList = new ArrayList<String>();

                            for (int i = 0; i < uploadFileBatch.length; i++) {
                                // Save Current Settings to properties

                                try {
                                    Thread.sleep(100L);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }

                                ClientConfiguration config = new ClientConfiguration();
                                config.setSocketTimeout(SOCKET_TIMEOUT);
                                config.setMaxErrorRetry(MAX_RETRIES);

                                BasicAWSCredentials credentials = new BasicAWSCredentials(accessString, secretString);
                                client = new AmazonGlacierClient(credentials, config);
                                final Endpoint endpoint = Endpoint.getByIndex(locInt);
                                client.setEndpoint(endpoint.getGlacierEndpoint());
                                String locationUpped = endpoint.name();
                                String thisFile = uploadFileBatch[i].getCanonicalPath();
                                String cleanFile = regexClean(thisFile);

                                try {

                                    ArchiveTransferManager atm = new ArchiveTransferManager(client, credentials);

                                    String fileLength = Long.toString(uploadFileBatch[i].length());

                                    uw.setTitle("(" + (i + 1) + "/"
                                            + uploadFileBatch.length + ")"
                                            + " Uploading: " + thisFile);

                                    UploadResult result = atm.upload(vaultName, cleanFile, uploadFileBatch[i]);

                                    uw.addToLog("Done: " + thisFile + "\n");

                                    uploadedSize += uploadFileBatch[i].length();

                                    int percentage = (int) (((double) uploadedSize / totalSize) * 100);

                                    uw.updateProgress(percentage);

                                    Writer plainOutputLog = null;
                                    Writer plainOutputTxt = null;
                                    Writer plainOutputCsv = null;
                                    Writer plainOutputYaml = null;

                                    // write to file
                                    if (logCheckMenuItem.isSelected()) {
                                        String treeHash = TreeHashGenerator
                                                .calculateTreeHash(uploadFileBatch[i]);

                                        try {
                                            plainOutputLog = new BufferedWriter(
                                                    new FileWriter(
                                                            getLogFilenamePath(0),
                                                            true));
                                            plainOutputTxt = new BufferedWriter(
                                                    new FileWriter(
                                                            getLogFilenamePath(1),
                                                            true));
                                            plainOutputCsv = new BufferedWriter(
                                                    new FileWriter(
                                                            getLogFilenamePath(2),
                                                            true));
                                            plainOutputYaml = new BufferedWriter(
                                                    new FileWriter(
                                                            getLogFilenamePath(3),
                                                            true));

                                        } catch (IOException ex) {
                                            JOptionPane.showMessageDialog(null,
                                                    LOG_CREATION_ERROR,
                                                    "IO Error",
                                                    JOptionPane.ERROR_MESSAGE);
                                            uw.dispose();
                                            System.exit(1);
                                        }

                                        try {

                                            Date d = new Date();

                                            String thisResult = result.getArchiveId();

                                            plainOutputLog.write(System.getProperty("line.separator"));
                                            plainOutputLog.write(" | ArchiveID: " + thisResult + " ");
                                            plainOutputLog.write(System.getProperty("line.separator"));
                                            plainOutputLog.write(" | File: " + thisFile + " ");
                                            plainOutputLog.write(" | Bytes: " + fileLength + " ");
                                            plainOutputLog.write(" | Vault: " + vaultName + " ");
                                            plainOutputLog.write(" | Location: " + locationUpped + " ");
                                            plainOutputLog.write(" | Date: " + d.toString() + " ");
                                            plainOutputLog.write(" | Hash: " + treeHash + " ");
                                            plainOutputLog.write(System.getProperty("line.separator"));
                                            plainOutputLog.close();

                                            plainOutputTxt.write(System.getProperty("line.separator"));
                                            plainOutputTxt.write(" | ArchiveID: " + thisResult + " ");
                                            plainOutputTxt.write(System.getProperty("line.separator"));
                                            plainOutputTxt.write(" | File: " + thisFile + " ");
                                            plainOutputTxt.write(" | Bytes: " + fileLength + " ");
                                            plainOutputTxt.write(" | Vault: " + vaultName + " ");
                                            plainOutputTxt.write(" | Location: " + locationUpped + " ");
                                            plainOutputTxt.write(" | Date: " + d.toString() + " ");
                                            plainOutputTxt.write(" | Hash: " + treeHash + " ");
                                            plainOutputTxt.write(System.getProperty("line.separator"));
                                            plainOutputTxt.close();

                                            plainOutputCsv.write("" + thisResult + ",");
                                            plainOutputCsv.write("" + thisFile + ",");
                                            plainOutputCsv.write("" + fileLength + ",");
                                            plainOutputCsv.write("" + vaultName + ",");
                                            plainOutputCsv.write("" + locationUpped + ",");
                                            plainOutputCsv.write("" + d.toString() + ",");
                                            plainOutputCsv.write("" + treeHash + ",");
                                            plainOutputCsv.write(System.getProperty("line.separator"));
                                            plainOutputCsv.close();

                                            plainOutputYaml.write(System.getProperty("line.separator"));
                                            plainOutputYaml.write("-  ArchiveID: \"" + thisResult + "\"" + System
                                                    .getProperty("line.separator"));
                                            plainOutputYaml.write("   File:      \"" + thisFile + "\"" + System
                                                    .getProperty("line.separator"));
                                            plainOutputYaml.write("   Bytes:     \"" + fileLength + "\"" + System
                                                    .getProperty("line.separator"));
                                            plainOutputYaml.write("   Vault:     \"" + vaultName + "\"" + System
                                                    .getProperty("line.separator"));
                                            plainOutputYaml.write("   Location:  \"" + locationUpped + "\"" + System
                                                    .getProperty("line.separator"));
                                            plainOutputYaml.write("   Date:      \"" + d.toString() + "\"" + System
                                                    .getProperty("line.separator"));
                                            plainOutputYaml.write("   Hash:      \"" + treeHash + "\"" + System
                                                    .getProperty("line.separator"));
                                            plainOutputYaml.close();

                                            uploadList
                                                    .add("Successfully uploaded " + thisFile
                                                            + " to vault " + vaultName
                                                            + " at " + locationUpped
                                                            + ". Bytes: " + fileLength
                                                            + ". ArchiveID Logged.\n");
                                        }

                                        catch (IOException c) {
                                            JOptionPane.showMessageDialog(null,
                                                    LOG_WRITE_ERROR,
                                                    "IO Error",
                                                    JOptionPane.ERROR_MESSAGE);
                                            uw.dispose();
                                            System.exit(1);
                                        }

                                    } else {
                                        JOptionPane.showMessageDialog(
                                                null,
                                                "Upload Complete!\nArchive ID: "
                                                        + result.getArchiveId()
                                                        + "\nIt may take some time for Amazon to update the inventory.",
                                                "Uploaded",
                                                JOptionPane.INFORMATION_MESSAGE);
                                        multiFiles = null;
                                        uw.dispose();
                                    }

                                    clearFile();

                                } catch (Exception h) {
                                    if (logCheckMenuItem.isSelected()) {
                                        writeToErrorLog(h, thisFile);
                                    }
                                    JOptionPane.showMessageDialog(null, "" + h, "Error", JOptionPane.ERROR_MESSAGE);
                                    uw.dispose();

                                }

                            }
                            StringBuilder sb = new StringBuilder();
                            for (int j = 0; j < uploadFileBatch.length; j++) {
                                sb.append(uploadList.get(j));
                            }
                            uw.dispose();

                            // Move the actual results string to a JTextArea
                            JTextArea uploadCompleteMsg = new JTextArea("Upload Complete! \n" + sb);
                            uploadCompleteMsg.setLineWrap(true);
                            uploadCompleteMsg.setWrapStyleWord(true);
                            uploadCompleteMsg.setEditable(false);

                            // Put the JTextArea in a JScollPane and present that in the JOptionPane
                            JScrollPane uploadCompleteScroll = new JScrollPane(uploadCompleteMsg);
                            uploadCompleteScroll.setPreferredSize(new Dimension(500, 400));
                            JOptionPane.showMessageDialog(null,
                                    uploadCompleteScroll, "Uploaded",
                                    JOptionPane.INFORMATION_MESSAGE);
                            // Close the JProgressBar
                            multiFiles = null;
                            clearFile();
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "This wasn't supposed to happen.", "Bug!",
                                    JOptionPane.ERROR_MESSAGE);
                            uw.dispose();

                        }

                        return null;
                    }

                    private void writeToErrorLog(Exception h, String thisFile) {
                        String thisError = h.toString();

                        Writer errorOutputLog = null;
                        try {
                            errorOutputLog = new BufferedWriter(new FileWriter(
                                    getLogFilenamePath(4), true));
                        } catch (Exception badLogCreate) {
                            JOptionPane.showMessageDialog(null,
                                    LOG_CREATION_ERROR, "IO Error",
                                    JOptionPane.ERROR_MESSAGE);
                            System.exit(1);
                        }
                        try {
                            Date d = new Date();

                            errorOutputLog.write(System.getProperty("line.separator"));
                            errorOutputLog.write("" + d.toString() + ": \"" + thisFile + "\" *ERROR* " + thisError);
                            errorOutputLog.write(System.getProperty("line.separator"));

                        } catch (Exception badLogWrite) {
                            JOptionPane.showMessageDialog(null,
                                    LOG_WRITE_ERROR, "IO Error",
                                    JOptionPane.ERROR_MESSAGE);
                            System.exit(1);
                        }
                    }
                };
                uploadWorker.execute();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        SimpleGlacierUploader g = new SimpleGlacierUploader();
        g.setBounds(300, 300, 650, 475);
        g.setTitle("Simple Amazon Glacier Uploader " + versionNumber);
        g.setVisible(true);
    }
}
