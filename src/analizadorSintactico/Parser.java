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
    0,    0,    0,    0,    0,    0,    0,    3,    3,    5,
    5,    5,    5,    5,    5,    6,    6,    8,    8,    2,
    2,    1,    1,    4,    4,   11,   11,   11,   11,   11,
   11,   13,   13,   13,   15,   15,   15,   15,   15,   15,
   14,   14,   14,   14,   14,   14,   14,   14,   14,   14,
   14,   14,   14,   14,   14,   14,   14,   14,   14,   14,
   14,   14,   16,   10,   10,   10,   10,   10,   10,   18,
   18,   18,   18,   19,   19,   19,   19,   19,   19,   20,
   20,   20,   20,   20,   21,   21,   21,   21,   21,   21,
   21,   21,   21,   25,   26,   22,   22,   22,   22,   22,
   22,   22,   22,   27,   27,   27,   27,   27,   27,   23,
   23,   23,   23,   23,    9,    9,    7,    7,    7,   28,
   28,   28,   28,   28,   29,   29,   24,   24,   17,   17,
   17,   31,   31,   31,   32,   32,   32,   30,   30,   30,
   30,   30,   30,   30,   30,   12,   12,
};
final static short yylen[] = {                            2,
    6,    4,    5,    5,    4,    6,    6,    1,    2,    4,
    3,    1,    4,    4,    4,    1,    2,    1,    2,    1,
    2,    1,    2,    1,    1,    3,    3,    2,    1,    3,
    4,    1,    3,    3,    6,    6,    6,    5,    6,    6,
   11,   10,   10,    9,    4,    3,    6,    5,    7,    6,
    8,    7,    9,    8,   10,    9,   11,   10,   11,   10,
   12,   11,    2,    1,    1,    1,    1,    1,    1,    4,
    4,    3,    3,    5,    5,    5,    4,    3,    5,    5,
    5,    5,    5,    5,    8,   10,    3,    5,    6,    8,
    8,   10,   10,    1,    1,    9,    8,    6,    3,    5,
    6,    9,    9,    1,    1,    1,    1,    1,    2,    7,
    4,    5,    7,    7,    1,    1,    1,    1,    1,    6,
    4,    5,    6,    6,    2,    2,    1,    3,    3,    3,
    1,    3,    3,    1,    1,    1,    2,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,  146,  147,    0,   22,    0,   29,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   20,   64,   65,   66,   67,   68,   69,    0,    0,
   23,   28,    0,    0,    0,    0,    0,    0,    0,   18,
  115,  116,    0,    0,    0,   30,    0,  135,  136,    0,
    0,    0,    0,    0,  134,    0,    0,    0,    0,    0,
    2,    0,    0,  104,  105,  106,  107,  108,    0,    0,
    0,    0,   21,    0,    0,    5,    0,    0,   27,   26,
    0,    0,    0,    0,    0,   46,   19,    0,    0,   31,
   34,   33,   73,    0,    0,    0,    0,  137,   72,    0,
    0,    0,    0,    0,   87,  140,  141,  142,  143,  144,
  145,  138,  139,    0,    0,    0,    0,    0,   99,  109,
    0,    0,   78,    3,    4,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   45,   63,    0,
   71,   70,    0,    0,    0,    0,    0,  132,  133,    0,
    0,    0,   77,    0,    0,    0,    0,    0,  111,    0,
    0,    7,    1,    6,    0,    0,    0,   38,    0,    0,
    0,   48,    0,    0,  121,    0,    0,    0,   36,   83,
   80,   84,   81,   82,   88,    0,    0,    0,    0,   94,
   25,   24,    0,   76,   75,   74,   79,  100,    0,    0,
    0,  112,   37,   39,   40,   35,    0,    0,    0,   50,
  122,    0,    0,    0,    0,    0,   47,    0,    0,    0,
    8,    0,    9,   89,  101,    0,    0,   98,  117,  118,
  119,    0,    0,    0,   52,    0,    0,  123,  124,  120,
    0,    0,    0,   49,    0,    0,    0,   11,    0,    0,
    0,    0,    0,   16,  126,  125,  114,  110,  113,    0,
   54,    0,    0,    0,   51,    0,    0,   90,   95,    0,
   91,   85,   15,   10,   14,   13,   97,    0,    0,   17,
   44,   56,    0,    0,    0,    0,   53,    0,    0,    0,
    0,  103,   96,  102,   60,   42,   58,    0,   43,   55,
    0,    0,    0,   92,   93,   86,   62,   59,   41,   57,
    0,   61,
};
final static short yydgoto[] = {                          2,
    7,   21,  188,  189,  190,  253,  228,   39,   40,   41,
  192,    9,   13,   10,   11,   89,   57,   23,   24,   25,
   26,   27,   28,   58,  193,  270,   70,   42,  231,  114,
   54,   55,
};
final static short yysindex[] = {                      -235,
  -69,    0, -213,  493,    0,    0,  265,    0, -176,    0,
  154,   31,  -36,  -24,   25,  -34,   -5,   27,  546, -216,
  510,    0,    0,    0,    0,    0,    0,    0,  517,  396,
    0,    0,   57, -155,  102,  406,   47,  171,  -16,    0,
    0,    0, -244,   52, -133,    0, -128,    0,    0,  229,
  -32, -111,  -25,   14,    0,   99,   81,  555, -108,  -28,
    0,   99,  566,    0,    0,    0,    0,    0, -100, -233,
  -92,  106,    0,  109,  430,    0, -244,  -33,    0,    0,
  130,  345,   38,  433,  457,    0,    0,  -86,  134,    0,
    0,    0,    0,   -8,  135,  139,  -77,    0,    0,   99,
   99,   99,   99,   65,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   99,  123,  -29,  -73,   90,    0,    0,
  -63,  528,    0,    0,    0,  -42,  -54,  163,  -11,  -50,
   99,   45,  -49,   51,  570,  168,  362,    0,    0,  -47,
    0,    0,   10,   16,  151,   14,   14,    0,    0,  -44,
  195,   81,    0,  156,   48,  157,  -43, -198,    0,  546,
  541,    0,    0,    0,  -18,   55,  -26,    0,   98,   63,
  -14,    0,   79,    4,    0,   99,   49,   93,    0,    0,
    0,    0,    0,    0,    0,  282,  303,  328,    0,    0,
    0,    0,  118,    0,    0,    0,    0,    0,  124,  212,
  466,    0,    0,    0,    0,    0,  158,  132,  110,    0,
    0,  136,   56,  147,   87,    5,    0, -144,  164,  231,
    0,  173,    0,    0,    0,  248,   71,    0,    0,    0,
    0,   76,  141,  -45,    0,  142,  -48,    0,    0,    0,
  260,  153,  146,    0,  287,  282,   77,    0,   95,  159,
  165,  327,  137,    0,    0,    0,    0,    0,    0,  334,
    0,  166,  379,  128,    0,  170,  -17,    0,    0, -163,
    0,    0,    0,    0,    0,    0,    0,  107,  175,    0,
    0,    0,  115,  183,  483,  344,    0,  188,  403,  351,
  119,    0,    0,    0,    0,    0,    0,  353,    0,    0,
  122,  189,  492,    0,    0,    0,    0,    0,    0,    0,
  355,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  113,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  113,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   -7,    0,    0,   54,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   17,   41,    0,    0,    0,
    0,   78,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  120,    0,
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
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  416,  138,  242,  -78,  201,    0, -194,  -35,  -39,   -2,
    3,   44,  439,    0,    0,  -38,  743,  432,  434,  437,
  438,  441,  444,  522,  281,    0,    0,    0,    0,    0,
   33,   60,
};
final static int YYTABLESIZE=958;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         87,
   82,   22,   85,    8,   22,   60,  129,   47,   97,   31,
  263,  155,  117,    8,  206,   51,  163,  100,   73,  101,
   52,    1,   46,   20,    5,    6,   22,   73,  100,  166,
  101,  254,  121,  131,  100,  131,  101,  131,  128,  130,
   31,  289,   87,   12,  213,   87,  122,  100,  137,  101,
  142,  131,  131,   61,  131,  102,   71,  129,  280,  129,
  103,  129,  212,  112,   56,  113,   62,  199,  181,   52,
   43,   52,   73,  200,  183,  129,  129,  134,  129,   32,
   33,  130,   52,  130,  170,  130,   88,   34,  215,   52,
  167,  173,  290,   52,  127,   52,   77,   87,  291,  130,
  130,   78,  130,  208,   83,  151,  196,   52,  221,  223,
   90,  245,  127,  127,  240,  127,  246,  247,  128,   22,
   88,   88,   91,  100,  112,  101,  113,  242,   92,  256,
  158,   52,  146,  147,  258,  272,  128,  128,  207,  128,
  100,  223,  101,   52,   30,   47,   98,  115,  191,  112,
  237,  113,  100,  274,  101,  120,   32,   22,   73,    8,
   80,  148,  149,  123,  124,  293,   75,  125,  236,  131,
  139,   32,   88,  296,  140,  143,   20,  306,  145,  144,
  309,  153,  156,  191,  191,  191,  267,  241,  100,  100,
  101,  101,  159,   20,    3,  279,    4,  229,   73,    5,
    6,  164,  230,  165,  266,  168,  172,  176,  179,  184,
   20,  185,  198,  162,  194,  197,  234,  191,  262,   44,
   45,  260,  248,  229,   95,   96,  154,  285,  230,  205,
   99,  251,   48,   49,   20,    5,    6,  203,   59,   86,
   14,  210,   15,  191,  116,   87,   16,  141,  131,  288,
  229,   20,  131,  303,   18,  230,   50,    5,    6,  161,
  244,   37,   19,   87,  131,  180,  131,  131,  131,  131,
   20,  182,  129,   52,  131,  131,  129,  106,  107,  108,
  109,   48,   49,   48,   49,  110,  111,   20,  129,  250,
  129,  129,  129,  129,   48,   49,  130,  201,  129,  129,
  130,   48,   49,  195,   20,   48,   49,   48,   49,  127,
  204,  239,  130,  127,  130,  130,  130,  130,  264,   48,
   49,   20,  130,  130,  150,  127,  255,  127,  127,  127,
  127,  257,  271,  128,  211,  127,  127,  128,  106,  107,
  108,  109,   20,   48,   49,  268,  110,  111,  217,  128,
  273,  128,  128,  128,  128,   48,   49,   79,   45,  128,
  128,  157,  292,  106,  107,  108,  109,   20,   32,   32,
  295,  110,  111,  224,  305,   12,    8,  308,    8,  225,
   12,   12,    8,    8,   20,  277,    8,  235,    8,    8,
    8,  238,  281,   14,  286,   15,  259,  261,    8,   16,
    3,   20,  299,  278,  227,    5,    6,   18,  265,  304,
   14,  307,   15,  312,  275,   19,   16,    3,   20,   36,
  276,  282,    5,    6,   18,  287,   38,   14,  220,   15,
  294,   37,   19,   16,    3,   20,   84,  284,  297,    5,
    6,   18,   20,  300,  310,   20,  269,   35,   37,   19,
   64,   14,   65,   15,  186,   66,   67,   16,    3,   68,
  187,  302,   69,    5,    6,   18,  218,    0,   14,   20,
   15,    0,   20,   19,   16,    3,    0,  226,    0,  227,
    5,    6,   18,    0,   93,   48,   49,   14,  127,   15,
   19,    0,    0,   16,    3,    0,   20,  249,    0,    5,
    6,   18,    0,    0,   14,   20,   15,    0,    0,   19,
   16,    3,    0,    0,  252,  227,    5,    6,   18,    0,
    0,   14,   20,   15,  233,    0,   19,   16,    3,    0,
   29,   20,   20,    5,    6,   18,    0,    0,   14,   63,
   15,    0,    0,   19,   16,    3,    0,  187,    0,   20,
    5,    6,   18,    0,    0,    0,   20,    0,    0,   14,
   19,   15,    0,    0,    0,   16,    3,   20,    0,  219,
    0,    5,    6,   18,    0,    0,    0,  104,    0,    0,
   20,   19,    0,  118,   14,   20,   15,    0,    0,    0,
   16,    3,    0,    0,  222,    0,    5,    6,   18,    0,
    0,   14,    0,   15,  135,    0,   19,   16,    0,  132,
    0,  133,    0,    0,  112,   18,  113,    0,   14,    0,
   15,    0,   37,   19,   16,  112,  177,  113,  178,  112,
    0,  113,   18,    0,    0,   14,    0,   15,    0,   37,
   19,   16,    0,    0,    0,  283,    0,    0,    0,   18,
    0,   76,   14,    0,   15,  174,   37,   19,   16,   14,
    0,   15,   14,    0,   15,   16,   18,    0,   16,  301,
   81,    0,    0,   18,   19,    0,   18,    0,    0,    0,
   37,   19,    0,   37,   19,    0,   14,    0,   15,   14,
    0,   15,   16,    0,    0,   16,  126,  136,    0,    0,
   18,    0,    0,   18,    0,    0,    0,    0,   19,    0,
   37,   19,  138,   14,    0,   15,    0,    0,    0,   16,
    0,    0,   14,    0,   15,    0,    0,   18,   16,    0,
    0,    0,  232,    0,   37,   19,   18,    0,  298,   14,
    0,   15,    0,    0,   19,   16,    0,  311,   14,   14,
   15,   15,    0,   18,   16,   16,   53,    0,    0,   17,
   37,   19,   18,   18,    0,    0,   14,    0,   15,   37,
   19,   19,   16,   14,    0,   15,   72,    0,    0,   16,
   18,    0,    0,   74,   14,    0,   15,   18,   19,    0,
   16,    0,   94,  160,    0,   19,  202,   14,   18,   15,
    0,    0,   14,   16,   15,    0,   19,    0,   16,    0,
  105,   18,    0,    0,    0,    0,   18,    0,    0,   19,
    0,  119,    0,    0,   19,  175,    0,    0,  106,  107,
  108,  109,    0,    0,    0,    0,  110,  111,    0,  106,
  107,  108,  109,  106,  107,  108,  109,  110,  111,    0,
    0,  110,  111,    0,    0,    0,  152,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  169,  171,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  209,    0,    0,    0,    0,    0,  214,  216,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  243,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         39,
   36,    4,   38,    1,    7,   40,   40,   44,   41,    7,
   59,   41,   41,   11,   41,   40,   59,   43,   21,   45,
   45,  257,   59,   40,  269,  270,   29,   30,   43,   41,
   45,  226,  266,   41,   43,   43,   45,   45,   77,   78,
   38,   59,   82,  257,   41,   85,  280,   43,   84,   45,
   59,   59,   60,   59,   62,   42,  273,   41,  253,   43,
   47,   45,   59,   60,   40,   62,   40,  266,   59,   45,
   40,   45,   75,  272,   59,   59,   60,   40,   62,  256,
  257,   41,   45,   43,   40,   45,   43,  264,   40,   45,
  129,   41,  256,   45,   41,   45,   40,  137,  262,   59,
   60,  257,   62,   41,   58,   41,   59,   45,  187,  188,
   59,  256,   59,   60,   59,   62,  261,  262,   41,  122,
   77,   78,  256,   43,   60,   45,   62,   41,  257,   59,
   41,   45,  100,  101,   59,   59,   59,   60,   41,   62,
   43,  220,   45,   45,    7,   44,  258,  256,  151,   60,
   41,   62,   43,   59,   45,  256,   44,  160,  161,   40,
   59,  102,  103,  256,   59,   59,   29,   59,   59,   40,
  257,   59,  129,   59,   41,   41,   40,   59,  256,   41,
   59,   59,  256,  186,  187,  188,   41,   41,   43,   43,
   45,   45,  256,   40,  264,   59,  266,  200,  201,  269,
  270,  256,  200,   41,   59,  256,  256,   40,  256,   59,
   40,  256,  256,  256,   59,   59,   59,  220,  267,  256,
  257,  267,   59,  226,  257,  258,  256,  263,  226,  256,
  256,   59,  257,  258,   40,  269,  270,  256,  273,  256,
  257,  256,  259,  246,  273,  285,  263,  256,  256,  267,
  253,   40,  260,  289,  271,  253,  281,  269,  270,  122,
  256,  278,  279,  303,  272,  256,  274,  275,  276,  277,
   40,  256,  256,   45,  282,  283,  260,  274,  275,  276,
  277,  257,  258,  257,  258,  282,  283,   40,  272,   59,
  274,  275,  276,  277,  257,  258,  256,  160,  282,  283,
  260,  257,  258,  256,   40,  257,  258,  257,  258,  256,
  256,  256,  272,  260,  274,  275,  276,  277,   59,  257,
  258,   40,  282,  283,  260,  272,  256,  274,  275,  276,
  277,  256,  256,  256,  256,  282,  283,  260,  274,  275,
  276,  277,   40,  257,  258,   59,  282,  283,  256,  272,
  256,  274,  275,  276,  277,  257,  258,  256,  257,  282,
  283,  272,  256,  274,  275,  276,  277,   40,  256,  257,
  256,  282,  283,  256,  256,  256,  257,  256,  259,  256,
  261,  262,  263,  264,   40,   59,  267,  256,  269,  270,
  271,  256,   59,  257,  267,  259,  256,  256,  279,  263,
  264,   40,   59,  267,  268,  269,  270,  271,  256,   59,
  257,   59,  259,   59,  256,  279,  263,  264,   40,  266,
  256,  256,  269,  270,  271,  256,   11,  257,  187,  259,
  256,  278,  279,  263,  264,   40,  266,   59,  256,  269,
  270,  271,   40,  256,  256,   40,  246,    9,  278,  279,
   19,  257,   19,  259,  260,   19,   19,  263,  264,   19,
  266,   59,   19,  269,  270,  271,  186,   -1,  257,   40,
  259,   -1,   40,  279,  263,  264,   -1,  266,   -1,  268,
  269,  270,  271,   -1,  256,  257,  258,  257,   59,  259,
  279,   -1,   -1,  263,  264,   -1,   40,  267,   -1,  269,
  270,  271,   -1,   -1,  257,   40,  259,   -1,   -1,  279,
  263,  264,   -1,   -1,  267,  268,  269,  270,  271,   -1,
   -1,  257,   40,  259,   59,   -1,  279,  263,  264,   -1,
  266,   40,   40,  269,  270,  271,   -1,   -1,  257,   18,
  259,   -1,   -1,  279,  263,  264,   -1,  266,   -1,   40,
  269,  270,  271,   -1,   -1,   -1,   40,   -1,   -1,  257,
  279,  259,   -1,   -1,   -1,  263,  264,   40,   -1,  267,
   -1,  269,  270,  271,   -1,   -1,   -1,   56,   -1,   -1,
   40,  279,   -1,   62,  257,   40,  259,   -1,   -1,   -1,
  263,  264,   -1,   -1,  267,   -1,  269,  270,  271,   -1,
   -1,  257,   -1,  259,   83,   -1,  279,  263,   -1,  265,
   -1,  267,   -1,   -1,   60,  271,   62,   -1,  257,   -1,
  259,   -1,  278,  279,  263,   60,  265,   62,  267,   60,
   -1,   62,  271,   -1,   -1,  257,   -1,  259,   -1,  278,
  279,  263,   -1,   -1,   -1,  267,   -1,   -1,   -1,  271,
   -1,  256,  257,   -1,  259,  134,  278,  279,  263,  257,
   -1,  259,  257,   -1,  259,  263,  271,   -1,  263,  267,
  265,   -1,   -1,  271,  279,   -1,  271,   -1,   -1,   -1,
  278,  279,   -1,  278,  279,   -1,  257,   -1,  259,  257,
   -1,  259,  263,   -1,   -1,  263,  267,  265,   -1,   -1,
  271,   -1,   -1,  271,   -1,   -1,   -1,   -1,  279,   -1,
  278,  279,  256,  257,   -1,  259,   -1,   -1,   -1,  263,
   -1,   -1,  257,   -1,  259,   -1,   -1,  271,  263,   -1,
   -1,   -1,  267,   -1,  278,  279,  271,   -1,  256,  257,
   -1,  259,   -1,   -1,  279,  263,   -1,  256,  257,  257,
  259,  259,   -1,  271,  263,  263,   14,   -1,   -1,  267,
  278,  279,  271,  271,   -1,   -1,  257,   -1,  259,  278,
  279,  279,  263,  257,   -1,  259,  267,   -1,   -1,  263,
  271,   -1,   -1,  267,  257,   -1,  259,  271,  279,   -1,
  263,   -1,   50,  266,   -1,  279,  256,  257,  271,  259,
   -1,   -1,  257,  263,  259,   -1,  279,   -1,  263,   -1,
  256,  271,   -1,   -1,   -1,   -1,  271,   -1,   -1,  279,
   -1,  256,   -1,   -1,  279,  256,   -1,   -1,  274,  275,
  276,  277,   -1,   -1,   -1,   -1,  282,  283,   -1,  274,
  275,  276,  277,  274,  275,  276,  277,  282,  283,   -1,
   -1,  282,  283,   -1,   -1,   -1,  114,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  131,  132,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  170,   -1,   -1,   -1,   -1,   -1,  176,  177,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  215,
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
"programa : ID bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables END ';'",
"programa : ID BEGIN END ';'",
"programa : ID BEGIN bloque_sentencias_ejecutables END ';'",
"programa : ID bloque_sentencias_declarativas BEGIN END ';'",
"programa : ID bloque_sentencias_declarativas bloque_sentencias_ejecutables error",
"programa : ID bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables ';' error",
"programa : ID bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables END error",
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
"sentencias_declarativas : tipo lista_de_variables ';'",
"sentencias_declarativas : tipo lista_de_variables error",
"sentencias_declarativas : tipo error",
"sentencias_declarativas : declaracion_func",
"sentencias_declarativas : FUNC lista_de_variables ';'",
"sentencias_declarativas : FUNC lista_de_variables error ';'",
"lista_de_variables : ID",
"lista_de_variables : lista_de_variables ',' ID",
"lista_de_variables : lista_de_variables ID error",
"encabezado_func : tipo FUNC ID '(' parametro ')'",
"encabezado_func : FUNC ID '(' parametro ')' error",
"encabezado_func : tipo ID '(' parametro ')' error",
"encabezado_func : tipo FUNC ID parametro error",
"encabezado_func : tipo FUNC ID '(' ')' error",
"encabezado_func : tipo FUNC ID '(' parametro error",
"declaracion_func : encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END ';'",
"declaracion_func : encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END ';'",
"declaracion_func : encabezado_func bloque_sentencias_declarativas BEGIN RETURN '(' expresion ')' ';' END ';'",
"declaracion_func : encabezado_func BEGIN RETURN '(' expresion ')' ';' END ';'",
"declaracion_func : encabezado_func bloque_sentencias_declarativas bloque_sentencias_ejecutables_funcion error",
"declaracion_func : encabezado_func bloque_sentencias_ejecutables_funcion error",
"declaracion_func : encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion END error",
"declaracion_func : encabezado_func BEGIN bloque_sentencias_ejecutables_funcion END error",
"declaracion_func : encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN expresion error",
"declaracion_func : encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN expresion error",
"declaracion_func : encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' ')' error",
"declaracion_func : encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' ')' error",
"declaracion_func : encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ';' error",
"declaracion_func : encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ';' error",
"declaracion_func : encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' END error",
"declaracion_func : encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' END error",
"declaracion_func : encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' ';' error",
"declaracion_func : encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' ';' error",
"declaracion_func : encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END error",
"declaracion_func : encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END error",
"declaracion_func : encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' bloque_sentencias_ejecutables_funcion error ';'",
"declaracion_func : encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' bloque_sentencias_ejecutables_funcion error ';'",
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
"salida : PRINT '(' CADENA error ';'",
"salida : PRINT CADENA error ';'",
"salida : '(' CADENA error",
"salida : PRINT '(' ')' error ';'",
"invocacion_func : ID '(' ID ')' ';'",
"invocacion_func : ID '(' CTE ')' ';'",
"invocacion_func : ID '(' ')' error ';'",
"invocacion_func : ID '(' ID ')' error",
"invocacion_func : ID '(' CTE ')' error",
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
"sentencia_while : WHILE '(' condicion ')' DO BEGIN bloque_sentencias_while END ';'",
"sentencia_while : WHILE '(' condicion ')' DO BEGIN END ';'",
"sentencia_while : WHILE '(' condicion ')' DO sentencias_while",
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
"contrato : CONTRACT ':' condicion error",
"contrato : CONTRACT ':' '(' ')' error",
"contrato : CONTRACT ':' '(' condicion ';' error",
"contrato : CONTRACT ':' '(' condicion ')' error",
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

//#line 236 "gramatica.y"

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
//#line 675 "Parser.java"
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
{ sintactico.agregarAnalisis("Se reconoció un programa. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 2:
//#line 19 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció un programa. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 3:
//#line 20 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció un programa. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 4:
//#line 21 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció un programa. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 5:
//#line 22 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta BEGIN al abrir el bloque de sentencias ejecutables."); }
break;
case 6:
//#line 23 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END al cerrar el bloque de sentencias ejecutables."); }
break;
case 7:
//#line 24 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del END en el bloque de sentencias ejecutables."); }
break;
case 13:
//#line 34 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta BEGIN al abrir el bloque."); }
break;
case 14:
//#line 35 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): bloque mal cerrado (falta 'END')."); }
break;
case 15:
//#line 36 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del bloque."); }
break;
case 26:
//#line 59 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de variable. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 27:
//#line 60 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' al final de la declaración de variable."); }
break;
case 28:
//#line 61 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' al final de la declaración de variable."); }
break;
case 30:
//#line 63 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de variable de tipo función. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 31:
//#line 64 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del bloque."); }
break;
case 34:
//#line 69 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta una ',' entre identificadores."); }
break;
case 36:
//#line 73 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el tipo de la función."); }
break;
case 37:
//#line 74 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta FUNC en la definición de la función."); }
break;
case 38:
//#line 75 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los parámetros deben ser colocados entre paréntesis."); }
break;
case 39:
//#line 76 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la función debe tener al menos un parámetro."); }
break;
case 40:
//#line 77 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros."); }
break;
case 41:
//#line 80 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 42:
//#line 81 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 43:
//#line 82 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 44:
//#line 83 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 45:
//#line 84 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el BEGIN antes del bloque de sentencias ejecutables."); }
break;
case 46:
//#line 85 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el BEGIN antes del bloque de sentencias ejecutables."); }
break;
case 47:
//#line 86 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el RETURN de la función."); }
break;
case 48:
//#line 87 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el RETURN de la función."); }
break;
case 49:
//#line 88 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la expresión de RETURN debe estar entre paréntesis."); }
break;
case 50:
//#line 89 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la expresión de RETURN debe estar entre paréntesis."); }
break;
case 51:
//#line 90 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): RETURN debe tener al menos una expresión como parámetro."); }
break;
case 52:
//#line 91 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): RETURN debe tener al menos una expresión como parámetro."); }
break;
case 53:
//#line 92 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en los parámetros de RETURN."); }
break;
case 54:
//#line 93 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en los parámetros de RETURN."); }
break;
case 55:
//#line 94 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de RETURN (expresión)."); }
break;
case 56:
//#line 95 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de RETURN (expresión)."); }
break;
case 57:
//#line 96 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END luego de ';'."); }
break;
case 58:
//#line 97 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END luego de ';'."); }
break;
case 59:
//#line 98 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
break;
case 60:
//#line 99 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
break;
case 61:
//#line 100 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): no se permiten sentencias luego del RETURN en la función."); }
break;
case 62:
//#line 101 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): no se permiten sentencias luego del RETURN en la función."); }
break;
case 70:
//#line 115 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una operación de asignación. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 71:
//#line 116 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' luego de la asignación."); }
break;
case 72:
//#line 117 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el operador de asignación."); }
break;
case 73:
//#line 118 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' luego de la asignación."); }
break;
case 74:
//#line 121 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una impresión por pantalla de una cadena. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 75:
//#line 122 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la impresión de cadena."); }
break;
case 76:
//#line 123 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): cierre erróneo de la lista de parámetros de PRINT."); }
break;
case 77:
//#line 124 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los parámetros de PRINT deben estar entre paréntesis."); }
break;
case 78:
//#line 125 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba PRINT, se encontró '('."); }
break;
case 79:
//#line 126 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta declarar una cadena para PRINT."); }
break;
case 80:
//#line 129 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una invocación a una función (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 81:
//#line 130 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una invocación a una función (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 82:
//#line 131 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta especificar un parámetro al invocar a la función."); }
break;
case 83:
//#line 132 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la invocación a la función."); }
break;
case 84:
//#line 133 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la invocación a la función."); }
break;
case 85:
//#line 136 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una sentencia IF. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 86:
//#line 137 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una sentencia IF. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 87:
//#line 138 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición de IF debe estar entre paréntesis."); }
break;
case 88:
//#line 139 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros."); }
break;
case 89:
//#line 140 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta la declaración de THEN."); }
break;
case 90:
//#line 141 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta cierre de ENDIF."); }
break;
case 91:
//#line 142 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de ENDIF."); }
break;
case 92:
//#line 143 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta cierre de ENDIF."); }
break;
case 93:
//#line 144 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de ENDIF."); }
break;
case 96:
//#line 153 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 97:
//#line 154 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 98:
//#line 155 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 99:
//#line 156 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición de WHILE debe estar entre paréntesis."); }
break;
case 100:
//#line 157 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros."); }
break;
case 101:
//#line 158 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba DO, se leyó BEGIN."); }
break;
case 102:
//#line 159 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba END, se leyó ';'."); }
break;
case 103:
//#line 160 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
break;
case 109:
//#line 168 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los bloques TRY/CATCH no pueden anidarse"); }
break;
case 110:
//#line 171 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció un bloque TRY/CATCH. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 111:
//#line 172 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se leyó un BEGIN sin previo reconocimiento de CATCH."); }
break;
case 112:
//#line 173 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): el cuerpo de CATCH no se inicializó con BEGIN."); }
break;
case 113:
//#line 174 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): cuerpo de CATCH mal cerrado; falta END."); }
break;
case 114:
//#line 175 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
break;
case 120:
//#line 187 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una definición de contrato. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 121:
//#line 188 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición debe estar entre paréntesis."); }
break;
case 122:
//#line 189 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): CONTRACT debe tener al menos una condición como parámetro."); }
break;
case 123:
//#line 190 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el paréntesis de cierre para los parámetros de CONTRACT."); }
break;
case 124:
//#line 191 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de los parámetros de CONTRACT."); }
break;
case 125:
//#line 194 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una sentencia de BREAK. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 126:
//#line 195 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de BREAK."); }
break;
case 136:
//#line 213 "gramatica.y"
{
                String tipo = sintactico.getTipoFromTS(val_peek(0).ival);
                if (tipo.equals("LONG"))
                    sintactico.verificarRangoEnteroLargo(val_peek(0).ival);
             }
break;
case 137:
//#line 218 "gramatica.y"
{  sintactico.setNegativoTablaSimb(val_peek(0).ival); }
break;
//#line 1188 "Parser.java"
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
