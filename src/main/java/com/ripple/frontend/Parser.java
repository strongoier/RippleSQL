// created by jay 1.1.0 (c) 2002-2006 ats@cs.rit.edu
// skeleton Java 1.1.0 (c) 2002-2006 ats@cs.rit.edu

					// line 2 "src/main/java/com/ripple/frontend/Parser.jay"
package com.ripple.frontend;

import com.ripple.database.*;
import com.ripple.database.binop.*;
import com.ripple.database.value.*;
import com.ripple.query.*;

import java.util.*;

public class Parser {

  private QueryManager manager;

  public Parser(QueryManager m) {
    manager = m;
  }

					// line 23 "-"
  // %token constants
  public static final int SHOW = 257;
  public static final int DATABASES = 258;
  public static final int USE = 259;
  public static final int TABLES = 260;
  public static final int DESC = 261;
  public static final int SELECT = 262;
  public static final int FROM = 263;
  public static final int WHERE = 264;
  public static final int AND = 265;
  public static final int LT = 266;
  public static final int LE = 267;
  public static final int GT = 268;
  public static final int GE = 269;
  public static final int EQ = 270;
  public static final int NE = 271;
  public static final int INT = 272;
  public static final int FLOAT = 273;
  public static final int STRING = 274;
  public static final int IDENTIFIER = 275;
  public static final int yyErrorCode = 256;

  /** number of final state.
    */
  protected static final int yyFinal = 5;

  /** parser tables.
      Order is mandated by <i>jay</i>.
    */
  protected static final short[] yyLhs = {
//yyLhs 30
    -1,     0,     0,     0,     0,     0,     6,     6,     5,     5,
     4,     4,     3,     3,     7,     7,     8,     8,     9,     9,
     1,     1,     1,     2,     2,     2,     2,     2,     2,    10,
    }, yyLen = {
//yyLen 30
     2,     3,     3,     3,     3,     6,     3,     1,     3,     1,
     1,     1,     3,     1,     2,     1,     3,     1,     3,     3,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     0,
    }, yyDefRed = {
//yyDefRed 50
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,    11,     9,     7,     0,     1,     3,     2,     4,     0,
     0,     0,    10,     8,    13,     0,     6,     0,     0,     0,
    15,     0,     0,    17,    12,     5,    23,    24,    25,    26,
    27,    28,     0,     0,    21,    22,    20,    19,    18,    16,
    }, yyDgoto = {
//yyDgoto 11
     5,    47,    42,    25,    12,    31,    14,    29,    32,    33,
    30,
    }, yySindex = {
//yySindex 50
  -243,  -254,  -260,  -253,   -41,     0,   -38,   -36,   -35,   -34,
   -20,     0,     0,     0,   -37,     0,     0,     0,     0,   -40,
  -248,   -41,     0,     0,     0,   -39,     0,   -41,  -247,   -30,
     0,  -258,  -235,     0,     0,     0,     0,     0,     0,     0,
     0,     0,   -42,   -41,     0,     0,     0,     0,     0,     0,
    }, yyRindex = {
//yyRindex 50
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
   -27,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,   -28,     0,     0,     0,     0,
     0,     0,   -26,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
    }, yyGindex = {
//yyGindex 11
     0,     0,     0,     0,    15,    -1,     0,     0,     0,    -8,
     0,
    }, yyTable = {
//yyTable 245
    11,    11,    11,    13,     6,    28,     7,    21,    36,    37,
    38,    39,    40,    41,     1,     8,     2,    10,     3,     4,
    26,    15,     9,    16,    17,    18,    19,    24,    34,    35,
    43,    29,    10,    14,    23,    49,     0,     0,     0,     0,
     0,    48,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,    27,    20,     0,     0,     0,
    44,    45,    46,    10,    10,    22,    10,     0,    10,    10,
    10,    10,    10,    10,    10,
    }, yyCheck = {
//yyCheck 245
    42,    42,    42,     4,   258,    44,   260,    44,   266,   267,
   268,   269,   270,   271,   257,   275,   259,    44,   261,   262,
    21,    59,   275,    59,    59,    59,    46,   275,   275,    59,
   265,    59,    59,    59,    19,    43,    -1,    -1,    -1,    -1,
    -1,    42,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,   264,   263,    -1,    -1,    -1,
   272,   273,   274,   275,   275,   275,   263,    -1,   265,   266,
   267,   268,   269,   270,   271,
    };

  /** maps symbol value to printable name.
      @see #yyExpecting
    */
  protected static final String[] yyNames = {
    "end-of-file",null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,"'*'",null,"','",null,"'.'",null,null,null,null,null,null,
    null,null,null,null,null,null,"';'",null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,"SHOW","DATABASES","USE",
    "TABLES","DESC","SELECT","FROM","WHERE","AND","LT","LE","GT","GE",
    "EQ","NE","INT","FLOAT","STRING","IDENTIFIER",
    };

//t  /** printable rules for debugging.
//t    */
//t  protected static final String [] yyRule = {
//t    "$accept : command",
//t    "command : SHOW DATABASES ';'",
//t    "command : USE IDENTIFIER ';'",
//t    "command : SHOW TABLES ';'",
//t    "command : DESC IDENTIFIER ';'",
//t    "command : SELECT attr_list FROM relation_list opt_where_clause ';'",
//t    "attr_list : attr_list ',' attr",
//t    "attr_list : attr",
//t    "attr : IDENTIFIER '.' attr_name",
//t    "attr : attr_name",
//t    "attr_name : IDENTIFIER",
//t    "attr_name : '*'",
//t    "relation_list : relation_list ',' IDENTIFIER",
//t    "relation_list : IDENTIFIER",
//t    "opt_where_clause : WHERE cond_list",
//t    "opt_where_clause : nothing",
//t    "cond_list : cond_list AND cond",
//t    "cond_list : cond",
//t    "cond : attr op attr",
//t    "cond : attr op value",
//t    "value : STRING",
//t    "value : INT",
//t    "value : FLOAT",
//t    "op : LT",
//t    "op : LE",
//t    "op : GT",
//t    "op : GE",
//t    "op : EQ",
//t    "op : NE",
//t    "nothing :",
//t    };
//t
//t  /** debugging support, requires the package <tt>jay.yydebug</tt>.
//t      Set to <tt>null</tt> to suppress debugging messages.
//t    */
//t  protected jay.yydebug.yyDebug yydebug;
//t
//t  /** index-checked interface to {@link #yyNames}.
//t      @param token single character or <tt>%token</tt> value.
//t      @return token name or <tt>[illegal]</tt> or <tt>[unknown]</tt>.
//t    */
//t  public static final String yyName (int token) {
//t    if (token < 0 || token > yyNames.length) return "[illegal]";
//t    String name;
//t    if ((name = yyNames[token]) != null) return name;
//t    return "[unknown]";
//t  }
//t
  /** thrown for irrecoverable syntax errors and stack overflow.
      Nested for convenience, does not depend on parser class.
    */
  public static class yyException extends java.lang.Exception {
    public yyException (String message) {
      super(message);
    }
  }

  /** must be implemented by a scanner object to supply input to the parser.
      Nested for convenience, does not depend on parser class.
    */
  public interface yyInput {

    /** move on to next token.
        @return <tt>false</tt> if positioned beyond tokens.
        @throws IOException on input error.
      */
    boolean advance () throws java.io.IOException;

    /** classifies current token.
        Should not be called if {@link #advance()} returned <tt>false</tt>.
        @return current <tt>%token</tt> or single character.
      */
    int token ();

    /** associated with current token.
        Should not be called if {@link #advance()} returned <tt>false</tt>.
        @return value for {@link #token()}.
      */
    Object value ();
  }

  /** simplified error message.
      @see #yyerror(java.lang.String, java.lang.String[])
    */
  public void yyerror (String message) {
    yyerror(message, null);
  }

  /** (syntax) error message.
      Can be overwritten to control message format.
      @param message text to be displayed.
      @param expected list of acceptable tokens, if available.
    */
  public void yyerror (String message, String[] expected) {
    if (expected != null && expected.length > 0) {
      System.err.print(message+", expecting");
      for (int n = 0; n < expected.length; ++ n)
        System.err.print(" "+expected[n]);
      System.err.println();
    } else
      System.err.println(message);
  }

  /** computes list of expected tokens on error by tracing the tables.
      @param state for which to compute the list.
      @return list of token names.
    */
  protected String[] yyExpecting (int state) {
    int token, n, len = 0;
    boolean[] ok = new boolean[yyNames.length];

    if ((n = yySindex[state]) != 0)
      for (token = n < 0 ? -n : 0;
           token < yyNames.length && n+token < yyTable.length; ++ token)
        if (yyCheck[n+token] == token && !ok[token] && yyNames[token] != null) {
          ++ len;
          ok[token] = true;
        }
    if ((n = yyRindex[state]) != 0)
      for (token = n < 0 ? -n : 0;
           token < yyNames.length && n+token < yyTable.length; ++ token)
        if (yyCheck[n+token] == token && !ok[token] && yyNames[token] != null) {
          ++ len;
          ok[token] = true;
        }

    String result[] = new String[len];
    for (n = token = 0; n < len;  ++ token)
      if (ok[token]) result[n++] = yyNames[token];
    return result;
  }

  /** the generated parser, with debugging messages.
      Maintains a dynamic state and value stack.
      @param yyLex scanner.
      @param yydebug debug message writer implementing <tt>yyDebug</tt>, or <tt>null</tt>.
      @return result of the last reduction, if any.
      @throws yyException on irrecoverable parse error.
    */
  public Object yyparse (yyInput yyLex, Object yydebug)
				throws java.io.IOException, yyException {
//t    this.yydebug = (jay.yydebug.yyDebug)yydebug;
    return yyparse(yyLex);
  }

  /** initial size and increment of the state/value stack [default 256].
      This is not final so that it can be overwritten outside of invocations
      of {@link #yyparse}.
    */
  protected int yyMax;

  /** executed at the beginning of a reduce action.
      Used as <tt>$$ = yyDefault($1)</tt>, prior to the user-specified action, if any.
      Can be overwritten to provide deep copy, etc.
      @param first value for <tt>$1</tt>, or <tt>null</tt>.
      @return first.
    */
  protected Object yyDefault (Object first) {
    return first;
  }

  /** the generated parser.
      Maintains a dynamic state and value stack.
      @param yyLex scanner.
      @return result of the last reduction, if any.
      @throws yyException on irrecoverable parse error.
    */
  public Object yyparse (yyInput yyLex) throws java.io.IOException, yyException {
    if (yyMax <= 0) yyMax = 256;			// initial size
    int yyState = 0, yyStates[] = new int[yyMax];	// state stack
    Object yyVal = null, yyVals[] = new Object[yyMax];	// value stack
    int yyToken = -1;					// current input
    int yyErrorFlag = 0;				// #tokens to shift

    yyLoop: for (int yyTop = 0;; ++ yyTop) {
      if (yyTop >= yyStates.length) {			// dynamically increase
        int[] i = new int[yyStates.length+yyMax];
        System.arraycopy(yyStates, 0, i, 0, yyStates.length);
        yyStates = i;
        Object[] o = new Object[yyVals.length+yyMax];
        System.arraycopy(yyVals, 0, o, 0, yyVals.length);
        yyVals = o;
      }
      yyStates[yyTop] = yyState;
      yyVals[yyTop] = yyVal;
//t      if (yydebug != null) yydebug.push(yyState, yyVal);

      yyDiscarded: for (;;) {	// discarding a token does not change stack
        int yyN;
        if ((yyN = yyDefRed[yyState]) == 0) {	// else [default] reduce (yyN)
          if (yyToken < 0) {
            yyToken = yyLex.advance() ? yyLex.token() : 0;
//t            if (yydebug != null)
//t              yydebug.lex(yyState, yyToken, yyName(yyToken), yyLex.value());
          }
          if ((yyN = yySindex[yyState]) != 0 && (yyN += yyToken) >= 0
              && yyN < yyTable.length && yyCheck[yyN] == yyToken) {
//t            if (yydebug != null)
//t              yydebug.shift(yyState, yyTable[yyN], yyErrorFlag > 0 ? yyErrorFlag-1 : 0);
            yyState = yyTable[yyN];		// shift to yyN
            yyVal = yyLex.value();
            yyToken = -1;
            if (yyErrorFlag > 0) -- yyErrorFlag;
            continue yyLoop;
          }
          if ((yyN = yyRindex[yyState]) != 0 && (yyN += yyToken) >= 0
              && yyN < yyTable.length && yyCheck[yyN] == yyToken)
            yyN = yyTable[yyN];			// reduce (yyN)
          else
            switch (yyErrorFlag) {
  
            case 0:
              yyerror("syntax error", yyExpecting(yyState));
//t              if (yydebug != null) yydebug.error("syntax error");
  
            case 1: case 2:
              yyErrorFlag = 3;
              do {
                if ((yyN = yySindex[yyStates[yyTop]]) != 0
                    && (yyN += yyErrorCode) >= 0 && yyN < yyTable.length
                    && yyCheck[yyN] == yyErrorCode) {
//t                  if (yydebug != null)
//t                    yydebug.shift(yyStates[yyTop], yyTable[yyN], 3);
                  yyState = yyTable[yyN];
                  yyVal = yyLex.value();
                  continue yyLoop;
                }
//t                if (yydebug != null) yydebug.pop(yyStates[yyTop]);
              } while (-- yyTop >= 0);
//t              if (yydebug != null) yydebug.reject();
              throw new yyException("irrecoverable syntax error");
  
            case 3:
              if (yyToken == 0) {
//t                if (yydebug != null) yydebug.reject();
                throw new yyException("irrecoverable syntax error at end-of-file");
              }
//t              if (yydebug != null)
//t                yydebug.discard(yyState, yyToken, yyName(yyToken), yyLex.value());
              yyToken = -1;
              continue yyDiscarded;		// leave stack alone
            }
        }
        int yyV = yyTop + 1-yyLen[yyN];
//t        if (yydebug != null)
//t          yydebug.reduce(yyState, yyStates[yyV-1], yyN, yyRule[yyN], yyLen[yyN]);
        yyVal = yyDefault(yyV > yyTop ? null : yyVals[yyV]);
        switch (yyN) {
case 1:
					// line 44 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    manager.showDatabases();
  }
  break;
case 2:
					// line 48 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    manager.activeDatabase(((String)yyVals[-1+yyTop]));
  }
  break;
case 3:
					// line 52 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    manager.showRelations();
  }
  break;
case 4:
					// line 56 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    manager.descRelation(((String)yyVals[-1+yyTop]));
  }
  break;
case 5:
					// line 60 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    manager.select(((List<Attribute>)yyVals[-4+yyTop]), ((List<String>)yyVals[-2+yyTop]), ((List<Condition>)yyVals[-1+yyTop]));
  }
  break;
case 6:
					// line 66 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    ((List<Attribute>)yyVal).add(((Attribute)yyVals[0+yyTop]));
  }
  break;
case 7:
					// line 70 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new ArrayList<>();
    ((List<Attribute>)yyVal).add(((Attribute)yyVals[0+yyTop]));
  }
  break;
case 8:
					// line 77 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new Attribute(((String)yyVals[-2+yyTop]), ((String)yyVals[0+yyTop]));
  }
  break;
case 9:
					// line 81 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new Attribute(((String)yyVals[0+yyTop]));
  }
  break;
case 10:
					// line 87 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = ((String)yyVals[0+yyTop]);
  }
  break;
case 11:
					// line 91 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = "*";
  }
  break;
case 12:
					// line 97 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    ((List<String>)yyVal).add(((String)yyVals[0+yyTop]));
  }
  break;
case 13:
					// line 101 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new ArrayList<>();
    ((List<String>)yyVal).add(((String)yyVals[0+yyTop]));
  }
  break;
case 14:
					// line 109 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = ((List<Condition>)yyVals[0+yyTop]);
  }
  break;
case 15:
					// line 113 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new ArrayList<>();
  }
  break;
case 16:
					// line 119 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    ((List<Condition>)yyVal).add(((Condition)yyVals[0+yyTop]));
  }
  break;
case 17:
					// line 123 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new ArrayList<>();
    ((List<Condition>)yyVal).add(((Condition)yyVals[0+yyTop]));
  }
  break;
case 18:
					// line 130 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new Condition(((Attribute)yyVals[-2+yyTop]), ((BinOp)yyVals[-1+yyTop]), ((Attribute)yyVals[0+yyTop]));
  }
  break;
case 19:
					// line 134 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new Condition(((Attribute)yyVals[-2+yyTop]), ((BinOp)yyVals[-1+yyTop]), ((Value)yyVals[0+yyTop]));
  }
  break;
case 20:
					// line 140 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new StringValue(((String)yyVals[0+yyTop]));
  }
  break;
case 21:
					// line 144 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new IntValue(((String)yyVals[0+yyTop]));
  }
  break;
case 22:
					// line 148 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new FloatValue(((String)yyVals[0+yyTop]));
  }
  break;
case 23:
					// line 154 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = BinOp.ltOp;
  }
  break;
case 24:
					// line 158 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = BinOp.leOp;
  }
  break;
case 25:
					// line 162 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = BinOp.gtOp;
  }
  break;
case 26:
					// line 166 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = BinOp.geOp;
  }
  break;
case 27:
					// line 170 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = BinOp.eqOp;
  }
  break;
case 28:
					// line 174 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = BinOp.neOp;
  }
  break;
case 29:
					// line 180 "src/main/java/com/ripple/frontend/Parser.jay"
  {
  }
  break;
					// line 603 "-"
        }
        yyTop -= yyLen[yyN];
        yyState = yyStates[yyTop];
        int yyM = yyLhs[yyN];
        if (yyState == 0 && yyM == 0) {
//t          if (yydebug != null) yydebug.shift(0, yyFinal);
          yyState = yyFinal;
          if (yyToken < 0) {
            yyToken = yyLex.advance() ? yyLex.token() : 0;
//t            if (yydebug != null)
//t               yydebug.lex(yyState, yyToken,yyName(yyToken), yyLex.value());
          }
          if (yyToken == 0) {
//t            if (yydebug != null) yydebug.accept(yyVal);
            return yyVal;
          }
          continue yyLoop;
        }
        if ((yyN = yyGindex[yyM]) != 0 && (yyN += yyState) >= 0
            && yyN < yyTable.length && yyCheck[yyN] == yyState)
          yyState = yyTable[yyN];
        else
          yyState = yyDgoto[yyM];
//t        if (yydebug != null) yydebug.shift(yyStates[yyTop], yyState);
        continue yyLoop;
      }
    }
  }

					// line 184 "src/main/java/com/ripple/frontend/Parser.jay"

} // closing brace for the parser class
      					// line 636 "-"
