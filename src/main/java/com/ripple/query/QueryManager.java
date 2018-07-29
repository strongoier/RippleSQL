package com.ripple.query;

import com.ripple.config.ConfigReader;
import com.ripple.database.Attribute;
import com.ripple.database.Condition;
import com.ripple.database.Database;
import com.ripple.database.Relation;
import com.ripple.mapreduce.TaskInfo;
import com.ripple.sqloperator.NopReduceOperator;
import com.ripple.sqloperator.SelectMapOperator;
import com.ripple.util.TaskUtil;
import org.apache.hadoop.mapreduce.Job;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public void showDatabases() {
        checkDatabaseMap();
        for (String name : databaseMap.keySet()) {
            System.out.println(name);
        }
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

    public void showRelations() {
        checkActiveDatabase();
        for (String name : activeDatabase.getRelationNames()) {
            System.out.println(name);
        }
    }

    public void descRelation(String relationName) {
        checkActiveDatabase();
        System.out.println(activeDatabase.getRelation(relationName));
    }

    public void select(List<Attribute> attrs, List<String> rels, List<Condition> conds) throws IOException {
        checkActiveDatabase();

        //Map<String, Relation> relations = activeDatabase.checkRelations(rels);

        //Map<Relation, Map<String, Attribute>> attributes = activeDatabase.checkAttributes(attrs, relations);

        //List<TaskInfo> taskInfos = new ArrayList<>();

        Relation relation = activeDatabase.getRelation(rels.get(0));

        attrs.forEach((a) -> {
            if (a.getRelationName() == null)
                a.setRelationName(relation.getName());
        });

        List<Attribute> attributes = attrs;

        List<TaskInfo> infos = new ArrayList<>();
        TaskInfo info = new TaskInfo();

        List<Attribute> cur = relation.getAttributeMap().values().stream().sorted(Comparator.comparing(Attribute::getIndex)).collect(Collectors.toList());
        SelectMapOperator selectMapOperator = new SelectMapOperator();
        cur = selectMapOperator.setup(attributes, cur);
        info.mapOperators.add(selectMapOperator);
        NopReduceOperator nopReduceOperator = new NopReduceOperator();
        cur = nopReduceOperator.setup(cur);
        info.reduceOperator = nopReduceOperator;
        info.inputPaths.add(getRelationPath(relation));
        info.outputPath = "file/result";

        infos.add(info);

        TaskUtil.createJob(infos);

        Path path = Paths.get("file/result/part-r-00000");
        BufferedReader reader = Files.newBufferedReader(path);
        String line = null;
        while ((line = reader.readLine()) != null)
            System.out.println(line);
        reader.close();
        Path dir = Paths.get("file/result");
        Files.delete(dir);
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

    private String getRelationPath(Relation relation) {
        return "file/" + activeDatabase.getName() + "/" + relation.getName();
    }
}
