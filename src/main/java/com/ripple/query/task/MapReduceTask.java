package com.ripple.query.task;

import com.ripple.database.Attribute;

import java.util.ArrayList;
import java.util.List;

public class MapReduceTask {
    // must not be null
    public List<String> inputPaths = new ArrayList<>();
    // must not be null
    public String outputPath = null;
    // attributes after task
    public List<Attribute> attributes;
    // will set after task
    public long length;
    // will set after task
    public long lines;
}
