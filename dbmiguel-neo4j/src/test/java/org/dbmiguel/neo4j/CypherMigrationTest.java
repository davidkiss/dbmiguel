package org.dbmiguel.neo4j;

import org.dbmiguel.core.ClasspathResourceMigration;
import org.dbmiguel.core.MigrationCoordinator;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Created by David on 6/25/2014.
 */
public class CypherMigrationTest {
    protected MigrationCoordinator migrationCoordinator;

    @Before
    public void setUp() throws Exception {
        migrationCoordinator = new MigrationCoordinator("Demo");
        // these can be also set as system properties:
        migrationCoordinator.getContext().put(Neo4jConstants.NEO4J_URL, "http://localhost:7474/db/data");
        migrationCoordinator.getContext().registerExecutor(new CypherExecutor());
        new File(System.getProperty("user.dir"), "Demo.hist").delete();
    }

    @Test
    public void testMigrate() throws Exception {
        ClasspathResourceMigration migration = new ClasspathResourceMigration("/migration/neo4j/2-test.cypher");
        migrationCoordinator.migrate(migration);
        assertEquals("2-test", migration.getId());
        assertEquals("dkiss", migration.getAuthor());

    }
}
