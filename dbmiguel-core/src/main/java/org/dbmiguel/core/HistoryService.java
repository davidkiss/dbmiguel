package org.dbmiguel.core;

import java.io.FileNotFoundException;

/**
 * Created by David on 6/25/2014.
 */
public interface HistoryService {
    /**
     *
     * @param migration
     * @return True, if migration has already been migrated
     */
    boolean isMigrated(MigrationContext context, Migration migration) throws FileNotFoundException, Exception;

    void onMigrationCompleted(Migration migration) throws Exception;
}
