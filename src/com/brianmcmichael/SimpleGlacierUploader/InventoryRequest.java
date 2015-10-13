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

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InventoryRequest extends JFrame implements ActionListener, WindowListener {

    public static final String DOWNLOAD_NOTICE = "<html><body><br>Your data is stored on Glacier Servers by ArchiveID.<br>This function requests a list of Glacier ArchiveID's within a particular vault.<br><br>>> Verify that the server and vault on the previous page match the vault<br> you are attmpting to obtain the inventory from.<br>>> Once you click the 'retrieve' button it will take approximately 4 hours <br>for Amazon to process your request.<br>>> Once your files have been prepared your download will begin automatically.<br>>> You will be notified when your inventory had been retrieved successfully.<br><br> WARNING: <br>Closing the program during a retrieval request will cancel your download.</body><html>";
    public static final String curDir = System.getProperty("user.dir");

    private AmazonGlacierClient irClient;
    private String irVault;
    private int irRegion;
    private JButton jbtInventoryRequest, jbtBack;

    //Wait between status requests.
    private long WAIT_TIME = 600000L;

    private int width = 200;
    private int height = 170;

    private Color wc = Color.WHITE;

    public InventoryRequest(AmazonGlacierClient thisClient, String thisVault, int thisRegion) {
        super("Request Inventory");

        this.irClient = thisClient;
        this.irVault = thisVault;
        this.irRegion = thisRegion;

        JLabel label1 = new JLabel("Request Archive Inventory from " + irVault + " in server region " + SimpleGlacierUploader.getRegion(irRegion) + ":");
        JLabel label2 = new JLabel(DOWNLOAD_NOTICE);
        jbtInventoryRequest = new JButton("Request Inventory");
        jbtBack = new JButton("Back");

        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout());
        p1.add(label1);
        p1.setBackground(wc);


        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        p2.add(label2, BorderLayout.CENTER);
        label2.setHorizontalAlignment(JLabel.CENTER);
        p2.setBackground(wc);

        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout());
        p3.add(jbtInventoryRequest);
        jbtInventoryRequest.addActionListener(this);
        jbtInventoryRequest.setBackground(wc);
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

        // Register listeners
        addWindowListener(this);

        // Prepare for display
        pack();
        if (width < getWidth()) {             // prevent setting width too small
            width = getWidth();
        }
        if (height < getHeight()) {           // prevent setting height too small
            height = getHeight();
        }
        centerOnScreen(width, height);
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
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == jbtInventoryRequest) {
            SwingWorker inventoryWorker = new SwingWorker() {

                @Override
                protected Object doInBackground() throws Exception {

                    //Create dumb progressbar
                    Date d = new Date();
                    JFrame inventoryFrame = new JFrame("Waiting for inventory");
                    {
                        inventoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(d);
                        cal.add(Calendar.MINUTE, 250);
                        String doneString = cal.getTime().toString();
                        JLabel doneTimeLabel = new JLabel("<html><body>Inventory of vault " + irVault + " requested.<br>Estimated completion by " + doneString + "</html></body>");
                        final JProgressBar dumJProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
                        dumJProgressBar.setIndeterminate(true);
                        inventoryFrame.add(dumJProgressBar, BorderLayout.NORTH);
                        inventoryFrame.add(doneTimeLabel, BorderLayout.CENTER);
                        inventoryFrame.setBackground(wc);
                        inventoryFrame.setSize(300, 60);
                    }
                    centerDefineFrame(inventoryFrame, 500, 100);
                    inventoryFrame.setVisible(true);

                    try {
                        JobParameters jParameters = new JobParameters()
                                .withType("inventory-retrieval");

                        InitiateJobRequest initJobRequest = new InitiateJobRequest()
                                .withVaultName(irVault)
                                .withJobParameters(jParameters);

                        InitiateJobResult initJobResult = irClient.initiateJob(initJobRequest);
                        String thisJobId = initJobResult.getJobId();

                        Thread.sleep(12600000);

                        Boolean success = waitForJob(irClient, irVault, thisJobId);

                        while (!success) {
                            Thread.sleep(WAIT_TIME);
                            success = waitForJob(irClient, irVault, thisJobId);
                        }

                        GetJobOutputRequest gjoRequest = new GetJobOutputRequest()
                                .withVaultName(irVault)
                                .withJobId(thisJobId);
                        GetJobOutputResult gjoResult = irClient.getJobOutput(gjoRequest);

                        Format formatter = new SimpleDateFormat("yyyyMMMdd_HHmmss");
                        String fileDate = formatter.format(d);

                        String fileName = irVault + fileDate + ".txt";

                        String filePath = "" + curDir + System.getProperty("file.separator") + fileName;

                        FileWriter fileStream = new FileWriter(filePath);

                        BufferedWriter out = new BufferedWriter(fileStream);

                        BufferedReader in = new BufferedReader(new InputStreamReader(gjoResult.getBody()));

                        String inputLine;

                        while ((inputLine = in.readLine()) != null) {
                            out.write(inputLine);
                        }
                        out.close();

                        inventoryFrame.setVisible(false);

                        JOptionPane.showMessageDialog(null, "Successfully exported " + irVault + " inventory to " + filePath.toString(), "Saved", JOptionPane.INFORMATION_MESSAGE);

                        return null;


                    } catch (AmazonServiceException k) {
                        JOptionPane.showMessageDialog(null, "The server returned an error. Files will not be inventoried for 24 hours after upload. Also check that correct location of vault has been set on the previous page.", "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("" + k);
                        inventoryFrame.setVisible(false);
                    } catch (AmazonClientException i) {
                        JOptionPane.showMessageDialog(null, "Client Error. Check that all fields are correct. Inventory not requested.", "Error", JOptionPane.ERROR_MESSAGE);
                        inventoryFrame.setVisible(false);
                    } catch (Exception j) {
                        JOptionPane.showMessageDialog(null, "Inventory not found. Unspecified Error.", "Error", JOptionPane.ERROR_MESSAGE);
                        inventoryFrame.setVisible(false);
                    }
                    return null;


                }
            };
            inventoryWorker.execute();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            this.setVisible(false);
            dispose();
        } else if (e.getSource() == jbtBack) {
            this.setVisible(false);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please choose a valid action.");
        }

    }

    private boolean waitForJob(AmazonGlacierClient client, String vaultName, String jobId) {
        boolean inventoryReady = false;
        try {
            DescribeJobRequest djRequest = new DescribeJobRequest(vaultName, jobId);
            DescribeJobResult djResult = client.describeJob(djRequest);
            inventoryReady = djResult.getCompleted();
        } catch (Exception e) {
        }

        return inventoryReady;
    }

    void centerDefineFrame(JFrame f, int width, int height) {

        Toolkit tk = Toolkit.getDefaultToolkit();

        // Get the screen dimensions.
        Dimension screen = tk.getScreenSize();

        //Set frame size
        f.setSize(width, height);

        // And place it in center of screen.
        int lx = (int) (screen.getWidth() * 3 / 8);
        int ly = (int) (screen.getHeight() * 3 / 8);
        f.setLocation(lx, ly);
    }

}
