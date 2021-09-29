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
public final static short ELSE=260;
public final static short THEN=261;
public final static short END_IF=262;
public final static short PRINT=263;
public final static short FUNC=264;
public final static short RETURN=265;
public final static short BEGIN=266;
public final static short END=267;
public final static short WHILE=268;
public final static short DO=269;
public final static short BREAK=270;
public final static short CONTRACT=271;
public final static short TRY=272;
public final static short CATCH=273;
public final static short LONG=274;
public final static short SINGLE=275;
public final static short CADENA=276;
public final static short MENORIGUAL=277;
public final static short MAYORIGUAL=278;
public final static short IGUAL=279;
public final static short DISTINTO=280;
public final static short OPASIGNACION=281;
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
   28,   28,   23,   16,   16,   16,   30,   30,   30,   31,
   31,   31,   29,   29,   29,   29,   29,   29,   11,   11,
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
    2,    2,    3,    3,    3,    1,    3,    3,    1,    1,
    1,    2,    1,    1,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,  129,  130,    0,    0,
    0,    2,   16,   15,    0,   20,    0,   51,   52,   53,
   54,   55,   56,  120,  121,    0,    0,    0,    0,    0,
    0,    0,  119,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   89,   90,   91,   92,   93,    0,    0,    0,
    3,   19,    0,    0,    0,    0,    0,    0,    0,   11,
  100,    0,  101,   60,    0,    0,    0,  122,   50,   68,
   59,    0,    0,    0,    0,    0,  125,  126,  127,  128,
  123,  124,    0,   74,   64,    0,    0,    0,   22,    0,
   21,    0,    0,   84,   94,    0,    0,   65,    0,    0,
   18,   17,    0,    0,    0,  106,   35,   12,    0,    0,
   58,   57,   69,    0,    0,    0,    0,  117,  118,    0,
    0,    0,   63,    0,   66,    0,   25,   24,    0,    0,
   96,    0,    0,   13,    0,    0,    0,    0,    0,  107,
    0,    0,    0,    0,   34,   70,   71,   67,   75,    0,
    0,    0,    0,   81,    0,   62,   61,    0,   85,    0,
    0,    0,   97,   14,    0,    0,    0,   29,    0,    0,
   37,  108,    0,    0,    0,    0,    0,    0,    0,    0,
   76,   27,   86,    0,    0,    0,   28,   30,   31,   26,
    0,    0,   39,  109,  110,    0,    0,    0,   36,    0,
    0,    0,    0,    0,    0,    0,    0,    9,  102,  103,
  104,   99,   95,   98,   41,    0,    0,  105,    0,    0,
   38,   82,    0,   78,   72,   77,    8,    4,    7,    6,
  112,  111,    0,    0,   10,   43,    0,    0,   40,    0,
    0,    0,    0,   88,   83,   87,   45,    0,    0,   42,
    0,    0,   80,   73,   79,   49,   33,   47,   44,    0,
    0,   48,   32,   46,
};
final static short yydgoto[] = {                         10,
  152,   51,  154,  207,  208,   59,   60,  133,   13,   14,
   15,   40,   16,   17,   30,   35,   18,   19,   20,   21,
   22,   23,   36,  155,  223,   49,   63,  211,   83,   32,
   33,
};
final static short yysindex[] = {                       141,
  -40,  -25,  -39, -234,   44,  172,    0,    0, -211,    0,
  141,    0,    0,    0, -188,    0,   32,    0,    0,    0,
    0,    0,    0,    0,    0,  243,  -17, -171, -161, -154,
   43,   12,    0,  121,  -27, -148, -144,  -34,   79,  -42,
  121, -139,    0,    0,    0,    0,    0, -134, -199, -132,
    0,    0,   90, -123,  126,  289,  -21, -121,  221,    0,
    0,  247,    0,    0,   38, -119,  -16,    0,    0,    0,
    0,  121,  121,  121,  121,  -35,    0,    0,    0,    0,
    0,    0,  121,    0,    0,  -32, -116, -204,    0, -110,
    0, -109,  -38,    0,    0, -107,  190,    0, -204,  -36,
    0,    0,  155,  323,  107,    0,    0,    0,  289,  269,
    0,    0,    0, -106,    4,   12,   12,    0,    0, -104,
   71,   50,    0,   57,    0,  114,    0,    0, -100, -187,
    0,  172,  310,    0,  116,    9,  -98,   54,  -97,    0,
  -96,   -2,  121,  171,    0,    0,    0,    0,    0,   99,
  141,  113,    0,    0,  -95,    0,    0,  -93,    0,  -91,
  -90,  200,    0,    0,  -85,  -81,   -1,    0,   59,   64,
    0,    0,  -79,  -77,  139,   83,  -73,   62,   85,  125,
    0,    0,    0,  127,   77,  -70,    0,    0,    0,    0,
  -69,   19,    0,    0,    0,  129,   97,   75,    0,   99,
   82,  -67,   95,  -66,  -65,  105,   51,    0,    0,    0,
    0,    0,    0,    0,    0,  -63,  -48,    0,  -60,   86,
    0,    0,  -37,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  110,  -58,    0,    0,  -56,  -46,    0,  -55,
  -45,  119,  -53,    0,    0,    0,    0,  133,  -52,    0,
  -51,    2,    0,    0,    0,    0,    0,    0,    0,  138,
  -49,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  208,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -33,    0,    0,    0,    0,    0,    0,  150,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  150,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -13,   -7,    0,    0,    0,
    0,    7,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   18,    0,    0,    0,    0,    0,    0,    0,
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
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   21,  393,   16,    0,   13,  -11,  -18,  165,  465,  185,
   74,  321,    0,    0,   15,   30,  390,  397,  402,  404,
  405,  411,   39,  271,    0,    0,    0,    0,    0,   33,
   58,
};
final static int YYTABLESIZE=672;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         27,
   38,   92,  130,  136,   28,  121,   87,  116,  124,  116,
  238,  116,  249,  252,   34,   72,   91,   73,  104,   28,
   11,  243,   39,   66,  115,  116,  116,  114,  116,  114,
   31,  114,   81,  115,   82,  115,  105,  115,  174,  190,
  108,   67,  114,   42,  103,  114,  114,  113,  114,  166,
  110,  115,  115,   74,  115,   65,  173,    2,   75,  217,
  261,   72,  148,   73,   50,  113,   96,   52,   53,    7,
    8,    9,   76,   97,   29,   54,    5,  216,  160,   93,
   72,  161,   73,   41,  108,   72,   68,   73,   28,   58,
    9,  108,   72,  169,   73,   69,  112,  144,   28,  191,
   29,   70,  126,   28,  116,  117,   72,   84,   73,  234,
    9,   85,  122,  135,  137,  157,   94,   72,   88,   73,
  202,   95,  197,   98,    9,  108,  241,   28,   72,   99,
   73,  118,  119,  100,  106,  213,  113,  219,    9,  125,
  225,   28,  142,  204,  240,  127,  143,  128,  131,  146,
  167,  149,    9,  228,  158,  159,  165,  168,  171,  172,
  181,   29,  182,  232,  183,   28,    9,  170,  245,   92,
  187,  179,   29,   29,  188,  184,  194,  254,  195,  196,
    9,  175,  199,  205,  102,  214,  215,  218,  226,  229,
  230,  257,  236,   23,    9,  239,  263,  246,  192,  247,
  250,   62,  255,  258,  259,  198,  264,    1,   23,   29,
    9,    9,   58,   89,   90,  222,   24,   25,  237,  235,
  248,  251,  116,  123,  242,  120,  220,  116,   58,    9,
  129,   24,   25,    7,    8,  116,   37,    7,    8,    9,
   26,   86,  114,  116,  116,  116,  116,  114,  115,   77,
   78,   79,   80,  115,  189,  114,    7,    8,  186,  147,
    9,  115,  113,  114,  114,  114,  114,  113,  260,  115,
  115,  115,  115,    5,    2,  113,    2,    5,   58,    5,
    2,    2,    7,    8,    2,    2,    9,   28,    1,    2,
    2,    2,    2,  111,    3,    4,  162,   56,   71,    5,
   24,   25,   57,    6,   58,    7,    8,    1,    9,    2,
   24,   25,  156,    3,    4,   24,   25,  233,    5,  193,
  206,  200,    6,  201,    7,    8,   58,    1,    9,    2,
  221,  150,  212,    3,    4,   55,  151,  224,    5,   24,
   25,    1,    6,    2,    7,    8,   58,    3,    4,    9,
  227,  203,    5,   24,   25,    1,    6,    2,    7,    8,
  231,    3,    4,  141,  151,  244,    5,   28,  210,    1,
    6,    2,    7,    8,  253,    3,    4,   24,   25,  180,
    5,  101,   90,    1,    6,    2,    7,    8,  256,    3,
    4,  210,   12,  262,    5,   43,  206,    1,    6,    2,
    7,    8,   44,    3,    4,   23,   23,   45,    5,   46,
   47,    1,    6,    2,    7,    8,   48,    3,    0,  138,
  178,  139,    5,    0,    0,   57,    6,    1,    1,    2,
    2,    0,    0,    3,    3,  176,    0,  177,    5,    5,
    0,   57,    6,    6,    0,    0,    1,    0,    2,    0,
    0,    0,    3,    0,    0,  132,    1,    5,    2,    0,
    0,    6,    3,    0,    0,    0,  185,    5,    0,    0,
    0,    6,    0,    0,    0,    0,  107,    1,    0,    2,
    0,   61,    0,    3,    0,    0,    0,    0,    5,    0,
    0,   57,    6,    0,    0,    0,    0,    0,   64,   24,
   25,    0,    0,    1,    0,    2,    0,    0,    0,    3,
    0,    0,  109,  153,    5,    0,    0,   57,    6,    0,
   61,    0,    0,   61,  145,    1,   61,    2,    0,    0,
    0,    3,    0,    0,    0,    0,    5,    0,    0,   57,
    6,    0,  153,   12,    0,    1,    0,    2,    0,    0,
    0,    3,    0,    0,    0,    0,    5,    0,    0,   57,
    6,  134,    0,    0,    0,  163,    1,   61,    2,    0,
    0,    0,    3,   61,   61,    0,    0,    5,  140,   24,
   25,    6,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  153,    0,    0,    0,  134,  164,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   61,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  164,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  209,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  209,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   40,   44,   41,   40,   45,   41,   41,   41,   41,   43,
   59,   45,   59,   59,   40,   43,   59,   45,   40,   45,
    0,   59,  257,   41,   41,   59,   60,   41,   62,   43,
    1,   45,   60,   41,   62,   43,   58,   45,   41,   41,
   59,   27,   59,    5,   56,   59,   60,   41,   62,   41,
   62,   59,   60,   42,   62,   26,   59,   40,   47,   41,
   59,   43,   59,   45,  276,   59,  266,  256,  257,  274,
  275,   40,   34,  273,    1,  264,   59,   59,  266,   41,
   43,  269,   45,   40,  103,   43,  258,   45,   45,   58,
   40,  110,   43,   40,   45,  257,   59,  109,   45,   41,
   27,  256,   88,   45,   72,   73,   43,  256,   45,   59,
   40,  256,   83,   99,  100,   59,  256,   43,   40,   45,
   59,  256,   40,  256,   40,  144,   41,   45,   43,   40,
   45,   74,   75,  257,  256,   59,  256,   41,   40,  256,
   59,   45,  104,   59,   59,  256,   40,  257,  256,  256,
  136,  256,   40,   59,   41,  256,   41,  256,  256,  256,
  256,   88,  256,   59,  256,   45,   40,  138,   59,   44,
  256,  151,   99,  100,  256,  266,  256,   59,  256,   41,
   40,  143,  256,   59,   59,  256,  256,   59,  256,  256,
  256,   59,  256,   44,   40,  256,   59,  256,  169,  256,
  256,   17,  256,  256,  256,  176,  256,    0,   59,  136,
   40,   40,   58,  256,  257,  200,  257,  258,  267,  207,
  267,  267,  256,  256,  262,  261,  197,  261,   58,   40,
  269,  257,  258,  274,  275,  269,  276,  274,  275,   40,
  281,  276,  256,  277,  278,  279,  280,  261,  256,  277,
  278,  279,  280,  261,  256,  269,  274,  275,   59,  256,
   40,  269,  256,  277,  278,  279,  280,  261,  267,  277,
  278,  279,  280,  256,  257,  269,  259,  260,   58,  262,
  263,  264,  274,  275,  267,  268,   40,   45,  257,  272,
  259,  274,  275,  256,  263,  264,  132,  266,  256,  268,
  257,  258,  271,  272,   58,  274,  275,  257,   40,  259,
  257,  258,  256,  263,  264,  257,  258,  267,  268,  256,
  270,  260,  272,  262,  274,  275,   58,  257,   40,  259,
  256,  261,  256,  263,  264,   15,  266,  256,  268,  257,
  258,  257,  272,  259,  274,  275,   58,  263,  264,   40,
  256,  267,  268,  257,  258,  257,  272,  259,  274,  275,
  256,  263,  264,   41,  266,  256,  268,   45,  184,  257,
  272,  259,  274,  275,  256,  263,  264,  257,  258,  267,
  268,  256,  257,  257,  272,  259,  274,  275,  256,  263,
  264,  207,    0,  256,  268,    6,  270,  257,  272,  259,
  274,  275,    6,  263,  264,  256,  257,    6,  268,    6,
    6,  257,  272,  259,  274,  275,    6,  263,   -1,  265,
  150,  267,  268,   -1,   -1,  271,  272,  257,  257,  259,
  259,   -1,   -1,  263,  263,  265,   -1,  267,  268,  268,
   -1,  271,  272,  272,   -1,   -1,  257,   -1,  259,   -1,
   -1,   -1,  263,   -1,   -1,  266,  257,  268,  259,   -1,
   -1,  272,  263,   -1,   -1,   -1,  267,  268,   -1,   -1,
   -1,  272,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,
   -1,   17,   -1,  263,   -1,   -1,   -1,   -1,  268,   -1,
   -1,  271,  272,   -1,   -1,   -1,   -1,   -1,  256,  257,
  258,   -1,   -1,  257,   -1,  259,   -1,   -1,   -1,  263,
   -1,   -1,  266,  121,  268,   -1,   -1,  271,  272,   -1,
   56,   -1,   -1,   59,  256,  257,   62,  259,   -1,   -1,
   -1,  263,   -1,   -1,   -1,   -1,  268,   -1,   -1,  271,
  272,   -1,  150,  151,   -1,  257,   -1,  259,   -1,   -1,
   -1,  263,   -1,   -1,   -1,   -1,  268,   -1,   -1,  271,
  272,   97,   -1,   -1,   -1,  256,  257,  103,  259,   -1,
   -1,   -1,  263,  109,  110,   -1,   -1,  268,  256,  257,
  258,  272,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  200,   -1,   -1,   -1,  132,  133,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  144,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  162,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  184,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  207,
};
}
final static short YYFINAL=10;
final static short YYMAXTOKEN=281;
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
null,null,null,null,null,null,"ID","CTE","IF","ELSE","THEN","END_IF","PRINT",
"FUNC","RETURN","BEGIN","END","WHILE","DO","BREAK","CONTRACT","TRY","CATCH",
"LONG","SINGLE","CADENA","MENORIGUAL","MAYORIGUAL","IGUAL","DISTINTO",
"OPASIGNACION",
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
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if END_IF ';'",
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if ELSE cuerpo_else END_IF ';'",
"sentencia_if : IF condicion error",
"sentencia_if : IF '(' condicion THEN error",
"sentencia_if : IF '(' condicion ')' cuerpo_if error",
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if ';' error",
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if END_IF error",
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if ELSE cuerpo_else ';' error",
"sentencia_if : IF '(' condicion ')' THEN cuerpo_if ELSE cuerpo_else END_IF error",
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
"condicion : expresion comparador expresion",
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
"tipo : LONG",
"tipo : SINGLE",
};

//#line 212 "gramatica.y"

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
//#line 577 "Parser.java"
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
