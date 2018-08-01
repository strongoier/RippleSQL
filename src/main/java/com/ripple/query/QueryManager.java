package com.ripple.query;

import com.ripple.config.ConfigReader;
import com.ripple.database.*;
import com.ripple.database.binop.BinOp;
import com.ripple.database.func.Func;
import com.ripple.query.operator.*;
import com.ripple.query.task.*;
import com.ripple.util.*;
import com.ripple.database.value.FloatValue;
import com.ripple.database.value.IntValue;
import com.ripple.database.value.Value;
import org.w3c.dom.Attr;

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
                       List<Condition> uncheckedConditions,
                       List<Attribute> uncheckedGroupByAttributes,
                       Attribute uncheckedOrderByAttribute) throws IOException {
        String date = StringUtil.getDate();

        checkActiveDatabase();
        Map<String, Relation> relations = checkRelations(relationNames);
        relationNames.clear();
        List<Attribute> attributes = checkAttributes(uncheckedAttributes, relations);
        uncheckedAttributes.clear();
        List<Condition> conditions = checkConditions(uncheckedConditions, relations);
        uncheckedConditions.clear();
        boolean hasGroupByFunc = checkGroupByAttributes(uncheckedGroupByAttributes, relations, attributes);
        uncheckedGroupByAttributes.clear();
        Attribute orderByAttribute = checkOrderByAttribute(uncheckedOrderByAttribute, relations, attributes);

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

        List<SelectFilterTask> selectFilterTasks = new ArrayList<>();

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
                task.selectOperator = selectOp;
                task.attributes = selectAttributes;
            }
            if (simpleConditionMap.containsKey(relationName)) {
                List<Condition> simpleConditions = simpleConditionMap.get(relationName);
                FilterOperator filterOp = new FilterOperator();
                filterOp.setup(simpleConditions, task.attributes);
                task.filterOperator = filterOp;
            }
            selectFilterTasks.add(task);
            ++tmpIndex;
        }

        SelectFilterTaskUtil.runTasks(selectFilterTasks);

        MapReduceTask lastJoinTask = null;

        if (selectFilterTasks.size() == 1) {
            lastJoinTask = selectFilterTasks.get(0);
        } else {
            PriorityQueue<MapReduceTask> needJoinRelations = new PriorityQueue<>(selectFilterTasks.size(),
                    Comparator.comparing(task->task.lines));
            needJoinRelations.addAll(selectFilterTasks);
            while (needJoinRelations.size() > 1) {
                MapReduceTask a = needJoinRelations.poll();
                MapReduceTask b = needJoinRelations.poll();
                List<Condition> equalConditionsBetweenAB = new ArrayList<>();
                List<Condition> notEqualConditionsBetweenAB = new ArrayList<>();
                List<Condition> remainEqualConditions = new ArrayList<>();
                List<Condition> remainNotEqualConditions = new ArrayList<>();
                for (Condition condition : equalConditionList) {
                    Attribute left = condition.getLeftAttribute();
                    Attribute right = condition.getRightAttribute();
                    if ((a.attributes.contains(left) && b.attributes.contains(right))
                            || (a.attributes.contains(right) && b.attributes.contains(left)))
                        equalConditionsBetweenAB.add(condition);
                    else
                        remainEqualConditions.add(condition);
                }
                for (Condition condition : notEqualConditionList) {
                    Attribute left = condition.getLeftAttribute();
                    Attribute right = condition.getRightAttribute();
                    if ((a.attributes.contains(left) && b.attributes.contains(right))
                            || (a.attributes.contains(right) && b.attributes.contains(left)))
                        notEqualConditionsBetweenAB.add(condition);
                    else
                        remainNotEqualConditions.add(condition);
                }
                equalConditionList = remainEqualConditions;
                notEqualConditionList = remainNotEqualConditions;
                JoinTask task = new JoinTask();
                task.inputPaths.add(a.outputPath);
                task.inputPaths.add(b.outputPath);
                task.outputPath = getTmpPath(date, tmpIndex);
                ++tmpIndex;
                task.attributes = new ArrayList<>();
                task.attributes.addAll(a.attributes);
                task.attributes.addAll(b.attributes);
                task.joinMapOperator = new JoinMapOperator();
                task.joinMapOperator.setup(a, b, equalConditionsBetweenAB);
                if (notEqualConditionsBetweenAB.size() != 0) {
                    task.filterOperator = new FilterOperator();
                    task.filterOperator.setup(notEqualConditionsBetweenAB, task.attributes);
                }
                JoinTaskUtil.runTask(task);
                needJoinRelations.offer(task);
                lastJoinTask = task;
            }
        }

        MapReduceTask finalTask = null;
        if (!hasGroupByFunc) {
            SelectFilterTask selectFilterTask = new SelectFilterTask();
            selectFilterTask.inputPaths.add(lastJoinTask.outputPath);
            //selectFilterTask.outputPath = getOutputPath(date);
            selectFilterTask.outputPath = getTmpPath(date, tmpIndex);
            ++tmpIndex;
            selectFilterTask.attributes = attributes;
            SelectMapOperator selectOp = new SelectMapOperator();
            selectOp.setup(attributes, lastJoinTask.attributes);
            selectFilterTask.selectOperator = selectOp;
            SelectFilterTaskUtil.runTask(selectFilterTask);
            finalTask = selectFilterTask;
        } else {
            GroupByFuncTask groupByFuncTask = new GroupByFuncTask();
            groupByFuncTask.inputPaths.add(lastJoinTask.outputPath);
            groupByFuncTask.outputPath = getTmpPath(date, tmpIndex);
            ++tmpIndex;
            groupByFuncTask.attributes = attributes;
            SelectMapOperator selectValueOp = new SelectMapOperator();
            selectValueOp.setup(attributes, lastJoinTask.attributes);
            groupByFuncTask.selectValueOperator = selectValueOp;
            SelectMapOperator selectKeyOp = null;
            List<Attribute> groupByAttributes = new ArrayList<>();
            for (Attribute attribute : attributes) {
                if (attribute.getFunc() == null) {
                    groupByAttributes.add(attribute);
                }
            }
            if (!groupByAttributes.isEmpty()) {
                selectKeyOp = new SelectMapOperator();
                selectKeyOp.setup(groupByAttributes, lastJoinTask.attributes);
            }
            groupByFuncTask.selectKeyOperator = selectKeyOp;
            FuncOperator funcOp = new FuncOperator();
            funcOp.setup(attributes);
            groupByFuncTask.funcOperator = funcOp;
            GroupByFuncTaskUtil.runTask(groupByFuncTask);
            finalTask = groupByFuncTask;
        }

        if (orderByAttribute != null) {
            OrderByTask orderByTask = new OrderByTask();
            orderByTask.inputPaths.add(finalTask.outputPath);
            orderByTask.outputPath = getTmpPath(date, tmpIndex);
            ++tmpIndex;
            orderByTask.attributes = attributes;
            SetKeyOperator setKeyOp = new SetKeyOperator();
            setKeyOp.setup(orderByAttribute, attributes);
            orderByTask.setKeyOperator = setKeyOp;
            orderByTask.orderByType = orderByAttribute.getType();
            OrderByTaskUtil.runTask(orderByTask);
            finalTask = orderByTask;
        }

        Path path = Paths.get(finalTask.outputPath + "/part-r-00000");
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
        List<Attribute> attributes = new ArrayList<>();
        for (Attribute uncheckedAttribute : uncheckedAttributes) {
            if (uncheckedAttribute.getAttributeName().equals("*")) {
                String relationName = uncheckedAttribute.getRelationName();
                if (relationName == null) {
                    for (Relation relation : relations.values()) {
                        attributes.addAll(relation.getAttributeMap().values());
                    }
                } else {
                    if (!relations.containsKey(relationName)) {
                        throw new RuntimeException(String.format("No Such Relation(%s:%s) in SQL From Statement!!!",
                                relationName, "*"));
                    }
                    attributes.addAll(relations.get(relationName).getAttributeMap().values());
                }
            } else {
                Attribute attribute = checkAttribute(uncheckedAttribute, relations);
                uncheckedAttribute.setRelationName(attribute.getRelationName());
                uncheckedAttribute.setIndex(attribute.getIndex());
                uncheckedAttribute.setType(attribute.getType());
                attribute = uncheckedAttribute;
                Func func = attribute.getFunc();
                if (func != null && !func.compatibleWith(attribute.getType())) {
                    throw new RuntimeException(String.format("Function %s(%s:%s) with invalid type!!!",
                            func, attribute.getRelationName(), attribute.getAttributeName()));
                }
                attributes.add(attribute);
            }
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
                throw new RuntimeException(String.format("Not Same Type between Attribute(%s:%s) and Attribute(%s:%s)",
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

    private boolean checkGroupByAttributes(List<Attribute> uncheckedGroupByAttributes, Map<String, Relation> relations, List<Attribute> attributes) {
        boolean hasGroupByFunc = !uncheckedGroupByAttributes.isEmpty();
        if (!hasGroupByFunc) {
            for (Attribute attribute : attributes) {
                if (attribute.getFunc() != null) {
                    hasGroupByFunc = true;
                    break;
                }
            }
            if (!hasGroupByFunc) {
                return false;
            }
        }
        List<Attribute> groupByAttributes = new ArrayList<>();
        for (Attribute uncheckedGroupByAttribute : uncheckedGroupByAttributes) {
            Attribute groupByAttribute = checkAttribute(uncheckedGroupByAttribute, relations);
            boolean found = false;
            for (Attribute attribute : attributes) {
                if (attribute.equals(groupByAttribute)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new RuntimeException(String.format("Group by attribute(%s:%s) is not in select clause",
                        groupByAttribute.getRelationName(), groupByAttribute.getAttributeName()));
            }
            groupByAttributes.add(groupByAttribute);
        }
        for (Attribute attribute : attributes) {
            if (attribute.getFunc() == null) {
                boolean found = false;
                for (Attribute groupByAttribute : groupByAttributes) {
                    if (attribute.equals(groupByAttribute)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new RuntimeException(String.format("Select attribute(%s:%s) is in neither function nor group by clause",
                            attribute.getRelationName(), attribute.getAttributeName()));
                }
            }
        }
        return true;
    }

    private Attribute checkOrderByAttribute(Attribute uncheckedOrderByAttribute, Map<String, Relation> relations, List<Attribute> attributes) {
        if (uncheckedOrderByAttribute == null) {
            return null;
        }
        Attribute orderByAttribute = checkAttribute(uncheckedOrderByAttribute, relations);
        uncheckedOrderByAttribute.setRelationName(orderByAttribute.getRelationName());
        uncheckedOrderByAttribute.setIndex(orderByAttribute.getIndex());
        uncheckedOrderByAttribute.setType(orderByAttribute.getType());
        orderByAttribute = uncheckedOrderByAttribute;
        Func func = orderByAttribute.getFunc();
        if (func != null && !func.compatibleWith(orderByAttribute.getType())) {
            throw new RuntimeException(String.format("Function %s(%s:%s) with invalid type!!!",
                    func, orderByAttribute.getRelationName(), orderByAttribute.getAttributeName()));
        }
        boolean found = false;
        for (Attribute attribute : attributes) {
            if (attribute.equals(orderByAttribute)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new RuntimeException(String.format("Order by attribute(%s:%s) is not in select clause",
                    orderByAttribute.getRelationName(), orderByAttribute.getAttributeName()));
        }
        return orderByAttribute;
    }

    private void classifyAttribute(Attribute attribute, Map<String, List<Attribute>> relationAttributeMap) {
        String relationName = attribute.getRelationName();
        attribute = activeDatabase.getAttribute(relationName, attribute.getAttributeName()).getValue();
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
