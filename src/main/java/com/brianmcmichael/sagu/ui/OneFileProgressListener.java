/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu.ui;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressTracker;

/**
 * Listener for upload progress reporting of one file (currently being uploaded).
 */
public class OneFileProgressListener extends ProgressTracker {

    private final UploadWindow uploadWindow;
    private final long totalBytesToTransfer;

    public OneFileProgressListener(final UploadWindow uploadWindow, final long totalBytesToTransfer) {
        this.uploadWindow = uploadWindow;
        this.totalBytesToTransfer = totalBytesToTransfer;
    }

    @Override
    public void progressChanged(final ProgressEvent progressEvent) {
        super.progressChanged(progressEvent);
        final int percents = getPercents(getProgress().getRequestBytesTransferred());
        uploadWindow.updateOneFileProgress(percents);
    }

    int getPercents(final double bytesTransferred) {
        return totalBytesToTransfer <= 0
                ? 0
                : (int) ((bytesTransferred / (double) totalBytesToTransfer) * 100.0);
    }
}
