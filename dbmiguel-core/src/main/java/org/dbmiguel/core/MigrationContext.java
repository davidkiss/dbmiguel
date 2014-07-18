package org.dbmiguel.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by David on 6/25/2014.
 */
public class MigrationContext {
    private final Map<String, Object> data = new HashMap<String, Object>();
    private final Map<String, MigrationExecutor> executors = new HashMap<String, MigrationExecutor>();

    public Map<String, Object> getData() {
        return data;
    }

    public Map<String, MigrationExecutor> getExecutors() {
        return executors;
    }

    public Object get(String key) {
        return data.get(key);
    }

    public Object put(String key, Object value) {
        return data.put(key, value);
    }

    public void registerExecutor(MigrationExecutor executor) {
        executor.init(this);
        String type = executor.getType();
        if (type == null) {
            throw new IllegalArgumentException("Migration executor has no type provided");
        }

        executors.put(type.toLowerCase(), executor);
    }
}
