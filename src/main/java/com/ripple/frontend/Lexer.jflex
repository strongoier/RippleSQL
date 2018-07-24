package com.ripple.frontend;

%%
%class Lexer
%public
%implements Parser.yyInput
%integer
%line
%column
%unicode
%ignorecase

%{
  private StringBuilder buffer;
  private int token;
  private Object value;

  public boolean advance() throws java.io.IOException {
    value = null;
    token = yylex();
    return (token != YYEOF);
  }

  public int token() {
    return token;
  }

  public Object value() {
    return value;
  }
%}

WHITESPACE = ([ \t]+)
DIGIT      = ([0-9])
LETTER     = ([A-Za-z])
INT        = ([+-]?{DIGIT}+)
FLOAT      = ([+-]?{DIGIT}+\.{DIGIT}+)
IDENTIFIER = ({LETTER}({LETTER}|{DIGIT}|_)*)

%x STRING

%%

{WHITESPACE} {}
"show"       { return Parser.SHOW; }
"databases"  { return Parser.DATABASES; }
"use"        { return Parser.USE; }
"tables"     { return Parser.TABLES; }
"desc"       { return Parser.DESC; }
"select"     { return Parser.SELECT; }
"from"       { return Parser.FROM; }
"where"      { return Parser.WHERE; }
"and"        { return Parser.AND; }
[;.,*]       { return yycharat(0); }
"<"          { return Parser.LT; }
"<="         { return Parser.LE; }
">"          { return Parser.GT; }
">="         { return Parser.GE; }
"="          { return Parser.EQ; }
"<>"         { return Parser.NE; }
{INT}        { value = new String(yytext()); return Parser.INT; }
{FLOAT}      { value = new String(yytext()); return Parser.FLOAT; }
{IDENTIFIER} { value = new String(yytext()); return Parser.IDENTIFIER; }
\"           { yybegin(STRING); buffer = new StringBuilder(); }
<STRING>\"   { yybegin(YYINITIAL); value = buffer.toString(); return Parser.STRING; }
<STRING>.    { buffer.append(yytext()); }
<STRING>[^]  { throw new RuntimeException("lexer error, unrecognized character " + yytext()); }
[^]          { throw new RuntimeException("lexer error, unrecognized character " + yytext()); }