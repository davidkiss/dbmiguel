dbmiguel
========

DBMiguel is a Java-based DB change management tool. It's similar to LiquiBase, but it also supports NoSQL databases. There are already adapters for Cassandra and Neo4J.

###Sample usage for Neo4J

Java code:
```
MigrationCoordinator migrationCoordinator = new MigrationCoordinator("Demo");
// these can be also set as system properties:
migrationCoordinator.getContext().put(Neo4jConstants.NEO4J_URL, "http://localhost:7474/db/data");
migrationCoordinator.getContext().registerExecutor(new CypherExecutor());

migrationCoordinator.migrate(new ClasspathResourceMigration("/migration/neo4j/2-test.cypher"));
```
Contents of /migration/neo4j/2-test.cypher:
```
--@type=cypher
--@author=dkiss

-- this is a comment for the sample query:
start n=node(*) where n.name = 'my node' return n
```
