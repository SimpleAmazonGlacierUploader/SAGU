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

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sqs.AmazonSQSClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

class AmazonDownloadRequest extends JFrame implements ActionListener, WindowListener {

    public static final String DOWNLOAD_NOTICE = "<html><body><br>Amazon stores your data as a stream of data by archive ID.<br>This information can be found in your log file.<br><br>>> Ensure that Amazon SQS and SNS messaging services are enabled in your AWS console.<br><br>>> Verify that the server and vault on the previous page match the archive<br> you are attmpting to retrieve and enter the archive ID.<br>>> You will then be prompted to select the file name and the location where <br>you would like to save the data.<br>>> Once you click the 'retrieve' button it will take approximately 4 hours <br>for Amazon to process your request.<br>>> Once your files have been prepared your download will begin automatically.<br>>> You will be notified when your download has completed successfully.<br><br> WARNING: <br>Closing the program during a retrieval request will cancel your download.</body><html>";

    private JTextField jtfDownloadField;
    private JButton jbtDownload, jbtBack;

    private AmazonGlacierClient dlClient;
    private BasicAWSCredentials dlCredentials;
    private int locationChoice;
    private String dlVault;

    private JFileChooser fc = new JFileChooser();

    private String archiveId;

    // Constructor
    public AmazonDownloadRequest(AmazonGlacierClient client, String vaultName,
                                 int region, BasicAWSCredentials credentials) {
        super("Request Download");

        int width = 200;
        int height = 170;

        Color wc = Color.WHITE;

        dlClient = client;
        dlVault = vaultName;
        locationChoice = region;
        dlCredentials = credentials;

        JLabel label1 = new JLabel("ArchiveID to Download from " + dlVault
                + " in server region "
                + Endpoint.getTitleByIndex(region) + ":");
        jtfDownloadField = new JTextField(100);
        JLabel label2 = new JLabel(DOWNLOAD_NOTICE);
        jbtDownload = new JButton("Request Download");
        jbtBack = new JButton("Back");

        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setDialogTitle("Save File As");

        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout());
        p1.add(label1);
        p1.setBackground(wc);

        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        p2.add(jtfDownloadField, BorderLayout.NORTH);
        jtfDownloadField.addMouseListener(new ContextMenuMouseListener());
        jtfDownloadField.setFocusable(true);
        p2.add(label2, BorderLayout.CENTER);
        label2.setHorizontalAlignment(JLabel.CENTER);
        p2.setBackground(wc);

        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout());
        p3.add(jbtDownload);
        jbtDownload.addActionListener(this);
        jbtDownload.setBackground(wc);
        p3.add(jbtBack);
        jbtBack.addActionListener(this);
        jbtBack.setBackground(wc);
        p3.setBackground(wc);

        JPanel p4 = new JPanel();
        p4.setBackground(wc);
        p4.setLayout(new BorderLayout());
        p4.add(p1, BorderLayout.NORTH);
        p4.add(p2, BorderLayout.CENTER);
        p4.add(p3, BorderLayout.SOUTH);

        setContentPane(p4);

        // Register listeners
        addWindowListener(this);

        // Prepare for display
        pack();
        if (width < getWidth()) // prevent setting width too small
            width = getWidth();
        if (height < getHeight()) // prevent setting height too small
            height = getHeight();
        centerOnScreen(width, height);
        jtfDownloadField.setText("");
        jtfDownloadField.requestFocus();

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
        jtfDownloadField.setText("");
        jtfDownloadField.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == jbtDownload) {
            archiveId = jtfDownloadField.getText().trim();
            if ((archiveId.equals(""))) {
                JOptionPane.showMessageDialog(null,
                        "Enter the Archive ID of the file to be requested.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                SwingWorker downloadWorker = new SwingWorker() {

                    private String archiveId = jtfDownloadField.getText()
                            .trim();

                    @Override
                    protected Object doInBackground() throws Exception {

                        // Create dumb progressbar
                        JFrame downloadFrame = new JFrame("Downloading");
                        {
                            downloadFrame
                                    .setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            final JProgressBar dumJProgressBar = new JProgressBar(
                                    JProgressBar.HORIZONTAL);
                            dumJProgressBar.setIndeterminate(true);
                            downloadFrame.add(dumJProgressBar,
                                    BorderLayout.NORTH);
                            downloadFrame.setSize(300, 60);
                        }
                        centerDefineFrame(downloadFrame, 300, 50);

                        String archiveId = jtfDownloadField.getText().trim();
                        try {
                            String vaultName = dlVault;

                            FileDialog fd = new FileDialog(new Frame(),
                                    "Save Archive As...", FileDialog.SAVE);
                            fd.setFile("Save Archive As...");
                            fd.setDirectory(System.getProperty("user.dir"));
                            fd.setLocation(50, 50);
                            fd.setVisible(true);

                            String filePath = "" + fd.getDirectory()
                                    + System.getProperty("file.separator")
                                    + fd.getFile();

                            File outFile = new File(filePath);

                            if (outFile != null) {
                                downloadFrame.setTitle("Downloading "
                                        + outFile.toString());
                                downloadFrame.setVisible(true);

                                final Endpoint endpoint = Endpoint.getByIndex(locationChoice);

                                AmazonSQSClient dlSQS = new AmazonSQSClient(dlCredentials);
                                AmazonSNSClient dlSNS = new AmazonSNSClient(dlCredentials);

                                dlSQS.setEndpoint(endpoint.getSQSEndpoint());
                                dlSNS.setEndpoint(endpoint.getSNSEndpoint());

                                // ArchiveTransferManager atm = new
                                // ArchiveTransferManager(dlClient,
                                // dlCredentials);
                                ArchiveTransferManager atm = new ArchiveTransferManager(
                                        dlClient, dlSQS, dlSNS);

                                atm.download("-", vaultName, archiveId, outFile);

                                JOptionPane.showMessageDialog(
                                        null,
                                        "Sucessfully downloaded "
                                                + outFile.toString(),
                                        "Success",
                                        JOptionPane.INFORMATION_MESSAGE);
                                downloadFrame.setVisible(false);
                            }
                        } catch (AmazonServiceException k) {
                            JOptionPane
                                    .showMessageDialog(
                                            null,
                                            "The server returned an error. Wait 24 hours after submitting an archive to attempt a download. Also check that correct location of archive has been set on the previous page.",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                            System.out.println("" + k);
                            downloadFrame.setVisible(false);
                        } catch (AmazonClientException i) {
                            JOptionPane
                                    .showMessageDialog(
                                            null,
                                            "Client Error. Check that all fields are correct. Archive not downloaded.",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                            downloadFrame.setVisible(false);
                        } catch (Exception j) {
                            JOptionPane.showMessageDialog(null,
                                    "Archive not found. Unspecified Error.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            downloadFrame.setVisible(false);
                        }
                        return null;
                    }
                };
                downloadWorker.execute();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                this.setVisible(false);
                dispose();
            }

        } else if (e.getSource() == jbtBack) {
            this.setVisible(false);
            dispose();
        } else {
            JOptionPane
                    .showMessageDialog(this, "Please choose a valid action.");
        }

    }

    void centerDefineFrame(JFrame f, int width, int height) {

        Toolkit tk = Toolkit.getDefaultToolkit();

        // Get the screen dimensions.
        Dimension screen = tk.getScreenSize();

        // Set frame size
        f.setSize(width, height);

        // And place it in center of screen.
        int lx = (int) (screen.getWidth() * 3 / 8);
        int ly = (int) (screen.getHeight() * 3 / 8);
        f.setLocation(lx, ly);
    } // centerFrame

}