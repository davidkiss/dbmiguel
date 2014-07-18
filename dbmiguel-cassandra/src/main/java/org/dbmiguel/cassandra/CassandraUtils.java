package org.dbmiguel.cassandra;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.ThriftCluster;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

/**
 * Created by David on 6/25/2014.
 */
public class CassandraUtils {
    public static ThriftCluster createCluster(String hosts) {
        CassandraHostConfigurator cassandraHostConfigurator = new CassandraHostConfigurator(hosts);
        cassandraHostConfigurator.setMaxActive(20);
        cassandraHostConfigurator.setCassandraThriftSocketTimeout(3000);
        cassandraHostConfigurator.setMaxWaitTimeWhenExhausted(4000);

        return new ThriftCluster("SystemCluster", cassandraHostConfigurator);
    }

    public static Keyspace createKeyspace(Cluster cluster, String keyspaceName) {
        ConfigurableConsistencyLevel consistencyLevel = new ConfigurableConsistencyLevel();
        consistencyLevel.setDefaultReadConsistencyLevel(HConsistencyLevel.ONE);
        return HFactory.createKeyspace(keyspaceName, cluster, consistencyLevel);
    }
}
