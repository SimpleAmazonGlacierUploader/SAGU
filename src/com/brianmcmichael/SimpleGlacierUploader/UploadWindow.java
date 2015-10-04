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
import java.awt.*;

public class UploadWindow extends JFrame {

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
