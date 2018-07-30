package com.ripple.query;

import com.ripple.config.ConfigReader;
import com.ripple.database.*;
import com.ripple.database.binop.BinOp;
import com.ripple.query.selectfilter.FilterMapOperator;
import com.ripple.query.selectfilter.SelectFilterTask;
import com.ripple.query.selectfilter.NopReduceOperator;
import com.ripple.query.selectfilter.SelectMapOperator;
import com.ripple.util.Pair;
import com.ripple.util.StringUtil;
import com.ripple.util.TaskUtil;
import com.ripple.database.value.FloatValue;
import com.ripple.database.value.IntValue;
import com.ripple.database.value.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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

    public void select(List<Attribute> uncheckedAttributes,
                       List<String> relationNames,
                       List<Condition> uncheckedConditions) throws IOException {
        String date = StringUtil.getDate();

        checkActiveDatabase();
        Map<String, Relation> relations = checkRelations(relationNames);
        relationNames.clear();
        List<Attribute> attributes = checkAttributes(uncheckedAttributes, relations);
        uncheckedAttributes.clear();
        List<Condition> conditions = checkConditions(uncheckedConditions, relations);
        uncheckedConditions.clear();

        Map<String, List<Attribute>> selectAttributeMap = new HashMap<>();
        Map<String, List<Condition>> simpleConditionMap = new HashMap<>();
        List<Condition> equalConditionList = new ArrayList<>();
        List<Condition> notEqualConditionList = new ArrayList<>();
        for (Attribute attribute : attributes) {
            classifyAttribute(attribute, selectAttributeMap);
        }
        for (Condition condition : conditions) {
            Attribute leftAttribute = condition.getLeftAttribute();
            classifyAttribute(leftAttribute, selectAttributeMap);
            if (condition.isRightIsAttribute()) {
                Attribute rightAttribute = condition.getRightAttribute();
                classifyAttribute(rightAttribute, selectAttributeMap);
                if (leftAttribute.getRelationName().equals(rightAttribute.getRelationName())) {
                    classifyCondition(condition, simpleConditionMap);
                } else {
                    boolean tmp = (condition.getBinOp() == BinOp.eqOp)
                            ? equalConditionList.add(condition)
                            : notEqualConditionList.add(condition);
                }
            } else {
                classifyCondition(condition, simpleConditionMap);
            }
        }

        List<SelectFilterTask> tasks = new ArrayList<>();

        int tmpIndex = 0;

        for (String relationName : relations.keySet()) {
            SelectFilterTask task = new SelectFilterTask();
            Relation relation = relations.get(relationName);
            task.inputPaths.add(getRelationPath(relation));
            task.outputPath = getTmpPath(date, tmpIndex);
            List<Attribute> originAttributes = relation.getAttributeMap().values().stream()
                    .sorted(Comparator.comparing(Attribute::getIndex)).collect(Collectors.toList());
            task.attributes = originAttributes;
            if (selectAttributeMap.containsKey(relationName)) {
                List<Attribute> selectAttributes = selectAttributeMap.get(relationName);
                SelectMapOperator selectOp = new SelectMapOperator();
                selectOp.setup(selectAttributes, originAttributes);
                task.mapOperators.add(selectOp);
                task.attributes = selectAttributes;
            }
            if (simpleConditionMap.containsKey(relationName)) {
                List<Condition> simpleConditions = simpleConditionMap.get(relationName);
                FilterMapOperator filterOp = new FilterMapOperator();
                filterOp.setup(simpleConditions, task.attributes);
                task.mapOperators.add(filterOp);
            }
            task.reduceOperator = new NopReduceOperator();
            tasks.add(task);
            ++tmpIndex;
        }

        String outputPath = null;

        if (tasks.size() == 1) {
            SelectFilterTask task = tasks.get(0);
            SelectMapOperator selectOp = new SelectMapOperator();
            selectOp.setup(attributes, task.attributes);
            task.mapOperators.add(selectOp);
            task.attributes = attributes;
            task.outputPath = getOutputPath(date);
            outputPath = task.outputPath;
        } else {
            // todo
        }

        TaskUtil.runTasks(tasks);

        Path path = Paths.get(outputPath + "/part-r-00000");
        BufferedReader reader = Files.newBufferedReader(path);
        String line = null;
        while ((line = reader.readLine()) != null)
            System.out.println(line);
        reader.close();
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

    private Map<String, Relation> checkRelations(List<String> relationNames) {
        Map<String, Relation> relations = new HashMap<>();
        for (String relationName : relationNames)
            relations.put(relationName, activeDatabase.getRelation(relationName));
        return relations;
    }

    private List<Attribute> checkAttributes(List<Attribute> uncheckedAttributes, Map<String, Relation> relations) {
        if (uncheckedAttributes.size() == 1 && uncheckedAttributes.get(0).getAttributeName().equals("*")) {
            List<Attribute> attributes = new ArrayList<>();
            for (Relation relation : relations.values())
                attributes.addAll(relation.getAttributeMap().values());
            return attributes;
        }
        List<Attribute> attributes = new ArrayList<>();
        for (Attribute uncheckedAttribute : uncheckedAttributes) {
            attributes.add(checkAttribute(uncheckedAttribute, relations));
        }
        return attributes;
    }

    private Attribute checkAttribute(Attribute uncheckedAttribute, Map<String, Relation> relations) {
        String relationName = uncheckedAttribute.getRelationName();
        String attributeName = uncheckedAttribute.getAttributeName();
        Pair<Relation, Attribute> relationAttributePair = null;
        if (relationName != null)
            relationAttributePair = activeDatabase.getAttribute(relationName, attributeName);
        else {
            List<Pair<Relation, Attribute>> pairs = activeDatabase.getAttribute(attributeName);
            if (pairs.size() > 1)
                throw new RuntimeException(String.format("More Than One Attribute(%s) in Database(%s)!!!",
                        attributeName, activeDatabase.getName()));
            relationAttributePair = pairs.get(0);
        }
        relationName = relationAttributePair.getKey().getName();
        if (!relations.containsKey(relationName)) {
            throw new RuntimeException(String.format("No Such Relation(%s:%s) in SQL From Statement!!!",
                    relationName, attributeName));
        }
        return relationAttributePair.getValue();
    }

    private List<Condition> checkConditions(List<Condition> uncheckedConditions, Map<String, Relation> relations) {
        List<Condition> conditions = new ArrayList<>();
        for (Condition uncheckedCondition : uncheckedConditions) {
            conditions.add(checkConditions(uncheckedCondition, relations));
        }
        return conditions;
    }

    private Condition checkConditions(Condition uncheckedCondition, Map<String, Relation> relations) {
        Condition condition = null;
        Attribute leftAttribute = checkAttribute(uncheckedCondition.getLeftAttribute(), relations);
        BinOp binOp = uncheckedCondition.getBinOp();
        if (uncheckedCondition.isRightIsAttribute()) {
            Attribute rightAttribute = checkAttribute(uncheckedCondition.getRightAttribute(), relations);
            if (leftAttribute.getType() != rightAttribute.getType())
                throw new RuntimeException(String.format("Not Same Type between Attribute(%s:%s) and Value(%s)",
                        leftAttribute.getRelationName(), leftAttribute.getAttributeName(),
                        rightAttribute.getRelationName(), rightAttribute.getAttributeName()));
            condition = new Condition(leftAttribute, binOp, rightAttribute);
        } else {
            Value value = uncheckedCondition.getRightValue();
            if (leftAttribute.getType() == FloatValue.class && value.getClass() == IntValue.class)
                value = new FloatValue((double) ((IntValue) value).get());
            if (leftAttribute.getType() != value.getClass())
                throw new RuntimeException(String.format("Not Same Type between Attribute(%s:%s:%s) and Value(%s:%s)",
                        leftAttribute.getRelationName(), leftAttribute.getAttributeName(),
                        leftAttribute.getType().getName(),
                        value.toString(), value.getClass().getName()));
            condition = new Condition(leftAttribute, binOp, value);
        }
        return condition;
    }

    private void classifyAttribute(Attribute attribute, Map<String, List<Attribute>> relationAttributeMap) {
        String relationName = attribute.getRelationName();
        if (!relationAttributeMap.containsKey(relationName))
            relationAttributeMap.put(relationName, new ArrayList<>());
        List<Attribute> relationAttributes = relationAttributeMap.get(relationName);
        if (!relationAttributes.contains(attribute))
            relationAttributes.add(attribute);
    }

    private void classifyCondition(Condition condition, Map<String, List<Condition>> relationConditionMap) {
        String relationName = condition.getLeftAttribute().getRelationName();
        if (!relationConditionMap.containsKey(relationName))
            relationConditionMap.put(relationName, new ArrayList<>());
        List<Condition> relationAttributes = relationConditionMap.get(relationName);
        if (!relationAttributes.contains(condition))
            relationAttributes.add(condition);
    }

    private String getRelationPath(Relation relation) {
        return "file/Database/" + activeDatabase.getName() + "/" + relation.getName();
    }

    private String getTmpPath(String date, int index) {
        return "file/Tmp/" + date + "-" + index;
    }

    private String getOutputPath(String date) {
        return "file/Result/" + date;
    }
}
