package com.ripple;

import com.ripple.config.JsonConfigReader;
import com.ripple.frontend.Lexer;
import com.ripple.frontend.Parser;
import com.ripple.query.QueryManager;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws Exception {
        Configuration config = new GenericOptionsParser(args).getConfiguration();
        String configPath = config.get("config.path", "relations.json");
        String sqlPath = config.get("sql.path");
        QueryManager manager = new QueryManager();
        manager.setConfigReader(new JsonConfigReader(configPath));
        manager.initialize();
        BufferedReader reader = null;
        if (sqlPath == null)
            reader = new BufferedReader(new InputStreamReader(System.in));
        else
            reader = Files.newBufferedReader(Paths.get(sqlPath));
        while (true) {
            System.out.print("RippleSQL >> ");
            String line = reader.readLine();
            if (line == null) break;
            if (line.equals("quit;")) break;
            Lexer lexer = new Lexer(new StringReader(line));
            Parser parser = new Parser(manager);
            try {
                parser.yyparse(lexer);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }
}
