package com.ripple;

import com.ripple.config.JsonConfigReader;
import com.ripple.frontend.Lexer;
import com.ripple.frontend.Parser;
import com.ripple.query.QueryManager;

import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws Exception {
        QueryManager manager = new QueryManager();
        manager.setConfigReader(new JsonConfigReader("file/relations.json"));
        manager.initialize();
        //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader reader = new BufferedReader(Files.newBufferedReader(Paths.get("file/Test/in0_1.sql")));
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
