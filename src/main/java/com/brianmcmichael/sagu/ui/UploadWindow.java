/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu.ui;

import javax.swing.*;
import java.awt.*;

public class UploadWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextArea uploadText = new JTextArea();

    private JScrollPane uploadScroll = new JScrollPane(uploadText);
    private JProgressBar totalProgressBar = new JProgressBar(0, 100);

    public UploadWindow() {
        setTitle("Uploading");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final JProgressBar dumJProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
        dumJProgressBar.setIndeterminate(true);
        add(dumJProgressBar, BorderLayout.NORTH);
        add(uploadScroll, BorderLayout.CENTER);
        add(totalProgressBar, BorderLayout.SOUTH);
        setSize(500, 400);
        uploadText.setEditable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void addToLog(String text) {
        uploadText.append(text);
    }

    public void updateProgress(final int percentage) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                totalProgressBar.setValue(percentage);
            }
        });
    }
}
