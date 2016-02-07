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

/**
 * Dialog window for upload progress displaying.
 */
public class UploadWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JTextArea finishedFilesArea = new JTextArea();
    private final JProgressBar allFilesProgressBar = new JProgressBar(0, 100);
    private final JProgressBar oneFileProgressBar = new JProgressBar(0, 100);

    /**
     * Initializes and displays the Upload Window.
     */
    public UploadWindow() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        initUI();

        setVisible(true);
    }

    /**
     * Adds file (path string) to the list of finished files.
     *
     * @param filePath file path string to be added
     */
    public void addToFinishedFiles(final String filePath) {
        finishedFilesArea.append(filePath);
    }

    /**
     * Updates total (all files) progress.
     *
     * @param percentage progress percentage to be displayed
     */
    public void updateAllFilesProgress(final int percentage) {
        SwingUtilities.invokeLater(() -> allFilesProgressBar.setValue(percentage));
    }

    /**
     * Updates one (current) file progress.
     *
     * @param percentage progress percentage to be displayed
     */
    public void updateOneFileProgress(final int percentage) {
        SwingUtilities.invokeLater(() -> oneFileProgressBar.setValue(percentage));
    }

    private void initUI() {
        setTitle("Uploading");
        setLayout(new GridBagLayout());

        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);
        final JLabel oneFileLabel = new JLabel();
        oneFileLabel.setText("Current file:");
        add(oneFileLabel, constraints);

        constraints.weightx = 10;
        constraints.gridx = 1;
        add(oneFileProgressBar, constraints);

        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        final JLabel allFilesLabel = new JLabel();
        allFilesLabel.setText("All files:");
        add(allFilesLabel, constraints);

        constraints.weightx = 10;
        constraints.gridx = 1;
        add(allFilesProgressBar, constraints);

        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(0, 0, 0, 0);
        final JSeparator separator = new JSeparator();
        add(separator, constraints);

        constraints.insets = new Insets(5, 5, 0, 5);
        constraints.gridy = 3;
        final JLabel finishedFilesLabel = new JLabel();
        finishedFilesLabel.setText("Finished files:");
        add(finishedFilesLabel, constraints);

        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        add(new JScrollPane(finishedFilesArea), constraints);

        finishedFilesArea.setEditable(false);
    }
}
