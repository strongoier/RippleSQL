package com.ripple;

import com.ripple.config.JsonConfigReader;
import com.ripple.frontend.Lexer;
import com.ripple.frontend.Parser;
import com.ripple.query.QueryManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;

public class App {
    public static void main(String[] args) throws Exception {
        QueryManager manager = new QueryManager();
        manager.setConfigReader(new JsonConfigReader("file/relations.json"));
        manager.initialize();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("RippleSQL >> ");
            String line = reader.readLine();
            if (line.equals("quit;")) break;
            Lexer lexer = new Lexer(new StringReader(line));
            Parser parser = new Parser(manager);
            try {
                parser.yyparse(lexer);
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
            } catch (Exception e) {
            }
        }
    }
}
