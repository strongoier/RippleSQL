java -jar lib/jflex-1.6.1.jar src/main/java/com/ripple/frontend/Lexer.jflex
java -jar lib/jay-1.1.1.jar src/main/java/com/ripple/frontend/Parser.jay > src/main/java/com/ripple/frontend/Parser.java
