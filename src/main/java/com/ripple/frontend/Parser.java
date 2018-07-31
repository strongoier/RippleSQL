// created by jay 1.1.0 (c) 2002-2006 ats@cs.rit.edu
// skeleton Java 1.1.0 (c) 2002-2006 ats@cs.rit.edu

					// line 2 "src/main/java/com/ripple/frontend/Parser.jay"
package com.ripple.frontend;

import com.ripple.database.*;
import com.ripple.database.binop.*;
import com.ripple.database.func.*;
import com.ripple.database.value.*;
import com.ripple.query.*;

import java.util.*;

public class Parser {

  private QueryManager manager;

  public Parser(QueryManager m) {
    manager = m;
  }

					// line 24 "-"
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
  public static final int SUM = 266;
  public static final int MAX = 267;
  public static final int MIN = 268;
  public static final int AVG = 269;
  public static final int COUNT = 270;
  public static final int GROUP = 271;
  public static final int ORDER = 272;
  public static final int BY = 273;
  public static final int LT = 274;
  public static final int LE = 275;
  public static final int GT = 276;
  public static final int GE = 277;
  public static final int EQ = 278;
  public static final int NE = 279;
  public static final int INT = 280;
  public static final int FLOAT = 281;
  public static final int STRING = 282;
  public static final int IDENTIFIER = 283;
  public static final int yyErrorCode = 256;

  /** number of final state.
    */
  protected static final int yyFinal = 5;

  /** parser tables.
      Order is mandated by <i>jay</i>.
    */
  protected static final short[] yyLhs = {
//yyLhs 44
    -1,     0,     0,     0,     0,     0,     1,     1,     2,     2,
     2,     3,     3,     4,     4,     5,     5,     6,     6,     7,
     7,     7,     7,     7,     7,     8,     8,     8,     9,     9,
    10,    10,    11,    11,    12,    12,    13,    13,    13,    13,
    13,    14,    14,    15,
    }, yyLen = {
//yyLen 44
     2,     3,     3,     3,     3,     8,     3,     1,     1,     3,
     1,     3,     1,     2,     1,     3,     1,     3,     3,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     3,     1,
     3,     1,     3,     1,     4,     1,     1,     1,     1,     1,
     1,     3,     1,     0,
    }, yyDefRed = {
//yyDefRed 75
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
    36,    37,    38,    39,    40,     0,    10,     0,     7,     8,
     0,    35,     1,     3,     2,     4,     0,     0,     0,     0,
    41,     9,    12,     0,     6,     0,     0,     0,     0,     0,
    14,     0,    34,     0,    16,     0,    11,     0,     0,    29,
     0,    19,    20,    21,    22,    23,    24,     0,     0,     0,
     0,    33,    15,    26,    27,    25,    18,    17,     0,    31,
     0,     5,     0,    32,    30,
    }, yyDgoto = {
//yyDgoto 16
     5,    17,    18,    33,    39,    43,    44,    57,    66,    48,
    68,    60,    19,    20,    21,    40,
    }, yySindex = {
//yySindex 75
  -235,  -250,  -271,  -258,   -42,     0,   -30,   -29,   -27,   -26,
     0,     0,     0,     0,     0,    -3,     0,   -40,     0,     0,
     8,     0,     0,     0,     0,     0,   -41,  -234,   -42,  -232,
     0,     0,     0,   -35,     0,     6,    12,  -232,  -229,  -216,
     0,  -227,     0,  -208,     0,  -237,     0,  -215,  -213,     0,
  -232,     0,     0,     0,     0,     0,     0,  -236,  -232,  -212,
     1,     0,     0,     0,     0,     0,     0,     0,    18,     0,
  -249,     0,  -232,     0,     0,
    }, yyRindex = {
//yyRindex 75
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,   -33,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,   -57,     0,   -28,     0,     0,     0,   -56,
     0,     0,     0,   -54,     0,     0,     0,     0,     4,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,   -53,     0,
     0,     0,     0,     0,     0,
    }, yyGindex = {
//yyGindex 16
     0,     0,    36,     0,     0,     0,    15,     0,     0,     0,
     0,     0,    -4,     0,   -22,   -25,
    }, yyTable = {
//yyTable 252
    16,    31,    43,    43,    28,    13,    28,    36,     6,    38,
     7,    42,     8,    42,    49,    45,    42,    10,    11,    12,
    13,    14,     1,    61,     2,     9,     3,     4,    45,    22,
    23,    42,    24,    25,    35,    67,    69,    51,    52,    53,
    54,    55,    56,    26,    63,    64,    65,    35,    29,    32,
    74,    35,    41,    42,    46,    47,    30,    50,    58,    59,
    71,    70,    72,    43,    34,    62,    73,     0,     0,     0,
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
     0,     0,     0,     0,    43,    43,    43,    13,    13,    28,
     0,     0,     0,    27,    10,    11,    12,    13,    14,    37,
    42,     0,     0,     0,     0,     0,     0,    42,     0,     0,
     0,    15,    30,    42,    42,     0,    42,    42,    42,    42,
    42,    42,
    }, yyCheck = {
//yyCheck 252
    42,    42,    59,    59,    44,    59,    59,    29,   258,    44,
   260,    44,   283,    41,    39,    37,    44,   266,   267,   268,
   269,   270,   257,    48,   259,   283,   261,   262,    50,    59,
    59,    59,    59,    59,   283,    57,    58,   274,   275,   276,
   277,   278,   279,    46,   280,   281,   282,   283,    40,   283,
    72,   283,    46,    41,   283,   271,   283,   265,   273,   272,
    59,   273,    44,    59,    28,    50,    70,    -1,    -1,    -1,
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
    -1,    -1,    -1,    -1,   271,   272,   272,   271,   272,   272,
    -1,    -1,    -1,   263,   266,   267,   268,   269,   270,   264,
   263,    -1,    -1,    -1,    -1,    -1,    -1,   265,    -1,    -1,
    -1,   283,   283,   271,   272,    -1,   274,   275,   276,   277,
   278,   279,
    };

  /** maps symbol value to printable name.
      @see #yyExpecting
    */
  protected static final String[] yyNames = {
    "end-of-file",null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    "'('","')'","'*'",null,"','",null,"'.'",null,null,null,null,null,null,
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
    "TABLES","DESC","SELECT","FROM","WHERE","AND","SUM","MAX","MIN","AVG",
    "COUNT","GROUP","ORDER","BY","LT","LE","GT","GE","EQ","NE","INT",
    "FLOAT","STRING","IDENTIFIER",
    };

//t  /** printable rules for debugging.
//t    */
//t  protected static final String [] yyRule = {
//t    "$accept : command",
//t    "command : SHOW DATABASES ';'",
//t    "command : USE IDENTIFIER ';'",
//t    "command : SHOW TABLES ';'",
//t    "command : DESC IDENTIFIER ';'",
//t    "command : SELECT func_or_attr_or_star_list FROM relation_list opt_where_clause opt_group_by_clause opt_order_by_clause ';'",
//t    "func_or_attr_or_star_list : func_or_attr_or_star_list ',' func_or_attr_or_star",
//t    "func_or_attr_or_star_list : func_or_attr_or_star",
//t    "func_or_attr_or_star : func_or_attr",
//t    "func_or_attr_or_star : IDENTIFIER '.' '*'",
//t    "func_or_attr_or_star : '*'",
//t    "relation_list : relation_list ',' IDENTIFIER",
//t    "relation_list : IDENTIFIER",
//t    "opt_where_clause : WHERE cond_list",
//t    "opt_where_clause : nothing",
//t    "cond_list : cond_list AND cond",
//t    "cond_list : cond",
//t    "cond : attr op attr",
//t    "cond : attr op value",
//t    "op : LT",
//t    "op : LE",
//t    "op : GT",
//t    "op : GE",
//t    "op : EQ",
//t    "op : NE",
//t    "value : STRING",
//t    "value : INT",
//t    "value : FLOAT",
//t    "opt_group_by_clause : GROUP BY attr_list",
//t    "opt_group_by_clause : nothing",
//t    "attr_list : attr_list ',' attr",
//t    "attr_list : attr",
//t    "opt_order_by_clause : ORDER BY func_or_attr",
//t    "opt_order_by_clause : nothing",
//t    "func_or_attr : func '(' attr ')'",
//t    "func_or_attr : attr",
//t    "func : SUM",
//t    "func : MAX",
//t    "func : MIN",
//t    "func : AVG",
//t    "func : COUNT",
//t    "attr : IDENTIFIER '.' IDENTIFIER",
//t    "attr : IDENTIFIER",
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
					// line 51 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    manager.showDatabases();
  }
  break;
case 2:
					// line 55 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    manager.activeDatabase(((String)yyVals[-1+yyTop]));
  }
  break;
case 3:
					// line 59 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    manager.showRelations();
  }
  break;
case 4:
					// line 63 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    manager.descRelation(((String)yyVals[-1+yyTop]));
  }
  break;
case 5:
					// line 67 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    manager.select(((List<Attribute>)yyVals[-6+yyTop]), ((List<String>)yyVals[-4+yyTop]), ((List<Condition>)yyVals[-3+yyTop]), ((List<Attribute>)yyVals[-2+yyTop]), ((Attribute)yyVals[-1+yyTop]));
  }
  break;
case 6:
					// line 73 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    ((List<Attribute>)yyVal).add(((Attribute)yyVals[0+yyTop]));
  }
  break;
case 7:
					// line 77 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new ArrayList<>();
    ((List<Attribute>)yyVal).add(((Attribute)yyVals[0+yyTop]));
  }
  break;
case 8:
					// line 85 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = ((Attribute)yyVals[0+yyTop]);
  }
  break;
case 9:
					// line 89 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new Attribute(((String)yyVals[-2+yyTop]), "*");
  }
  break;
case 10:
					// line 93 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new Attribute("*");
  }
  break;
case 11:
					// line 99 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    ((List<String>)yyVal).add(((String)yyVals[0+yyTop]));
  }
  break;
case 12:
					// line 103 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new ArrayList<>();
    ((List<String>)yyVal).add(((String)yyVals[0+yyTop]));
  }
  break;
case 13:
					// line 110 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = ((List<Condition>)yyVals[0+yyTop]);
  }
  break;
case 14:
					// line 114 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new ArrayList<>();
  }
  break;
case 15:
					// line 120 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    ((List<Condition>)yyVal).add(((Condition)yyVals[0+yyTop]));
  }
  break;
case 16:
					// line 124 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new ArrayList<>();
    ((List<Condition>)yyVal).add(((Condition)yyVals[0+yyTop]));
  }
  break;
case 17:
					// line 131 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new Condition(((Attribute)yyVals[-2+yyTop]), ((BinOp)yyVals[-1+yyTop]), ((Attribute)yyVals[0+yyTop]));
  }
  break;
case 18:
					// line 135 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new Condition(((Attribute)yyVals[-2+yyTop]), ((BinOp)yyVals[-1+yyTop]), ((Value)yyVals[0+yyTop]));
  }
  break;
case 19:
					// line 141 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = BinOp.ltOp;
  }
  break;
case 20:
					// line 145 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = BinOp.leOp;
  }
  break;
case 21:
					// line 149 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = BinOp.gtOp;
  }
  break;
case 22:
					// line 153 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = BinOp.geOp;
  }
  break;
case 23:
					// line 157 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = BinOp.eqOp;
  }
  break;
case 24:
					// line 161 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = BinOp.neOp;
  }
  break;
case 25:
					// line 167 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new StringValue(((String)yyVals[0+yyTop]));
  }
  break;
case 26:
					// line 171 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new IntValue(((String)yyVals[0+yyTop]));
  }
  break;
case 27:
					// line 175 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new FloatValue(((String)yyVals[0+yyTop]));
  }
  break;
case 28:
					// line 181 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = ((List<Attribute>)yyVals[0+yyTop]);
  }
  break;
case 29:
					// line 185 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new ArrayList<>();
  }
  break;
case 30:
					// line 191 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    ((List<Attribute>)yyVal).add(((Attribute)yyVals[0+yyTop]));
  }
  break;
case 31:
					// line 195 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new ArrayList<>();
    ((List<Attribute>)yyVal).add(((Attribute)yyVals[0+yyTop]));
  }
  break;
case 32:
					// line 202 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = ((Attribute)yyVals[0+yyTop]);
  }
  break;
case 33:
					// line 206 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = null;
  }
  break;
case 34:
					// line 212 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = ((Attribute)yyVals[-1+yyTop]);
    ((Attribute)yyVal).setFunc(((Func)yyVals[-3+yyTop]));
  }
  break;
case 35:
					// line 217 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = ((Attribute)yyVals[0+yyTop]);
  }
  break;
case 36:
					// line 223 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = Func.sumFunc;
  }
  break;
case 37:
					// line 227 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = Func.maxFunc;
  }
  break;
case 38:
					// line 231 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = Func.minFunc;
  }
  break;
case 39:
					// line 235 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = Func.avgFunc;
  }
  break;
case 40:
					// line 239 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = Func.countFunc;
  }
  break;
case 41:
					// line 245 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new Attribute(((String)yyVals[-2+yyTop]), ((String)yyVals[0+yyTop]));
  }
  break;
case 42:
					// line 249 "src/main/java/com/ripple/frontend/Parser.jay"
  {
    yyVal = new Attribute(((String)yyVals[0+yyTop]));
  }
  break;
case 43:
					// line 255 "src/main/java/com/ripple/frontend/Parser.jay"
  {
  }
  break;
					// line 728 "-"
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

					// line 259 "src/main/java/com/ripple/frontend/Parser.jay"

} // closing brace for the parser class
      					// line 761 "-"
