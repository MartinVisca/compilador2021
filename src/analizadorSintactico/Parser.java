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
   24,   25,   21,   21,   21,   21,   21,   21,   21,   26,
   26,   26,   26,   26,   26,   22,   22,   22,   22,   22,
    7,    7,    5,    5,    5,   27,   27,   27,   27,   27,
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
    5,    5,    4,    3,    3,    4,    5,    4,    4,    5,
    5,    8,   10,    3,    5,    6,    8,    8,   10,   10,
    1,    1,    9,    6,    3,    5,    6,    9,    9,    1,
    1,    1,    1,    1,    2,    7,    4,    5,    7,    7,
    1,    1,    1,    1,    1,    6,    4,    5,    6,    6,
    2,    2,    1,    3,    3,    3,    1,    3,    3,    1,
    1,    1,    2,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,  132,  133,    0,    0,    0,    0,
    0,    2,   16,   15,    0,   20,    0,   51,   52,   53,
   54,   55,   56,    0,  122,    0,    0,    0,    0,    0,
  120,  121,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   90,   91,   92,   93,   94,    0,    0,    0,    3,
   19,    0,    0,    0,    0,    0,    0,   11,  101,    0,
  102,    0,   60,    0,    0,    0,  123,   59,    0,    0,
    0,    0,    0,   74,  126,  127,  128,  129,  130,  131,
  124,  125,    0,   64,    0,    0,    0,   22,    0,   21,
    0,    0,   85,   95,    0,    0,   65,    0,    0,   18,
   17,    0,    0,   35,   12,    0,    0,   68,   58,   57,
    0,    0,   69,    0,    0,  118,  119,    0,    0,    0,
   63,    0,   66,    0,    0,   25,   24,    0,    0,   97,
    0,    0,   13,    0,    0,    0,    0,    0,    0,    0,
    0,   34,   70,   71,   67,   75,    0,    0,    0,    0,
   81,    0,   62,   61,   50,    0,   86,    0,    0,    0,
   98,   14,    0,    0,    0,   29,    0,    0,   37,    0,
    0,  107,    0,    0,    0,    0,    0,   76,   27,   87,
    0,    0,   84,  103,  104,  105,    0,    0,   28,   30,
   31,   26,    0,    0,   39,  108,    0,    0,    0,    0,
   36,    0,    0,    0,    0,    0,    0,    0,    9,  112,
  111,  100,   96,   99,   41,    0,    0,  109,  110,  106,
    0,    0,   38,   82,    0,   78,   72,   77,    8,    4,
    7,    6,    0,    0,   10,   43,    0,    0,   40,    0,
    0,    0,    0,   89,   83,   88,   45,    0,    0,   42,
    0,    0,   80,   73,   79,   49,   33,   47,   44,    0,
    0,   48,   32,   46,
};
final static short yydgoto[] = {                         10,
  149,   50,  151,  208,  183,   57,   58,  132,   13,   14,
   15,   39,   16,   17,  125,   34,   18,   19,   20,   21,
   22,   23,   35,  152,  225,   48,   61,  186,   83,   30,
   31,
};
final static short yysindex[] = {                       247,
  -31,  -37,  -35, -241,    0,    0,   19,  373, -237,    0,
  247,    0,    0,    0, -112,    0,  107,    0,    0,    0,
    0,    0,    0,    1,    0,  -33,  -17, -210,   42,   23,
    0,    0,  110,   53,  397, -189,  -34,   38,   94,  110,
  409,    0,    0,    0,    0,    0, -172, -214, -167,    0,
    0,   57, -147,   99,  173,   79,  291,    0,    0,  301,
    0, -132,    0,   -5,   -6, -129,    0,    0,  110,  110,
  110,  110,   20,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  110,    0,  -13, -125, -197,    0, -122,    0,
 -111,   45,    0,    0, -108,  349,    0, -197,    5,    0,
    0,  264,   43,    0,    0,  173,  325,    0,    0,    0,
 -102,  -48,    0,   23,   23,    0,    0,  -96,  124,   53,
    0,   50,    0,  -94,  120,    0,    0,  -90, -243,    0,
  373,  366,    0,  126,  -40,  -88,   54,  -87,    6,  413,
  273,    0,    0,    0,    0,    0,  176,  247,  197,    0,
    0,  -85,    0,    0,    0,  -83,    0,  -82,  141,  342,
    0,    0,  -79,  -78,  -11,    0,   81,   58,    0,  -77,
  -16,    0,   66,  -76,  113,  158,  128,    0,    0,    0,
  222,   77,    0,    0,    0,    0,   92,  -73,    0,    0,
    0,    0,  -71,   59,    0,    0,  -68,  116,   88,   69,
    0,  176,  123,  -67,  135,  -66,  -65,   90,    0,    0,
    0,    0,    0,    0,    0,  -64,  -53,    0,    0,    0,
  -63,   76,    0,    0,   56,    0,    0,    0,    0,    0,
    0,    0,  136,  -60,    0,    0,  -59,  -49,    0,  -56,
  -10,  140,  -55,    0,    0,    0,    0,  143,  -52,    0,
  -50,   32,    0,    0,    0,    0,    0,    0,    0,  146,
  -47,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  203,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   34,    0,    0,    0,    0,    0,  -41,
    0,    0,    0,    9,    0,    0,    0,  106,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  106,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -28,   -4,    0,    0,    0,    0,   33,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   73,
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
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   22,  438,    8,    0, -148,    2,   18,  122,  601,    3,
   41,  192,    0,    0,  -72,  482,  204,  274,  280,  318,
  372,  389,   83,  261,    0,    0,    0,    0,    0,   72,
   85,
};
final static int YYTABLESIZE=809;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        117,
  164,  117,   33,  117,   37,  238,   86,   28,   27,  249,
  145,   28,  115,   28,  115,   38,  115,  117,  117,   60,
  117,   11,  158,   66,  198,  134,  136,  122,  159,  192,
  115,  115,  209,  115,  112,   49,  116,   69,  116,   70,
  116,   62,  197,   81,  135,   82,  170,   67,  252,  113,
   28,   95,  111,  110,  116,  116,  102,  116,   40,  235,
  119,  107,  165,   28,   71,   96,   84,  113,  113,   72,
  113,    5,    6,  114,  105,  121,  121,   87,  121,   81,
  121,   82,  139,   94,   69,  129,   70,   28,   97,   41,
  261,  114,  114,  167,  114,   69,   98,   70,   28,  217,
   69,   69,   70,   70,   81,  199,   82,  141,  154,   99,
   28,   69,    2,   70,  243,   73,  241,  216,   69,  105,
   70,  193,   92,  108,  105,   28,  113,  124,  221,    9,
  123,    5,   28,  126,  240,  211,  103,   91,  124,  124,
  114,  115,   91,   51,   52,  127,    9,  130,  234,   23,
  213,   53,   90,  143,   28,  116,  117,  101,  105,  146,
  156,  185,  155,    9,   23,  157,  163,  166,  169,  176,
  178,  204,  179,  180,  220,  124,  189,  190,  196,  201,
    9,  227,  214,  185,  215,  140,  207,  218,  228,  231,
  232,  236,  239,  230,  245,  246,  247,    9,  254,  250,
  255,  257,    1,  258,  263,  259,   54,  144,  264,  224,
  185,   42,    9,  237,  117,    9,  206,  248,  117,   32,
   25,  171,   63,   32,   25,   24,   25,  115,    5,    6,
  117,  115,  117,  117,  117,  117,    9,   36,   85,   65,
  117,  117,  121,  115,  191,  115,  115,  115,  115,   26,
  109,  116,  160,  115,  115,  116,  251,   75,   76,   77,
   78,    9,   32,   25,  113,   79,   80,  116,  113,  116,
  116,  116,  116,    5,    6,   32,   25,  116,  116,  118,
  113,   43,  113,  113,  113,  113,    9,   44,  114,  121,
  113,  113,  114,   75,   76,   77,   78,   68,  260,   32,
   25,   79,   80,    9,  114,  153,  114,  114,  114,  114,
   32,   25,    9,  195,  114,  114,  128,  242,   75,   76,
   77,   78,   32,   25,  223,   45,   79,   80,    5,    2,
    9,    2,  210,    5,    5,    2,    2,   32,   25,    2,
    9,    2,    2,    2,   32,   25,    1,  212,    2,   88,
   89,    2,    3,    4,  100,   89,  233,  182,    5,    6,
    7,   23,   23,    1,    9,    2,   32,   25,    8,    3,
    4,  219,   55,  202,  203,    5,    6,    7,  226,   46,
    1,    9,    2,  147,   56,    8,    3,    4,    9,  148,
  229,  244,    5,    6,    7,  253,   47,    1,  256,    2,
  188,  262,    8,    3,    4,    9,  181,  175,  182,    5,
    6,    7,    9,    0,    1,    0,    2,    0,    0,    8,
    3,    4,    0,    0,  205,    0,    5,    6,    7,    1,
    0,    2,    1,    0,    2,    3,    8,   12,    3,    4,
    0,  148,    0,    7,    5,    6,    7,    0,    0,    0,
   56,    8,    0,    1,    8,    2,   81,    0,   82,    3,
    4,    0,    0,  177,    0,    5,    6,    7,   81,    0,
   82,    0,   81,    0,   82,    8,    0,    0,    1,    0,
    2,    0,   29,    0,    3,    4,    0,    0,    0,  182,
    5,    6,    7,    0,    0,    0,    0,    0,    0,    0,
    8,    0,    0,    1,    0,    2,    0,   64,    0,    3,
    4,    0,    0,    0,    0,    5,    6,    7,    0,    0,
    1,    0,    2,    0,    0,    8,    3,    0,  137,    1,
  138,    2,    0,    0,    7,    3,    0,  173,    0,  174,
    0,   56,    8,    7,    0,    0,  104,    1,    0,    2,
   56,    8,    0,    3,    0,    0,  150,    1,    0,    2,
    0,    7,    0,    3,  120,    0,  106,    0,   56,    8,
    0,    7,    0,    0,    0,    0,    0,    0,   56,    8,
  142,    1,    0,    2,  150,   12,    0,    3,    0,    0,
    0,    0,    0,    0,    0,    7,    0,    0,    1,    0,
    2,    0,   56,    8,    3,    1,    0,    2,  187,    0,
    0,    3,    7,    0,  131,    0,    0,   59,  168,    7,
    8,  161,    1,    0,    2,    0,    0,    8,    3,    1,
    0,    2,    0,    0,    0,    3,    7,    0,    0,  150,
    0,    0,    0,    7,    8,    0,    0,    0,  194,    0,
    0,    8,   74,    0,  200,   59,    0,   59,    0,    0,
   59,    0,    0,    0,   93,    0,    0,    0,  172,    0,
   75,   76,   77,   78,    0,    0,    0,    0,   79,   80,
  222,    0,   75,   76,   77,   78,   75,   76,   77,   78,
   79,   80,    0,    0,   79,   80,  133,    0,    0,    0,
    0,    0,   59,    0,    0,    0,   59,   59,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  133,  162,    0,    0,    0,    0,    0,    0,    0,
    0,   59,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  184,
  162,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  184,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  184,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   43,   40,   45,   40,   59,   41,   45,   40,   59,
   59,   45,   41,   45,   43,  257,   45,   59,   60,   17,
   62,    0,  266,   41,   41,   98,   99,   41,  272,   41,
   59,   60,  181,   62,   41,  273,   41,   43,   43,   45,
   45,   41,   59,   60,   40,   62,   41,  258,   59,   41,
   45,  266,   59,   59,   59,   60,   55,   62,   40,  208,
   41,   60,  135,   45,   42,  280,  256,   59,   60,   47,
   62,  269,  270,   41,   57,   42,   43,   40,   45,   60,
   47,   62,   40,  256,   43,   41,   45,   45,  256,    7,
   59,   59,   60,   40,   62,   43,   40,   45,   45,   41,
   43,   43,   45,   45,   60,   40,   62,  106,   59,  257,
   45,   43,   40,   45,   59,   33,   41,   59,   43,  102,
   45,   41,   40,  256,  107,   45,  256,   87,   41,   40,
  256,   59,   45,  256,   59,   59,   58,   44,   98,   99,
   69,   70,   44,  256,  257,  257,   40,  256,   59,   44,
   59,  264,   59,  256,   45,   71,   72,   59,  141,  256,
   41,  159,  257,   40,   59,  256,   41,  256,  256,  148,
  256,   59,  256,  256,   59,  135,  256,  256,  256,  256,
   40,   59,  256,  181,  256,  103,   59,  256,  256,  256,
  256,  256,  256,   59,   59,  256,  256,   40,   59,  256,
  256,   59,    0,  256,   59,  256,   15,  256,  256,  202,
  208,    8,   40,  267,  256,   40,   59,  267,  260,  257,
  258,  139,  256,  257,  258,  257,  258,  256,  269,  270,
  272,  260,  274,  275,  276,  277,   40,  273,  273,  257,
  282,  283,  256,  272,  256,  274,  275,  276,  277,  281,
  256,  256,  131,  282,  283,  260,  267,  274,  275,  276,
  277,   40,  257,  258,  256,  282,  283,  272,  260,  274,
  275,  276,  277,  269,  270,  257,  258,  282,  283,  260,
  272,    8,  274,  275,  276,  277,   40,    8,  256,  256,
  282,  283,  260,  274,  275,  276,  277,  256,  267,  257,
  258,  282,  283,   40,  272,  256,  274,  275,  276,  277,
  257,  258,   40,  256,  282,  283,  272,  262,  274,  275,
  276,  277,  257,  258,  256,    8,  282,  283,  256,  257,
   40,  259,  256,  261,  262,  263,  264,  257,  258,  267,
   40,  269,  270,  271,  257,  258,  257,  256,  259,  256,
  257,  279,  263,  264,  256,  257,  267,  268,  269,  270,
  271,  256,  257,  257,   40,  259,  257,  258,  279,  263,
  264,  256,  266,  261,  262,  269,  270,  271,  256,    8,
  257,   40,  259,  260,  278,  279,  263,  264,   40,  266,
  256,  256,  269,  270,  271,  256,    8,  257,  256,  259,
   59,  256,  279,  263,  264,   40,  266,  147,  268,  269,
  270,  271,   40,   -1,  257,   -1,  259,   -1,   -1,  279,
  263,  264,   -1,   -1,  267,   -1,  269,  270,  271,  257,
   -1,  259,  257,   -1,  259,  263,  279,    0,  263,  264,
   -1,  266,   -1,  271,  269,  270,  271,   -1,   -1,   -1,
  278,  279,   -1,  257,  279,  259,   60,   -1,   62,  263,
  264,   -1,   -1,  267,   -1,  269,  270,  271,   60,   -1,
   62,   -1,   60,   -1,   62,  279,   -1,   -1,  257,   -1,
  259,   -1,    1,   -1,  263,  264,   -1,   -1,   -1,  268,
  269,  270,  271,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  279,   -1,   -1,  257,   -1,  259,   -1,   26,   -1,  263,
  264,   -1,   -1,   -1,   -1,  269,  270,  271,   -1,   -1,
  257,   -1,  259,   -1,   -1,  279,  263,   -1,  265,  257,
  267,  259,   -1,   -1,  271,  263,   -1,  265,   -1,  267,
   -1,  278,  279,  271,   -1,   -1,  256,  257,   -1,  259,
  278,  279,   -1,  263,   -1,   -1,  119,  257,   -1,  259,
   -1,  271,   -1,  263,   83,   -1,  266,   -1,  278,  279,
   -1,  271,   -1,   -1,   -1,   -1,   -1,   -1,  278,  279,
  256,  257,   -1,  259,  147,  148,   -1,  263,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  271,   -1,   -1,  257,   -1,
  259,   -1,  278,  279,  263,  257,   -1,  259,  267,   -1,
   -1,  263,  271,   -1,  266,   -1,   -1,   17,  137,  271,
  279,  256,  257,   -1,  259,   -1,   -1,  279,  263,  257,
   -1,  259,   -1,   -1,   -1,  263,  271,   -1,   -1,  202,
   -1,   -1,   -1,  271,  279,   -1,   -1,   -1,  167,   -1,
   -1,  279,  256,   -1,  173,   55,   -1,   57,   -1,   -1,
   60,   -1,   -1,   -1,  256,   -1,   -1,   -1,  256,   -1,
  274,  275,  276,  277,   -1,   -1,   -1,   -1,  282,  283,
  199,   -1,  274,  275,  276,  277,  274,  275,  276,  277,
  282,  283,   -1,   -1,  282,  283,   96,   -1,   -1,   -1,
   -1,   -1,  102,   -1,   -1,   -1,  106,  107,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  131,  132,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  141,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  159,
  160,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  181,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  208,
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
"invocacion_func : ID '(' ID ')' ';'",
"invocacion_func : ID ID ')' error",
"invocacion_func : ID '(' ')' error",
"invocacion_func : ID '(' ID ';' error",
"invocacion_func : ID '(' ID ')' error",
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

//#line 220 "gramatica.y"

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
//#line 614 "Parser.java"
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
case 6:
//#line 27 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta BEGIN al abrir el bloque."); }
break;
case 7:
//#line 28 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): bloque mal cerrado (falta 'END')."); }
break;
case 8:
//#line 29 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del bloque."); }
break;
case 17:
//#line 48 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de variable. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 18:
//#line 49 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' al final de la declaración de variable."); }
break;
case 19:
//#line 50 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta declarar el/los identificador/es."); }
break;
case 21:
//#line 52 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de variable de tipo función. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 22:
//#line 53 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del bloque."); }
break;
case 25:
//#line 58 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta una ',' entre identificadores."); }
break;
case 27:
//#line 62 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el tipo de la función."); }
break;
case 28:
//#line 63 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta FUNC en la definición de la función."); }
break;
case 29:
//#line 64 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los parámetros deben ser colocados entre paréntesis."); }
break;
case 30:
//#line 65 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la función debe tener al menos un parámetro."); }
break;
case 31:
//#line 66 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros."); }
break;
case 32:
//#line 69 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 33:
//#line 70 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 34:
//#line 71 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el BEGIN antes del bloque de sentencias ejecutables."); }
break;
case 35:
//#line 72 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el BEGIN antes del bloque de sentencias ejecutables."); }
break;
case 36:
//#line 73 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el RETURN de la función."); }
break;
case 37:
//#line 74 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el RETURN de la función."); }
break;
case 38:
//#line 75 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la expresión de RETURN debe estar entre paréntesis."); }
break;
case 39:
//#line 76 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la expresión de RETURN debe estar entre paréntesis."); }
break;
case 40:
//#line 77 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): RETURN debe tener al menos una expresión como parámetro."); }
break;
case 41:
//#line 78 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): RETURN debe tener al menos una expresión como parámetro."); }
break;
case 42:
//#line 79 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en los parámetros de RETURN."); }
break;
case 43:
//#line 80 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en los parámetros de RETURN."); }
break;
case 44:
//#line 81 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de RETURN (expresión)."); }
break;
case 45:
//#line 82 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de RETURN (expresión)."); }
break;
case 46:
//#line 83 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END luego de ';'."); }
break;
case 47:
//#line 84 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END luego de ';'."); }
break;
case 48:
//#line 85 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
break;
case 49:
//#line 86 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
break;
case 57:
//#line 100 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una operación de asignación. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 58:
//#line 101 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la asignación."); }
break;
case 59:
//#line 102 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el operador de asignación."); }
break;
case 60:
//#line 103 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta la expresión a asignar."); }
break;
case 61:
//#line 106 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una impresión por pantalla de una cadena. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 62:
//#line 107 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la impresión de cadena."); }
break;
case 63:
//#line 108 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): cierre erróneo de la lista de parámetros de PRINT."); }
break;
case 64:
//#line 109 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los parámetros de PRINT deben estar entre paréntesis."); }
break;
case 65:
//#line 110 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba PRINT, se encontró '('."); }
break;
case 66:
//#line 111 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta declarar una cadena para PRINT."); }
break;
case 67:
//#line 114 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una invocación a una función (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 68:
//#line 115 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): el parámetro deben estar entre paréntesis."); }
break;
case 69:
//#line 116 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): número incorrecto de parámetros en la invocación."); }
break;
case 70:
//#line 117 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros."); }
break;
case 71:
//#line 118 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la invocación a la función."); }
break;
case 72:
//#line 121 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una sentencia IF. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 73:
//#line 122 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una sentencia IF. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 74:
//#line 123 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición de IF debe estar entre paréntesis."); }
break;
case 75:
//#line 124 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros."); }
break;
case 76:
//#line 125 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta la declaración de THEN."); }
break;
case 77:
//#line 126 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta cierre de ENDIF."); }
break;
case 78:
//#line 127 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de ENDIF."); }
break;
case 79:
//#line 128 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta cierre de ENDIF."); }
break;
case 80:
//#line 129 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de ENDIF."); }
break;
case 83:
//#line 138 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 84:
//#line 139 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 85:
//#line 140 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición de WHILE debe estar entre paréntesis."); }
break;
case 86:
//#line 141 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros."); }
break;
case 87:
//#line 142 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba DO, se leyó BEGIN."); }
break;
case 88:
//#line 143 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba END, se leyó ';'."); }
break;
case 89:
//#line 144 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
break;
case 95:
//#line 152 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los bloques TRY/CATCH no pueden anidarse"); }
break;
case 96:
//#line 155 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció un bloque TRY/CATCH. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 97:
//#line 156 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se leyó un BEGIN sin previo reconocimiento de CATCH."); }
break;
case 98:
//#line 157 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): el cuerpo de CATCH no se inicializó con BEGIN."); }
break;
case 99:
//#line 158 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): cuerpo de CATCH mal cerrado; falta END."); }
break;
case 100:
//#line 159 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
break;
case 106:
//#line 171 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una definición de contrato. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 107:
//#line 172 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición debe estar entre paréntesis."); }
break;
case 108:
//#line 173 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): CONTRACT debe tener al menos una condición como parámetro."); }
break;
case 109:
//#line 174 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el paréntesis de cierre para los parámetros de CONTRACT."); }
break;
case 110:
//#line 175 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de los parámetros de CONTRACT."); }
break;
case 111:
//#line 178 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconoció una sentencia de BREAK. (Línea " + AnalizadorLexico.LINEA + ")"); }
break;
case 112:
//#line 179 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de BREAK."); }
break;
case 122:
//#line 197 "gramatica.y"
{
                String tipo = sintactico.getTipoFromTS(val_peek(0).ival);
                if (tipo.equals("LONG"))
                    sintactico.verificarRangoEnteroLargo(val_peek(0).ival);
             }
break;
case 123:
//#line 202 "gramatica.y"
{  sintactico.setNegativoTablaSimb(val_peek(0).ival); }
break;
//#line 1083 "Parser.java"
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
