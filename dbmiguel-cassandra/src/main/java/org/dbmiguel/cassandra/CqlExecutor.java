package org.dbmiguel.cassandra;

import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.query.QueryResult;
import org.dbmiguel.core.MigrationContext;
import org.dbmiguel.core.MigrationExecutor;

/**
 * Created by David on 6/25/2014.
 */
public class CqlExecutor implements MigrationExecutor<QueryResult<CqlRows>> {
    private Keyspace keyspace;
    private Serializer keySerializer = StringSerializer.get();
    private Serializer nameSerializer = StringSerializer.get();
    private Serializer valueSerializer = ObjectSerializer.get();

    @Override
    public String getType() {
        return "cql";
    }

    public void setKeySerializer(Serializer keySerializer) {
        this.keySerializer = keySerializer;
    }

    public void setNameSerializer(Serializer nameSerializer) {
        this.nameSerializer = nameSerializer;
    }

    public void setValueSerializer(Serializer valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    protected QueryResult<CqlRows> executeCqlQuery(String query) {
        CqlQuery cqlQuery = new CqlQuery(keyspace, keySerializer, nameSerializer, valueSerializer);
        cqlQuery.setQuery(query);
        return cqlQuery.execute();
    }

    @Override
    public void init(MigrationContext context) {
        initKeyspace(context);
        Serializer keySerializer = (Serializer) context.get(CassandraConstants.CASSANDRA_KEYSERIALIZER);
        if (keySerializer != null) {
            setKeySerializer(keySerializer);
        }
        Serializer nameSerializer = (Serializer) context.get(CassandraConstants.CASSANDRA_NAMESERIALIZER);
        if (nameSerializer != null) {
            setNameSerializer(nameSerializer);
        }
        Serializer valueSerializer = (Serializer) context.get(CassandraConstants.CASSANDRA_VALUESERIALIZER);
        if (valueSerializer != null) {
            setValueSerializer(valueSerializer);
        }
    }

    @Override
    public QueryResult<CqlRows> executeCommand(String cmd) {
        return executeCqlQuery(cmd);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onError(Exception e) {
    }

    private void initKeyspace(MigrationContext context) {
        keyspace = (Keyspace) context.get(CassandraConstants.CASSANDRA_KEYSPACE);
        if (keyspace == null) {
            String hosts = (String) context.get(CassandraConstants.CASSANDRA_HOSTS);
            String keyspaceName = (String) context.get(CassandraConstants.CASSANDRA_KEYSPACE_NAME);
            if (keyspaceName != null && hosts != null) {
                Cluster cluster = CassandraUtils.createCluster(hosts);
                keyspace = CassandraUtils.createKeyspace(cluster, keyspaceName);
                context.put(CassandraConstants.CASSANDRA_KEYSPACE, keyspace);
            }
        }
    }
}
