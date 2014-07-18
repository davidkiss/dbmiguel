package org.dbmiguel.core;

import java.io.InputStreamReader;

/**
 * Created by David on 6/25/2014.
 */
public class ClasspathResourceMigration extends ResourceMigration {

    public ClasspathResourceMigration(String path) {
        setReader(new InputStreamReader(getClass().getResourceAsStream(path)));
        setId(extractNameFromPath(path));
    }

    private String extractNameFromPath(String path) {
        int pathSepIndex = path.lastIndexOf('/');
        int extensionIndex = path.lastIndexOf('.');

        return path.substring(pathSepIndex > -1 ? pathSepIndex + 1 : 0, extensionIndex > -1 ? extensionIndex : path.length());
    }
}
