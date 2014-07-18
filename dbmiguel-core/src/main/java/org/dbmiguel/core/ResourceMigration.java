package org.dbmiguel.core;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Map;

/**
 * Created by David on 6/24/2014.
 */
public class ResourceMigration implements Migration {

    public static final String COMMENT_PREFIX = "--";
    public static final String META_PREFIX = COMMENT_PREFIX+"@";

    private Map<String, MigrationExecutor> executors;
    private BufferedReader reader;
    private String id;
    private String author;
    private String type;
    private boolean executionStarted;

    public void setReader(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public void init(MigrationContext context) {
        executors = context.getExecutors();
    }

    @Override
    public void migrate() throws Exception {
        try {
            String newLine = "";
            while ((newLine = reader.readLine()) != null) {
                if (newLine.trim().length() == 0) {
                    continue;
                } else if (newLine.startsWith(META_PREFIX)) {
                    processMetaLine(newLine);
                } else if (!newLine.startsWith(COMMENT_PREFIX)) {
                    executeCommand(newLine);
                }
            }
            if (executionStarted) {
                getMigrationExecutor().onComplete();
            }
        } catch (Exception e) {
            if (executionStarted) {
                getMigrationExecutor().onError(e);
            }
            throw e;
        } finally {
            reader.close();
        }
    }

    private void executeCommand(String cmd) {
        MigrationExecutor executor = getMigrationExecutor();
        if (!executionStarted) {
            executor.onStart();
            executionStarted = true;
        }

        executor.executeCommand(cmd);
    }

    private void processMetaLine(String line) {
        String metaStr = line.replaceFirst(META_PREFIX, "");
        int separatorIndex = metaStr.indexOf('=');
        if (separatorIndex > 0 && separatorIndex < metaStr.length() - 1) {
            String key = metaStr.substring(0, separatorIndex).trim();
            String value = metaStr.substring(separatorIndex + 1).trim();

            if (MigrationConstants.TYPE.equals(key)) {
                type = value;
            } else if (MigrationConstants.AUTHOR.equals(key)) {
                author = value;
            }
        }
    }

    private MigrationExecutor getMigrationExecutor() {
        if (type == null) {
            throw new IllegalStateException("No type configured for resource with id: "+getId());
        }

        String typeLowerCase = type.toLowerCase();
        MigrationExecutor executor = executors.get(typeLowerCase);
        if (executor == null) {
            throw new IllegalStateException("No executor found for type: "+typeLowerCase);
        }
        return executor;
    }
}
