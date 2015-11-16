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

	private final JTextArea uploadText = new JTextArea();
    private final JProgressBar allFilesProgressBar = new JProgressBar(0, 100);
    private final JProgressBar oneFileProgressBar = new JProgressBar(0, 100);

    public UploadWindow() {
        setTitle("Uploading");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        oneFileProgressBar.setIndeterminate(true);
        add(oneFileProgressBar, BorderLayout.NORTH);
        add(new JScrollPane(uploadText), BorderLayout.CENTER);
        add(allFilesProgressBar, BorderLayout.SOUTH);
        setSize(500, 400);
        uploadText.setEditable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void addToLog(String text) {
        uploadText.append(text);
    }

    public void updateAllFilesProgress(final int percentage) {
        SwingUtilities.invokeLater(() -> allFilesProgressBar.setValue(percentage));
    }

    public void updateOneFileProgress(final int percentage) {
        SwingUtilities.invokeLater(() -> oneFileProgressBar.setValue(percentage));
    }
}
