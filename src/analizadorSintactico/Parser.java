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
    9,    9,    3,    3,    2,    2,    5,    5,   12,   12,
   12,   12,   12,   12,   14,   14,   14,   16,   16,   16,
   17,   17,   15,   15,   11,   11,   11,   11,   11,   24,
   19,   19,   19,   19,   20,   20,   20,   20,   20,   20,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   26,
   27,   28,   22,   22,   22,   29,   29,   29,   29,   29,
   30,   30,   23,   23,   23,   23,   10,   10,    8,    8,
    8,   31,   31,   31,   31,   31,   32,   32,   25,   33,
   33,   34,   34,   36,   36,   18,   18,   18,   39,   39,
   39,   40,   40,   40,   40,   40,   38,   38,   38,   38,
   38,   38,   37,   35,   13,   13,
};
final static short yylen[] = {                            2,
    3,    2,    3,    2,    4,    3,    4,    4,    3,    3,
    1,    2,    4,    3,    1,    4,    4,    4,    1,    2,
    1,    2,    1,    2,    1,    2,    1,    1,    3,    3,
    2,    1,    3,    4,    1,    3,    3,    4,    4,    4,
    4,    5,   10,    9,    1,    1,    1,    1,    1,    1,
    4,    4,    3,    3,    5,    5,    5,    4,    3,    5,
    8,   10,    3,    5,    6,    8,    8,   10,   10,    1,
    1,    2,    8,    7,    5,    1,    1,    1,    1,    2,
    3,    4,    5,    3,    5,    5,    1,    1,    1,    1,
    1,    6,    4,    5,    6,    6,    2,    2,    1,    1,
    3,    1,    3,    1,    3,    3,    3,    1,    3,    3,
    1,    1,    1,    2,    4,    4,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    4,    0,    2,  125,  126,    0,   25,
    0,   32,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   23,   45,   46,   47,   48,   49,    0,    0,
    0,    0,    3,    1,   26,   31,    0,    0,    0,    0,
    0,    0,  113,   50,    0,    0,    0,    0,  111,    0,
    0,    0,    0,    0,    0,    0,    0,   10,    6,   72,
   76,   77,   78,   79,    0,    0,    9,    0,    0,    0,
   24,    0,    0,    0,    0,    0,    0,   33,    0,    0,
    0,   30,   29,    0,    0,    0,    0,   21,   87,   88,
    0,  114,   53,    0,    0,   54,    0,    0,    0,    0,
   63,  124,    0,  123,    0,  119,  120,  121,  122,  117,
  118,    0,    0,    0,    0,   80,    0,   81,   59,    8,
    5,    7,    0,    0,   84,   39,   34,   37,   36,   40,
   38,    0,    0,    0,    0,   22,    0,    0,    0,    0,
   52,   51,  109,  110,    0,    0,    0,    0,    0,   58,
    0,    0,    0,   82,    0,    0,    0,   41,    0,    0,
    0,    0,    0,  115,  116,   64,    0,    0,    0,    0,
   70,   28,   27,    0,   57,   56,   55,   60,    0,    0,
   75,   89,   90,   91,   86,   83,   85,   42,    0,    0,
    0,   93,    0,    0,    0,    0,   11,    0,   12,   65,
    0,    0,   19,   98,   97,    0,   94,    0,    0,    0,
    0,    0,    0,   14,    0,    0,    0,   74,    0,   20,
    0,   95,   96,   92,    0,   66,   71,    0,   67,   61,
   18,   13,   17,   16,   73,   44,    0,    0,    0,   43,
   68,   69,   62,
};
final static short yydgoto[] = {                          2,
    3,    9,   22,  169,  170,  171,  202,  181,   87,   88,
  172,  173,   11,   32,   12,   13,   41,   51,   24,   25,
   26,   27,   28,   47,   52,  174,  228,   29,   66,   30,
   90,  184,   53,   54,  103,   55,  105,  112,   48,   49,
};
final static short yysindex[] = {                      -235,
 -147,    0,  232,    0, -227,    0,    0,    0, -136,    0,
 -151,    0, -134,  -20,   24,  -25,  -43,   32,  316, -173,
 -179,  233,    0,    0,    0,    0,    0,    0,  116,  292,
   64,   83,    0,    0,    0,    0,   68, -146,   95, -142,
  256,   85,    0,    0, -129,    4,  210,   -5,    0,  116,
   -4, -119, -138, -135,  306,  -99,  -34,    0,    0,    0,
    0,    0,    0,    0,  -86, -199,    0,  -85,  -39,  -84,
    0,  132,  316,  305,  -82,  117,  -78,    0,  -77,  -75,
  139,    0,    0,  141,  143,  127,  265,    0,    0,    0,
 -114,    0,    0,  116,  116,    0,   15,  116,  116,  -38,
    0,    0,  116,    0,  116,    0,    0,    0,    0,    0,
    0,  116,  129,  -28,  -70,    0,  -66,    0,    0,    0,
    0,    0,  -81,  266,    0,    0,    0,    0,    0,    0,
    0, -118,  116,   31,  153,    0,  154,  155,   -5,   -5,
    0,    0,    0,    0,  -59,   84, -135,  306,   -4,    0,
  140,  -35,  142,    0,  101,  -26,  -58,    0, -104,   47,
   50,  -56,  116,    0,    0,    0,  169,  186,  214,    0,
    0,    0,    0,  -54,    0,    0,    0,    0,  118,  -24,
    0,    0,    0,    0,    0,    0,    0,    0,  144,  -52,
   41,    0,   69, -130,  146,  135,    0,  147,    0,    0,
  148,  152,    0,    0,    0,  -55,    0,  -45,   18,  157,
  159,  169,   42,    0,   59,  -42,  -16,    0,  197,    0,
  198,    0,    0,    0,   -3,    0,    0, -177,    0,    0,
    0,    0,    0,    0,    0,    0,  206,  207,   62,    0,
    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  105,    0,    0,    0,    0,    0,  105,    0,    0,    0,
    0,  -41,    0,    0,    0,    0,    0,  -31,    0,    0,
   27,    0,    2,  -36,   34,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   -7,    3,
    0,    0,    0,    0,    0,    0,  -33,   39,   37,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   67,
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
    0,  168,  -19,  165, -112,   72,    0, -152,    0,  240,
   29,    8,  322,  331,    0,    0,    0,   26,  330,  338,
  340,  357,  359,    0,   16,  212,    0,    0,    0,    0,
    0,    0,    0,  280,    0,  279,    0,    0,   61,   70,
};
final static int YYTABLESIZE=595;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        112,
  112,  112,  146,  112,  100,  112,  115,  101,   10,  108,
   74,  108,  152,  108,   57,   59,   35,  112,  112,  121,
  112,    1,  100,  177,   45,  101,  203,  108,  108,   31,
  108,   23,  186,  106,  205,  106,   98,  106,   94,   46,
   95,   99,   99,  107,   72,  107,   94,  107,   95,  220,
   71,  106,  106,  124,  106,  197,  199,   94,   23,   95,
   99,  107,  107,   50,  107,  100,  117,  104,   45,   89,
  161,   60,   97,  142,  102,   45,  224,  105,  238,  103,
  118,  209,   67,  199,  239,  104,  104,  189,  104,   94,
  190,   95,  102,   68,   45,  105,  105,  103,  105,  208,
  230,   23,   71,   75,   36,   37,   11,   80,    4,  210,
   81,   94,   38,   95,   84,   89,    5,  232,    6,   33,
  243,    7,    8,   21,   91,  211,   79,    5,   92,   34,
  212,  213,    7,    8,    7,    8,  101,  149,   79,   10,
   21,   78,  137,  138,  102,    5,  104,  158,   35,  162,
    7,    8,   71,   83,  139,  140,  113,   21,  160,    5,
   45,  188,  183,   35,    7,    8,   35,  143,  144,  116,
  119,  122,  123,  126,   21,  127,  191,  128,  131,  129,
  130,  132,  133,  182,  134,  153,  183,  150,  193,  154,
  155,   21,  163,  216,  164,  165,  166,  187,  175,  192,
  178,  200,  206,  207,  214,  217,  218,  182,   21,  183,
  222,  221,   58,  233,  112,  225,  120,  226,  112,  100,
  176,  145,  101,  100,  108,   21,  101,  151,  108,  185,
  182,  204,  112,  112,  112,  112,   42,   43,  114,  234,
  112,  112,  108,  108,  108,  108,  100,   56,  106,  101,
  108,  108,  106,   21,   45,  235,  236,   99,  107,   93,
   44,   99,  107,  237,  240,  241,  106,  106,  106,  106,
  141,   21,   21,  223,  106,  106,  107,  107,  107,  107,
   42,   43,  104,  227,  107,  107,  104,   42,   43,  102,
   20,   70,  105,  102,  103,   21,  105,  229,  103,  159,
  104,  104,  104,  104,   21,   21,   42,   43,  104,  104,
  105,  105,  105,  105,  231,  102,  102,  242,  105,  105,
  103,  103,   15,   11,  157,   11,  136,   15,   15,   11,
   11,   21,  196,   11,   40,   11,   11,   11,   76,   77,
   14,   39,   15,  167,   21,   11,   16,    5,   61,  168,
   82,   77,    7,    8,   18,   21,   62,   14,   63,   15,
   35,   35,   19,   16,    5,  110,  179,  111,  180,    7,
    8,   18,   42,   43,   14,   64,   15,   65,  194,   19,
   16,    5,  147,  148,  201,  180,    7,    8,   18,    0,
    0,   14,    0,   15,    0,    0,   19,   16,    5,    0,
    0,  215,    0,    7,    8,   18,    0,    0,   14,    0,
   15,    0,    0,   19,   16,    5,    0,    0,  219,  180,
    7,    8,   18,    0,    0,   14,    0,   15,    0,    0,
   19,   16,    5,    0,  168,    0,    0,    7,    8,   18,
    0,    0,   14,    0,   15,    0,    0,   19,   16,    5,
    0,    0,  195,    0,    7,    8,   18,    0,    0,    0,
    0,    0,    0,    0,   19,   96,   42,   43,    0,    0,
   14,    0,   15,    0,    0,    0,   16,    5,    0,    0,
  198,    0,    7,    8,   18,    0,    0,    0,   14,   14,
   15,   15,   19,    0,   16,   16,    0,    0,   17,   69,
    0,    0,   18,   18,    0,    0,    0,    0,    0,    0,
   19,   19,   14,    0,   15,    0,    0,    0,   16,    0,
   85,   14,   14,   15,   15,    0,   18,   16,   16,  135,
    0,    0,  156,   86,   19,   18,   18,    0,    0,    0,
    0,    0,   86,   19,   19,    0,    0,    0,   14,    0,
   15,    0,    0,    0,   16,    0,    0,   73,    0,    0,
  125,   14,   18,   15,    0,    0,    0,   16,    0,    0,
   19,    0,   14,    0,   15,   18,    0,    0,   16,  106,
  107,  108,  109,   19,    0,    0,   18,    0,    0,    0,
    0,    0,    0,    0,   19,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   41,   45,   41,   47,   41,   41,    1,   41,
   30,   43,   41,   45,   40,   59,    9,   59,   60,   59,
   62,  257,   59,   59,   45,   59,  179,   59,   60,  257,
   62,    3,   59,   41,   59,   43,   42,   45,   43,   14,
   45,   47,   41,   41,   29,   43,   43,   45,   45,  202,
   22,   59,   60,   73,   62,  168,  169,   43,   30,   45,
   59,   59,   60,   40,   62,   50,  266,   41,   45,   41,
   40,   40,   47,   59,   41,   45,   59,   41,  256,   41,
  280,   41,  256,  196,  262,   59,   60,   41,   62,   43,
   41,   45,   59,  273,   45,   59,   60,   59,   62,   59,
   59,   73,   74,   40,  256,  257,   40,   40,  256,   41,
  257,   43,  264,   45,  257,   87,  264,   59,  266,  256,
   59,  269,  270,   40,   40,  256,   44,  264,  258,  266,
  261,  262,  269,  270,  269,  270,  256,  112,   44,  132,
   40,   59,  257,  258,  283,  264,  282,  266,   44,  134,
  269,  270,  124,   59,   94,   95,  256,   40,  133,  264,
   45,  266,  155,   59,  269,  270,  159,   98,   99,  256,
  256,  256,   41,  256,   40,   59,  161,  256,   40,  257,
  256,   41,   40,  155,   58,  256,  179,   59,  163,  256,
  272,   40,   40,   59,   41,   41,  256,  256,   59,  256,
   59,  256,   59,  256,   59,   59,   59,  179,   40,  202,
  256,  267,  256,  256,  256,   59,  256,   59,  260,  256,
  256,  260,  256,  260,  256,   40,  260,  256,  260,  256,
  202,  256,  274,  275,  276,  277,  257,  258,  273,  256,
  282,  283,  274,  275,  276,  277,  283,  273,  256,  283,
  282,  283,  260,   40,   45,   59,   59,  256,  256,  256,
  281,  260,  260,  267,   59,   59,  274,  275,  276,  277,
  256,   40,   40,  256,  282,  283,  274,  275,  276,  277,
  257,  258,  256,  212,  282,  283,  260,  257,  258,  256,
   59,   59,  256,  260,  256,   40,  260,  256,  260,  132,
  274,  275,  276,  277,   40,   40,  257,  258,  282,  283,
  274,  275,  276,  277,  256,  282,  283,  256,  282,  283,
  282,  283,  256,  257,   59,  259,   87,  261,  262,  263,
  264,   40,  168,  267,   13,  269,  270,  271,  256,  257,
  257,   11,  259,  260,   40,  279,  263,  264,   19,  266,
  256,  257,  269,  270,  271,   40,   19,  257,   19,  259,
  256,  257,  279,  263,  264,   60,  266,   62,  268,  269,
  270,  271,  257,  258,  257,   19,  259,   19,  167,  279,
  263,  264,  103,  105,  267,  268,  269,  270,  271,   -1,
   -1,  257,   -1,  259,   -1,   -1,  279,  263,  264,   -1,
   -1,  267,   -1,  269,  270,  271,   -1,   -1,  257,   -1,
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
  279,  279,  257,   -1,  259,   -1,   -1,   -1,  263,   -1,
  265,  257,  257,  259,  259,   -1,  271,  263,  263,  265,
   -1,   -1,  267,  278,  279,  271,  271,   -1,   -1,   -1,
   -1,   -1,  278,  279,  279,   -1,   -1,   -1,  257,   -1,
  259,   -1,   -1,   -1,  263,   -1,   -1,  266,   -1,   -1,
  256,  257,  271,  259,   -1,   -1,   -1,  263,   -1,   -1,
  279,   -1,  257,   -1,  259,  271,   -1,   -1,  263,  274,
  275,  276,  277,  279,   -1,   -1,  271,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  279,
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
"sentencias_declarativas : tipo lista_de_variables ';'",
"sentencias_declarativas : tipo lista_de_variables error",
"sentencias_declarativas : tipo error",
"sentencias_declarativas : declaracion_func",
"sentencias_declarativas : FUNC lista_de_variables ';'",
"sentencias_declarativas : FUNC lista_de_variables error ';'",
"lista_de_variables : ID",
"lista_de_variables : lista_de_variables ',' ID",
"lista_de_variables : lista_de_variables ID error",
"encabezado_func : tipo FUNC ID '('",
"encabezado_func : FUNC ID '(' error",
"encabezado_func : tipo ID '(' error",
"parametro : tipo ID ')' BEGIN",
"parametro : tipo ID ')' bloque_sentencias_declarativas BEGIN",
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

//#line 458 "gramatica.y"

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
//#line 557 "Parser.java"
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
{ sintactico.agregarAnalisis("Se reconoció una declaración de variable. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 30:
//#line 73 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' al final de la declaración de variable."); }
break;
case 31:
//#line 74 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' al final de la declaración de variable."); }
break;
case 33:
//#line 76 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de variable de tipo función. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 34:
//#line 77 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del bloque."); }
break;
case 35:
//#line 80 "gramatica.y"
{
                                                          sintactico.setAmbitoTablaSimb(val_peek(0).ival);
                                                          sintactico.setUsoTablaSimb(val_peek(0).ival, "VARIABLE");
                                                          if (!sintactico.variableFueDeclarada(val_peek(0).ival))
                                                                sintactico.setTipoVariableTablaSimb(val_peek(0).ival);
                                                          else
                                                                sintactico.addErrorSintactico("ERROR (Línea " + AnalizadorLexico.LINEA + "): Variable " + val_peek(0).ival + " ya declarada.");
                                                    }
break;
case 36:
//#line 88 "gramatica.y"
{
                                                          sintactico.setAmbitoTablaSimb(val_peek(0).ival);
                                                          sintactico.setUsoTablaSimb(val_peek(0).ival, "VARIABLE");
                                                          if (!sintactico.variableFueDeclarada(val_peek(0).ival))
                                                                sintactico.setTipoVariableTablaSimb(val_peek(0).ival);
                                                          else
                                                                sintactico.addErrorSintactico("ERROR (Línea " + AnalizadorLexico.LINEA + "): Variable " + val_peek(2).ival + " ya declarada.");
                                                    }
break;
case 37:
//#line 96 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta una ',' entre identificadores."); }
break;
case 38:
//#line 99 "gramatica.y"
{
                                        sintactico.setUsoTablaSimb(val_peek(1).ival, "FUNC");
                                        if (sintactico.variableFueDeclarada(val_peek(1).ival))
                                            sintactico.setErrorFunc(true);
                                        else {
                                            sintactico.setTipoVariableTablaSimb(val_peek(1).ival);
                                            sintactico.setAmbitoTablaSimb(val_peek(1).ival);
                                            sintactico.agregarReferencia(val_peek(1).ival);
                                            sintactico.setAmbito(sintactico.getAmbito() + "@" + sintactico.getLexemaFromTS(val_peek(1).ival));
                                        }
                                    }
break;
case 39:
//#line 111 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el tipo de la función."); }
break;
case 40:
//#line 112 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta FUNC en la definición de la función."); }
break;
case 41:
//#line 115 "gramatica.y"
{
                                        sintactico.setAmbitoTablaSimb(val_peek(2).ival);
                                        sintactico.setTipoVariableTablaSimb(val_peek(2).ival);
                                        sintactico.setUsoTablaSimb(val_peek(2).ival, "VARIABLE");
                                        sintactico.agregarAPolaca("INIC_" + sintactico.getAmbitoFromTS(sintactico.obtenerReferencia()));
                                    }
break;
case 42:
//#line 121 "gramatica.y"
{
                                                                    sintactico.setAmbitoTablaSimb(val_peek(3).ival);
                                                                    sintactico.setTipoVariableTablaSimb(val_peek(3).ival);
                                                                    sintactico.setUsoTablaSimb(val_peek(3).ival, "VARIABLE");
                                                                    sintactico.agregarAPolaca("INIC_" + sintactico.getAmbitoFromTS(sintactico.obtenerReferencia()));
                                                                }
break;
case 43:
//#line 129 "gramatica.y"
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
case 44:
//#line 149 "gramatica.y"
{
                                                                                                                            if (!sintactico.huboErrorFunc()) {
                                                                                                                                sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                                                RegistroSimbolo tokenReturn = new RegistroSimbolo(sintactico.getLexemaFromTS(sintactico.obtenerReferencia()) + "ret", "ID");
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
case 50:
//#line 178 "gramatica.y"
{ yyval.sval = new String(":="); }
break;
case 51:
//#line 181 "gramatica.y"
{
                                                        int referencia = sintactico.referenciaCorrecta(val_peek(3).ival);
                                                        if (referencia == -1)
                                                            sintactico.addErrorSintactico("ERROR SEMÁNTICO (Línea " + (AnalizadorLexico.LINEA) + "): error en la asignación, la variable a asignar se encuentra fuera de alcance.");
                                                        else {
                                                            if (!sintactico.huboErrorInvocacion()) {
                                                                sintactico.agregarAnalisis("Se reconoció una operación de asignación. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(referencia));
                                                                sintactico.agregarAPolaca(val_peek(2).sval);
                                                            }
                                                            else
                                                                sintactico.addErrorSintactico("ERROR SEMÁNTICO (Línea " + (AnalizadorLexico.LINEA) + "): error en la asignación, se produjo error en la invocación a función.");
                                                            sintactico.setErrorInvocacion(false);
                                                        }
                                               }
break;
case 52:
//#line 196 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' luego de la asignación."); }
break;
case 53:
//#line 197 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el operador de asignación."); }
break;
case 54:
//#line 198 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' luego de la asignación."); }
break;
case 55:
//#line 201 "gramatica.y"
{
                                            sintactico.agregarAnalisis("Se reconoció una impresión por pantalla de una cadena. (Línea " + AnalizadorLexico.LINEA + ")");
                                            sintactico.setUsoTablaSimb(val_peek(2).ival, "CADENA DE CARACTERES");
                                            sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(2).ival));
                                            sintactico.agregarAPolaca("PRINT");
                                      }
break;
case 56:
//#line 207 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la impresión de cadena."); }
break;
case 57:
//#line 208 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): cierre erróneo de la lista de parámetros de PRINT."); }
break;
case 58:
//#line 209 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los parámetros de PRINT deben estar entre paréntesis."); }
break;
case 59:
//#line 210 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba PRINT, se encontró '('."); }
break;
case 60:
//#line 211 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta declarar una cadena para PRINT."); }
break;
case 61:
//#line 214 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una sentencia IF. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 62:
//#line 215 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una sentencia IF. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 63:
//#line 216 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición de IF debe estar entre paréntesis."); }
break;
case 64:
//#line 217 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros."); }
break;
case 65:
//#line 218 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta la declaración de THEN."); }
break;
case 66:
//#line 219 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta cierre de ENDIF."); }
break;
case 67:
//#line 220 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de ENDIF."); }
break;
case 68:
//#line 221 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta cierre de ENDIF."); }
break;
case 69:
//#line 222 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de ENDIF."); }
break;
case 70:
//#line 225 "gramatica.y"
{
                                        sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca() + 2));   /* Desapila dirección incompleta y completa el destino de BF*/
                                        sintactico.agregarAPolaca(" ");                               /* Crea paso incompleto*/
                                        sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);  /* Apila el nro de paso incompleto*/
                                        sintactico.agregarAPolaca("BI");                              /* Se crea el paso BI*/
                                    }
break;
case 71:
//#line 233 "gramatica.y"
{ sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca())); }
break;
case 72:
//#line 236 "gramatica.y"
{ sintactico.pushElementoPila(sintactico.getSizePolaca()); }
break;
case 73:
//#line 239 "gramatica.y"
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
case 74:
//#line 249 "gramatica.y"
{
                                                                                                sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca() + 2)); /* Desapila dirección incompleta y completa el destino de BF*/
                                                                                                sintactico.agregarAPolaca(String.valueOf(sintactico.popElementoPila()));    /* Desapilar paso de inicio*/
                                                                                                sintactico.agregarAPolaca("BI");
                                                                                            }
break;
case 75:
//#line 255 "gramatica.y"
{
                                                                                                sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca() + 2)); /* Desapila dirección incompleta y completa el destino de BF*/
                                                                                                sintactico.agregarAPolaca(String.valueOf(sintactico.popElementoPila()));    /* Desapilar paso de inicio*/
                                                                                                sintactico.agregarAPolaca("BI");
                                                                                            }
break;
case 80:
//#line 267 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los bloques TRY/CATCH no pueden anidarse"); }
break;
case 81:
//#line 270 "gramatica.y"
{
                                                                                    sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca() + 2)); /* Desapila dirección incompleta y completa el destino de BF*/
                                                                                    sintactico.agregarAPolaca(String.valueOf(sintactico.popElementoPila()));    /* Desapilar paso de inicio*/
                                                                                    sintactico.agregarAPolaca("BI"); /* Salto desde contrato*/
                                                                                }
break;
case 82:
//#line 275 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se leyó un BEGIN sin previo reconocimiento de CATCH."); }
break;
case 83:
//#line 278 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció un bloque TRY/CATCH. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 84:
//#line 279 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): el cuerpo de CATCH no se inicializó con BEGIN."); }
break;
case 85:
//#line 280 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): cuerpo de CATCH mal cerrado; falta END."); }
break;
case 86:
//#line 281 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
break;
case 92:
//#line 293 "gramatica.y"
{
                                                      sintactico.agregarAnalisis("Se reconoció una definición de contrato. (Línea " + AnalizadorLexico.LINEA + ")");
                                                      sintactico.agregarAPolaca(" "); /* COMPROBAR; agrego paso incompleto para hacer el salto al catch.*/
                                                      sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);
                                                      sintactico.agregarAPolaca("BF");
                                                 }
break;
case 93:
//#line 299 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición debe estar entre paréntesis."); }
break;
case 94:
//#line 300 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): CONTRACT debe tener al menos una condición como parámetro."); }
break;
case 95:
//#line 301 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el paréntesis de cierre para los parámetros de CONTRACT."); }
break;
case 96:
//#line 302 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de los parámetros de CONTRACT."); }
break;
case 97:
//#line 305 "gramatica.y"
{
                                    sintactico.agregarAnalisis("Se reconoció una sentencia de BREAK. (Línea " + AnalizadorLexico.LINEA + ")");
                                    sintactico.agregarAPolaca(" ");                               /* Crea paso incompleto*/
                                    sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);  /* Apila el nro de paso incompleto*/
                                    sintactico.agregarAPolaca("BI");                              /* Se crea el paso BI*/
                                    sintactico.setTieneSentBreak(true);
                               }
break;
case 98:
//#line 312 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de BREAK."); }
break;
case 99:
//#line 315 "gramatica.y"
{
                                sintactico.agregarAPolaca(" ");
                                sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);
                                sintactico.agregarAPolaca("BF");
                            }
break;
case 101:
//#line 323 "gramatica.y"
{ sintactico.agregarAPolaca(val_peek(1).sval); }
break;
case 103:
//#line 327 "gramatica.y"
{ sintactico.agregarAPolaca(val_peek(1).sval); }
break;
case 105:
//#line 331 "gramatica.y"
{ sintactico.agregarAPolaca(val_peek(1).sval); }
break;
case 106:
//#line 333 "gramatica.y"
{
                                                sintactico.agregarAnalisis("Se reconoció una suma. (Línea " + AnalizadorLexico.LINEA + ")");
                                                sintactico.agregarAPolaca("+");
                                            }
break;
case 107:
//#line 337 "gramatica.y"
{
                                                sintactico.agregarAnalisis("Se reconoció una resta. (Línea " + AnalizadorLexico.LINEA + ")");
                                                sintactico.agregarAPolaca("-");
                                            }
break;
case 109:
//#line 344 "gramatica.y"
{
                                                sintactico.agregarAnalisis("Se reconoció una multiplicación. (Línea " + AnalizadorLexico.LINEA + ")");
                                                sintactico.agregarAPolaca("*");
                                            }
break;
case 110:
//#line 348 "gramatica.y"
{
                                                sintactico.agregarAnalisis("Se reconoció una división. (Línea " + AnalizadorLexico.LINEA + ")");
                                                sintactico.agregarAPolaca("/");
                                            }
break;
case 112:
//#line 355 "gramatica.y"
{
                        int ref = sintactico.referenciaCorrecta(val_peek(0).ival);
                        if (ref == -1)
                            sintactico.addErrorSintactico("ERROR SEMÁNTICO (Línea " + (AnalizadorLexico.LINEA) + "): la variable no fue declarada o se encuentra fuera de alcance.");
                        else
                            if (sintactico.getUsoFromTS(ref).equals("VARIABLE"))
                                sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(ref));
                    }
break;
case 113:
//#line 363 "gramatica.y"
{
                        String tipo = sintactico.getTipoFromTS(val_peek(0).ival);
                        if (tipo.equals("LONG"))
                            sintactico.verificarRangoEnteroLargo(val_peek(0).ival);
                        sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(0).ival));
                    }
break;
case 114:
//#line 369 "gramatica.y"
{
                        sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(0).ival));
                        sintactico.setNegativoTablaSimb(val_peek(0).ival);
                        sintactico.agregarAPolaca("-");
                    }
break;
case 115:
//#line 374 "gramatica.y"
{
                                sintactico.setRefInvocacion(sintactico.referenciaCorrecta(val_peek(3).ival));
                                if (sintactico.getRefInvocacion() == -1) {
                                    sintactico.addErrorSintactico("ERROR SEMÁNTICO(Línea " + AnalizadorLexico.LINEA + "): Error en la invocación. Variable de invocación " + sintactico.getLexemaFromTS(val_peek(3).ival) + " no declarada.");
                                    sintactico.setErrorInvocacion(true);
                                }
                                else {
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
                            }
break;
case 116:
//#line 406 "gramatica.y"
{
                                sintactico.setRefInvocacion(sintactico.referenciaCorrecta(val_peek(3).ival));
                                if (sintactico.getRefInvocacion() == -1) {
                                    sintactico.addErrorSintactico("ERROR SEMÁNTICO(Línea " + AnalizadorLexico.LINEA + "): Error en la invocación. Variable de invocación " + sintactico.getLexemaFromTS(val_peek(3).ival) + " no declarada.");
                                    sintactico.setErrorInvocacion(true);
                                }
                                else {
                                    /* Comparo tipos de parámetro formal con real*/
                                    int refParamFormal = sintactico.getRefInvocacion() + 1;
                                    if (!sintactico.getTipoVariableFromTS(refParamFormal).equals(sintactico.getTipoFromTS(val_peek(1).ival))) {
                                        sintactico.addErrorSintactico("ERROR SEMÁNTICO(Línea " + AnalizadorLexico.LINEA + "): Error en la invocación. El tipo del parámetro real no coincide con el tipo del parámetro formal.");
                                        sintactico.setErrorInvocacion(true);
                                    }
                                    else {
                                        sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(1).ival));
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
break;
case 117:
//#line 433 "gramatica.y"
{ yyval.sval = new String("<"); }
break;
case 118:
//#line 434 "gramatica.y"
{ yyval.sval = new String(">"); }
break;
case 119:
//#line 435 "gramatica.y"
{ yyval.sval = new String("<="); }
break;
case 120:
//#line 436 "gramatica.y"
{ yyval.sval = new String(">="); }
break;
case 121:
//#line 437 "gramatica.y"
{ yyval.sval = new String("=="); }
break;
case 122:
//#line 438 "gramatica.y"
{ yyval.sval = new String("<>"); }
break;
case 123:
//#line 441 "gramatica.y"
{ yyval.sval = new String("&&"); }
break;
case 124:
//#line 444 "gramatica.y"
{ yyval.sval = new String("||"); }
break;
case 125:
//#line 447 "gramatica.y"
{
                    sintactico.setTipo("LONG");
                    yyval.sval = new String("LONG");
                }
break;
case 126:
//#line 451 "gramatica.y"
{
                    sintactico.setTipo("SINGLE");
                    yyval.sval = new String("SINGLE");
                }
break;
//#line 1301 "Parser.java"
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
