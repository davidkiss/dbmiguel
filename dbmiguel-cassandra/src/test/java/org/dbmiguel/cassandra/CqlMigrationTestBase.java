package org.dbmiguel.cassandra;

import org.junit.Before;
import org.dbmiguel.core.MigrationCoordinator;

/**
 * Created by David on 6/25/2014.
 */
public abstract class CqlMigrationTestBase {
    protected MigrationCoordinator migrationCoordinator;

    @Before
    public void setUp() throws Exception {
        migrationCoordinator = new MigrationCoordinator("Demo");
        // these can be also set as system properties:
        migrationCoordinator.getContext().put(CassandraConstants.CASSANDRA_HOSTS, "localhost");
        migrationCoordinator.getContext().put(CassandraConstants.CASSANDRA_KEYSPACE_NAME, "webspinedata2");
    }
}
