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

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LeftButtonBar extends JPanel {
    private JTextField textField;
    private JPasswordField passwordField;
    private S s;

    private String accessKey;
    private char[] secretKey;
    private Endpoint location;
    private String vault;

    /**
     * Create the panel.
     */
    public LeftButtonBar() {
        setBorder(new SoftBevelBorder(BevelBorder.RAISED, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE));
        setBackground(Color.WHITE);
        setLayout(null);

        JLabel lblAwsAccessKey = new JLabel("AWS Access Key");
        lblAwsAccessKey.setFont(new Font("Arial", lblAwsAccessKey.getFont().getStyle(), lblAwsAccessKey.getFont().getSize()));
        lblAwsAccessKey.setBounds(10, 11, 180, 14);
        add(lblAwsAccessKey);

        textField = new JTextField();
        lblAwsAccessKey.setLabelFor(textField);
        textField.setToolTipText("Enter your AWS Access Key here.");
        textField.setBounds(10, 36, 180, 20);
        add(textField);
        textField.setColumns(10);

        JLabel getAWSKeys = new JLabel("");
        getAWSKeys.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OpenURI.open(s.GET_AWS_CREDENTIALS);
            }
        });
        getAWSKeys.setFont(new Font("Arial", getAWSKeys.getFont().getStyle(), getAWSKeys.getFont().getSize()));
        getAWSKeys.setToolTipText("Get AWS Keys");
        getAWSKeys.setIcon(new ImageIcon(LeftButtonBar.class.getResource("/Silk/world_link.png")));
        getAWSKeys.setBounds(174, 9, 16, 16);
        add(getAWSKeys);

        JLabel lblAwsSecretKey = new JLabel("AWS Secret Key");
        lblAwsSecretKey.setFont(new Font("Arial", lblAwsSecretKey.getFont().getStyle(), lblAwsSecretKey.getFont().getSize()));
        lblAwsSecretKey.setBounds(10, 67, 180, 14);
        add(lblAwsSecretKey);

        passwordField = new JPasswordField();
        lblAwsSecretKey.setLabelFor(passwordField);
        passwordField.setToolTipText("Enter your AWS Secret Key Here");
        passwordField.setBounds(10, 92, 180, 20);
        add(passwordField);

        JLabel lblServerLocation = new JLabel("Set Server Location");
        lblServerLocation.setFont(new Font("Arial", lblServerLocation.getFont().getStyle(), lblServerLocation.getFont().getSize()));
        lblServerLocation.setBounds(10, 123, 180, 14);
        add(lblServerLocation);

        JComboBox comboBox = new JComboBox();
        comboBox.setToolTipText("Select a server location.");
        comboBox.setBackground(Color.WHITE);
        comboBox.setBounds(10, 148, 154, 20);
        add(comboBox);

        JLabel lblRefresh = new JLabel("");
        lblRefresh.setToolTipText("Refresh vaults on the selected server.");
        lblRefresh.setIcon(new ImageIcon(LeftButtonBar.class.getResource("/Silk/arrow_refresh.png")));
        lblRefresh.setBounds(174, 148, 16, 16);
        add(lblRefresh);

        JLabel lblSelectOrCreate = new JLabel("Select or Create a Vault");
        lblSelectOrCreate.setFont(new Font("Arial", lblSelectOrCreate.getFont().getStyle(), lblSelectOrCreate.getFont().getSize()));
        lblSelectOrCreate.setBounds(10, 179, 180, 14);
        add(lblSelectOrCreate);

        JComboBox comboBox_1 = new JComboBox();
        comboBox_1.setBackground(Color.WHITE);
        comboBox_1.setBounds(10, 204, 154, 20);
        add(comboBox_1);

        JLabel lblCreateVault = new JLabel("");
        lblCreateVault.setToolTipText("Create a new vault on the selected server.");
        lblCreateVault.setIcon(new ImageIcon(LeftButtonBar.class.getResource("/Silk/add.png")));
        lblCreateVault.setBounds(174, 204, 16, 16);
        add(lblCreateVault);

    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public char[] getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(char[] secretKey) {
        this.secretKey = secretKey;
    }

    public Endpoint getEndpointLocation() {
        return location;
    }

    public void setEndpointLocation(Endpoint location) {
        this.location = location;
    }

    public String getVault() {
        return vault;
    }

    public void setVault(String vault) {
        this.vault = vault;
    }
}
