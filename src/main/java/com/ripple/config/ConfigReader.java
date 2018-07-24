package com.ripple.config;

import com.ripple.database.Database;

import java.io.IOException;
import java.util.Map;

public abstract class ConfigReader {
    public abstract Map<String, Database> getDatabases() throws IOException;
}
