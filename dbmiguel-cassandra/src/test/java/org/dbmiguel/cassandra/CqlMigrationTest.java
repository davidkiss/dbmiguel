package org.dbmiguel.cassandra;

import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.hector.api.query.QueryResult;
import org.dbmiguel.core.Migration;
import org.dbmiguel.core.MigrationContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by David on 6/24/2014.
 */
public class CqlMigrationTest extends CqlMigrationTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(CqlMigrationTest.class);

    @Test
    public void testExecuteUpdate() throws Exception {

        Migration migration = new Migration() {
            private CqlExecutor executor = new CqlExecutor();

            @Override
            public String getId() {
                return "1";
            }

            @Override
            public String getAuthor() {
                return "dkiss";
            }

            @Override
            public void init(MigrationContext context) {
                executor.init(context);
            }

            @Override
            public void migrate() throws Exception {
                QueryResult<CqlRows> rows = executor.executeCqlQuery("SELECT count(*) FROM useraccount");
                LOG.info("Returned result: " + rows.get().getAsCount());
            }
        };

        migrationCoordinator.migrate(migration);
    }
}
