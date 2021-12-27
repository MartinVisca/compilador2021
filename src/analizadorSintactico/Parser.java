//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"

package analizadorSintactico;

import java.util.Vector;

import analizadorLexico.AnalizadorLexico;
import analizadorSintactico.AnalizadorSintactico;
import analizadorLexico.RegistroSimbolo;

//#line 27 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CTE=258;
public final static short IF=259;
public final static short THEN=260;
public final static short ELSE=261;
public final static short ENDIF=262;
public final static short PRINT=263;
public final static short FUNC=264;
public final static short RETURN=265;
public final static short BEGIN=266;
public final static short END=267;
public final static short BREAK=268;
public final static short LONG=269;
public final static short SINGLE=270;
public final static short WHILE=271;
public final static short DO=272;
public final static short CADENA=273;
public final static short MENORIGUAL=274;
public final static short MAYORIGUAL=275;
public final static short IGUAL=276;
public final static short DISTINTO=277;
public final static short CONTRACT=278;
public final static short TRY=279;
public final static short CATCH=280;
public final static short OPASIGNACION=281;
public final static short AND=282;
public final static short OR=283;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    1,    1,    1,    1,    0,    0,    0,    0,    0,    0,
    4,    4,    6,    6,    6,    6,    6,    6,    7,    7,
    9,    9,    3,    3,    2,    2,    5,    5,   13,   12,
   12,   12,   12,   12,   12,   15,   15,   15,   17,   17,
   18,   19,   18,   16,   16,   11,   11,   11,   11,   11,
   26,   21,   21,   21,   21,   22,   22,   22,   22,   22,
   22,   23,   23,   23,   23,   23,   23,   23,   23,   23,
   28,   29,   30,   24,   24,   24,   31,   31,   31,   31,
   31,   32,   32,   25,   25,   25,   25,   10,   10,    8,
    8,    8,   33,   33,   33,   33,   33,   34,   34,   27,
   35,   35,   36,   36,   38,   38,   20,   20,   20,   41,
   41,   41,   42,   42,   42,   42,   42,   40,   40,   40,
   40,   40,   40,   39,   37,   14,   14,
};
final static short yylen[] = {                            2,
    3,    2,    3,    2,    4,    3,    4,    4,    3,    3,
    1,    2,    4,    3,    1,    4,    4,    4,    1,    2,
    1,    2,    1,    2,    1,    2,    1,    1,    1,    3,
    3,    2,    1,    3,    4,    1,    3,    3,    4,    4,
    4,    0,    6,   10,    9,    1,    1,    1,    1,    1,
    1,    4,    4,    3,    3,    5,    5,    5,    4,    3,
    5,    8,   10,    3,    5,    6,    8,    8,   10,   10,
    1,    1,    2,    8,    7,    5,    1,    1,    1,    1,
    2,    3,    4,    5,    3,    5,    5,    1,    1,    1,
    1,    1,    6,    4,    5,    6,    6,    2,    2,    1,
    1,    3,    1,    3,    1,    3,    3,    3,    1,    3,
    3,    1,    1,    1,    2,    4,    4,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    4,   29,    2,  126,  127,    0,   25,
    0,    0,   33,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   23,   46,   47,   48,   49,   50,    0,
    0,    3,    1,   26,   36,    0,   32,    0,    0,    0,
    0,    0,    0,  114,   51,    0,    0,    0,    0,  112,
    0,    0,    0,    0,    0,    0,    0,    0,   10,    6,
   73,   77,   78,   79,   80,    0,    0,    9,    0,    0,
    0,   24,    0,    0,    0,    0,    0,   34,    0,    0,
    0,   31,   30,    0,    0,    0,    0,   21,   88,   89,
    0,  115,   54,    0,    0,   55,    0,    0,    0,    0,
   64,  125,    0,  124,    0,  120,  121,  122,  123,  118,
  119,    0,    0,    0,    0,   81,    0,   82,   60,    8,
    5,    7,    0,    0,   85,   35,   38,   37,   40,   39,
    0,    0,    0,    0,   22,    0,    0,    0,    0,   53,
   52,  110,  111,    0,    0,    0,    0,    0,   59,    0,
    0,    0,   83,    0,    0,    0,   41,    0,    0,    0,
    0,    0,  116,  117,   65,    0,    0,    0,    0,   71,
   28,   27,    0,   58,   57,   56,   61,    0,    0,   76,
   90,   91,   92,   87,   84,   86,    0,    0,    0,    0,
   94,    0,    0,    0,    0,   11,    0,   12,   66,    0,
    0,   19,   99,   98,   43,    0,   95,    0,    0,    0,
    0,    0,    0,   14,    0,    0,    0,   75,    0,   20,
    0,   96,   97,   93,    0,   67,   72,    0,   68,   62,
   18,   13,   17,   16,   74,   45,    0,    0,    0,   44,
   69,   70,   63,
};
final static short yydgoto[] = {                          2,
    3,    9,   23,  168,  169,  170,  201,  180,   87,   88,
  171,  172,   11,   12,   36,   13,   14,   42,  158,   52,
   25,   26,   27,   28,   29,   48,   53,  173,  228,   30,
   67,   31,   90,  183,   54,   55,  103,   56,  105,  112,
   49,   50,
};
final static short yysindex[] = {                      -227,
 -164,    0,  232,    0,    0,    0,    0,    0, -153,    0,
 -218, -142,    0, -228,  -20,   24,  -25,  -50,   17,  316,
 -173, -179,  233,    0,    0,    0,    0,    0,    0,  116,
  292,    0,    0,    0,    0,   83,    0,   61, -149,   95,
 -147,  256,   64,    0,    0, -146,    4,  210,   12,    0,
  116,   45, -123, -145, -148,  306, -116,  -34,    0,    0,
    0,    0,    0,    0,    0, -104, -229,    0, -103,  -48,
 -101,    0,  115,  316,  305,  104,  -96,    0,  -95,  -91,
  126,    0,    0,  127,  129,  109,  265,    0,    0,    0,
 -139,    0,    0,  116,  116,    0,   15,  116,  116,  -38,
    0,    0,  116,    0,  116,    0,    0,    0,    0,    0,
    0,  116,  111,  -28,  -85,    0,  -84,    0,    0,    0,
    0,    0,  -99,  266,    0,    0,    0,    0,    0,    0,
  -92,  116,   31,  136,    0,  137,  138,   12,   12,    0,
    0,    0,    0,  -79,   84, -148,  306,   45,    0,  121,
  -43,  122,    0,  101,  -42,  -74,    0, -185,  102,   50,
  -73,  116,    0,    0,    0,  169,  186,  214,    0,    0,
    0,    0,  -72,    0,    0,    0,    0,  118,  -39,    0,
    0,    0,    0,    0,    0,    0, -141,  130,  -71,   -9,
    0,  103, -130,  131,  135,    0,  132,    0,    0,  134,
  152,    0,    0,    0,    0,  -70,    0,  -68,  -35,  139,
  140,  169,  -26,    0,  -24,  -61,  -60,    0,  141,    0,
  142,    0,    0,    0,  -65,    0,    0, -190,    0,    0,
    0,    0,    0,    0,    0,    0,  144,  145,    8,    0,
    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  105,    0,    0,
    0,    0,  -41,    0,    0,    0,    0,    0,  -31,    0,
    0,   27,    0,    2,  -36,   34,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -134,    0,    0,    0,    0,    0,    0,   -7,    3,    0,
    0,    0,    0,    0,    0,  -33,   39,   37,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   67,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   47,   -4,   40,  -86,   -2,    0, -156,    0,  124,
  517,  399,    0,  198,  204,    0,    0,    0,    0,   25,
  211,  220,  236,  237,  245,    0,   26,   52,    0,    0,
    0,    0,    0,    0,    0,  163,    0,  179,    0,    0,
   56,   22,
};
final static int YYTABLESIZE=718;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        113,
  113,  113,  145,  113,  101,  113,  115,  102,   60,  109,
  121,  109,  151,  109,   58,  176,  185,  113,  113,  204,
  113,  202,  101,  224,   46,  102,   75,  109,  109,    1,
  109,  209,  230,  107,  232,  107,  117,  107,   35,   47,
    7,    8,  100,  108,  220,  108,   94,  108,   95,  208,
  118,  107,  107,   98,  107,   73,   61,   94,   99,   95,
  100,  108,  108,   51,  108,  238,  243,  105,   46,  124,
  160,  239,   97,  141,  103,   46,  100,  106,    5,  104,
  196,  198,   68,    7,    8,  105,  105,   94,  105,   95,
  189,    4,  103,   69,   46,  106,  106,  104,  106,    5,
   80,    6,   32,   91,    7,    8,   11,   81,  198,   84,
    5,   92,   33,   37,   38,    7,    8,  136,  137,  142,
  143,   39,    5,   22,  205,  211,   79,    7,    8,   42,
  212,  213,  101,  104,   42,   42,  148,  102,   79,  113,
   22,   78,  188,  210,   94,   94,   95,   95,   36,  138,
  139,  116,  119,   83,  122,  123,  159,   22,  161,  127,
   46,  128,  126,   36,  129,  130,  133,  131,  132,  149,
  152,  153,  154,  157,   22,  162,  165,  163,  164,  174,
  177,  186,  191,  199,  207,  190,  192,  222,  206,  214,
  217,   22,  218,  216,  233,  234,  221,  225,  226,  235,
  236,  237,  240,  241,  187,   59,  195,  120,   22,  227,
  135,   41,  175,  184,  113,   40,  203,  193,  113,  101,
  223,  144,  102,  101,  109,   22,  102,  150,  109,  229,
   62,  231,  113,  113,  113,  113,   43,   44,  114,   63,
  113,  113,  109,  109,  109,  109,  101,   57,  107,  102,
  109,  109,  107,   22,   46,   64,   65,  100,  108,   93,
   45,  100,  108,  242,   66,  146,  107,  107,  107,  107,
  140,   22,   22,    0,  107,  107,  108,  108,  108,  108,
   43,   44,  105,  147,  108,  108,  105,   43,   44,  103,
   21,   71,  106,  103,  104,   22,  106,    0,  104,    0,
  105,  105,  105,  105,   22,   22,   43,   44,  105,  105,
  106,  106,  106,  106,    0,  103,  103,    0,  106,  106,
  104,  104,   15,   11,  156,   11,    0,   15,   15,   11,
   11,   22,    0,   11,    0,   11,   11,   11,   76,   77,
   15,    0,   16,  166,   22,   11,   17,    5,    0,  167,
   82,   77,    7,    8,   19,   22,    0,   15,    0,   16,
   36,   36,   20,   17,    5,  110,  178,  111,  179,    7,
    8,   19,   43,   44,   15,    0,   16,    0,    0,   20,
   17,    5,    0,    0,  200,  179,    7,    8,   19,    0,
    0,   15,    0,   16,    0,    0,   20,   17,    5,   10,
    0,  215,    0,    7,    8,   19,    0,   34,   15,    0,
   16,    0,    0,   20,   17,    5,    0,    0,  219,  179,
    7,    8,   19,    0,    0,   15,    0,   16,    0,    0,
   20,   17,    5,    0,  167,    0,    0,    7,    8,   19,
    0,    0,   15,    0,   16,    0,    0,   20,   17,    5,
    0,    0,  194,    0,    7,    8,   19,    0,    0,    0,
    0,    0,    0,    0,   20,   96,   43,   44,    0,    0,
   15,    0,   16,    0,    0,    0,   17,    5,    0,    0,
  197,    0,    7,    8,   19,    0,    0,    0,   15,   15,
   16,   16,   20,    0,   17,   17,    0,    0,   18,   70,
    0,    0,   19,   19,    0,    0,    0,    0,    0,    0,
   20,   20,   15,    0,   16,    0,    0,    0,   17,   24,
   85,   15,   15,   16,   16,    0,   19,   17,   17,  134,
    0,    0,  155,   86,   20,   19,   19,    0,    0,   72,
    0,    0,   86,   20,   20,    0,    0,   24,   15,    0,
   16,    0,  182,    0,   17,    0,   10,   74,   89,    0,
  125,   15,   19,   16,    0,    0,    0,   17,    0,    0,
   20,    0,   15,    0,   16,   19,  182,    0,   17,  106,
  107,  108,  109,   20,    0,   34,   19,    0,    0,    0,
   24,   72,    0,    0,   20,    0,    0,    0,    0,  182,
    0,    0,    0,   89,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   72,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  181,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  181,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  181,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   41,   45,   41,   47,   41,   41,   59,   41,
   59,   43,   41,   45,   40,   59,   59,   59,   60,   59,
   62,  178,   59,   59,   45,   59,   31,   59,   60,  257,
   62,   41,   59,   41,   59,   43,  266,   45,  257,   15,
  269,  270,   41,   41,  201,   43,   43,   45,   45,   59,
  280,   59,   60,   42,   62,   30,   40,   43,   47,   45,
   59,   59,   60,   40,   62,  256,   59,   41,   45,   74,
   40,  262,   48,   59,   41,   45,   51,   41,  264,   41,
  167,  168,  256,  269,  270,   59,   60,   43,   62,   45,
   41,  256,   59,  273,   45,   59,   60,   59,   62,  264,
   40,  266,  256,   40,  269,  270,   40,  257,  195,  257,
  264,  258,  266,  256,  257,  269,  270,  257,  258,   98,
   99,  264,  264,   40,  266,  256,   44,  269,  270,  264,
  261,  262,  256,  282,  269,  270,  112,  283,   44,  256,
   40,   59,   41,   41,   43,   43,   45,   45,   44,   94,
   95,  256,  256,   59,  256,   41,  132,   40,  133,  256,
   45,  257,   59,   59,  256,   40,   58,   41,   40,   59,
  256,  256,  272,  266,   40,   40,  256,   41,   41,   59,
   59,  256,  256,  256,  256,  160,  162,  256,   59,   59,
   59,   40,   59,   59,  256,  256,  267,   59,   59,   59,
   59,  267,   59,   59,  158,  256,  167,  256,   40,  212,
   87,   14,  256,  256,  256,   12,  256,  166,  260,  256,
  256,  260,  256,  260,  256,   40,  260,  256,  260,  256,
   20,  256,  274,  275,  276,  277,  257,  258,  273,   20,
  282,  283,  274,  275,  276,  277,  283,  273,  256,  283,
  282,  283,  260,   40,   45,   20,   20,  256,  256,  256,
  281,  260,  260,  256,   20,  103,  274,  275,  276,  277,
  256,   40,   40,   -1,  282,  283,  274,  275,  276,  277,
  257,  258,  256,  105,  282,  283,  260,  257,  258,  256,
   59,   59,  256,  260,  256,   40,  260,   -1,  260,   -1,
  274,  275,  276,  277,   40,   40,  257,  258,  282,  283,
  274,  275,  276,  277,   -1,  282,  283,   -1,  282,  283,
  282,  283,  256,  257,   59,  259,   -1,  261,  262,  263,
  264,   40,   -1,  267,   -1,  269,  270,  271,  256,  257,
  257,   -1,  259,  260,   40,  279,  263,  264,   -1,  266,
  256,  257,  269,  270,  271,   40,   -1,  257,   -1,  259,
  256,  257,  279,  263,  264,   60,  266,   62,  268,  269,
  270,  271,  257,  258,  257,   -1,  259,   -1,   -1,  279,
  263,  264,   -1,   -1,  267,  268,  269,  270,  271,   -1,
   -1,  257,   -1,  259,   -1,   -1,  279,  263,  264,    1,
   -1,  267,   -1,  269,  270,  271,   -1,    9,  257,   -1,
  259,   -1,   -1,  279,  263,  264,   -1,   -1,  267,  268,
  269,  270,  271,   -1,   -1,  257,   -1,  259,   -1,   -1,
  279,  263,  264,   -1,  266,   -1,   -1,  269,  270,  271,
   -1,   -1,  257,   -1,  259,   -1,   -1,  279,  263,  264,
   -1,   -1,  267,   -1,  269,  270,  271,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  279,  256,  257,  258,   -1,   -1,
  257,   -1,  259,   -1,   -1,   -1,  263,  264,   -1,   -1,
  267,   -1,  269,  270,  271,   -1,   -1,   -1,  257,  257,
  259,  259,  279,   -1,  263,  263,   -1,   -1,  267,  267,
   -1,   -1,  271,  271,   -1,   -1,   -1,   -1,   -1,   -1,
  279,  279,  257,   -1,  259,   -1,   -1,   -1,  263,    3,
  265,  257,  257,  259,  259,   -1,  271,  263,  263,  265,
   -1,   -1,  267,  278,  279,  271,  271,   -1,   -1,   23,
   -1,   -1,  278,  279,  279,   -1,   -1,   31,  257,   -1,
  259,   -1,  154,   -1,  263,   -1,  158,  266,   42,   -1,
  256,  257,  271,  259,   -1,   -1,   -1,  263,   -1,   -1,
  279,   -1,  257,   -1,  259,  271,  178,   -1,  263,  274,
  275,  276,  277,  279,   -1,  187,  271,   -1,   -1,   -1,
   74,   75,   -1,   -1,  279,   -1,   -1,   -1,   -1,  201,
   -1,   -1,   -1,   87,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  124,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  154,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  178,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  201,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=283;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'",null,"'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,"ID","CTE","IF","THEN","ELSE","ENDIF","PRINT",
"FUNC","RETURN","BEGIN","END","BREAK","LONG","SINGLE","WHILE","DO","CADENA",
"MENORIGUAL","MAYORIGUAL","IGUAL","DISTINTO","CONTRACT","TRY","CATCH",
"OPASIGNACION","AND","OR",
};
final static String yyrule[] = {
"$accept : programa",
"encabezado_programa : ID bloque_sentencias_declarativas BEGIN",
"encabezado_programa : ID BEGIN",
"encabezado_programa : ID bloque_sentencias_declarativas error",
"encabezado_programa : ID error",
"programa : encabezado_programa bloque_sentencias_ejecutables END ';'",
"programa : encabezado_programa END ';'",
"programa : encabezado_programa bloque_sentencias_ejecutables ';' error",
"programa : encabezado_programa bloque_sentencias_ejecutables END error",
"programa : encabezado_programa ';' error",
"programa : encabezado_programa END error",
"bloque : sentencias",
"bloque : bloque sentencias",
"bloque_de_sentencias : BEGIN bloque END ';'",
"bloque_de_sentencias : BEGIN END ';'",
"bloque_de_sentencias : sentencias",
"bloque_de_sentencias : bloque END ';' error",
"bloque_de_sentencias : BEGIN bloque ';' error",
"bloque_de_sentencias : BEGIN bloque END error",
"bloque_sentencias_while : sentencias_while",
"bloque_sentencias_while : bloque_sentencias_while sentencias_while",
"bloque_sentencias_ejecutables_funcion : sentencias_ejecutables_func",
"bloque_sentencias_ejecutables_funcion : bloque_sentencias_ejecutables_funcion sentencias_ejecutables_func",
"bloque_sentencias_ejecutables : sentencias_ejecutables",
"bloque_sentencias_ejecutables : bloque_sentencias_ejecutables sentencias_ejecutables",
"bloque_sentencias_declarativas : sentencias_declarativas",
"bloque_sentencias_declarativas : bloque_sentencias_declarativas sentencias_declarativas",
"sentencias : sentencias_declarativas",
"sentencias : sentencias_ejecutables",
"tipo_func : FUNC",
"sentencias_declarativas : tipo lista_de_variables ';'",
"sentencias_declarativas : tipo lista_de_variables error",
"sentencias_declarativas : tipo error",
"sentencias_declarativas : declaracion_func",
"sentencias_declarativas : tipo_func lista_de_variables ';'",
"sentencias_declarativas : tipo_func lista_de_variables error ';'",
"lista_de_variables : ID",
"lista_de_variables : lista_de_variables ',' ID",
"lista_de_variables : lista_de_variables ID error",
"encabezado_func : tipo FUNC ID '('",
"encabezado_func : tipo ID '(' error",
"parametro : tipo ID ')' BEGIN",
"$$1 :",
"parametro : tipo ID ')' $$1 bloque_sentencias_declarativas BEGIN",
"declaracion_func : encabezado_func parametro bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END ';'",
"declaracion_func : encabezado_func parametro RETURN '(' expresion ')' ';' END ';'",
"sentencias_ejecutables : asignacion",
"sentencias_ejecutables : salida",
"sentencias_ejecutables : sentencia_if",
"sentencias_ejecutables : sentencia_while",
"sentencias_ejecutables : sentencia_try_catch",
"op_asignacion : OPASIGNACION",
"asignacion : ID op_asignacion expresion ';'",
"asignacion : ID op_asignacion expresion error",
"asignacion : ID expresion error",
"asignacion : ID op_asignacion error",
"salida : PRINT '(' CADENA ')' ';'",
"salida : PRINT '(' CADENA ')' error",
"salida : PRINT '(' CADENA error ';'",
"salida : PRINT CADENA error ';'",
"salida : '(' CADENA error",
"salida : PRINT '(' ')' error ';'",
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if ENDIF ';'",
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if ELSE cuerpo_else ENDIF ';'",
"sentencia_if : IF condicion error",
"sentencia_if : IF '(' condicion THEN error",
"sentencia_if : IF '(' condicion ')' cuerpo_if error",
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if error ';'",
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if ENDIF error",
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if ELSE cuerpo_else error ';'",
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if ELSE cuerpo_else ENDIF error",
"cuerpo_if : bloque_de_sentencias",
"cuerpo_else : bloque_de_sentencias",
"encabezado_while : WHILE '('",
"sentencia_while : encabezado_while condicion ')' DO BEGIN bloque_sentencias_while END ';'",
"sentencia_while : encabezado_while condicion ')' DO BEGIN END ';'",
"sentencia_while : encabezado_while condicion ')' DO sentencias_while",
"sentencias_ejecutables_sin_try_catch : asignacion",
"sentencias_ejecutables_sin_try_catch : salida",
"sentencias_ejecutables_sin_try_catch : sentencia_if",
"sentencias_ejecutables_sin_try_catch : sentencia_while",
"sentencias_ejecutables_sin_try_catch : sentencia_try_catch error",
"encabezado_try_catch : TRY sentencias_ejecutables_sin_try_catch CATCH",
"encabezado_try_catch : TRY sentencias_ejecutables_sin_try_catch BEGIN error",
"sentencia_try_catch : encabezado_try_catch BEGIN bloque_sentencias_ejecutables END ';'",
"sentencia_try_catch : encabezado_try_catch bloque_sentencias_ejecutables error",
"sentencia_try_catch : encabezado_try_catch BEGIN bloque_sentencias_ejecutables ';' error",
"sentencia_try_catch : encabezado_try_catch BEGIN bloque_sentencias_ejecutables END error",
"sentencias_ejecutables_func : sentencias_ejecutables",
"sentencias_ejecutables_func : contrato",
"sentencias_while : sentencias_ejecutables",
"sentencias_while : sentencias_declarativas",
"sentencias_while : sentencia_break",
"contrato : CONTRACT ':' '(' condicion ')' ';'",
"contrato : CONTRACT ':' condicion error",
"contrato : CONTRACT ':' '(' ')' error",
"contrato : CONTRACT ':' '(' condicion ';' error",
"contrato : CONTRACT ':' '(' condicion ')' error",
"sentencia_break : BREAK ';'",
"sentencia_break : BREAK error",
"condicion : expresion_or",
"expresion_or : expresion_and",
"expresion_or : expresion_or comparador_or expresion_and",
"expresion_and : expresion_relacional",
"expresion_and : expresion_and comparador_and expresion_relacional",
"expresion_relacional : expresion",
"expresion_relacional : expresion_relacional comparador expresion",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : CTE",
"factor : '-' CTE",
"factor : ID '(' ID ')'",
"factor : ID '(' CTE ')'",
"comparador : '<'",
"comparador : '>'",
"comparador : MENORIGUAL",
"comparador : MAYORIGUAL",
"comparador : IGUAL",
"comparador : DISTINTO",
"comparador_and : AND",
"comparador_or : OR",
"tipo : LONG",
"tipo : SINGLE",
};

//#line 531 "gramatica.y"

private AnalizadorLexico lexico;
private AnalizadorSintactico sintactico;

public void setLexico(AnalizadorLexico lexico) { this.lexico = lexico; }

public void setSintactico(AnalizadorSintactico sintactico) { this.sintactico = sintactico; }

public AnalizadorLexico getLexico() { return this.lexico; }

public AnalizadorSintactico getSintactico() { return this.sintactico; }

public int yylex() {
    int token = lexico.procesarYylex();
    if (lexico.getRefTablaSimbolos() != -1)
        yylval = new ParserVal(lexico.getRefTablaSimbolos());
    return token;
}

public void yyerror(String string) {
	//sintactico.addErrorSintactico("par: " + string);
}
//#line 584 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 18 "gramatica.y"
{ sintactico.agregarAPolaca("START"); }
break;
case 2:
//#line 19 "gramatica.y"
{ sintactico.agregarAPolaca("START"); }
break;
case 3:
//#line 20 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta BEGIN al abrir el bloque de sentencias ejecutables."); }
break;
case 4:
//#line 21 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta BEGIN al abrir el bloque de sentencias ejecutables."); }
break;
case 5:
//#line 24 "gramatica.y"
{
                                                                                                    sintactico.agregarAnalisis("Se reconoció un programa. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                    sintactico.setUsoTablaSimb(val_peek(3).ival, "NOMBRE DE PROGRAMA");
                                                                                                    sintactico.agregarAPolaca("END START");
                                                                                                }
break;
case 6:
//#line 29 "gramatica.y"
{
                                                                                                    sintactico.agregarAnalisis("Se reconoció un programa. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                    sintactico.setUsoTablaSimb(val_peek(2).ival, "NOMBRE DE PROGRAMA");
                                                                                                    sintactico.agregarAPolaca("END START");
                                                                                                }
break;
case 7:
//#line 34 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END al cerrar el bloque de sentencias ejecutables."); }
break;
case 8:
//#line 35 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del END en el bloque de sentencias ejecutables."); }
break;
case 9:
//#line 36 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END al cerrar el bloque de sentencias ejecutables."); }
break;
case 10:
//#line 37 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del END en el bloque de sentencias ejecutables."); }
break;
case 16:
//#line 47 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta BEGIN al abrir el bloque."); }
break;
case 17:
//#line 48 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): bloque mal cerrado (falta 'END')."); }
break;
case 18:
//#line 49 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del bloque."); }
break;
case 29:
//#line 72 "gramatica.y"
{
                        sintactico.setTipo("FUNC");
                        yyval.sval = new String("FUNC");
                    }
break;
case 30:
//#line 78 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de variable. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 31:
//#line 79 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' al final de la declaración de variable."); }
break;
case 32:
//#line 80 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' al final de la declaración de variable."); }
break;
case 34:
//#line 82 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de variable de tipo función. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 35:
//#line 83 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del bloque."); }
break;
case 36:
//#line 86 "gramatica.y"
{
                                                          sintactico.setAmbitoTablaSimb(val_peek(0).ival);
                                                          if(sintactico.getTipo().equals("FUNC")) {
                                                                if(!sintactico.variableFueDeclarada(val_peek(0).ival))
                                                                    sintactico.setUsoTablaSimb(val_peek(0).ival, "FUNC");
                                                          }
                                                          else {
                                                                sintactico.setUsoTablaSimb(val_peek(0).ival, "VARIABLE");
                                                                if (!sintactico.variableFueDeclarada(val_peek(0).ival))
                                                                    sintactico.setTipoVariableTablaSimb(val_peek(0).ival);
                                                          }
                                                    }
break;
case 37:
//#line 98 "gramatica.y"
{
                                                        sintactico.setAmbitoTablaSimb(val_peek(0).ival);
                                                        if(sintactico.getTipo().equals("FUNC")) {
                                                            if(!sintactico.variableFueDeclarada(val_peek(0).ival))
                                                                sintactico.setUsoTablaSimb(val_peek(0).ival, "FUNC");
                                                        }
                                                        else {
                                                            sintactico.setUsoTablaSimb(val_peek(0).ival, "VARIABLE");
                                                            if (!sintactico.variableFueDeclarada(val_peek(0).ival))
                                                            sintactico.setTipoVariableTablaSimb(val_peek(0).ival);
                                                        }
                                                    }
break;
case 38:
//#line 110 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta una ',' entre identificadores."); }
break;
case 39:
//#line 113 "gramatica.y"
{
                                        sintactico.setUsoTablaSimb(val_peek(1).ival, "FUNC");
                                        sintactico.setAmbitoTablaSimb(val_peek(1).ival);
                                        if (sintactico.variableFueDeclarada(val_peek(1).ival))
                                            sintactico.setErrorFunc(true);
                                        else {
                                            sintactico.setTipoVariableTablaSimb(val_peek(1).ival);
                                            sintactico.agregarReferencia(val_peek(1).ival);
                                            sintactico.setFuncReferenciadaTablaSimb(val_peek(1).ival, sintactico.getAmbitoFromTS(val_peek(1).ival));
                                            sintactico.setAmbito(sintactico.getAmbito() + "@" + sintactico.getLexemaFromTS(val_peek(1).ival));
                                        }
                                    }
break;
case 40:
//#line 125 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta FUNC en la definición de la función."); }
break;
case 41:
//#line 128 "gramatica.y"
{
                                        if(!sintactico.huboErrorFunc()) {
                                            sintactico.setAmbitoTablaSimb(val_peek(2).ival);
                                            sintactico.setTipoVariableTablaSimb(val_peek(2).ival);
                                            sintactico.setUsoTablaSimb(val_peek(2).ival, "PARAMETRO");
                                            sintactico.agregarAPolaca("INIC_" + sintactico.getAmbitoFromTS(sintactico.obtenerReferencia()));
                                        }
                                        else
                                            sintactico.eliminarRegistroTS(val_peek(2).ival);
                                    }
break;
case 42:
//#line 138 "gramatica.y"
{
                            if(!sintactico.huboErrorFunc()) {
                                sintactico.setAmbitoTablaSimb(val_peek(1).ival);
                                sintactico.setTipoVariableTablaSimb(val_peek(1).ival);
                                sintactico.setUsoTablaSimb(val_peek(1).ival, "PARAMETRO");
                                sintactico.agregarAPolaca("INIC_" + sintactico.getAmbitoFromTS(sintactico.obtenerReferencia()));
                            }
                            else
                                sintactico.eliminarRegistroTS(val_peek(1).ival);
                       }
break;
case 44:
//#line 150 "gramatica.y"
{
                                                                                                                            if (!sintactico.huboErrorFunc()) {
                                                                                                                                sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                                                RegistroSimbolo tokenReturn = new RegistroSimbolo(sintactico.getLexemaFromTS(sintactico.obtenerReferencia()) + "_ret", "ID");
                                                                                                                                tokenReturn.setTipoToken("ID");
                                                                                                                                tokenReturn.setTipoVariable(sintactico.getTipoVariableFromTS(sintactico.obtenerReferencia()));
                                                                                                                                tokenReturn.setUso("VARIABLE RETORNO");
                                                                                                                                tokenReturn.setAmbito(tokenReturn.getLexema() + "@" + sintactico.getAmbito());
                                                                                                                                sintactico.agregarRegistroATS(tokenReturn);
                                                                                                                                sintactico.agregarAPolaca(tokenReturn.getAmbito());
                                                                                                                                sintactico.agregarAPolaca(":=");
                                                                                                                                sintactico.agregarAPolaca("RET");
                                                                                                                                sintactico.agregarAPolaca("FIN_" + sintactico.getAmbitoFromTS(sintactico.eliminarReferencia()));
                                                                                                                                sintactico.setAmbito(sintactico.getAmbito().substring(0, sintactico.getAmbito().lastIndexOf("@")));
                                                                                                                            }
                                                                                                                            else {
                                                                                                                                sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se produjo un error al declarar la función.");
                                                                                                                                sintactico.setErrorFunc(false);
                                                                                                                            }
                                                                                                                        }
break;
case 45:
//#line 170 "gramatica.y"
{
                                                                                                                            if (!sintactico.huboErrorFunc()) {
                                                                                                                                sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                                                RegistroSimbolo tokenReturn = new RegistroSimbolo(sintactico.getLexemaFromTS(sintactico.obtenerReferencia()) + "_ret", "ID");
                                                                                                                                tokenReturn.setTipoToken("ID");
                                                                                                                                tokenReturn.setTipoVariable(sintactico.getTipoVariableFromTS(sintactico.obtenerReferencia()));
                                                                                                                                tokenReturn.setUso("VARIABLE RETORNO");
                                                                                                                                tokenReturn.setAmbito(tokenReturn.getLexema() + "@" + sintactico.getAmbito());
                                                                                                                                sintactico.agregarRegistroATS(tokenReturn);
                                                                                                                                sintactico.agregarAPolaca(tokenReturn.getAmbito());
                                                                                                                                sintactico.agregarAPolaca(":=");
                                                                                                                                sintactico.agregarAPolaca("RET");
                                                                                                                                sintactico.agregarAPolaca("FIN_" + sintactico.getAmbitoFromTS(sintactico.eliminarReferencia()));
                                                                                                                                sintactico.setAmbito(sintactico.getAmbito().substring(0, sintactico.getAmbito().lastIndexOf("@")));
                                                                                                                            }
                                                                                                                            else {
                                                                                                                                sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se produjo un error al declarar la función.");
                                                                                                                                sintactico.setErrorFunc(false);
                                                                                                                            }
                                                                                                                        }
break;
case 51:
//#line 199 "gramatica.y"
{ yyval.sval = new String(":="); }
break;
case 52:
//#line 202 "gramatica.y"
{
                                                     int referencia = sintactico.referenciaCorrecta(val_peek(3).ival);
                                                     if (referencia == -1)
                                                         sintactico.addErrorSintactico("ERROR SEMÁNTICO (Línea " + (AnalizadorLexico.LINEA) + "): error en la asignación, la variable a asignar se encuentra fuera de alcance.");
                                                     else {
                                                         if (!sintactico.huboErrorInvocacion()) {
                                                             if(sintactico.getUsoFromTS(referencia).equals("FUNC") && sintactico.getTipoVariableFromTS(referencia).equals(""))
                                                                 if(sintactico.getUsoFromTS(sintactico.getRefInvocacion()).equals("FUNC")) {
                                                                     sintactico.agregarAnalisis("Se reconoció una operación de asignación. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                     if(!sintactico.getFuncReferenciadaFromTS(sintactico.getRefInvocacion()).equals("")) {
                                                                         sintactico.setTipoVariableTablaSimb(referencia, sintactico.getTipoVariableFromTS(sintactico.getRefInvocacion()));
                                                                         sintactico.setFuncReferenciadaTablaSimb(referencia, sintactico.getFuncReferenciadaFromTS(sintactico.getRefInvocacion()));
                                                                         sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(referencia));
                                                                         sintactico.agregarAPolaca(val_peek(2).sval);
                                                                     }
                                                                     else {
                                                                         sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(referencia));
                                                                         sintactico.agregarAPolaca(val_peek(2).sval);
                                                                     }

                                                                 }
                                                                 else
                                                                     sintactico.addErrorSintactico("ERROR SEMÁNTICO (Línea " + (AnalizadorLexico.LINEA) + "): error en la asignación, no se puede asignar a un tipo FUNC una variable de distinto tipo.");
                                                             else {
                                                                 sintactico.agregarAnalisis("Se reconoció una operación de asignación. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                 sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(referencia));
                                                                 sintactico.agregarAPolaca(val_peek(2).sval);
                                                             }
                                                         }
                                                         else {
                                                             sintactico.addErrorSintactico("ERROR SEMÁNTICO (Línea " + (AnalizadorLexico.LINEA) + "): error en la asignación, se produjo error en la invocación a función.");
                                                             sintactico.setErrorInvocacion(false);
                                                         }
                                                     }
                                                 }
break;
case 53:
//#line 237 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' luego de la asignación."); }
break;
case 54:
//#line 238 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el operador de asignación."); }
break;
case 55:
//#line 239 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' luego de la asignación."); }
break;
case 56:
//#line 242 "gramatica.y"
{
                                            sintactico.agregarAnalisis("Se reconoció una impresión por pantalla de una cadena. (Línea " + AnalizadorLexico.LINEA + ")");
                                            sintactico.setUsoTablaSimb(val_peek(2).ival, "CADENA DE CARACTERES");
                                            sintactico.setAmbitoTablaSimb(val_peek(2).ival, sintactico.getLexemaFromTS(val_peek(2).ival));
                                            sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(2).ival));
                                            sintactico.agregarAPolaca("PRINT");
                                      }
break;
case 57:
//#line 249 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la impresión de cadena."); }
break;
case 58:
//#line 250 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): cierre erróneo de la lista de parámetros de PRINT."); }
break;
case 59:
//#line 251 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los parámetros de PRINT deben estar entre paréntesis."); }
break;
case 60:
//#line 252 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba PRINT, se encontró '('."); }
break;
case 61:
//#line 253 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta declarar una cadena para PRINT."); }
break;
case 62:
//#line 256 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una sentencia IF. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 63:
//#line 257 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una sentencia IF. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 64:
//#line 258 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición de IF debe estar entre paréntesis."); }
break;
case 65:
//#line 259 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros."); }
break;
case 66:
//#line 260 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta la declaración de THEN."); }
break;
case 67:
//#line 261 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta cierre de ENDIF."); }
break;
case 68:
//#line 262 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de ENDIF."); }
break;
case 69:
//#line 263 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta cierre de ENDIF."); }
break;
case 70:
//#line 264 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de ENDIF."); }
break;
case 71:
//#line 267 "gramatica.y"
{
                                        sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca() + 2));   /* Desapila dirección incompleta y completa el destino de BF*/
                                        sintactico.agregarAPolaca(" ");                               /* Crea paso incompleto*/
                                        sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);  /* Apila el nro de paso incompleto*/
                                        sintactico.agregarAPolaca("BI");                              /* Se crea el paso BI*/
                                    }
break;
case 72:
//#line 275 "gramatica.y"
{ sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca())); }
break;
case 73:
//#line 278 "gramatica.y"
{ sintactico.pushElementoPila(sintactico.getSizePolaca()); }
break;
case 74:
//#line 281 "gramatica.y"
{
                                                                                                sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                if (sintactico.tieneSentBreak()) {
                                                                                                        sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca() + 2)); /* Desapila dirección incompleta y completa el destino de BI del BREAK*/
                                                                                                        sintactico.setTieneSentBreak(false);
                                                                                                }
                                                                                                sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca() + 2)); /* Desapila dirección incompleta y completa el destino de BF*/
                                                                                                sintactico.agregarAPolaca(String.valueOf(sintactico.popElementoPila()));    /* Desapilar paso de inicio*/
                                                                                                sintactico.agregarAPolaca("BI");
                                                                                            }
break;
case 75:
//#line 291 "gramatica.y"
{
                                                                                                sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca() + 2)); /* Desapila dirección incompleta y completa el destino de BF*/
                                                                                                sintactico.agregarAPolaca(String.valueOf(sintactico.popElementoPila()));    /* Desapilar paso de inicio*/
                                                                                                sintactico.agregarAPolaca("BI");
                                                                                            }
break;
case 76:
//#line 297 "gramatica.y"
{
                                                                                                sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca() + 2)); /* Desapila dirección incompleta y completa el destino de BF*/
                                                                                                sintactico.agregarAPolaca(String.valueOf(sintactico.popElementoPila()));    /* Desapilar paso de inicio*/
                                                                                                sintactico.agregarAPolaca("BI");
                                                                                            }
break;
case 81:
//#line 309 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los bloques TRY/CATCH no pueden anidarse"); }
break;
case 82:
//#line 312 "gramatica.y"
{
                                                                                    /* Desapila dirección incompleta y completa el destino de BF*/
                                                                                    sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca()));
                                                                                }
break;
case 83:
//#line 316 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se leyó un BEGIN sin previo reconocimiento de CATCH."); }
break;
case 84:
//#line 319 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció un bloque TRY/CATCH. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 85:
//#line 320 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): el cuerpo de CATCH no se inicializó con BEGIN."); }
break;
case 86:
//#line 321 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): cuerpo de CATCH mal cerrado; falta END."); }
break;
case 87:
//#line 322 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
break;
case 93:
//#line 334 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una definición de contrato. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 94:
//#line 335 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición debe estar entre paréntesis."); }
break;
case 95:
//#line 336 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): CONTRACT debe tener al menos una condición como parámetro."); }
break;
case 96:
//#line 337 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el paréntesis de cierre para los parámetros de CONTRACT."); }
break;
case 97:
//#line 338 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de los parámetros de CONTRACT."); }
break;
case 98:
//#line 341 "gramatica.y"
{
                                    sintactico.agregarAnalisis("Se reconoció una sentencia de BREAK. (Línea " + AnalizadorLexico.LINEA + ")");
                                    sintactico.agregarAPolaca(" ");                               /* Crea paso incompleto*/
                                    sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);  /* Apila el nro de paso incompleto*/
                                    sintactico.agregarAPolaca("BI");                              /* Se crea el paso BI*/
                                    sintactico.setTieneSentBreak(true);
                               }
break;
case 99:
//#line 348 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de BREAK."); }
break;
case 100:
//#line 351 "gramatica.y"
{
                                sintactico.agregarAPolaca(" ");
                                sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);
                                sintactico.agregarAPolaca("BF");
                            }
break;
case 102:
//#line 359 "gramatica.y"
{ sintactico.agregarAPolaca(val_peek(1).sval); }
break;
case 104:
//#line 363 "gramatica.y"
{ sintactico.agregarAPolaca(val_peek(1).sval); }
break;
case 106:
//#line 367 "gramatica.y"
{ sintactico.agregarAPolaca(val_peek(1).sval); }
break;
case 107:
//#line 370 "gramatica.y"
{
                                                sintactico.agregarAnalisis("Se reconoció una suma. (Línea " + AnalizadorLexico.LINEA + ")");
                                                sintactico.agregarAPolaca("+");
                                            }
break;
case 108:
//#line 374 "gramatica.y"
{
                                                sintactico.agregarAnalisis("Se reconoció una resta. (Línea " + AnalizadorLexico.LINEA + ")");
                                                sintactico.agregarAPolaca("-");
                                            }
break;
case 110:
//#line 381 "gramatica.y"
{
                                                sintactico.agregarAnalisis("Se reconoció una multiplicación. (Línea " + AnalizadorLexico.LINEA + ")");
                                                sintactico.agregarAPolaca("*");
                                            }
break;
case 111:
//#line 385 "gramatica.y"
{
                                                sintactico.agregarAnalisis("Se reconoció una división. (Línea " + AnalizadorLexico.LINEA + ")");
                                                sintactico.agregarAPolaca("/");
                                            }
break;
case 113:
//#line 392 "gramatica.y"
{
                        int ref = sintactico.referenciaCorrecta(val_peek(0).ival);
                        if (ref == -1)
                            sintactico.addErrorSintactico("ERROR SEMÁNTICO (Línea " + (AnalizadorLexico.LINEA) + "): la variable no fue declarada o se encuentra fuera de alcance.");
                        else {
                            sintactico.setRefInvocacion(ref);
                            sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(ref));
                        }
                    }
break;
case 114:
//#line 401 "gramatica.y"
{
                        sintactico.setTipo(sintactico.getTipoFromTS(val_peek(0).ival));
                        if (sintactico.getTipo().equals("LONG"))
                            sintactico.verificarRangoEnteroLargo(val_peek(0).ival);

                        sintactico.setTipoVariableTablaSimb(val_peek(0).ival);
                        sintactico.setAmbitoTablaSimb(val_peek(0).ival, sintactico.getLexemaFromTS(val_peek(0).ival));
                        sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(0).ival));
                    }
break;
case 115:
//#line 410 "gramatica.y"
{
                        sintactico.setTipo(sintactico.getTipoFromTS(val_peek(0).ival));
                        sintactico.setTipoVariableTablaSimb(val_peek(0).ival);
                        sintactico.setAmbitoTablaSimb(val_peek(0).ival, sintactico.getLexemaFromTS(val_peek(0).ival));
                        sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(0).ival));
                        sintactico.setNegativoTablaSimb(val_peek(0).ival);
                        sintactico.agregarAPolaca("-");
                    }
break;
case 116:
//#line 418 "gramatica.y"
{
                               int referencia = sintactico.referenciaCorrecta(val_peek(3).ival);
                               if (referencia == -1) {
                                   sintactico.addErrorSintactico("ERROR SEMÁNTICO(Línea " + AnalizadorLexico.LINEA + "): Error en la invocación. Variable de invocación " + sintactico.getLexemaFromTS(val_peek(3).ival) + " no declarada.");
                                   sintactico.setErrorInvocacion(true);
                               }
                               else {
                                   String funcionReferenciada = sintactico.getFuncReferenciadaFromTS(referencia);
                                   if (!funcionReferenciada.equals("")) {
                                       if (sintactico.getAmbitoFromTS(referencia).equals(funcionReferenciada))
                                           sintactico.setRefInvocacion(referencia);
                                       else {
                                           int indiceFuncRef = sintactico.getIndiceFuncRef(funcionReferenciada);
                                           sintactico.setRefInvocacion(indiceFuncRef);
                                       }
                                       int refParamReal = sintactico.referenciaCorrecta(val_peek(1).ival - 1);
                                       if (refParamReal == -1) {
                                           sintactico.addErrorSintactico("ERROR SEMÁNTICO(Línea " + AnalizadorLexico.LINEA + "): Error en la invocación. Parámetro de invocación " + sintactico.getLexemaFromTS(val_peek(3).ival) + " no declarado.");
                                           sintactico.setErrorInvocacion(true);
                                       }
                                       else {
                                       /* Comparo tipos de parámetro formal con real*/
                                           int refParamFormal = sintactico.getRefInvocacion() + 1;
                                           if (!sintactico.getTipoVariableFromTS(refParamFormal).equals(sintactico.getTipoVariableFromTS(refParamReal))) {
                                               sintactico.addErrorSintactico("ERROR SEMÁNTICO(Línea " + AnalizadorLexico.LINEA + "): Error en la invocación. El tipo del parámetro real no coincide con el tipo del parámetro formal.");
                                               sintactico.setErrorInvocacion(true);
                                           }
                                           else {
                                               sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(refParamReal));
                                               sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(refParamFormal));
                                               sintactico.agregarAPolaca(":=");
                                               sintactico.agregarAnalisis("Se reconoció una invocación a una función (Línea " + AnalizadorLexico.LINEA + ")");
                                               sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(sintactico.getRefInvocacion()));
                                               sintactico.agregarAPolaca("CALL");
                                               int referenciaReturn = sintactico.getReferenciaReturn(sintactico.getLexemaFromTS(sintactico.getRefInvocacion()));
                                               sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(referenciaReturn));
                                           }
                                       }
                                   }
                                   else {
                                       sintactico.addErrorSintactico("ERROR SEMÁNTICO(Línea " + AnalizadorLexico.LINEA + "): error en la invocación. La variable de invocación " + sintactico.getLexemaFromTS(val_peek(3).ival) + " no hace referencia a ninguna función.");
                                       sintactico.setErrorInvocacion(true);
                                   }

                               }
                           }
break;
case 117:
//#line 464 "gramatica.y"
{
                               int referencia = sintactico.referenciaCorrecta(val_peek(3).ival);
                               if (referencia == -1) {
                                   sintactico.addErrorSintactico("ERROR SEMÁNTICO(Línea " + AnalizadorLexico.LINEA + "): Error en la invocación. Variable de invocación " + sintactico.getLexemaFromTS(val_peek(3).ival) + " no declarada.");
                                   sintactico.setErrorInvocacion(true);
                               }
                               else {
                                   String funcionReferenciada = sintactico.getFuncReferenciadaFromTS(referencia);
                                   if (!funcionReferenciada.equals("")) {
                                       if (sintactico.getAmbitoFromTS(referencia).equals(funcionReferenciada))
                                           sintactico.setRefInvocacion(referencia);
                                       else {
                                           int indiceFuncRef = sintactico.getIndiceFuncRef(funcionReferenciada);
                                           sintactico.setRefInvocacion(indiceFuncRef);
                                       }
                                       /* Comparo tipos de parámetro formal con real*/
                                       sintactico.setTipoVariableTablaSimb(val_peek(1).ival - 1, sintactico.getTipoFromTS(val_peek(1).ival -1));
                                       sintactico.setAmbitoTablaSimb(val_peek(1).ival - 1, sintactico.getLexemaFromTS(val_peek(1).ival - 1));
                                       int refParamFormal = sintactico.getRefInvocacion() + 1;
                                       if (!sintactico.getTipoVariableFromTS(refParamFormal).equals(sintactico.getTipoVariableFromTS(val_peek(1).ival - 1))) {
                                           sintactico.addErrorSintactico("ERROR SEMÁNTICO(Línea " + AnalizadorLexico.LINEA + "): Error en la invocación. El tipo del parámetro real no coincide con el tipo del parámetro formal.");
                                           sintactico.setErrorInvocacion(true);
                                       }
                                       else {
                                           sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(1).ival - 1));
                                           sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(refParamFormal));
                                           sintactico.agregarAPolaca(":=");
                                           sintactico.agregarAnalisis("Se reconoció una invocación a una función (Línea " + AnalizadorLexico.LINEA + ")");
                                           sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(sintactico.getRefInvocacion()));
                                           sintactico.agregarAPolaca("CALL");
                                           int referenciaReturn = sintactico.getReferenciaReturn(sintactico.getLexemaFromTS(sintactico.getRefInvocacion()));
                                           sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(referenciaReturn));
                                       }
                                   }
                                   else {
                                       sintactico.addErrorSintactico("ERROR SEMÁNTICO(Línea " + AnalizadorLexico.LINEA + "): error en la invocación. La variable de invocación " + sintactico.getLexemaFromTS(val_peek(3).ival) + " no hace referencia a ninguna función.");
                                       sintactico.setErrorInvocacion(true);
                                   }
                               }
                           }
break;
case 118:
//#line 506 "gramatica.y"
{ yyval.sval = new String("<"); }
break;
case 119:
//#line 507 "gramatica.y"
{ yyval.sval = new String(">"); }
break;
case 120:
//#line 508 "gramatica.y"
{ yyval.sval = new String("<="); }
break;
case 121:
//#line 509 "gramatica.y"
{ yyval.sval = new String(">="); }
break;
case 122:
//#line 510 "gramatica.y"
{ yyval.sval = new String("=="); }
break;
case 123:
//#line 511 "gramatica.y"
{ yyval.sval = new String("<>"); }
break;
case 124:
//#line 514 "gramatica.y"
{ yyval.sval = new String("&&"); }
break;
case 125:
//#line 517 "gramatica.y"
{ yyval.sval = new String("||"); }
break;
case 126:
//#line 520 "gramatica.y"
{
                    sintactico.setTipo("LONG");
                    yyval.sval = new String("LONG");
                }
break;
case 127:
//#line 524 "gramatica.y"
{
                    sintactico.setTipo("SINGLE");
                    yyval.sval = new String("SINGLE");
                }
break;
//#line 1399 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
