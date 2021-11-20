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
   14,   14,   16,   10,   10,   10,   10,   10,   10,   24,
   18,   18,   18,   18,   19,   19,   19,   19,   19,   19,
   20,   20,   20,   20,   20,   21,   21,   21,   21,   21,
   21,   21,   21,   21,   26,   27,   22,   22,   22,   22,
   22,   22,   22,   22,   28,   28,   28,   28,   28,   28,
   29,   29,   23,   23,   23,   23,    9,    9,    7,    7,
    7,   30,   30,   30,   30,   30,   31,   31,   25,   32,
   32,   33,   33,   35,   35,   17,   17,   17,   38,   38,
   38,   39,   39,   39,   37,   37,   37,   37,   37,   37,
   36,   34,   12,   12,
};
final static short yylen[] = {                            2,
    6,    4,    5,    5,    4,    6,    6,    1,    2,    4,
    3,    1,    4,    4,    4,    1,    2,    1,    2,    1,
    2,    1,    2,    1,    1,    3,    3,    2,    1,    3,
    4,    1,    3,    3,    6,    6,    6,    5,    6,    6,
   11,   10,   10,    9,    4,    3,    6,    5,    7,    6,
    8,    7,    9,    8,   10,    9,   11,   10,   11,   10,
   12,   11,    2,    1,    1,    1,    1,    1,    1,    1,
    4,    4,    3,    3,    5,    5,    5,    4,    3,    5,
    5,    5,    5,    5,    5,    8,   10,    3,    5,    6,
    8,    8,   10,   10,    1,    1,    9,    8,    6,    3,
    5,    6,    9,    9,    1,    1,    1,    1,    1,    2,
    3,    4,    5,    3,    5,    5,    1,    1,    1,    1,
    1,    6,    4,    5,    6,    6,    2,    2,    1,    1,
    3,    1,    3,    1,    3,    3,    3,    1,    3,    3,
    1,    1,    1,    2,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,  153,  154,    0,   22,    0,   29,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   20,   64,   65,   66,   67,   68,   69,    0,    0,
    0,   23,   28,    0,    0,    0,    0,    0,    0,    0,
   18,  117,  118,    0,    0,    0,   30,    0,  142,  143,
   70,    0,    0,    0,    0,    0,  141,    0,    0,    0,
    0,    0,    0,    0,    0,    2,    0,    0,  105,  106,
  107,  108,  109,    0,    0,    0,    0,   21,    0,    0,
    0,    0,    5,    0,    0,   27,   26,    0,    0,    0,
    0,    0,   46,   19,    0,    0,   31,   34,   33,    0,
    0,    0,  144,   73,    0,    0,   74,    0,    0,    0,
    0,   88,  152,    0,  151,    0,  147,  148,  149,  150,
  145,  146,    0,    0,    0,    0,    0,  100,  110,    0,
  111,   79,    3,    0,  114,    4,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   45,   63,
    0,    0,    0,    0,    0,    0,   72,   71,  139,  140,
    0,    0,    0,    0,    0,   78,    0,    0,    0,    0,
    0,  112,    0,    0,    7,    1,    6,    0,    0,    0,
   38,    0,    0,    0,   48,    0,    0,  123,    0,    0,
    0,   36,   84,   81,   85,   82,   83,   89,    0,    0,
    0,    0,   95,   25,   24,    0,   77,   76,   75,   80,
  101,    0,    0,  116,  113,  115,   37,   39,   40,   35,
    0,    0,    0,   50,  124,    0,    0,    0,    0,    0,
   47,    0,    0,    0,    8,    0,    9,   90,  102,    0,
    0,   99,  119,  120,  121,    0,   52,    0,    0,  125,
  126,  122,    0,    0,    0,   49,    0,    0,    0,   11,
    0,    0,    0,    0,    0,   16,  128,  127,    0,   54,
    0,    0,    0,   51,    0,    0,   91,   96,    0,   92,
   86,   15,   10,   14,   13,   98,    0,    0,   17,   44,
   56,    0,    0,    0,    0,   53,    0,    0,    0,    0,
  104,   97,  103,   60,   42,   58,    0,   43,   55,    0,
    0,    0,   93,   94,   87,   62,   59,   41,   57,    0,
   61,
};
final static short yydgoto[] = {                          2,
    7,   21,  201,  202,  203,  265,  242,   40,   41,   42,
  205,    9,   13,   10,   11,   96,   59,   23,   24,   25,
   26,   27,   28,   55,   60,  206,  279,   75,   29,   43,
  245,   61,   62,  114,   63,  116,  123,   56,   57,
};
final static short yysindex[] = {                      -240,
  -61,    0, -233,  450,    0,    0,  265,    0, -108,    0,
  141,   -1,  145,  -24,   13,  -33,    7,   50,  520, -204,
  475,    0,    0,    0,    0,    0,    0,    0,   10,  492,
  501,    0,    0,   33, -183,  158,  391,   25,  159,  386,
    0,    0,    0, -214,   26, -147,    0, -142,    0,    0,
    0,  102, -140,   35,  215,   78,    0,  229,   91, -129,
 -144, -141,  521, -110,  -22,    0,  229, -106,    0,    0,
    0,    0,    0, -102, -221,  -95,  103,    0,  520,  510,
  111,  409,    0, -214,  -20,    0,    0,  126,  342,   61,
  416,  427,    0,    0,  -83,  135,    0,    0,    0,  138,
  144,  -74,    0,    0,  229,  229,    0,   -8,  229,  229,
  -35,    0,    0,  229,    0,  229,    0,    0,    0,    0,
    0,    0,  229,  127,  -32,  -69,  -16,    0,    0,  -62,
    0,    0,    0,  457,    0,    0,  -49,  -55,  170,   23,
  -41,  229,   77,  -13,  122,    5,  173,  345,    0,    0,
   41,  -36,   12,  164,   78,   78,    0,    0,    0,    0,
   42,  176, -141,  521,   91,    0,  169,   32,  177,   65,
 -184,    0,   39,   88,    0,    0,    0,  143,  153,  -27,
    0,   51,  132,   76,    0,  161,   -5,    0,  229,   93,
  178,    0,    0,    0,    0,    0,    0,    0,  288,  309,
  327,    0,    0,    0,    0,  185,    0,    0,    0,    0,
    0,  187,  195,    0,    0,    0,    0,    0,    0,    0,
  299,  188,  110,    0,    0,  192,   57,  150,  139,   92,
    0,  214,  392,  225,    0,  394,    0,    0,    0,  242,
   69,    0,    0,    0,    0,  183,    0,  201,  -28,    0,
    0,    0,  401,  206,  147,    0,  410,  288,   81,    0,
  106,  221,  222,  420,  124,    0,    0,    0,  422,    0,
  227,  368,  218,    0,  235,   44,    0,    0, -149,    0,
    0,    0,    0,    0,    0,    0,  109,  237,    0,    0,
    0,  116,  244,  440,  439,    0,  246,  373,  444,  119,
    0,    0,    0,    0,    0,    0,  448,    0,    0,  168,
  252,  474,    0,    0,    0,    0,    0,    0,    0,  458,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  362,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  362,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -30,    0,    0,   40,    0,
   34,   71,   70,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    3,   27,    0,    0,    0,    0,
    0,    0,  101,   73,   64,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  107,    0,    0,    0,    0,    0,    0,    0,    0,
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
    0,
};
final static short yygindex[] = {                         0,
  507,   31,  319, -167,  -26,    0, -218,  -34,  -40,   -3,
    1,   60,  511,    0,    0,  -43,   29,  504,  506,  508,
  514,  518,  519,    0,  717,  331,    0,    0,    0,    0,
    0,    0,  412,    0,  423,    0,    0,   52,  112,
};
final static int YYTABLESIZE=862;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         94,
   22,    8,   89,   22,   92,  162,   65,   32,  168,  176,
  138,    8,  138,  220,  138,   52,    1,   78,  126,  140,
   53,  266,  194,   12,  171,   22,   22,   78,  138,  138,
  272,  138,  235,  237,  105,  227,  106,   31,   44,   32,
  139,  141,   54,  136,  130,  136,  289,  136,   94,   20,
  158,   94,   58,  226,    5,    6,  148,   53,  131,   80,
   82,  136,  136,  179,  136,   66,  237,  137,   76,  137,
  196,  137,   84,   85,  129,   22,   78,  105,   78,  106,
  134,  212,   90,  108,   97,  137,  137,  213,  137,   67,
  209,  221,  129,  105,   53,  106,  180,  215,  134,  134,
  145,  134,  298,   95,  135,   53,  299,   94,   98,  134,
  132,  130,  300,  133,   99,  252,  183,  103,  105,  109,
  106,   53,  135,  135,  110,  135,  112,  268,  132,  130,
   78,  133,  229,  105,  105,  106,  106,   53,  113,  281,
  115,  131,  102,   95,   95,  124,    8,   33,   34,  128,
  249,  165,  105,  129,  106,   35,  155,  156,  204,  131,
  132,  133,  186,   20,  283,  142,   53,  302,  248,  136,
  182,  184,  222,  150,  305,  151,   53,  315,  152,  254,
   20,  154,  288,   53,  153,  166,  169,  276,   48,  105,
  253,  106,  105,  172,  106,  204,  204,  204,   20,   95,
  177,   48,    3,   47,    4,  275,  175,    5,    6,  243,
  178,  223,  189,  244,  181,   20,   87,  228,  230,  193,
  159,  160,  197,  167,  161,  138,  318,  207,  219,  138,
  204,  278,   49,   50,   20,  210,  243,  294,  271,   64,
  244,  138,  185,  138,  138,  138,  138,  157,    5,    6,
  125,  138,  138,   94,  204,  170,   51,  255,  136,   53,
  188,  243,  136,  312,   20,  244,   14,  195,   15,   49,
   50,   94,   16,   53,  136,   79,  136,  136,  136,  136,
   18,   20,  137,  262,  136,  136,  137,  208,   19,  129,
  104,    5,    6,  129,  214,  134,  192,  198,  137,  134,
  137,  137,  137,  137,   20,  129,   49,   50,  137,  137,
  297,  134,  251,  134,  134,  134,  134,   49,   50,  135,
  211,  134,  134,  135,  267,  132,  130,   20,  133,  132,
  130,  224,  133,   49,   50,  135,  280,  135,  135,  135,
  135,  132,  130,  216,  133,  135,  135,  256,   20,   49,
   50,  132,  132,  130,  133,  133,  131,  246,  100,  101,
  131,  282,   12,    8,  301,    8,   20,   12,   12,    8,
    8,  304,  131,    8,  314,    8,    8,    8,   49,   50,
   14,   20,   15,  131,   20,    8,   16,    3,   49,   50,
  287,  241,    5,    6,   18,   49,   50,   14,  217,   15,
   45,   46,   19,   16,    3,   32,   37,   20,  218,    5,
    6,   18,   20,   86,   46,   14,  225,   15,   38,   19,
   32,   16,    3,  317,   91,   20,  293,    5,    6,   18,
   20,  311,   14,  231,   15,  199,   38,   19,   16,    3,
  238,  200,  239,  247,    5,    6,   18,  250,   20,  269,
  260,   14,  263,   15,   19,   20,  270,   16,    3,  273,
  240,  274,  241,    5,    6,   18,   20,  138,  277,  257,
  107,   49,   50,   19,  258,  259,  284,  285,  286,   20,
  290,   14,  291,   15,  295,   49,   50,   16,    3,   20,
  296,  261,  303,    5,    6,   18,   20,  308,   14,  306,
   15,  309,  313,   19,   16,    3,  316,  319,  264,  241,
    5,    6,   18,   20,   20,  174,  321,   39,  234,   36,
   19,   14,   69,   15,   70,  163,   71,   16,    3,  232,
   30,   20,   72,    5,    6,   18,   73,   74,  164,    0,
   20,    0,    0,   19,   14,    0,   15,    0,    0,   20,
   16,    3,    0,  200,    0,    0,    5,    6,   18,   20,
    0,    0,    0,    0,    0,   14,   19,   15,    0,    0,
    0,   16,    3,    0,    0,  233,    0,    5,    6,   18,
  121,    0,  122,   14,    0,   15,    0,   19,    0,   16,
    3,    0,    0,  236,    0,    5,    6,   18,   14,    0,
   15,   14,    0,   15,   16,   19,  143,   16,  144,  190,
    0,  191,   18,    0,    0,   18,    0,   32,   32,   38,
   19,    0,   38,   19,   14,    0,   15,    0,    0,   14,
   16,   15,    0,    0,  292,   16,    0,    0,   18,  310,
    0,   93,   14,   18,   15,   38,   19,   14,   16,   15,
   38,   19,    0,   16,    0,   88,   18,    0,    0,    0,
    0,   18,    0,   38,   19,   14,    0,   15,   38,   19,
    0,   16,   14,    0,   15,  137,    0,    0,   16,   18,
  147,    0,  149,   14,    0,   15,   18,   19,    0,   16,
    0,    0,    0,   38,   19,  307,   14,   18,   15,    0,
    0,    0,   16,    0,   38,   19,   14,    0,   15,    0,
   18,    0,   16,   14,    0,   15,   17,   38,   19,   16,
   18,    0,    0,  173,    0,    0,    0,   18,   19,  320,
   14,   14,   15,   15,   68,   19,   16,   16,    0,    0,
    0,   77,    0,    0,   18,   18,    0,    0,   14,    0,
   15,   38,   19,   19,   16,    0,   83,   14,   81,   15,
    0,    0,   18,   16,    0,  135,   14,    0,   15,    0,
   19,   18,   16,    0,  111,    0,   14,    0,   15,   19,
   18,    0,   16,  127,    0,    0,    0,    0,   19,    0,
   18,    0,    0,    0,  117,  118,  119,  120,   19,    0,
    0,    0,    0,    0,    0,    0,  146,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  187,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    4,    1,   37,    7,   39,   41,   40,    7,   41,   59,
   41,   11,   43,   41,   45,   40,  257,   21,   41,   40,
   45,  240,   59,  257,   41,   29,   30,   31,   59,   60,
   59,   62,  200,  201,   43,   41,   45,    7,   40,   39,
   84,   85,   14,   41,  266,   43,  265,   45,   89,   40,
   59,   92,   40,   59,  269,  270,   91,   45,  280,   29,
   30,   59,   60,   41,   62,   59,  234,   41,  273,   43,
   59,   45,   40,  257,   41,   79,   80,   43,   82,   45,
   41,  266,   58,   55,   59,   59,   60,  272,   62,   40,
   59,   41,   59,   43,   45,   45,  140,   59,   59,   60,
   40,   62,   59,   44,   41,   45,  256,  148,  256,   79,
   41,   41,  262,   41,  257,   59,   40,  258,   43,   42,
   45,   45,   59,   60,   47,   62,  256,   59,   59,   59,
  134,   59,   40,   43,   43,   45,   45,   45,  283,   59,
  282,   41,   41,   84,   85,  256,   40,  256,  257,  256,
   41,  123,   43,  256,   45,  264,  105,  106,  162,   59,
  256,   59,   41,   40,   59,   40,   45,   59,   59,   59,
  142,  143,   41,  257,   59,   41,   45,   59,   41,   41,
   40,  256,   59,   45,   41,   59,  256,   41,   44,   43,
   41,   45,   43,  256,   45,  199,  200,  201,   40,  140,
  256,   44,  264,   59,  266,   59,  256,  269,  270,  213,
   41,  183,   40,  213,  256,   40,   59,  189,  190,  256,
  109,  110,   59,  256,  260,  256,   59,   59,  256,  260,
  234,  258,  257,  258,   40,   59,  240,  272,  267,  273,
  240,  272,  256,  274,  275,  276,  277,  256,  269,  270,
  273,  282,  283,  294,  258,  272,  281,  229,  256,   45,
  256,  265,  260,  298,   40,  265,  257,  256,  259,  257,
  258,  312,  263,   45,  272,  266,  274,  275,  276,  277,
  271,   40,  256,   59,  282,  283,  260,  256,  279,  256,
  256,  269,  270,  260,  256,  256,  256,  256,  272,  260,
  274,  275,  276,  277,   40,  272,  257,  258,  282,  283,
  267,  272,  256,  274,  275,  276,  277,  257,  258,  256,
  256,  282,  283,  260,  256,  256,  256,   40,  256,  260,
  260,  256,  260,  257,  258,  272,  256,  274,  275,  276,
  277,  272,  272,  256,  272,  282,  283,  256,   40,  257,
  258,  282,  283,  283,  282,  283,  256,   59,  257,  258,
  260,  256,  256,  257,  256,  259,   40,  261,  262,  263,
  264,  256,  272,  267,  256,  269,  270,  271,  257,  258,
  257,   40,  259,  283,   40,  279,  263,  264,  257,  258,
  267,  268,  269,  270,  271,  257,  258,  257,  256,  259,
  256,  257,  279,  263,  264,   44,  266,   40,  256,  269,
  270,  271,   40,  256,  257,  257,  256,  259,  278,  279,
   59,  263,  264,  256,  266,   40,   59,  269,  270,  271,
   40,   59,  257,  256,  259,  260,  278,  279,  263,  264,
  256,  266,  256,  256,  269,  270,  271,  256,   40,  267,
   59,  257,   59,  259,  279,   40,  256,  263,  264,   59,
  266,  256,  268,  269,  270,  271,   40,   59,   59,  256,
  256,  257,  258,  279,  261,  262,  256,  256,   59,   40,
   59,  257,  256,  259,  267,  257,  258,  263,  264,   40,
  256,  267,  256,  269,  270,  271,   40,   59,  257,  256,
  259,  256,   59,  279,  263,  264,   59,  256,  267,  268,
  269,  270,  271,   40,   40,   59,   59,   11,  200,    9,
  279,  257,   19,  259,   19,  114,   19,  263,  264,  199,
  266,   40,   19,  269,  270,  271,   19,   19,  116,   -1,
   40,   -1,   -1,  279,  257,   -1,  259,   -1,   -1,   40,
  263,  264,   -1,  266,   -1,   -1,  269,  270,  271,   40,
   -1,   -1,   -1,   -1,   -1,  257,  279,  259,   -1,   -1,
   -1,  263,  264,   -1,   -1,  267,   -1,  269,  270,  271,
   60,   -1,   62,  257,   -1,  259,   -1,  279,   -1,  263,
  264,   -1,   -1,  267,   -1,  269,  270,  271,  257,   -1,
  259,  257,   -1,  259,  263,  279,  265,  263,  267,  265,
   -1,  267,  271,   -1,   -1,  271,   -1,  256,  257,  278,
  279,   -1,  278,  279,  257,   -1,  259,   -1,   -1,  257,
  263,  259,   -1,   -1,  267,  263,   -1,   -1,  271,  267,
   -1,  256,  257,  271,  259,  278,  279,  257,  263,  259,
  278,  279,   -1,  263,   -1,  265,  271,   -1,   -1,   -1,
   -1,  271,   -1,  278,  279,  257,   -1,  259,  278,  279,
   -1,  263,  257,   -1,  259,  267,   -1,   -1,  263,  271,
  265,   -1,  256,  257,   -1,  259,  271,  279,   -1,  263,
   -1,   -1,   -1,  278,  279,  256,  257,  271,  259,   -1,
   -1,   -1,  263,   -1,  278,  279,  257,   -1,  259,   -1,
  271,   -1,  263,  257,   -1,  259,  267,  278,  279,  263,
  271,   -1,   -1,  267,   -1,   -1,   -1,  271,  279,  256,
  257,  257,  259,  259,   18,  279,  263,  263,   -1,   -1,
   -1,  267,   -1,   -1,  271,  271,   -1,   -1,  257,   -1,
  259,  278,  279,  279,  263,   -1,  256,  257,  267,  259,
   -1,   -1,  271,  263,   -1,  256,  257,   -1,  259,   -1,
  279,  271,  263,   -1,   58,   -1,  257,   -1,  259,  279,
  271,   -1,  263,   67,   -1,   -1,   -1,   -1,  279,   -1,
  271,   -1,   -1,   -1,  274,  275,  276,  277,  279,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   90,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  145,
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

//#line 382 "gramatica.y"

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
//#line 669 "Parser.java"
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
{
                                                                                                    sintactico.agregarAnalisis("Se reconoció un programa. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                    sintactico.setUsoTablaSimb(val_peek(5).ival, "NOMBRE DE PROGRAMA");
                                                                                                }
break;
case 2:
//#line 22 "gramatica.y"
{
                                                                                                    sintactico.agregarAnalisis("Se reconoció un programa. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                    sintactico.setUsoTablaSimb(val_peek(3).ival, "NOMBRE DE PROGRAMA");
                                                                                                }
break;
case 3:
//#line 26 "gramatica.y"
{
                                                                                                    sintactico.agregarAnalisis("Se reconoció un programa. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                    sintactico.setUsoTablaSimb(val_peek(4).ival, "NOMBRE DE PROGRAMA");
                                                                                                }
break;
case 4:
//#line 30 "gramatica.y"
{
                                                                                                    sintactico.agregarAnalisis("Se reconoció un programa. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                    sintactico.setUsoTablaSimb(val_peek(4).ival, "NOMBRE DE PROGRAMA");
                                                                                                }
break;
case 5:
//#line 34 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta BEGIN al abrir el bloque de sentencias ejecutables."); }
break;
case 6:
//#line 35 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END al cerrar el bloque de sentencias ejecutables."); }
break;
case 7:
//#line 36 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del END en el bloque de sentencias ejecutables."); }
break;
case 13:
//#line 46 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta BEGIN al abrir el bloque."); }
break;
case 14:
//#line 47 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): bloque mal cerrado (falta 'END')."); }
break;
case 15:
//#line 48 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del bloque."); }
break;
case 26:
//#line 71 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de variable. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 27:
//#line 72 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' al final de la declaración de variable."); }
break;
case 28:
//#line 73 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' al final de la declaración de variable."); }
break;
case 30:
//#line 75 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de variable de tipo función. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 31:
//#line 76 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del bloque."); }
break;
case 32:
//#line 79 "gramatica.y"
{
                                                          sintactico.setAmbitoTablaSimb(val_peek(0).ival);
                                                          sintactico.setUsoTablaSimb(val_peek(0).ival, "VARIABLE");
                                                          if (!sintactico.variableFueDeclarada(val_peek(0).ival))
                                                                sintactico.setTipoVariableTablaSimb(val_peek(0).ival);
                                                          else
                                                                sintactico.addErrorSintactico("ERROR (Línea " + AnalizadorLexico.LINEA + "): Variable " + val_peek(0).ival + " ya declarada.");
                                                    }
break;
case 33:
//#line 87 "gramatica.y"
{
                                                          sintactico.setAmbitoTablaSimb(val_peek(0).ival);
                                                          sintactico.setUsoTablaSimb(val_peek(0).ival, "VARIABLE");
                                                          if (!sintactico.variableFueDeclarada(val_peek(0).ival))
                                                                sintactico.setTipoVariableTablaSimb(val_peek(0).ival);
                                                          else
                                                                sintactico.addErrorSintactico("ERROR (Línea " + AnalizadorLexico.LINEA + "): Variable " + val_peek(2).ival + " ya declarada.");
                                                    }
break;
case 34:
//#line 95 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta una ',' entre identificadores."); }
break;
case 35:
//#line 98 "gramatica.y"
{
                                                                                     sintactico.setUsoTablaSimb(val_peek(3).ival, "FUNC");
                                                                                     if (sintactico.variableFueDeclarada(val_peek(3).ival))
                                                                                         sintactico.setErrorFunc(true);
                                                                                     else {
                                                                                         sintactico.setAmbitoTablaSimb(val_peek(3).ival);
                                                                                         /* Agregar referencia parametro*/
                                                                                         sintactico.setAmbito(sintactico.getAmbito() + "@" + sintactico.getLexemaFromTS(val_peek(3).ival));
                                                                                }

                                                    }
break;
case 36:
//#line 109 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el tipo de la función."); }
break;
case 37:
//#line 110 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta FUNC en la definición de la función."); }
break;
case 38:
//#line 111 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los parámetros deben ser colocados entre paréntesis."); }
break;
case 39:
//#line 112 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la función debe tener al menos un parámetro."); }
break;
case 40:
//#line 113 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros."); }
break;
case 41:
//#line 116 "gramatica.y"
{
                                                                                                                                                            if (!sintactico.huboErrorFunc()) {
                                                                                                                                                                sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                                                                                sintactico.setAmbito(sintactico.getAmbito().substring(0, sintactico.getAmbito().lastIndexOf("@"));
                                                                                                                                                            }
                                                                                                                                                            else
                                                                                                                                                                sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se produjo un error al declarar la función.");
                                                                                                                                                       }
break;
case 42:
//#line 124 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 43:
//#line 125 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 44:
//#line 126 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 45:
//#line 127 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el BEGIN antes del bloque de sentencias ejecutables."); }
break;
case 46:
//#line 128 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el BEGIN antes del bloque de sentencias ejecutables."); }
break;
case 47:
//#line 129 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el RETURN de la función."); }
break;
case 48:
//#line 130 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el RETURN de la función."); }
break;
case 49:
//#line 131 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la expresión de RETURN debe estar entre paréntesis."); }
break;
case 50:
//#line 132 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la expresión de RETURN debe estar entre paréntesis."); }
break;
case 51:
//#line 133 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): RETURN debe tener al menos una expresión como parámetro."); }
break;
case 52:
//#line 134 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): RETURN debe tener al menos una expresión como parámetro."); }
break;
case 53:
//#line 135 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en los parámetros de RETURN."); }
break;
case 54:
//#line 136 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en los parámetros de RETURN."); }
break;
case 55:
//#line 137 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de RETURN (expresión)."); }
break;
case 56:
//#line 138 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de RETURN (expresión)."); }
break;
case 57:
//#line 139 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END luego de ';'."); }
break;
case 58:
//#line 140 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END luego de ';'."); }
break;
case 59:
//#line 141 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
break;
case 60:
//#line 142 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
break;
case 61:
//#line 143 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): no se permiten sentencias luego del RETURN en la función."); }
break;
case 62:
//#line 144 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): no se permiten sentencias luego del RETURN en la función."); }
break;
case 63:
//#line 147 "gramatica.y"
{ sintactico.setUsoTablaSimb(val_peek(0).ival, "NOMBRE DE PARÁMETRO"); }
break;
case 70:
//#line 158 "gramatica.y"
{ yyval.sval = new String(":="); }
break;
case 71:
//#line 161 "gramatica.y"
{    if (!sintactico.variableFueDeclarada(val_peek(3).ival))
                                                        sintactico.addErrorSintactico("ERROR (Línea " + AnalizadorLexico.LINEA + "): Variable " + val_peek(3).ival + " no declarada.");
                                                    else {
                                                        int referencia = sintactico.referenciaCorrecta(val_peek(3).ival);
                                                        if (referencia == -1)
                                                            sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA) + "): error en la asignación, la variable se encuentra fuera de alcance.");
                                                        else {
                                                            sintactico.agregarAnalisis("Se reconoció una operación de asignación. (Línea " + AnalizadorLexico.LINEA + ")");
                                                            sintactico.agregarAPolaca(sintactico.getLexemaFromTS(referencia));
                                                            sintactico.agregarAPolaca(val_peek(2).sval);
                                                        }
                                                    }
                                              }
break;
case 72:
//#line 174 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' luego de la asignación."); }
break;
case 73:
//#line 175 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el operador de asignación."); }
break;
case 74:
//#line 176 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' luego de la asignación."); }
break;
case 75:
//#line 179 "gramatica.y"
{
                                            sintactico.agregarAnalisis("Se reconoció una impresión por pantalla de una cadena. (Línea " + AnalizadorLexico.LINEA + ")");
                                            sintactico.setUsoTablaSimb(val_peek(2).ival, "CADENA DE CARACTERES");
                                            sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(2).ival));
                                            sintactico.agregarAPolaca("OUT"); /*/////////// COMPROBAR //////////////*/
                                      }
break;
case 76:
//#line 185 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la impresión de cadena."); }
break;
case 77:
//#line 186 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): cierre erróneo de la lista de parámetros de PRINT."); }
break;
case 78:
//#line 187 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los parámetros de PRINT deben estar entre paréntesis."); }
break;
case 79:
//#line 188 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba PRINT, se encontró '('."); }
break;
case 80:
//#line 189 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta declarar una cadena para PRINT."); }
break;
case 81:
//#line 192 "gramatica.y"
{
                                            sintactico.agregarAnalisis("Se reconoció una invocación a una función (Línea " + AnalizadorLexico.LINEA + ")");
                                            if (!sintactico.variableFueDeclarada(val_peek(4).ival))
                                                sintactico.addErrorSintactico("ERROR (Línea " + AnalizadorLexico.LINEA + "): Variable " + val_peek(4).ival + " no declarada.");
                                        }
break;
case 82:
//#line 197 "gramatica.y"
{
                                            sintactico.agregarAnalisis("Se reconoció una invocación a una función (Línea " + AnalizadorLexico.LINEA + ")");
                                            if (!sintactico.variableFueDeclarada(val_peek(4).ival))
                                                sintactico.addErrorSintactico("ERROR (Línea " + AnalizadorLexico.LINEA + "): Variable " + val_peek(4).ival + " no declarada.");
                                        }
break;
case 83:
//#line 202 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta especificar un parámetro al invocar a la función."); }
break;
case 84:
//#line 203 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la invocación a la función."); }
break;
case 85:
//#line 204 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la invocación a la función."); }
break;
case 86:
//#line 207 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una sentencia IF. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 87:
//#line 208 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una sentencia IF. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 88:
//#line 209 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición de IF debe estar entre paréntesis."); }
break;
case 89:
//#line 210 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros."); }
break;
case 90:
//#line 211 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta la declaración de THEN."); }
break;
case 91:
//#line 212 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta cierre de ENDIF."); }
break;
case 92:
//#line 213 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de ENDIF."); }
break;
case 93:
//#line 214 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta cierre de ENDIF."); }
break;
case 94:
//#line 215 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de ENDIF."); }
break;
case 95:
//#line 218 "gramatica.y"
{
                                        sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + (sintactico.getSizePolaca() + 2) + "]");   /* Desapila dirección incompleta y completa el destino de BF*/
                                        sintactico.agregarAPolaca(" ");                               /* Crea paso incompleto*/
                                        sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);  /* Apila el nro de paso incompleto*/
                                        sintactico.agregarAPolaca("BI");                              /* Se crea el paso BI*/
                                    }
break;
case 96:
//#line 226 "gramatica.y"
{ sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + sintactico.getSizePolaca() + "]"); }
break;
case 97:
//#line 229 "gramatica.y"
{
                                                                                            sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                            sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + (sintactico.getSizePolaca() + 2) + "]"); /* Desapila dirección incompleta y completa el destino de BF*/
                                                                                            sintactico.agregarAPolaca("[" + sintactico.popElementoPila() + "]");    /* Desapilar paso de inicio*/
                                                                                            sintactico.agregarAPolaca("BI");
                                                                                       }
break;
case 98:
//#line 235 "gramatica.y"
{
                                                                                            sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                            sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + (sintactico.getSizePolaca() + 2) + "]"); /* Desapila dirección incompleta y completa el destino de BF*/
                                                                                            sintactico.agregarAPolaca("[" + sintactico.popElementoPila() + "]");    /* Desapilar paso de inicio*/
                                                                                            sintactico.agregarAPolaca("BI");
                                                                                       }
break;
case 99:
//#line 241 "gramatica.y"
{
                                                                                            sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                            sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + (sintactico.getSizePolaca() + 2) + "]"); /* Desapila dirección incompleta y completa el destino de BF*/
                                                                                            sintactico.agregarAPolaca("[" + sintactico.popElementoPila() + "]");    /* Desapilar paso de inicio*/
                                                                                            sintactico.agregarAPolaca("BI");
                                                                                       }
break;
case 100:
//#line 247 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición de WHILE debe estar entre paréntesis."); }
break;
case 101:
//#line 248 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros."); }
break;
case 102:
//#line 249 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba DO, se leyó BEGIN."); }
break;
case 103:
//#line 250 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba END, se leyó ';'."); }
break;
case 104:
//#line 251 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
break;
case 110:
//#line 259 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los bloques TRY/CATCH no pueden anidarse"); }
break;
case 111:
//#line 262 "gramatica.y"
{
                                                                                    sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + (sintactico.getSizePolaca() + 2) + "]"); /* Desapila dirección incompleta y completa el destino de BF*/
                                                                                    sintactico.agregarAPolaca("[" + sintactico.popElementoPila() + "]");    /* Desapilar paso de inicio*/
                                                                                    sintactico.agregarAPolaca("BI"); /* Salto desde contrato*/
                                                                                }
break;
case 112:
//#line 267 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se leyó un BEGIN sin previo reconocimiento de CATCH."); }
break;
case 113:
//#line 270 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció un bloque TRY/CATCH. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 114:
//#line 271 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): el cuerpo de CATCH no se inicializó con BEGIN."); }
break;
case 115:
//#line 272 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): cuerpo de CATCH mal cerrado; falta END."); }
break;
case 116:
//#line 273 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
break;
case 122:
//#line 285 "gramatica.y"
{
                                                      sintactico.agregarAnalisis("Se reconoció una definición de contrato. (Línea " + AnalizadorLexico.LINEA + ")");
                                                      sintactico.agregarAPolaca(" "); /* COMPROBAR; agrego paso incompleto para hacer el salto al catch.*/
                                                      sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);
                                                      sintactico.agregarAPolaca("BF");
                                                 }
break;
case 123:
//#line 291 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición debe estar entre paréntesis."); }
break;
case 124:
//#line 292 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): CONTRACT debe tener al menos una condición como parámetro."); }
break;
case 125:
//#line 293 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el paréntesis de cierre para los parámetros de CONTRACT."); }
break;
case 126:
//#line 294 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de los parámetros de CONTRACT."); }
break;
case 127:
//#line 297 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una sentencia de BREAK. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 128:
//#line 298 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de BREAK."); }
break;
case 129:
//#line 301 "gramatica.y"
{   sintactico.agregarAPolaca(" ");
                                sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);
                                sintactico.agregarAPolaca("BF");
                            }
break;
case 131:
//#line 308 "gramatica.y"
{ sintactico.agregarAPolaca(val_peek(1).sval); }
break;
case 133:
//#line 312 "gramatica.y"
{ sintactico.agregarAPolaca(val_peek(1).sval); }
break;
case 135:
//#line 316 "gramatica.y"
{ sintactico.agregarAPolaca(val_peek(1).sval); }
break;
case 136:
//#line 318 "gramatica.y"
{
                                                sintactico.agregarAnalisis("Se reconoció una suma. (Línea " + AnalizadorLexico.LINEA + ")");
                                                sintactico.agregarAPolaca("+");
                                            }
break;
case 137:
//#line 322 "gramatica.y"
{
                                                sintactico.agregarAnalisis("Se reconoció una resta. (Línea " + AnalizadorLexico.LINEA + ")");
                                                sintactico.agregarAPolaca("-");
                                            }
break;
case 139:
//#line 329 "gramatica.y"
{
                                                sintactico.agregarAnalisis("Se reconoció una multiplicación. (Línea " + AnalizadorLexico.LINEA + ")");
                                                sintactico.agregarAPolaca("*");
                                            }
break;
case 140:
//#line 333 "gramatica.y"
{
                                                sintactico.agregarAnalisis("Se reconoció una división. (Línea " + AnalizadorLexico.LINEA + ")");
                                                sintactico.agregarAPolaca("/");
                                            }
break;
case 142:
//#line 340 "gramatica.y"
{
                        if (sintactico.getUsoFromTS(val_peek(0).ival).equals("VARIABLE"))
                            sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(0).ival));
                    }
break;
case 143:
//#line 344 "gramatica.y"
{
                        String tipo = sintactico.getTipoFromTS(val_peek(0).ival);
                        if (tipo.equals("LONG"))
                            sintactico.verificarRangoEnteroLargo(val_peek(0).ival);
                        sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(0).ival));
                    }
break;
case 144:
//#line 350 "gramatica.y"
{
                        sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(0).ival));
                        sintactico.setNegativoTablaSimb(val_peek(0).ival);
                        sintactico.agregarAPolaca("-");
                    }
break;
case 145:
//#line 357 "gramatica.y"
{ yyval.sval = new String("<"); }
break;
case 146:
//#line 358 "gramatica.y"
{ yyval.sval = new String(">"); }
break;
case 147:
//#line 359 "gramatica.y"
{ yyval.sval = new String("<="); }
break;
case 148:
//#line 360 "gramatica.y"
{ yyval.sval = new String(">="); }
break;
case 149:
//#line 361 "gramatica.y"
{ yyval.sval = new String("=="); }
break;
case 150:
//#line 362 "gramatica.y"
{ yyval.sval = new String("<>"); }
break;
case 151:
//#line 365 "gramatica.y"
{ yyval.sval = new String("&&"); }
break;
case 152:
//#line 368 "gramatica.y"
{ yyval.sval = new String("||"); }
break;
case 153:
//#line 371 "gramatica.y"
{
                    sintactico.setTipo("LONG");
                    yyval.sval = new String("LONG");
                }
break;
case 154:
//#line 375 "gramatica.y"
{
                    sintactico.setTipo("SINGLE");
                    yyval.sval = new String("SINGLE");
                }
break;
//#line 1416 "Parser.java"
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
