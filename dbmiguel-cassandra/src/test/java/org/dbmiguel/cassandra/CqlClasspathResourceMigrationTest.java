package org.dbmiguel.cassandra;

import org.dbmiguel.core.ClasspathResourceMigration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * Created by David on 6/25/2014.
 */
public class CqlClasspathResourceMigrationTest extends CqlMigrationTestBase {
    private static final Logger LOG = LoggerFactory.getLogger(CqlMigrationTest.class);

    @Test
    public void testUpdate() throws Exception {
        migrationCoordinator.getContext().registerExecutor(new CqlExecutor());

        ClasspathResourceMigration migration = new ClasspathResourceMigration("/src/test/resources/cql/1-test.cql");
        migrationCoordinator.migrate(migration);

        assertEquals("1-test", migration.getId());
        assertEquals("dkiss", migration.getAuthor());
    }
}
