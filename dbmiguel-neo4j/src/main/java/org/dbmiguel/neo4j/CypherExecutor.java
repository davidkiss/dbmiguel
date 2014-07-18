package org.dbmiguel.neo4j;

import org.dbmiguel.core.MigrationContext;
import org.dbmiguel.core.MigrationExecutor;
import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.RestAPIFacade;
import org.neo4j.rest.graphdb.query.QueryEngine;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.neo4j.rest.graphdb.util.QueryResult;

/**
 * Created by David on 6/25/2014.
 */
public class CypherExecutor implements MigrationExecutor<QueryResult> {

    private QueryEngine engine;

    @Override
    public String getType() {
        return "cypher";
    }

    @Override
    public void init(MigrationContext context) {
        engine = (QueryEngine) context.get(Neo4jConstants.NEO4J_ENGINE);
        if (engine == null) {
            String neo4jUrl = (String) context.get(Neo4jConstants.NEO4J_URL);
            if (neo4jUrl == null) {
                throw new IllegalStateException("No value provided for ne4j.url");
            }

            RestAPI restAPI = new RestAPIFacade(neo4jUrl);
            engine = new RestCypherQueryEngine(restAPI);
            context.put(Neo4jConstants.NEO4J_ENGINE, engine);
        }
    }

    @Override
    public QueryResult executeCommand(String query) {
        return engine.query(query, null);
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

}
