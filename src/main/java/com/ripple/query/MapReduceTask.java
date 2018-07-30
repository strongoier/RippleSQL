package com.ripple.query;

import com.ripple.database.Attribute;

import java.util.List;

public class MapReduceTask {
    // must not be null
    public String inputPath = null;
    // must not be null
    public String outputPath = null;
    // attributes after task
    public List<Attribute> attributes;
}
