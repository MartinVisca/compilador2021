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
    0,    1,    1,    3,    3,    3,    3,    3,    4,    4,
    6,    6,    8,    8,    2,    2,   10,   10,   10,   10,
   10,   10,   12,   12,   12,   14,   14,   14,   14,   14,
   14,   13,   13,   13,   13,   13,   13,   13,   13,   13,
   13,   13,   13,   13,   13,   13,   13,   13,   13,   15,
    9,    9,    9,    9,    9,    9,   17,   17,   17,   17,
   18,   18,   18,   18,   18,   18,   19,   19,   19,   19,
   19,   20,   20,   20,   20,   20,   20,   20,   20,   20,
   24,   25,   21,   21,   21,   21,   21,   21,   26,   26,
   26,   26,   26,   26,   22,   22,   22,   22,   22,    7,
    7,    5,    5,    5,   27,   27,   27,   27,   27,   27,
   28,   28,   23,   23,   16,   16,   16,   30,   30,   30,
   31,   31,   31,   29,   29,   29,   29,   29,   29,   29,
   29,   11,   11,
};
final static short yylen[] = {                            2,
    1,    1,    2,    4,    1,    4,    4,    4,    1,    2,
    1,    2,    1,    2,    1,    1,    3,    3,    2,    1,
    3,    3,    1,    3,    3,    6,    6,    6,    5,    6,
    6,   11,   10,    4,    3,    6,    5,    7,    6,    8,
    7,    9,    8,   10,    9,   11,   10,   11,   10,    2,
    1,    1,    1,    1,    1,    1,    4,    4,    3,    3,
    5,    5,    4,    3,    3,    4,    5,    3,    4,    5,
    5,    8,   10,    3,    5,    6,    8,    8,   10,   10,
    1,    1,    9,    3,    5,    6,    9,    9,    1,    1,
    1,    1,    1,    2,    7,    4,    5,    7,    7,    1,
    1,    1,    1,    1,    6,    2,    3,    4,    5,    5,
    2,    2,    1,    3,    3,    3,    1,    3,    3,    1,
    1,    1,    2,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,  132,  133,    0,    0,    0,    0,
    0,    2,   16,   15,    0,   20,    0,   51,   52,   53,
   54,   55,   56,  121,  122,    0,    0,    0,    0,    0,
    0,    0,  120,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   89,   90,   91,   92,   93,    0,    0,    0,
    3,   19,    0,    0,    0,    0,    0,    0,    0,   11,
  100,    0,  101,   60,    0,    0,    0,  123,   50,   68,
   59,    0,    0,    0,    0,    0,   74,  126,  127,  128,
  129,  130,  131,  124,  125,    0,   64,    0,    0,    0,
   22,    0,   21,    0,    0,   84,   94,    0,    0,   65,
    0,    0,   18,   17,    0,    0,    0,  106,   35,   12,
    0,    0,   58,   57,   69,    0,    0,    0,    0,  118,
  119,    0,    0,    0,   63,    0,   66,    0,   25,   24,
    0,    0,   96,    0,    0,   13,    0,    0,    0,    0,
    0,  107,    0,    0,    0,    0,   34,   70,   71,   67,
   75,    0,    0,    0,    0,   81,    0,   62,   61,    0,
   85,    0,    0,    0,   97,   14,    0,    0,    0,   29,
    0,    0,   37,  108,    0,    0,    0,    0,    0,    0,
    0,    0,   76,   27,   86,    0,    0,    0,   28,   30,
   31,   26,    0,    0,   39,  109,  110,    0,    0,    0,
   36,    0,    0,    0,    0,    0,    0,    0,    0,    9,
  102,  103,  104,   99,   95,   98,   41,    0,    0,  105,
    0,    0,   38,   82,    0,   78,   72,   77,    8,    4,
    7,    6,  112,  111,    0,    0,   10,   43,    0,    0,
   40,    0,    0,    0,    0,   88,   83,   87,   45,    0,
    0,   42,    0,    0,   80,   73,   79,   49,   33,   47,
   44,    0,    0,   48,   32,   46,
};
final static short yydgoto[] = {                         10,
  154,   51,  156,  209,  210,   59,   60,  135,   13,   14,
   15,   40,   16,   17,   30,   35,   18,   19,   20,   21,
   22,   23,   36,  157,  225,   49,   63,  213,   86,   32,
   33,
};
final static short yysindex[] = {                       247,
  -31,  -35,  -33, -233,    0,    0,   19,  355, -240,    0,
  247,    0,    0,    0, -173,    0,   94,    0,    0,    0,
    0,    0,    0,    0,    0,  290,  -40, -209, -204, -191,
   32,  -19,    0,  212,   55,  238, -180,  -30,   17,  -32,
  212,  367,    0,    0,    0,    0,    0, -178, -218, -175,
    0,    0,   54, -158,  310,  331,    2, -153,  271,    0,
    0,  297,    0,    0,   -5, -150,  -12,    0,    0,    0,
    0,  212,  212,  212,  212,   20,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  212,    0,  -38, -148, -181,
    0, -143,    0, -137,   45,    0,    0, -133,  266,    0,
 -181,    5,    0,    0,  154,  390,  100,    0,    0,    0,
  331,  326,    0,    0,    0, -128,  -43,  -19,  -19,    0,
    0, -113,  128,   55,    0,  -39,    0,  104,    0,    0,
 -112, -199,    0,  355,  336,    0,  108,   -6, -106,   56,
 -101,    0, -100,  -16,  212,  163,    0,    0,    0,    0,
    0,  180,  247,  197,    0,    0,  -99,    0,    0,  -98,
    0,  -97, -105,  302,    0,    0,  -96,  -94,  -11,    0,
  143,   96,    0,    0,  -92,  -90,   49,   92,  -89,  318,
  145,  110,    0,    0,    0,  222,   26,  -85,    0,    0,
    0,    0,  -84,   71,    0,    0,    0,  114,  148,  103,
    0,  180,   43,  -80,  106,  -79,  -78,  127,  111,    0,
    0,    0,    0,    0,    0,    0,    0,  -77,  -53,    0,
  -76,   88,    0,    0,   28,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  137,  -75,    0,    0,  -74,  -51,
    0,  -73,   51,  140,  -69,    0,    0,    0,    0,  147,
  -65,    0,  -64,   63,    0,    0,    0,    0,    0,    0,
    0,  242,  -61,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  198,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -41,    0,    0,    9,    0,    0,    0,  379,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  379,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -28,   -4,    0,
    0,    0,    0,   33,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   77,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   22,  418,   -2,    0,   -7,   10,   -8,   67,  560,  287,
   25,  182,    0,    0,   52,  498,  199,  200,  201,  202,
  203,  245,   29,   53,    0,    0,    0,    0,    0,   46,
   50,
};
final static int YYTABLESIZE=769;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        117,
   66,  117,  126,  117,   34,  240,   38,  251,   27,   28,
   89,   94,  115,   28,  115,  150,  115,  117,  117,  159,
  117,   11,   74,   39,  176,   29,   93,   75,  117,  192,
  115,  115,   50,  115,  168,   42,  116,   72,  116,   73,
  116,  106,  175,   84,  138,   85,  116,   98,   68,  113,
  110,   29,   69,  114,  116,  116,   90,  116,   41,  107,
  123,   99,   76,   28,   70,  105,  162,  113,  113,   95,
  113,  112,  163,  114,   72,   87,   73,   97,   67,   84,
  100,   85,   52,   53,  215,  132,  245,    5,    6,  198,
   54,  114,  114,  101,  114,  171,  110,   72,  102,   73,
   28,  227,  108,  110,   84,  115,   85,  127,   84,  254,
   85,  219,  129,   72,   29,   73,    2,  118,  119,  130,
  146,  263,  133,  120,  121,   29,   29,  148,  243,  218,
   72,  199,   73,    9,  144,    5,   28,  110,   72,  145,
   73,  128,  151,  161,  160,   72,  242,   73,  167,  170,
    9,   58,  137,  139,  173,  174,  183,  184,  185,  189,
  186,  190,   29,  196,  230,  197,  201,    9,  207,  236,
  216,  217,  220,  177,  181,  228,  231,  232,  238,  241,
  248,  249,  252,  193,    9,  234,  257,   28,  221,  169,
  260,  261,   28,    9,  266,  247,   55,    1,  256,  224,
  164,  237,    9,  206,  180,  259,   43,   44,   45,   46,
   47,   58,  149,  239,  117,  250,  158,  125,  117,    9,
   58,   24,   25,   91,   92,   24,   25,  115,    5,    6,
  117,  115,  117,  117,  117,  117,    9,    5,    6,   37,
  117,  117,   88,  115,  191,  115,  115,  115,  115,   26,
  113,  116,   48,  115,  115,  116,   28,   78,   79,   80,
   81,    9,    5,    6,  113,   82,   83,  116,  113,  116,
  116,  116,  116,    5,    6,   24,   25,  116,  116,  122,
  113,  214,  113,  113,  113,  113,    9,   71,  114,  244,
  113,  113,  114,   78,   79,   80,   81,   84,  226,   85,
  265,   82,   83,   62,  114,    9,  114,  114,  114,  114,
    9,    0,   24,   25,  114,  114,  131,  253,   78,   79,
   80,   81,   78,   79,   80,   81,   82,   83,   58,  262,
   82,   83,    5,    2,   28,    2,    9,    5,    5,    2,
    2,    9,    0,    2,    0,    2,    2,    2,   24,   25,
    1,  195,    2,   94,   58,    2,    3,    4,  223,   56,
  188,  229,    5,    6,    7,    9,    0,    1,  104,    2,
    9,   57,    8,    3,    4,    9,  204,  235,  208,    5,
    6,    7,  233,   58,    1,    0,    2,  152,   58,    8,
    3,    4,  246,  153,    9,  255,    5,    6,    7,   24,
   25,    1,  258,    2,   24,   25,    8,    3,    4,    0,
    1,  205,    2,    5,    6,    7,    3,   12,  140,    1,
  141,    2,   23,    8,    7,    3,   84,  178,   85,  179,
  143,   57,    8,    7,   28,    0,    1,   23,    2,    0,
   57,    8,    3,    4,    0,  153,    0,    0,    5,    6,
    7,    0,    0,    1,    0,    2,    0,    0,    8,    3,
    4,    0,    0,  182,    0,    5,    6,    7,   24,   25,
    0,    0,  212,    0,    0,    8,    0,    0,    1,    0,
    2,    0,    0,    0,    3,    4,    0,    0,    0,  208,
    5,    6,    7,   77,    0,  212,    0,  264,   31,    0,
    8,    0,    0,    1,    0,    2,    0,    0,    0,    3,
    4,   78,   79,   80,   81,    5,    6,    7,    0,   82,
   83,    0,    1,   65,    2,    8,  109,    1,    3,    2,
    0,  134,    0,    3,    0,    0,    7,    0,    0,    0,
  155,    7,    0,    0,    8,   64,   24,   25,   57,    8,
    0,    0,    0,    1,    0,    2,    0,    0,    1,    3,
    2,    0,  111,    0,    3,  103,   92,    7,  187,  155,
   12,    0,    7,    0,   57,    8,   61,    0,  202,  203,
    8,  147,    1,  124,    2,    0,    0,    1,    3,    2,
    0,  165,    1,    3,    2,    0,    7,    0,    3,    0,
    0,    7,    0,   57,    8,    0,    7,    0,   57,    8,
    0,    1,    0,    2,    8,   61,    0,    3,   61,  155,
    0,   61,   96,    0,    0,    7,    0,    0,    0,    0,
    0,    0,    0,    8,   23,   23,    0,  172,    0,    0,
   78,   79,   80,   81,    0,  142,   24,   25,   82,   83,
    0,    0,    0,    0,    0,    0,    0,    0,  136,    0,
    0,    0,    0,    0,   61,    0,    0,    0,  194,    0,
   61,   61,    0,    0,    0,  200,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  136,  166,    0,  222,    0,    0,    0,
    0,    0,    0,    0,    0,   61,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  166,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  211,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  211,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   43,   41,   45,   40,   59,   40,   59,   40,   45,
   41,   44,   41,   45,   43,   59,   45,   59,   60,   59,
   62,    0,   42,  257,   41,    1,   59,   47,   41,   41,
   59,   60,  273,   62,   41,    7,   41,   43,   43,   45,
   45,   40,   59,   60,   40,   62,   59,  266,  258,   41,
   59,   27,  257,   59,   59,   60,   40,   62,   40,   58,
   41,  280,   34,   45,  256,   56,  266,   59,   60,   41,
   62,   62,  272,   41,   43,  256,   45,  256,   27,   60,
  256,   62,  256,  257,   59,   41,   59,  269,  270,   41,
  264,   59,   60,   40,   62,   40,  105,   43,  257,   45,
   45,   59,  256,  112,   60,  256,   62,  256,   60,   59,
   62,   41,  256,   43,   90,   45,   40,   72,   73,  257,
  111,   59,  256,   74,   75,  101,  102,  256,   41,   59,
   43,   40,   45,   40,  106,   59,   45,  146,   43,   40,
   45,   90,  256,  256,   41,   43,   59,   45,   41,  256,
   40,   58,  101,  102,  256,  256,  256,  256,  256,  256,
  266,  256,  138,  256,   59,  256,  256,   40,   59,   59,
  256,  256,   59,  145,  153,  256,  256,  256,  256,  256,
  256,  256,  256,   41,   40,   59,  256,   45,   41,  138,
  256,  256,   45,   40,  256,   59,   15,    0,   59,  202,
  134,  209,   40,   59,  152,   59,    8,    8,    8,    8,
    8,   58,  256,  267,  256,  267,  256,  256,  260,   40,
   58,  257,  258,  256,  257,  257,  258,  256,  269,  270,
  272,  260,  274,  275,  276,  277,   40,  269,  270,  273,
  282,  283,  273,  272,  256,  274,  275,  276,  277,  281,
  256,  256,    8,  282,  283,  260,   45,  274,  275,  276,
  277,   40,  269,  270,  256,  282,  283,  272,  260,  274,
  275,  276,  277,  269,  270,  257,  258,  282,  283,  260,
  272,  256,  274,  275,  276,  277,   40,  256,  256,  262,
  282,  283,  260,  274,  275,  276,  277,   60,  256,   62,
   59,  282,  283,   17,  272,   40,  274,  275,  276,  277,
   40,   -1,  257,  258,  282,  283,  272,  267,  274,  275,
  276,  277,  274,  275,  276,  277,  282,  283,   58,  267,
  282,  283,  256,  257,   45,  259,   40,  261,  262,  263,
  264,   40,   -1,  267,   -1,  269,  270,  271,  257,  258,
  257,  256,  259,   44,   58,  279,  263,  264,  256,  266,
   59,  256,  269,  270,  271,   40,   -1,  257,   59,  259,
   40,  278,  279,  263,  264,   40,   59,  267,  268,  269,
  270,  271,  256,   58,  257,   -1,  259,  260,   58,  279,
  263,  264,  256,  266,   40,  256,  269,  270,  271,  257,
  258,  257,  256,  259,  257,  258,  279,  263,  264,   -1,
  257,  267,  259,  269,  270,  271,  263,    0,  265,  257,
  267,  259,   44,  279,  271,  263,   60,  265,   62,  267,
   41,  278,  279,  271,   45,   -1,  257,   59,  259,   -1,
  278,  279,  263,  264,   -1,  266,   -1,   -1,  269,  270,
  271,   -1,   -1,  257,   -1,  259,   -1,   -1,  279,  263,
  264,   -1,   -1,  267,   -1,  269,  270,  271,  257,  258,
   -1,   -1,  186,   -1,   -1,  279,   -1,   -1,  257,   -1,
  259,   -1,   -1,   -1,  263,  264,   -1,   -1,   -1,  268,
  269,  270,  271,  256,   -1,  209,   -1,  256,    1,   -1,
  279,   -1,   -1,  257,   -1,  259,   -1,   -1,   -1,  263,
  264,  274,  275,  276,  277,  269,  270,  271,   -1,  282,
  283,   -1,  257,   26,  259,  279,  256,  257,  263,  259,
   -1,  266,   -1,  263,   -1,   -1,  271,   -1,   -1,   -1,
  123,  271,   -1,   -1,  279,  256,  257,  258,  278,  279,
   -1,   -1,   -1,  257,   -1,  259,   -1,   -1,  257,  263,
  259,   -1,  266,   -1,  263,  256,  257,  271,  267,  152,
  153,   -1,  271,   -1,  278,  279,   17,   -1,  261,  262,
  279,  256,  257,   86,  259,   -1,   -1,  257,  263,  259,
   -1,  256,  257,  263,  259,   -1,  271,   -1,  263,   -1,
   -1,  271,   -1,  278,  279,   -1,  271,   -1,  278,  279,
   -1,  257,   -1,  259,  279,   56,   -1,  263,   59,  202,
   -1,   62,  256,   -1,   -1,  271,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  279,  256,  257,   -1,  140,   -1,   -1,
  274,  275,  276,  277,   -1,  256,  257,  258,  282,  283,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   99,   -1,
   -1,   -1,   -1,   -1,  105,   -1,   -1,   -1,  171,   -1,
  111,  112,   -1,   -1,   -1,  178,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  134,  135,   -1,  199,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  146,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  164,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  186,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  209,
};
}
final static short YYFINAL=10;
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
"programa : bloque",
"bloque : sentencias",
"bloque : bloque sentencias",
"bloque_de_sentencias : BEGIN bloque END ';'",
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
"sentencias : sentencias_declarativas",
"sentencias : sentencias_ejecutables",
"sentencias_declarativas : tipo lista_de_variables ';'",
"sentencias_declarativas : tipo lista_de_variables error",
"sentencias_declarativas : tipo error",
"sentencias_declarativas : declaracion_func",
"sentencias_declarativas : FUNC lista_de_variables ';'",
"sentencias_declarativas : FUNC lista_de_variables error",
"lista_de_variables : ID",
"lista_de_variables : lista_de_variables ',' ID",
"lista_de_variables : lista_de_variables ID error",
"encabezado_func : tipo FUNC ID '(' parametro ')'",
"encabezado_func : FUNC ID '(' parametro ')' error",
"encabezado_func : tipo ID '(' parametro ')' error",
"encabezado_func : tipo FUNC ID parametro error",
"encabezado_func : tipo FUNC ID '(' ')' error",
"encabezado_func : tipo FUNC ID '(' parametro error",
"declaracion_func : encabezado_func sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END ';'",
"declaracion_func : encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END ';'",
"declaracion_func : encabezado_func sentencias_declarativas bloque_sentencias_ejecutables_funcion error",
"declaracion_func : encabezado_func bloque_sentencias_ejecutables_funcion error",
"declaracion_func : encabezado_func sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion END error",
"declaracion_func : encabezado_func BEGIN bloque_sentencias_ejecutables_funcion END error",
"declaracion_func : encabezado_func sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN expresion error",
"declaracion_func : encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN expresion error",
"declaracion_func : encabezado_func sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' ')' error",
"declaracion_func : encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' ')' error",
"declaracion_func : encabezado_func sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ';' error",
"declaracion_func : encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ';' error",
"declaracion_func : encabezado_func sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' END error",
"declaracion_func : encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' END error",
"declaracion_func : encabezado_func sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' ';' error",
"declaracion_func : encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' ';' error",
"declaracion_func : encabezado_func sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END error",
"declaracion_func : encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END error",
"parametro : tipo ID",
"sentencias_ejecutables : asignacion",
"sentencias_ejecutables : salida",
"sentencias_ejecutables : invocacion_func",
"sentencias_ejecutables : sentencia_if",
"sentencias_ejecutables : sentencia_while",
"sentencias_ejecutables : sentencia_try_catch",
"asignacion : ID OPASIGNACION expresion ';'",
"asignacion : ID OPASIGNACION expresion error",
"asignacion : ID expresion error",
"asignacion : ID OPASIGNACION error",
"salida : PRINT '(' CADENA ')' ';'",
"salida : PRINT '(' CADENA ')' error",
"salida : PRINT '(' CADENA error",
"salida : PRINT CADENA error",
"salida : '(' CADENA error",
"salida : PRINT '(' ')' error",
"invocacion_func : ID '(' parametro ')' ';'",
"invocacion_func : ID parametro error",
"invocacion_func : ID '(' ')' error",
"invocacion_func : ID '(' parametro ';' error",
"invocacion_func : ID '(' parametro ')' error",
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if ENDIF ';'",
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if ELSE cuerpo_else ENDIF ';'",
"sentencia_if : IF condicion error",
"sentencia_if : IF '(' condicion THEN error",
"sentencia_if : IF '(' condicion ')' cuerpo_if error",
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if ';' error",
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if ENDIF error",
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if ELSE cuerpo_else ';' error",
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if ELSE cuerpo_else ENDIF error",
"cuerpo_if : bloque_de_sentencias",
"cuerpo_else : bloque_de_sentencias",
"sentencia_while : WHILE '(' condicion ')' DO BEGIN bloque_sentencias_while END ';'",
"sentencia_while : WHILE condicion error",
"sentencia_while : WHILE '(' condicion DO error",
"sentencia_while : WHILE '(' condicion ')' BEGIN error",
"sentencia_while : WHILE '(' condicion ')' DO BEGIN bloque_sentencias_while ';' error",
"sentencia_while : WHILE '(' condicion ')' DO BEGIN bloque_sentencias_while END error",
"sentencias_ejecutables_sin_try_catch : asignacion",
"sentencias_ejecutables_sin_try_catch : salida",
"sentencias_ejecutables_sin_try_catch : invocacion_func",
"sentencias_ejecutables_sin_try_catch : sentencia_if",
"sentencias_ejecutables_sin_try_catch : sentencia_while",
"sentencias_ejecutables_sin_try_catch : sentencia_try_catch error",
"sentencia_try_catch : TRY sentencias_ejecutables_sin_try_catch CATCH BEGIN bloque_sentencias_ejecutables END ';'",
"sentencia_try_catch : TRY sentencias_ejecutables_sin_try_catch BEGIN error",
"sentencia_try_catch : TRY sentencias_ejecutables_sin_try_catch CATCH bloque_sentencias_ejecutables error",
"sentencia_try_catch : TRY sentencias_ejecutables_sin_try_catch CATCH BEGIN bloque_sentencias_ejecutables ';' error",
"sentencia_try_catch : TRY sentencias_ejecutables_sin_try_catch CATCH BEGIN bloque_sentencias_ejecutables END error",
"sentencias_ejecutables_func : sentencias_ejecutables",
"sentencias_ejecutables_func : contrato",
"sentencias_while : sentencias_ejecutables",
"sentencias_while : sentencias_declarativas",
"sentencias_while : sentencia_break",
"contrato : CONTRACT ':' '(' condicion ')' ';'",
"contrato : ':' error",
"contrato : CONTRACT '(' error",
"contrato : CONTRACT '(' ')' error",
"contrato : CONTRACT '(' condicion ';' error",
"contrato : CONTRACT '(' condicion ')' error",
"sentencia_break : BREAK ';'",
"sentencia_break : BREAK error",
"condicion : expresion",
"condicion : condicion comparador expresion",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : CTE",
"factor : '-' CTE",
"comparador : '<'",
"comparador : '>'",
"comparador : MENORIGUAL",
"comparador : MAYORIGUAL",
"comparador : IGUAL",
"comparador : DISTINTO",
"comparador : AND",
"comparador : OR",
"tipo : LONG",
"tipo : SINGLE",
};

//#line 215 "gramatica.y"

private AnalizadorLexico lexico;
private AnalizadorSintactico sintactico;

public void setLexico(AnalizadorLexico lexico) { this.lexico = lexico; }

public void setSintactico(AnalizadorSintactico sintactico) { this.sintactico = sintactico; }

public int yylex() {
    int token = lexico.procesarYylex();
    if (lexico.getRefTablaSimbolos() != -1)
        yylval = new ParserVal(lexico.getRefTablaSimbolos());
    return token;
}

public void yyerror(String string) {
	sintactico.addErrorSintactico("par: " + string);
}
//#line 602 "Parser.java"
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
