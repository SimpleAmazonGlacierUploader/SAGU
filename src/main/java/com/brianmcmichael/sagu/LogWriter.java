/*
 * Simple Amazon Glacier Uploader - GUI client for Amazon Glacier
 * Copyright (C) 2012-2015 Brian L. McMichael, Libor Rysavy and other contributors
 *
 * This program is free software licensed under GNU General Public License
 * found in the LICENSE file in the root directory of this source tree.
 */

package com.brianmcmichael.sagu;

import java.io.*;
import java.util.Date;

/**
 * Handles writing logs to files
 */
public class LogWriter {

    private static final String LOG_FILE_NAME_LOG = "Glacier.log";
    private static final String LOG_FILE_NAME_TXT = "Glacier.txt";
    private static final String LOG_FILE_NAME_CSV = "Glacier.csv";
    private static final String LOG_FILE_NAME_YAML = "Glacier.yaml";
    private static final String LOG_FILE_NAME_ERR = "GlacierErrors.txt";

    private final Writer plainOutputLog;
    private final Writer plainOutputTxt;
    private final Writer plainOutputCsv;
    private final Writer plainOutputYaml;

    /**
     * Initializes log writers.
     *
     * @param appProperties Application properties (to get directory for log files)
     * @throws IOException If an I/O error occurs
     */
    public LogWriter(final AppProperties appProperties) throws IOException {
        plainOutputLog = new BufferedWriter(new FileWriter(getLogFile(0, appProperties), true));
        plainOutputTxt = new BufferedWriter(new FileWriter(getLogFile(1, appProperties), true));
        plainOutputCsv = new BufferedWriter(new FileWriter(getLogFile(2, appProperties), true));
        plainOutputYaml = new BufferedWriter(new FileWriter(getLogFile(3, appProperties), true));
    }

    /**
     * Return the {@link File} object of log file by type of the log. The file is located in directory from properties.
     *
     * @param logType    log type
     * @param properties application properties
     * @return the log file representation
     */
    public static File getLogFile(final int logType, final AppProperties properties) {
        if (logType == 0) {
            return new File(properties.getDir() + System.getProperty("file.separator") + LOG_FILE_NAME_LOG);
        }
        if (logType == 1) {
            return new File(properties.getDir() + System.getProperty("file.separator") + LOG_FILE_NAME_TXT);
        }
        if (logType == 2) {
            return new File(properties.getDir() + System.getProperty("file.separator") + LOG_FILE_NAME_CSV);
        }
        if (logType == 3) {
            return new File(properties.getDir() + System.getProperty("file.separator") + LOG_FILE_NAME_YAML);
        }
        if (logType == 4) {
            return new File(properties.getDir() + System.getProperty("file.separator") + LOG_FILE_NAME_ERR);
        } else {
            return new File(properties.getDir() + System.getProperty("file.separator") + LOG_FILE_NAME_LOG);
        }
    }

    /**
     * Writes information about uploaded file to logs.
     *
     * @param vaultName  The name of vault
     * @param region     Amazon Region
     * @param filePath   File path
     * @param fileLength The length of file, in bytes
     * @param treeHash   The hex encoded binary tree hash for the data in the specified file.
     * @param archiveId  The ID of the uploaded archive
     * @throws IOException If an I/O error occurs
     */
    public void logUploadedFile(final String vaultName, final String region, final String filePath,
                                final String fileLength, final String treeHash,
                                final String archiveId) throws IOException {
        final Date currentDate = new Date();
        final String lineSeparator = System.getProperty("line.separator");

        plainOutputLog.write(lineSeparator);
        plainOutputLog.write(" | ArchiveID: " + archiveId + " ");
        plainOutputLog.write(lineSeparator);
        plainOutputLog.write(" | File: " + filePath + " ");
        plainOutputLog.write(" | Bytes: " + fileLength + " ");
        plainOutputLog.write(" | Vault: " + vaultName + " ");
        plainOutputLog.write(" | Location: " + region + " ");
        plainOutputLog.write(" | Date: " + currentDate.toString() + " ");
        plainOutputLog.write(" | Hash: " + treeHash + " ");
        plainOutputLog.write(lineSeparator);
        plainOutputLog.close();

        plainOutputTxt.write(lineSeparator);
        plainOutputTxt.write(" | ArchiveID: " + archiveId + " ");
        plainOutputTxt.write(lineSeparator);
        plainOutputTxt.write(" | File: " + filePath + " ");
        plainOutputTxt.write(" | Bytes: " + fileLength + " ");
        plainOutputTxt.write(" | Vault: " + vaultName + " ");
        plainOutputTxt.write(" | Location: " + region + " ");
        plainOutputTxt.write(" | Date: " + currentDate.toString() + " ");
        plainOutputTxt.write(" | Hash: " + treeHash + " ");
        plainOutputTxt.write(lineSeparator);
        plainOutputTxt.close();

        plainOutputCsv.write("\"" + csvEscaping(archiveId) + "\",");
        plainOutputCsv.write("\"" + csvEscaping(filePath) + "\",");
        plainOutputCsv.write("\"" + csvEscaping(fileLength) + "\",");
        plainOutputCsv.write("\"" + csvEscaping(vaultName) + "\",");
        plainOutputCsv.write("\"" + csvEscaping(region) + "\",");
        plainOutputCsv.write("\"" + csvEscaping(currentDate.toString()) + "\",");
        plainOutputCsv.write("\"" + csvEscaping(treeHash) + "\"");
        plainOutputCsv.write(lineSeparator);
        plainOutputCsv.close();

        plainOutputYaml.write(lineSeparator);
        plainOutputYaml.write("-  ArchiveID: \"" + yamlEscaping(archiveId) + "\"" + lineSeparator);
        plainOutputYaml.write("   File:      \"" + yamlEscaping(filePath) + "\"" + lineSeparator);
        plainOutputYaml.write("   Bytes:     \"" + yamlEscaping(fileLength) + "\"" + lineSeparator);
        plainOutputYaml.write("   Vault:     \"" + yamlEscaping(vaultName) + "\"" + lineSeparator);
        plainOutputYaml.write("   Location:  \"" + yamlEscaping(region) + "\"" + lineSeparator);
        plainOutputYaml.write("   Date:      \"" + yamlEscaping(currentDate.toString()) + "\"" + lineSeparator);
        plainOutputYaml.write("   Hash:      \"" + yamlEscaping(treeHash) + "\"" + lineSeparator);
        plainOutputYaml.close();
    }

    private String yamlEscaping(final String value) {
        return value.replaceAll("\"", "\\\\\"");
    }

    private String csvEscaping(final String value) {
        return value.replaceAll("\"", "\"\"");
    }
}
