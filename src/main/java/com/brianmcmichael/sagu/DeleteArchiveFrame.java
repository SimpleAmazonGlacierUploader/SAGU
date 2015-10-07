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
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.DeleteArchiveRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class DeleteArchiveFrame extends JFrame implements ActionListener, WindowListener {

    private JTextField jtfDeleteField;
    private JButton jbtDelete, jbtBack;

    private AmazonGlacierClient deleteClient;
    private String deleteVault;

    //Constructor
    public DeleteArchiveFrame(AmazonGlacierClient client, String vaultName, int region) {
        super("Delete Archive");

        int width = 200;
        int height = 170;

        Color wc = Color.WHITE;

        deleteClient = client;
        deleteVault = vaultName;

        JLabel label1 = new JLabel("ArchiveID to Delete from " + Endpoint.getTitleByIndex(region) + ":");
        jtfDeleteField = new JTextField(100);
        jbtDelete = new JButton("Delete");
        jbtBack = new JButton("Back");

        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout());
        p1.add(label1);
        p1.setBackground(wc);


        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout());
        p2.add(jtfDeleteField);
        jtfDeleteField.addMouseListener(new ContextMenuMouseListener());
        jtfDeleteField.setFocusable(true);
        p2.setBackground(wc);

        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout());
        p3.add(jbtDelete);
        jbtDelete.addActionListener(this);
        jbtDelete.setBackground(wc);
        p3.add(jbtBack);
        jbtBack.addActionListener(this);
        jbtBack.setBackground(wc);
        p3.setBackground(wc);

        JPanel p4 = new JPanel();
        p4.setLayout(new BorderLayout());
        p4.setBackground(wc);
        p4.add(p1, BorderLayout.NORTH);
        p4.add(p2, BorderLayout.CENTER);
        p4.add(p3, BorderLayout.SOUTH);

        setContentPane(p4);

        // Prepare for display
        pack();
        if (width < getWidth())                // prevent setting width too small
            width = getWidth();
        if (height < getHeight())            // prevent setting height too small
            height = getHeight();
        centerOnScreen(width, height);
        jtfDeleteField.setText("");
        jtfDeleteField.requestFocus();

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
        jtfDeleteField.setText("");
        jtfDeleteField.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbtDelete) {
            if ((jtfDeleteField.getText().trim().equals(""))) {
                JOptionPane.showMessageDialog(null, "Enter the Archive ID of the file to be deleted.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {

                try {
                    String archiveId = jtfDeleteField.getText().trim();

                    //Banish the extra chars printed in early logs.
                    String sendThis = archiveId.replaceAll("[^\\p{Print}]", "");

                    String vaultName = deleteVault;

                    // Delete the archive.
                    deleteClient.deleteArchive(new DeleteArchiveRequest()
                            .withVaultName(vaultName)
                            .withArchiveId(sendThis));

                    JOptionPane.showMessageDialog(null, "Deleted archive successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                } catch (AmazonServiceException k) {
                    JOptionPane.showMessageDialog(null, "The server returned an error. Wait 24 hours after submitting an archive to attempt a delete. Also check that correct location of archive has been set on the previous page.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("" + k);
                } catch (AmazonClientException i) {
                    JOptionPane.showMessageDialog(null, "Client Error. Check that all fields are correct. Archive not deleted.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception j) {
                    JOptionPane.showMessageDialog(null, "Archive not deleted. Unspecified Error.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                jtfDeleteField.setText("");
                jtfDeleteField.requestFocus();
            }

        } else if (e.getSource() == jbtBack) {
            this.setVisible(false);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please choose a valid action.");
        }

    }

}