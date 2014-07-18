package org.dbmiguel.core;

/**
 * Created by David on 6/25/2014.
 */
public interface MigrationExecutor<T> {
    String getType();

    void init(MigrationContext migrationContext);

    T executeCommand(String cmd);

    void onStart();

    void onComplete();

    void onError(Exception e);
}
