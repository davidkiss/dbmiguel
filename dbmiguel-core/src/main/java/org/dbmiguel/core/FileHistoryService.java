package org.dbmiguel.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Date;

/**
 * Created by David on 7/7/2014.
 */
public class FileHistoryService implements HistoryService {
    private static final Logger LOG = LoggerFactory.getLogger(FileHistoryService.class);
    private final String appName;
    private final String historyDir;
    private final File historyFile;

    public FileHistoryService(String historyDir, String appName) {
        if (historyDir == null) {
            throw new IllegalArgumentException("History directory cannot be null");
        }
        if (appName == null) {
            throw new IllegalArgumentException("App name cannot be null");
        }

        this.historyDir = historyDir;
        this.appName = appName;

        historyFile = new File(historyDir, appName+".hist");
        LOG.info("Using file to store migration history: "+historyFile.getAbsolutePath());
    }

    @Override
    public boolean isMigrated(MigrationContext context, Migration migration) throws Exception {
        boolean result = false;
        if (historyFile.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(historyFile));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    if (migration.getId().equals(line)) {
                        result = true;
                        break;
                    }
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
        return result;
    }

    @Override
    public void onMigrationCompleted(Migration migration) throws Exception{
        Writer writer = null;
        try {
            writer = new FileWriter(historyFile, true);
            writer.append(String.format("// %Tc - by %s%n", new Date(), migration.getAuthor()));
            writer.append(migration.getId());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
