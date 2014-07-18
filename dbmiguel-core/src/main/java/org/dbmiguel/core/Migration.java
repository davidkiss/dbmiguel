package org.dbmiguel.core;

/**
 * Created by David on 6/24/2014.
 */
public interface Migration {
    String getId();
    String getAuthor();

    /**
     * Initializes Migration using data in the context.
     * @param context
     */
    void init(MigrationContext context);

    /**
     * Method to update DB
     * @throws Exception
     */
    void migrate() throws Exception;
}
