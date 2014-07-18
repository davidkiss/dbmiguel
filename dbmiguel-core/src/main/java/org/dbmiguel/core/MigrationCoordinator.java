package org.dbmiguel.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 6/24/2014.
 */
public class MigrationCoordinator {
    private static final Logger LOG = LoggerFactory.getLogger(MigrationCoordinator.class);

    private final MigrationContext context;
    private final String appName;
    private HistoryService history;
    private LockService lockService;

    public MigrationCoordinator(String appName) {
        this.appName = appName;
        this.context = new MigrationContext();
        this.history = new FileHistoryService(System.getProperty("user.dir"), appName);

        initContextWithSystemProperties();
    }

    public void setHistory(HistoryService history) {
        this.history = history;
    }

    public void setLockService(LockService lockService) {
        this.lockService = lockService;
    }

    private void initContextWithSystemProperties() {
        for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
            context.getData().put(String.valueOf(entry.getKey()), entry.getValue());
        }
    }

    public MigrationContext getContext() {
        return context;
    }

    public void migrate(Migration migration) throws Exception {
        if (migration != null) {
            LOG.info("Migrating '{}'", migration.getId());

            if (!isMigrated(context, migration)) {
                migration.init(context);
                if (lock(context)) {
                    try {
                        migration.migrate();
                        history.onMigrationCompleted(migration);
                    } finally {
                        unlock(context);
                    }

                }
            } else {
                LOG.debug("Skipping - already migrated", migration.getId());
            }
        }
    }

    /**
     * Overwrite method to add business logic that decides if migration has already been migrated.
     * May use file, RDBMS, Zookeeper, etc.
     * @param migration
     * @return
     */
    protected boolean isMigrated(MigrationContext context, Migration migration) throws Exception {
        return history == null ? false : history.isMigrated(context, migration);
    }

    /**
     * Overwrite method to implement locking mechanism
     * @param context
     * @return
     */
    protected boolean lock(MigrationContext context) {
        return lockService == null ? true : lockService.lock(context);
    }

    /**
     * Overwrite method to implement locking mechanism
     * @param context
     * @return
     */
    protected void unlock(MigrationContext context) {
        if (lockService != null) {
            lockService.unlock(context);
        }
    }

    public void migrate(List<Migration> migrations) throws Exception {
        for (Migration migration : migrations) {
            migrate(migration);
        }
    }
}
