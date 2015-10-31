/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu.ui;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.CreateVaultRequest;
import com.amazonaws.services.glacier.model.CreateVaultResult;
import com.brianmcmichael.sagu.Endpoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AddVaultFrame extends JFrame implements ActionListener, WindowListener {
    
	private static final long serialVersionUID = 1L;
	private JTextField jtfAddField;
    private JButton jbtAdd, jbtBack;

    private AmazonGlacierClient addClient;

    //Constructor
    public AddVaultFrame(AmazonGlacierClient client, int region) {
        super("Add Vault");

        int width = 200;
        int height = 170;

        Color wc = Color.WHITE;

        this.addClient = client;

        JLabel label1 = new JLabel("Name of Vault to add to " + Endpoint.getTitleByIndex(region) + ":");
        jtfAddField = new JTextField(30);
        jbtAdd = new JButton("Add");
        jbtBack = new JButton("Back");

        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout());
        p1.add(label1);
        p1.setBackground(wc);


        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout());
        p2.add(jtfAddField);
        jtfAddField.addMouseListener(new ContextMenuMouseListener());
        jtfAddField.setFocusable(true);
        p2.setBackground(wc);

        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout());
        p3.add(jbtAdd);
        jbtAdd.addActionListener(this);
        jbtAdd.setBackground(wc);
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

        // Prepare for display
        pack();
        if (width < getWidth()) {             // prevent setting width too small
            width = getWidth();
        }
        if (height < getHeight()) {           // prevent setting height too small
            height = getHeight();
        }
        centerOnScreen(width, height);
        jtfAddField.setText("");
        jtfAddField.requestFocus();

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
        jtfAddField.setText("");
        jtfAddField.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbtAdd) {
            if ((jtfAddField.getText().trim().equals(""))) {
                JOptionPane.showMessageDialog(null, "Enter the name of the vault to add.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {

                try {
                    String vaultToAdd = jtfAddField.getText().trim();

                    //TODO Limit to valid chars

                    // Add the archive.

                    CreateVaultRequest cvreq = new CreateVaultRequest(vaultToAdd);

                    CreateVaultResult cvres = addClient.createVault(cvreq);

                    JOptionPane.showMessageDialog(null, "Added vault " + cvres.toString() + " successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();

                } catch (AmazonServiceException k) {
                    JOptionPane.showMessageDialog(null, "The server returned an error.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (AmazonClientException i) {
                    JOptionPane.showMessageDialog(null, "Client Error. Check that all fields are correct. Archive not added.", "Error", JOptionPane.ERROR_MESSAGE);

                } catch (Exception j) {
                    JOptionPane.showMessageDialog(null, "Vault not Added. Unspecified Error.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                jtfAddField.setText("");
                jtfAddField.requestFocus();
            }

        } else if (e.getSource() == jbtBack) {
            this.setVisible(false);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please choose a valid action.");
        }

    }


}