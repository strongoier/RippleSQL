package com.ripple.query;

import com.ripple.config.ConfigReader;
import com.ripple.database.Attribute;
import com.ripple.database.Condition;
import com.ripple.database.Database;
import com.ripple.database.Relation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class QueryManager {
    private ConfigReader configReader;
    private Map<String, Database> databaseMap;
    private Database activeDatabase;

    public QueryManager() {
        configReader = null;
        databaseMap = null;
    }

    public void setConfigReader(ConfigReader configReader) {
        this.configReader = configReader;
    }

    public void initialize() throws IOException {
        checkConfigReader();
        databaseMap = configReader.getDatabases();
    }

    public void activeDatabase(String databaseName) {
        checkDatabaseMap();
        if (databaseName == null) {
            activeDatabase = null;
            return;
        }
        if (!databaseMap.containsKey(databaseName))
            throw new RuntimeException(String.format("No Such Database(%s)!!!", databaseName));
        activeDatabase = databaseMap.get(databaseName);
    }

    public void select(List<Attribute> attrs, List<String> rels, List<Condition> conds) {
        checkActiveDatabase();

        Map<String, Relation> relations = activeDatabase.checkRelations(rels);

        Map<Relation, Map<String, Attribute>> attributes = activeDatabase.checkAttributes(attrs, relations);
    }

    private void checkConfigReader() {
        if (configReader == null)
            throw new RuntimeException("Need Set ConfigReader!!!");
    }

    private void checkDatabaseMap() {
        checkConfigReader();
        if (databaseMap == null)
            throw new RuntimeException("Need Initialize QueryManager!!!");
    }

    private void checkActiveDatabase() {
        checkDatabaseMap();
        if (activeDatabase == null)
            throw new RuntimeException("Need Active Database!!!");
    }
}
