package org.dbmiguel.core;

/**
 * Created by David on 6/25/2014.
 */
public interface LockService {
    boolean lock(MigrationContext context);
    void unlock(MigrationContext context);
}
