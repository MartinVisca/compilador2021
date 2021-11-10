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


public class Parser {

    boolean yydebug;        //do I want debug output?
    int yynerrs;            //number of errors so far
    int yyerrflag;          //was there an error?
    int yychar;             //the current working character

    //########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
    void debug(String msg) {
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
    final void state_push(int state) {
        try {
            stateptr++;
            statestk[stateptr] = state;
        } catch (ArrayIndexOutOfBoundsException e) {
            int oldsize = statestk.length;
            int newsize = oldsize * 2;
            int[] newstack = new int[newsize];
            System.arraycopy(statestk, 0, newstack, 0, oldsize);
            statestk = newstack;
            statestk[stateptr] = state;
        }
    }

    final int state_pop() {
        return statestk[stateptr--];
    }

    final void state_drop(int cnt) {
        stateptr -= cnt;
    }

    final int state_peek(int relative) {
        return statestk[stateptr - relative];
    }

    //###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
    final boolean init_stacks() {
        stateptr = -1;
        val_init();
        return true;
    }

    //###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
    void dump_stacks(int count) {
        int i;
        System.out.println("=index==state====value=     s:" + stateptr + "  v:" + valptr);
        for (i = 0; i < count; i++)
            System.out.println(" " + i + "    " + statestk[i] + "      " + valstk[i]);
        System.out.println("======================");
    }


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


    String yytext;//user variable to return contextual strings
    ParserVal yyval; //used to return semantic vals from action routines
    ParserVal yylval;//the 'lval' (result) I got from yylex()
    ParserVal valstk[];
    int valptr;

    //###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
    void val_init() {
        valstk = new ParserVal[YYSTACKSIZE];
        yyval = new ParserVal();
        yylval = new ParserVal();
        valptr = -1;
    }

    void val_push(ParserVal val) {
        if (valptr >= YYSTACKSIZE)
            return;
        valstk[++valptr] = val;
    }

    ParserVal val_pop() {
        if (valptr < 0)
            return new ParserVal();
        return valstk[valptr--];
    }

    void val_drop(int cnt) {
        int ptr;
        ptr = valptr - cnt;
        if (ptr < 0)
            return;
        valptr = ptr;
    }

    ParserVal val_peek(int relative) {
        int ptr;
        ptr = valptr - relative;
        if (ptr < 0)
            return new ParserVal();
        return valstk[ptr];
    }

    final ParserVal dup_yyval(ParserVal val) {
        ParserVal dup = new ParserVal();
        dup.ival = val.ival;
        dup.dval = val.dval;
        dup.sval = val.sval;
        dup.obj = val.obj;
        return dup;
    }

    //#### end semantic value section ####
    public final static short ID = 257;
    public final static short CTE = 258;
    public final static short IF = 259;
    public final static short THEN = 260;
    public final static short ELSE = 261;
    public final static short ENDIF = 262;
    public final static short PRINT = 263;
    public final static short FUNC = 264;
    public final static short RETURN = 265;
    public final static short BEGIN = 266;
    public final static short END = 267;
    public final static short BREAK = 268;
    public final static short LONG = 269;
    public final static short SINGLE = 270;
    public final static short WHILE = 271;
    public final static short DO = 272;
    public final static short CADENA = 273;
    public final static short MENORIGUAL = 274;
    public final static short MAYORIGUAL = 275;
    public final static short IGUAL = 276;
    public final static short DISTINTO = 277;
    public final static short CONTRACT = 278;
    public final static short TRY = 279;
    public final static short CATCH = 280;
    public final static short OPASIGNACION = 281;
    public final static short AND = 282;
    public final static short OR = 283;
    public final static short YYERRCODE = 256;
    final static short yylhs[] = {-1,
            0, 0, 0, 0, 0, 0, 0, 3, 3, 5,
            5, 5, 5, 5, 5, 6, 6, 8, 8, 2,
            2, 1, 1, 4, 4, 11, 11, 11, 11, 11,
            11, 13, 13, 13, 15, 15, 15, 15, 15, 15,
            14, 14, 14, 14, 14, 14, 14, 14, 14, 14,
            14, 14, 14, 14, 14, 14, 14, 14, 14, 14,
            14, 14, 16, 10, 10, 10, 10, 10, 10, 18,
            18, 18, 18, 19, 19, 19, 19, 19, 19, 20,
            20, 20, 20, 20, 21, 21, 21, 21, 21, 21,
            21, 21, 21, 25, 26, 22, 22, 22, 22, 22,
            22, 22, 22, 27, 27, 27, 27, 27, 27, 28,
            28, 23, 23, 23, 23, 9, 9, 7, 7, 7,
            29, 29, 29, 29, 29, 30, 30, 24, 24, 17,
            17, 17, 32, 32, 32, 33, 33, 33, 31, 31,
            31, 31, 31, 31, 31, 31, 12, 12,
    };
    final static short yylen[] = {2,
            6, 4, 5, 5, 4, 6, 6, 1, 2, 4,
            3, 1, 4, 4, 4, 1, 2, 1, 2, 1,
            2, 1, 2, 1, 1, 3, 3, 2, 1, 3,
            4, 1, 3, 3, 6, 6, 6, 5, 6, 6,
            11, 10, 10, 9, 4, 3, 6, 5, 7, 6,
            8, 7, 9, 8, 10, 9, 11, 10, 11, 10,
            12, 11, 2, 1, 1, 1, 1, 1, 1, 4,
            4, 3, 3, 5, 5, 5, 4, 3, 5, 5,
            5, 5, 5, 5, 8, 10, 3, 5, 6, 8,
            8, 10, 10, 1, 1, 9, 8, 6, 3, 5,
            6, 9, 9, 1, 1, 1, 1, 1, 2, 3,
            4, 5, 3, 5, 5, 1, 1, 1, 1, 1,
            6, 4, 5, 6, 6, 2, 2, 1, 3, 3,
            3, 1, 3, 3, 1, 1, 1, 2, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1,
    };
    final static short yydefred[] = {0,
            0, 0, 0, 0, 147, 148, 0, 22, 0, 29,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 20, 64, 65, 66, 67, 68, 69, 0, 0,
            0, 23, 28, 0, 0, 0, 0, 0, 0, 0,
            18, 116, 117, 0, 0, 0, 30, 0, 136, 137,
            0, 0, 0, 0, 0, 135, 0, 0, 0, 0,
            0, 2, 0, 0, 104, 105, 106, 107, 108, 0,
            0, 0, 0, 21, 0, 0, 0, 0, 5, 0,
            0, 27, 26, 0, 0, 0, 0, 0, 46, 19,
            0, 0, 31, 34, 33, 73, 0, 0, 0, 0,
            138, 72, 0, 0, 0, 0, 0, 87, 141, 142,
            143, 144, 145, 146, 139, 140, 0, 0, 0, 0,
            0, 99, 109, 0, 110, 78, 3, 0, 113, 4,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 45, 63, 0, 71, 70, 0, 0, 0,
            0, 0, 133, 134, 0, 0, 0, 77, 0, 0,
            0, 0, 0, 111, 0, 0, 7, 1, 6, 0,
            0, 0, 38, 0, 0, 0, 48, 0, 0, 122,
            0, 0, 0, 36, 83, 80, 84, 81, 82, 88,
            0, 0, 0, 0, 94, 25, 24, 0, 76, 75,
            74, 79, 100, 0, 0, 115, 112, 114, 37, 39,
            40, 35, 0, 0, 0, 50, 123, 0, 0, 0,
            0, 0, 47, 0, 0, 0, 8, 0, 9, 89,
            101, 0, 0, 98, 118, 119, 120, 0, 52, 0,
            0, 124, 125, 121, 0, 0, 0, 49, 0, 0,
            0, 11, 0, 0, 0, 0, 0, 16, 127, 126,
            0, 54, 0, 0, 0, 51, 0, 0, 90, 95,
            0, 91, 85, 15, 10, 14, 13, 97, 0, 0,
            17, 44, 56, 0, 0, 0, 0, 53, 0, 0,
            0, 0, 103, 96, 102, 60, 42, 58, 0, 43,
            55, 0, 0, 0, 92, 93, 86, 62, 59, 41,
            57, 0, 61,
    };
    final static short yydgoto[] = {2,
            7, 21, 193, 194, 195, 257, 234, 40, 41, 42,
            197, 9, 13, 10, 11, 92, 58, 23, 24, 25,
            26, 27, 28, 59, 198, 271, 71, 29, 43, 237,
            117, 55, 56,
    };
    final static short yysindex[] = {-235,
            -83, 0, -214, 477, 0, 0, 269, 0, -164, 0,
            161, 27, 67, -31, -21, -33, -13, -9, 518, -203,
            490, 0, 0, 0, 0, 0, 0, 0, 501, 511,
            528, 0, 0, 31, -169, 79, 388, 43, 178, 405,
            0, 0, 0, -137, 46, -141, 0, -133, 0, 0,
            103, -26, -124, -1, 66, 0, 134, 11, 537, -119,
            -35, 0, 134, 548, 0, 0, 0, 0, 0, -114,
            -241, -112, 88, 0, 518, 539, 91, 428, 0, -137,
            -27, 0, 0, 124, -6, 10, 423, 433, 0, 0,
            -95, 131, 0, 0, 0, 0, -22, 132, 151, -71,
            0, 0, 134, 134, 134, 134, 68, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 134, 136, 18, -65,
            94, 0, 0, -57, 0, 0, 0, 446, 0, 0,
            -49, -46, 167, -25, -45, 134, 12, -41, 41, 552,
            174, 344, 0, 0, -36, 0, 0, -39, 4, 157,
            66, 66, 0, 0, -32, 195, 11, 0, 162, 21,
            163, -15, -234, 0, 59, 7, 0, 0, 0, 28,
            47, 20, 0, 135, 49, 69, 0, 52, 98, 0,
            134, 29, 58, 0, 0, 0, 0, 0, 0, 0,
            290, 307, 325, 0, 0, 0, 0, 82, 0, 0,
            0, 0, 0, 106, 212, 0, 0, 0, 0, 0,
            0, 0, 226, 130, 86, 0, 0, 142, 90, 155,
            54, 122, 0, -165, 238, 231, 0, 246, 0, 0,
            0, 252, 96, 0, 0, 0, 0, 55, 0, 143,
            -48, 0, 0, 0, 251, 148, 118, 0, 295, 290,
            111, 0, 123, 160, 166, 341, 144, 0, 0, 0,
            358, 0, 170, 362, 169, 0, 173, -42, 0, 0,
            -221, 0, 0, 0, 0, 0, 0, 0, 138, 177,
            0, 0, 0, 150, 187, 459, 375, 0, 190, 379,
            391, 153, 0, 0, 0, 0, 0, 0, 392, 0,
            0, 154, 197, 464, 0, 0, 0, 0, 0, 0,
            0, 401, 0,
    };
    final static short yyrindex[] = {0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 92, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 92, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 6, 0, 0, 57, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            19, 44, 0, 0, 0, 0, 81, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 126, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0,
    };
    final static short yygindex[] = {0,
            451, 164, 275, -116, 220, 0, -199, -34, -40, -3,
            1, 40, 463, 0, 0, -51, 700, 458, 460, 465,
            466, 470, 473, 89, 302, 0, 0, 0, 0, 0,
            0, 65, 99,
    };
    final static int YYTABLESIZE = 921;
    static short yytable[];

    static {
        yytable();
    }

    static void yytable() {
        yytable = new short[]{90,
                22, 8, 85, 22, 88, 120, 61, 32, 52, 168,
                264, 8, 134, 53, 100, 171, 290, 74, 57, 186,
                103, 1, 104, 53, 124, 22, 22, 74, 133, 135,
                63, 204, 258, 20, 291, 53, 147, 205, 125, 32,
                292, 103, 12, 104, 90, 62, 132, 90, 132, 139,
                132, 175, 142, 103, 53, 104, 53, 281, 160, 130,
                212, 130, 188, 130, 132, 132, 44, 132, 221, 72,
                80, 22, 74, 53, 74, 227, 229, 130, 130, 201,
                130, 178, 172, 91, 131, 53, 131, 81, 131, 214,
                249, 33, 34, 53, 246, 250, 251, 128, 53, 35,
                86, 90, 131, 131, 93, 131, 64, 105, 156, 229,
                48, 103, 106, 104, 94, 128, 128, 207, 128, 91,
                91, 129, 48, 95, 74, 47, 241, 115, 103, 116,
                104, 5, 6, 101, 163, 32, 118, 83, 219, 129,
                129, 123, 129, 126, 240, 107, 127, 53, 244, 130,
                32, 121, 196, 115, 260, 116, 218, 115, 268, 116,
                103, 144, 104, 136, 103, 8, 104, 151, 152, 273,
                31, 145, 148, 91, 140, 213, 267, 103, 53, 104,
                3, 275, 4, 20, 150, 5, 6, 196, 196, 196,
                161, 149, 76, 78, 158, 245, 294, 103, 164, 104,
                20, 235, 280, 153, 154, 236, 167, 170, 297, 169,
                173, 307, 310, 181, 177, 189, 185, 20, 263, 184,
                199, 202, 196, 190, 289, 49, 50, 179, 235, 286,
                98, 99, 236, 146, 20, 49, 50, 119, 128, 60,
                203, 5, 6, 5, 6, 90, 196, 49, 50, 51,
                14, 20, 15, 235, 102, 304, 16, 236, 137, 187,
                138, 132, 208, 90, 18, 132, 49, 50, 49, 50,
                20, 38, 19, 159, 130, 211, 200, 132, 130, 132,
                132, 132, 132, 209, 238, 49, 50, 132, 132, 254,
                130, 20, 130, 130, 130, 130, 252, 49, 50, 131,
                130, 130, 210, 131, 255, 49, 50, 217, 20, 265,
                49, 50, 128, 223, 206, 131, 128, 131, 131, 131,
                131, 261, 45, 46, 216, 131, 131, 155, 128, 20,
                128, 128, 128, 128, 82, 46, 129, 230, 128, 128,
                129, 109, 110, 111, 112, 243, 20, 32, 32, 113,
                114, 259, 129, 269, 129, 129, 129, 129, 96, 49,
                50, 231, 129, 129, 20, 162, 272, 109, 110, 111,
                112, 109, 110, 111, 112, 113, 114, 248, 274, 113,
                114, 12, 8, 20, 8, 239, 12, 12, 8, 8,
                49, 50, 8, 293, 8, 8, 8, 242, 262, 278,
                14, 20, 15, 266, 8, 296, 16, 3, 306, 309,
                279, 233, 5, 6, 18, 276, 282, 14, 20, 15,
                285, 277, 19, 16, 3, 283, 37, 20, 288, 5,
                6, 18, 295, 300, 14, 287, 15, 303, 38, 19,
                16, 3, 298, 87, 20, 301, 5, 6, 18, 305,
                308, 14, 311, 15, 191, 38, 19, 16, 3, 313,
                192, 39, 20, 5, 6, 18, 226, 20, 14, 270,
                15, 36, 20, 19, 16, 3, 65, 232, 66, 233,
                5, 6, 18, 67, 68, 20, 132, 14, 69, 15,
                19, 70, 224, 16, 3, 0, 0, 253, 20, 5,
                6, 18, 0, 20, 166, 0, 0, 0, 14, 19,
                15, 0, 0, 0, 16, 3, 20, 0, 256, 233,
                5, 6, 18, 0, 0, 14, 0, 15, 0, 20,
                19, 16, 3, 0, 30, 0, 0, 5, 6, 18,
                20, 0, 0, 0, 0, 0, 14, 19, 15, 0,
                20, 0, 16, 3, 0, 192, 0, 20, 5, 6,
                18, 0, 0, 14, 0, 15, 0, 20, 19, 16,
                3, 0, 0, 225, 0, 5, 6, 18, 20, 0,
                0, 14, 0, 15, 0, 19, 0, 16, 3, 0,
                0, 228, 0, 5, 6, 18, 115, 0, 116, 0,
                14, 0, 15, 19, 0, 0, 16, 115, 182, 116,
                183, 115, 0, 116, 18, 0, 0, 0, 14, 0,
                15, 38, 19, 0, 16, 0, 0, 0, 284, 0,
                0, 0, 18, 0, 0, 14, 0, 15, 0, 38,
                19, 16, 0, 0, 14, 302, 15, 0, 0, 18,
                16, 0, 84, 0, 0, 0, 38, 19, 18, 0,
                89, 14, 0, 15, 0, 38, 19, 16, 0, 0,
                0, 0, 0, 0, 0, 18, 0, 0, 0, 14,
                0, 15, 38, 19, 14, 16, 15, 141, 143, 14,
                16, 15, 0, 18, 131, 16, 0, 0, 18, 0,
                38, 19, 14, 18, 15, 0, 19, 0, 16, 0,
                38, 19, 165, 54, 299, 14, 18, 15, 0, 312,
                14, 16, 15, 0, 19, 0, 16, 0, 0, 18,
                0, 0, 0, 14, 18, 15, 38, 19, 0, 16,
                0, 38, 19, 17, 0, 0, 14, 18, 15, 0,
                97, 0, 16, 0, 0, 19, 73, 14, 0, 15,
                18, 0, 0, 16, 0, 0, 75, 14, 19, 15,
                0, 18, 0, 16, 14, 0, 15, 77, 0, 19,
                16, 18, 0, 79, 14, 0, 15, 0, 18, 19,
                16, 0, 108, 0, 129, 14, 19, 15, 18, 0,
                0, 16, 0, 122, 0, 0, 19, 180, 0, 18,
                109, 110, 111, 112, 0, 0, 157, 19, 113, 114,
                0, 109, 110, 111, 112, 109, 110, 111, 112, 113,
                114, 0, 0, 113, 114, 174, 176, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 215, 0, 0, 0, 0, 0,
                220, 222, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                247,
        };
    }

    static short yycheck[];

    static {
        yycheck();
    }

    static void yycheck() {
        yycheck = new short[]{40,
                4, 1, 37, 7, 39, 41, 40, 7, 40, 59,
                59, 11, 40, 45, 41, 41, 59, 21, 40, 59,
                43, 257, 45, 45, 266, 29, 30, 31, 80, 81,
                40, 266, 232, 40, 256, 45, 59, 272, 280, 39,
                262, 43, 257, 45, 85, 59, 41, 88, 43, 40,
                45, 40, 87, 43, 45, 45, 45, 257, 41, 41,
                41, 43, 59, 45, 59, 60, 40, 62, 40, 273,
                40, 75, 76, 45, 78, 192, 193, 59, 60, 59,
                62, 41, 134, 44, 41, 45, 43, 257, 45, 41,
                256, 256, 257, 45, 41, 261, 262, 41, 45, 264,
                58, 142, 59, 60, 59, 62, 18, 42, 41, 226,
                44, 43, 47, 45, 256, 59, 60, 59, 62, 80,
                81, 41, 44, 257, 128, 59, 41, 60, 43, 62,
                45, 269, 270, 258, 41, 44, 256, 59, 41, 59,
                60, 256, 62, 256, 59, 57, 59, 45, 59, 59,
                59, 63, 156, 60, 59, 62, 59, 60, 41, 62,
                43, 257, 45, 40, 43, 40, 45, 103, 104, 59,
                7, 41, 41, 134, 86, 41, 59, 43, 45, 45,
                264, 59, 266, 40, 256, 269, 270, 191, 192, 193,
                256, 41, 29, 30, 59, 41, 59, 43, 256, 45,
                40, 205, 59, 105, 106, 205, 256, 41, 59, 256,
                256, 59, 59, 40, 256, 59, 256, 40, 267, 256,
                59, 59, 226, 256, 267, 257, 258, 139, 232, 264,
                257, 258, 232, 256, 40, 257, 258, 273, 75, 273,
                256, 269, 270, 269, 270, 286, 250, 257, 258, 281,
                257, 40, 259, 257, 256, 290, 263, 257, 265, 256,
                267, 256, 256, 304, 271, 260, 257, 258, 257, 258,
                40, 278, 279, 256, 256, 256, 256, 272, 260, 274,
                275, 276, 277, 256, 59, 257, 258, 282, 283, 59,
                272, 40, 274, 275, 276, 277, 59, 257, 258, 256,
                282, 283, 256, 260, 59, 257, 258, 256, 40, 59,
                257, 258, 256, 256, 256, 272, 260, 274, 275, 276,
                277, 267, 256, 257, 256, 282, 283, 260, 272, 40,
                274, 275, 276, 277, 256, 257, 256, 256, 282, 283,
                260, 274, 275, 276, 277, 256, 40, 256, 257, 282,
                283, 256, 272, 59, 274, 275, 276, 277, 256, 257,
                258, 256, 282, 283, 40, 272, 256, 274, 275, 276,
                277, 274, 275, 276, 277, 282, 283, 256, 256, 282,
                283, 256, 257, 40, 259, 256, 261, 262, 263, 264,
                257, 258, 267, 256, 269, 270, 271, 256, 256, 59,
                257, 40, 259, 256, 279, 256, 263, 264, 256, 256,
                267, 268, 269, 270, 271, 256, 59, 257, 40, 259,
                59, 256, 279, 263, 264, 256, 266, 40, 256, 269,
                270, 271, 256, 59, 257, 267, 259, 59, 278, 279,
                263, 264, 256, 266, 40, 256, 269, 270, 271, 59,
                59, 257, 256, 259, 260, 278, 279, 263, 264, 59,
                266, 11, 40, 269, 270, 271, 192, 40, 257, 250,
                259, 9, 40, 279, 263, 264, 19, 266, 19, 268,
                269, 270, 271, 19, 19, 40, 59, 257, 19, 259,
                279, 19, 191, 263, 264, -1, -1, 267, 40, 269,
                270, 271, -1, 40, 59, -1, -1, -1, 257, 279,
                259, -1, -1, -1, 263, 264, 40, -1, 267, 268,
                269, 270, 271, -1, -1, 257, -1, 259, -1, 40,
                279, 263, 264, -1, 266, -1, -1, 269, 270, 271,
                40, -1, -1, -1, -1, -1, 257, 279, 259, -1,
                40, -1, 263, 264, -1, 266, -1, 40, 269, 270,
                271, -1, -1, 257, -1, 259, -1, 40, 279, 263,
                264, -1, -1, 267, -1, 269, 270, 271, 40, -1,
                -1, 257, -1, 259, -1, 279, -1, 263, 264, -1,
                -1, 267, -1, 269, 270, 271, 60, -1, 62, -1,
                257, -1, 259, 279, -1, -1, 263, 60, 265, 62,
                267, 60, -1, 62, 271, -1, -1, -1, 257, -1,
                259, 278, 279, -1, 263, -1, -1, -1, 267, -1,
                -1, -1, 271, -1, -1, 257, -1, 259, -1, 278,
                279, 263, -1, -1, 257, 267, 259, -1, -1, 271,
                263, -1, 265, -1, -1, -1, 278, 279, 271, -1,
                256, 257, -1, 259, -1, 278, 279, 263, -1, -1,
                -1, -1, -1, -1, -1, 271, -1, -1, -1, 257,
                -1, 259, 278, 279, 257, 263, 259, 265, 256, 257,
                263, 259, -1, 271, 267, 263, -1, -1, 271, -1,
                278, 279, 257, 271, 259, -1, 279, -1, 263, -1,
                278, 279, 267, 14, 256, 257, 271, 259, -1, 256,
                257, 263, 259, -1, 279, -1, 263, -1, -1, 271,
                -1, -1, -1, 257, 271, 259, 278, 279, -1, 263,
                -1, 278, 279, 267, -1, -1, 257, 271, 259, -1,
                51, -1, 263, -1, -1, 279, 267, 257, -1, 259,
                271, -1, -1, 263, -1, -1, 266, 257, 279, 259,
                -1, 271, -1, 263, 257, -1, 259, 267, -1, 279,
                263, 271, -1, 256, 257, -1, 259, -1, 271, 279,
                263, -1, 256, -1, 256, 257, 279, 259, 271, -1,
                -1, 263, -1, 256, -1, -1, 279, 256, -1, 271,
                274, 275, 276, 277, -1, -1, 117, 279, 282, 283,
                -1, 274, 275, 276, 277, 274, 275, 276, 277, 282,
                283, -1, -1, 282, 283, 136, 137, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, 175, -1, -1, -1, -1, -1,
                181, 182, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                221,
        };
    }

    final static short YYFINAL = 2;
    final static short YYMAXTOKEN = 283;
    final static String yyname[] = {
            "end-of-file", null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, "'('", "')'", "'*'", "'+'", "','",
            "'-'", null, "'/'", null, null, null, null, null, null, null, null, null, null, "':'", "';'",
            "'<'", null, "'>'", null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, "ID", "CTE", "IF", "THEN", "ELSE", "ENDIF", "PRINT",
            "FUNC", "RETURN", "BEGIN", "END", "BREAK", "LONG", "SINGLE", "WHILE", "DO", "CADENA",
            "MENORIGUAL", "MAYORIGUAL", "IGUAL", "DISTINTO", "CONTRACT", "TRY", "CATCH",
            "OPASIGNACION", "AND", "OR",
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

//#line 333 "gramatica.y"

    private AnalizadorLexico lexico;
    private AnalizadorSintactico sintactico;

    public void setLexico(AnalizadorLexico lexico) {
        this.lexico = lexico;
    }

    public void setSintactico(AnalizadorSintactico sintactico) {
        this.sintactico = sintactico;
    }

    public AnalizadorLexico getLexico() {
        return this.lexico;
    }

    public AnalizadorSintactico getSintactico() {
        return this.sintactico;
    }

    public int yylex() {
        int token = lexico.procesarYylex();
        if (lexico.getRefTablaSimbolos() != -1)
            yylval = new ParserVal(lexico.getRefTablaSimbolos());
        return token;
    }

    public void yyerror(String string) {
        //sintactico.addErrorSintactico("par: " + string);
    }

    //#line 670 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
    void yylexdebug(int state, int ch) {
        String s = null;
        if (ch < 0) ch = 0;
        if (ch <= YYMAXTOKEN) //check index bounds
            s = yyname[ch];    //now get it
        if (s == null)
            s = "illegal-symbol";
        debug("state " + state + ", reading " + ch + " (" + s + ")");
    }


    //The following are now global, to aid in error reporting
    int yyn;       //next next thing to do
    int yym;       //
    int yystate;   //current parsing state from state table
    String yys;    //current token string


    //###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
    int yyparse() {
        boolean doaction;
        init_stacks();
        yynerrs = 0;
        yyerrflag = 0;
        yychar = -1;          //impossible char forces a read
        yystate = 0;            //initial state
        state_push(yystate);  //save it
        val_push(yylval);     //save empty value
        while (true) //until parsing is done, either correctly, or w/error
        {
            doaction = true;
            if (yydebug) debug("loop");
            //#### NEXT ACTION (from reduction table)
            for (yyn = yydefred[yystate]; yyn == 0; yyn = yydefred[yystate]) {
                if (yydebug) debug("yyn:" + yyn + "  state:" + yystate + "  yychar:" + yychar);
                if (yychar < 0)      //we want a char?
                {
                    yychar = yylex();  //get next token
                    if (yydebug) debug(" next yychar:" + yychar);
                    //#### ERROR CHECK ####
                    if (yychar < 0)    //it it didn't work/error
                    {
                        yychar = 0;      //change it to default string (no -1!)
                        if (yydebug)
                            yylexdebug(yystate, yychar);
                    }
                }//yychar<0
                yyn = yysindex[yystate];  //get amount to shift by (shift index)
                if ((yyn != 0) && (yyn += yychar) >= 0 &&
                        yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {
                    if (yydebug)
                        debug("state " + yystate + ", shifting to state " + yytable[yyn]);
                    //#### NEXT STATE ####
                    yystate = yytable[yyn];//we are in a new state
                    state_push(yystate);   //save it
                    val_push(yylval);      //push our lval as the input for next rule
                    yychar = -1;           //since we have 'eaten' a token, say we need another
                    if (yyerrflag > 0)     //have we recovered an error?
                        --yyerrflag;        //give ourselves credit
                    doaction = false;        //but don't process yet
                    break;   //quit the yyn=0 loop
                }

                yyn = yyrindex[yystate];  //reduce
                if ((yyn != 0) && (yyn += yychar) >= 0 &&
                        yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {   //we reduced!
                    if (yydebug) debug("reduce");
                    yyn = yytable[yyn];
                    doaction = true; //get ready to execute
                    break;         //drop down to actions
                } else //ERROR RECOVERY
                {
                    if (yyerrflag == 0) {
                        yyerror("syntax error");
                        yynerrs++;
                    }
                    if (yyerrflag < 3) //low error count?
                    {
                        yyerrflag = 3;
                        while (true)   //do until break
                        {
                            if (stateptr < 0)   //check for under & overflow here
                            {
                                yyerror("stack underflow. aborting...");  //note lower case 's'
                                return 1;
                            }
                            yyn = yysindex[state_peek(0)];
                            if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE) {
                                if (yydebug)
                                    debug("state " + state_peek(0) + ", error recovery shifting to state " + yytable[yyn] + " ");
                                yystate = yytable[yyn];
                                state_push(yystate);
                                val_push(yylval);
                                doaction = false;
                                break;
                            } else {
                                if (yydebug)
                                    debug("error recovery discarding state " + state_peek(0) + " ");
                                if (stateptr < 0)   //check for under & overflow here
                                {
                                    yyerror("Stack underflow. aborting...");  //capital 'S'
                                    return 1;
                                }
                                state_pop();
                                val_pop();
                            }
                        }
                    } else            //discard this token
                    {
                        if (yychar == 0)
                            return 1; //yyabort
                        if (yydebug) {
                            yys = null;
                            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
                            if (yys == null) yys = "illegal-symbol";
                            debug("state " + yystate + ", error recovery discards token " + yychar + " (" + yys + ")");
                        }
                        yychar = -1;  //read another
                    }
                }//end error recovery
            }//yyn=0 loop
            if (!doaction)   //any reason not to proceed?
                continue;      //skip action
            yym = yylen[yyn];          //get count of terminals on rhs
            if (yydebug)
                debug("state " + yystate + ", reducing " + yym + " by rule " + yyn + " (" + yyrule[yyn] + ")");
            if (yym > 0)                 //if count of rhs not 'nil'
                yyval = val_peek(yym - 1); //get current semantic value
            yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
            switch (yyn) {
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
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta BEGIN al abrir el bloque de sentencias ejecutables.");
                }
                break;
                case 6:
//#line 35 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END al cerrar el bloque de sentencias ejecutables.");
                }
                break;
                case 7:
//#line 36 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del END en el bloque de sentencias ejecutables.");
                }
                break;
                case 13:
//#line 46 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta BEGIN al abrir el bloque.");
                }
                break;
                case 14:
//#line 47 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): bloque mal cerrado (falta 'END').");
                }
                break;
                case 15:
//#line 48 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del bloque.");
                }
                break;
                case 26:
//#line 71 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una declaración de variable. (Línea " + AnalizadorLexico.LINEA + ")");
                }
                break;
                case 27:
//#line 72 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' al final de la declaración de variable.");
                }
                break;
                case 28:
//#line 73 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' al final de la declaración de variable.");
                }
                break;
                case 30:
//#line 75 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una declaración de variable de tipo función. (Línea " + AnalizadorLexico.LINEA + ")");
                }
                break;
                case 31:
//#line 76 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del bloque.");
                }
                break;
                case 32:
//#line 79 "gramatica.y"
                {
                    sintactico.setAmbitoTablaSimb(val_peek(0).ival);
                    sintactico.setUsoTablaSimb(val_peek(0).ival, "VARIABLE");
                    if (!sintactico.variableFueDeclarada(val_peek(0).ival)) {
                        sintactico.setTipoVariableTablaSimb(val_peek(0).ival);
                    } else {
                        sintactico.addErrorSintactico("---------------variable ya declarada---------------");
                    }
                }
                break;
                case 33:
//#line 87 "gramatica.y"
                {
                    sintactico.setAmbitoTablaSimb(val_peek(0).ival);
                    sintactico.setUsoTablaSimb(val_peek(0).ival, "VARIABLE");
                    if (!sintactico.variableFueDeclarada(val_peek(0).ival)) {
                        sintactico.setTipoVariableTablaSimb(val_peek(0).ival);
                    } else {
                        sintactico.addErrorSintactico("---------------variable ya declarada---------------");
                    }
                }
                break;
                case 34:
//#line 95 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta una ',' entre identificadores.");
                }
                break;
                case 35:
//#line 98 "gramatica.y"
                {
                    sintactico.setUsoTablaSimb(val_peek(3).ival, "NOMBRE DE FUNCIÓN");
                    sintactico.setAmbito(val_peek(3).sval);
                }
                break;
                case 36:
//#line 102 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el tipo de la función.");
                }
                break;
                case 37:
//#line 103 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta FUNC en la definición de la función.");
                }
                break;
                case 38:
//#line 104 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los parámetros deben ser colocados entre paréntesis.");
                }
                break;
                case 39:
//#line 105 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la función debe tener al menos un parámetro.");
                }
                break;
                case 40:
//#line 106 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros.");
                }
                break;
                case 41:
//#line 109 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")");
                }
                break;
                case 42:
//#line 110 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")");
                }
                break;
                case 43:
//#line 111 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")");
                }
                break;
                case 44:
//#line 112 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")");
                }
                break;
                case 45:
//#line 113 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el BEGIN antes del bloque de sentencias ejecutables.");
                }
                break;
                case 46:
//#line 114 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el BEGIN antes del bloque de sentencias ejecutables.");
                }
                break;
                case 47:
//#line 115 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el RETURN de la función.");
                }
                break;
                case 48:
//#line 116 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el RETURN de la función.");
                }
                break;
                case 49:
//#line 117 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la expresión de RETURN debe estar entre paréntesis.");
                }
                break;
                case 50:
//#line 118 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la expresión de RETURN debe estar entre paréntesis.");
                }
                break;
                case 51:
//#line 119 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): RETURN debe tener al menos una expresión como parámetro.");
                }
                break;
                case 52:
//#line 120 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): RETURN debe tener al menos una expresión como parámetro.");
                }
                break;
                case 53:
//#line 121 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en los parámetros de RETURN.");
                }
                break;
                case 54:
//#line 122 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en los parámetros de RETURN.");
                }
                break;
                case 55:
//#line 123 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de RETURN (expresión).");
                }
                break;
                case 56:
//#line 124 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de RETURN (expresión).");
                }
                break;
                case 57:
//#line 125 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END luego de ';'.");
                }
                break;
                case 58:
//#line 126 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END luego de ';'.");
                }
                break;
                case 59:
//#line 127 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END.");
                }
                break;
                case 60:
//#line 128 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END.");
                }
                break;
                case 61:
//#line 129 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): no se permiten sentencias luego del RETURN en la función.");
                }
                break;
                case 62:
//#line 130 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): no se permiten sentencias luego del RETURN en la función.");
                }
                break;
                case 63:
//#line 133 "gramatica.y"
                {
                    sintactico.setUsoTablaSimb(val_peek(0).ival, "NOMBRE DE PARÁMETRO");
                }
                break;
                case 70:
//#line 144 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una operación de asignación. (Línea " + AnalizadorLexico.LINEA + ")");
                }
                break;
                case 71:
//#line 145 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' luego de la asignación.");
                }
                break;
                case 72:
//#line 146 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el operador de asignación.");
                }
                break;
                case 73:
//#line 147 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' luego de la asignación.");
                }
                break;
                case 74:
//#line 150 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una impresión por pantalla de una cadena. (Línea " + AnalizadorLexico.LINEA + ")");
                    sintactico.setUsoTablaSimb(val_peek(2).ival, "CADENA DE CARACTERES");
                    sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(2).ival));
                    sintactico.agregarAPolaca("OUT"); /*/////////// COMPROBAR //////////////*/
                }
                break;
                case 75:
//#line 156 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la impresión de cadena.");
                }
                break;
                case 76:
//#line 157 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): cierre erróneo de la lista de parámetros de PRINT.");
                }
                break;
                case 77:
//#line 158 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los parámetros de PRINT deben estar entre paréntesis.");
                }
                break;
                case 78:
//#line 159 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba PRINT, se encontró '('.");
                }
                break;
                case 79:
//#line 160 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta declarar una cadena para PRINT.");
                }
                break;
                case 80:
//#line 163 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una invocación a una función (Línea " + AnalizadorLexico.LINEA + ")");
                }
                break;
                case 81:
//#line 164 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una invocación a una función (Línea " + AnalizadorLexico.LINEA + ")");
                }
                break;
                case 82:
//#line 165 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta especificar un parámetro al invocar a la función.");
                }
                break;
                case 83:
//#line 166 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la invocación a la función.");
                }
                break;
                case 84:
//#line 167 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la invocación a la función.");
                }
                break;
                case 85:
//#line 170 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una sentencia IF. (Línea " + AnalizadorLexico.LINEA + ")");
                }
                break;
                case 86:
//#line 171 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una sentencia IF. (Línea " + AnalizadorLexico.LINEA + ")");
                }
                break;
                case 87:
//#line 172 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición de IF debe estar entre paréntesis.");
                }
                break;
                case 88:
//#line 173 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros.");
                }
                break;
                case 89:
//#line 174 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta la declaración de THEN.");
                }
                break;
                case 90:
//#line 175 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta cierre de ENDIF.");
                }
                break;
                case 91:
//#line 176 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de ENDIF.");
                }
                break;
                case 92:
//#line 177 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta cierre de ENDIF.");
                }
                break;
                case 93:
//#line 178 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de ENDIF.");
                }
                break;
                case 94:
//#line 181 "gramatica.y"
                {
                    sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + (sintactico.getSizePolaca() + 2) + "]");   /* Desapila dirección incompleta y completa el destino de BF*/
                    sintactico.agregarAPolaca(" ");                               /* Crea paso incompleto*/
                    sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);  /* Apila el nro de paso incompleto*/
                    sintactico.agregarAPolaca("BI");                              /* Se crea el paso BI*/
                }
                break;
                case 95:
//#line 189 "gramatica.y"
                {
                    sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + sintactico.getSizePolaca() + "]");
                }
                break;
                case 96:
//#line 192 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")");
                    sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + (sintactico.getSizePolaca() + 2) + "]"); /* Desapila dirección incompleta y completa el destino de BF*/
                    sintactico.agregarAPolaca("[" + sintactico.popElementoPila() + "]");    /* Desapilar paso de inicio*/
                    sintactico.agregarAPolaca("BI");
                }
                break;
                case 97:
//#line 198 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")");
                    sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + (sintactico.getSizePolaca() + 2) + "]"); /* Desapila dirección incompleta y completa el destino de BF*/
                    sintactico.agregarAPolaca("[" + sintactico.popElementoPila() + "]");    /* Desapilar paso de inicio*/
                    sintactico.agregarAPolaca("BI");
                }
                break;
                case 98:
//#line 204 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")");
                    sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + (sintactico.getSizePolaca() + 2) + "]"); /* Desapila dirección incompleta y completa el destino de BF*/
                    sintactico.agregarAPolaca("[" + sintactico.popElementoPila() + "]");    /* Desapilar paso de inicio*/
                    sintactico.agregarAPolaca("BI");
                }
                break;
                case 99:
//#line 210 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición de WHILE debe estar entre paréntesis.");
                }
                break;
                case 100:
//#line 211 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros.");
                }
                break;
                case 101:
//#line 212 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba DO, se leyó BEGIN.");
                }
                break;
                case 102:
//#line 213 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba END, se leyó ';'.");
                }
                break;
                case 103:
//#line 214 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END.");
                }
                break;
                case 109:
//#line 222 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los bloques TRY/CATCH no pueden anidarse");
                }
                break;
                case 110:
//#line 225 "gramatica.y"
                {
                    sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + (sintactico.getSizePolaca() + 2) + "]"); /* Desapila dirección incompleta y completa el destino de BF*/
                    sintactico.agregarAPolaca("[" + sintactico.popElementoPila() + "]");    /* Desapilar paso de inicio*/
                    sintactico.agregarAPolaca("BI"); /* Salto desde contrato*/
                }
                break;
                case 111:
//#line 230 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se leyó un BEGIN sin previo reconocimiento de CATCH.");
                }
                break;
                case 112:
//#line 233 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció un bloque TRY/CATCH. (Línea " + AnalizadorLexico.LINEA + ")");
                }
                break;
                case 113:
//#line 234 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): el cuerpo de CATCH no se inicializó con BEGIN.");
                }
                break;
                case 114:
//#line 235 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): cuerpo de CATCH mal cerrado; falta END.");
                }
                break;
                case 115:
//#line 236 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END.");
                }
                break;
                case 121:
//#line 248 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una definición de contrato. (Línea " + AnalizadorLexico.LINEA + ")");
                    sintactico.agregarAPolaca(" "); /* COMPROBAR; agrego paso incompleto para hacer el salto al catch.*/
                    sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);
                    sintactico.agregarAPolaca("BF");
                }
                break;
                case 122:
//#line 254 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición debe estar entre paréntesis.");
                }
                break;
                case 123:
//#line 255 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): CONTRACT debe tener al menos una condición como parámetro.");
                }
                break;
                case 124:
//#line 256 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el paréntesis de cierre para los parámetros de CONTRACT.");
                }
                break;
                case 125:
//#line 257 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de los parámetros de CONTRACT.");
                }
                break;
                case 126:
//#line 260 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una sentencia de BREAK. (Línea " + AnalizadorLexico.LINEA + ")");
                }
                break;
                case 127:
//#line 261 "gramatica.y"
                {
                    sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de BREAK.");
                }
                break;
                case 128:
//#line 264 "gramatica.y"
                {
                    sintactico.agregarAPolaca(" ");
                    sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);
                    sintactico.agregarAPolaca("BF");
                }
                break;
                case 129:
//#line 269 "gramatica.y"
                {
                    sintactico.agregarAPolaca(val_peek(1).sval);
                    sintactico.agregarAPolaca(" ");
                    sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);
                    sintactico.agregarAPolaca("BF");
                }
                break;
                case 130:
//#line 277 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una suma. (Línea " + AnalizadorLexico.LINEA + ")");
                    sintactico.agregarAPolaca("+");
                }
                break;
                case 131:
//#line 281 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una resta. (Línea " + AnalizadorLexico.LINEA + ")");
                    sintactico.agregarAPolaca("-");
                }
                break;
                case 133:
//#line 288 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una multiplicación. (Línea " + AnalizadorLexico.LINEA + ")");
                    sintactico.agregarAPolaca("*");
                }
                break;
                case 134:
//#line 292 "gramatica.y"
                {
                    sintactico.agregarAnalisis("Se reconoció una división. (Línea " + AnalizadorLexico.LINEA + ")");
                    sintactico.agregarAPolaca("/");
                }
                break;
                case 136:
//#line 299 "gramatica.y"
                {
                    if (sintactico.getUsoFromTS(val_peek(0).ival).equals("VARIABLE"))
                        sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(0).ival));
                }
                break;
                case 137:
//#line 303 "gramatica.y"
                {
                    String tipo = sintactico.getTipoFromTS(val_peek(0).ival);
                    if (tipo.equals("LONG"))
                        sintactico.verificarRangoEnteroLargo(val_peek(0).ival);
                    sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(0).ival));
                }
                break;
                case 138:
//#line 309 "gramatica.y"
                {
                    sintactico.setNegativoTablaSimb(val_peek(0).ival);
                }
                break;
                case 139:
//#line 312 "gramatica.y"
                {
                    yyval.sval = new String("<");
                }
                break;
                case 140:
//#line 313 "gramatica.y"
                {
                    yyval.sval = new String(">");
                }
                break;
                case 141:
//#line 314 "gramatica.y"
                {
                    yyval.sval = new String("<=");
                }
                break;
                case 142:
//#line 315 "gramatica.y"
                {
                    yyval.sval = new String(">=");
                }
                break;
                case 143:
//#line 316 "gramatica.y"
                {
                    yyval.sval = new String("==");
                }
                break;
                case 144:
//#line 317 "gramatica.y"
                {
                    yyval.sval = new String("<>");
                }
                break;
                case 145:
//#line 318 "gramatica.y"
                {
                    yyval.sval = new String("&&");
                }
                break;
                case 146:
//#line 319 "gramatica.y"
                {
                    yyval.sval = new String("||");
                }
                break;
                case 147:
//#line 322 "gramatica.y"
                {
                    sintactico.setTipo("LONG");
                    yyval.sval = new String("LONG");
                }
                break;
                case 148:
//#line 326 "gramatica.y"
                {
                    sintactico.setTipo("SINGLE");
                    yyval.sval = new String("SINGLE");
                }
                break;
//#line 1373 "Parser.java"
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
                if (yydebug) debug("After reduction, shifting from state 0 to state " + YYFINAL + "");
                yystate = YYFINAL;         //explicitly say we're done
                state_push(YYFINAL);       //and save it
                val_push(yyval);           //also save the semantic value of parsing
                if (yychar < 0)            //we want another character?
                {
                    yychar = yylex();        //get next character
                    if (yychar < 0) yychar = 0;  //clean, if necessary
                    if (yydebug)
                        yylexdebug(yystate, yychar);
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
                if (yydebug)
                    debug("after reduction, shifting from state " + state_peek(0) + " to state " + yystate + "");
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
    public void run() {
        yyparse();
    }
//## end of method run() ########################################


//## Constructors ###############################################

    /**
     * Default constructor.  Turn off with -Jnoconstruct .
     */
    public Parser() {
        //nothing to do
    }


    /**
     * Create a parser, setting the debug to true or false.
     *
     * @param debugMe true for debugging, false for no debug.
     */
    public Parser(boolean debugMe) {
        yydebug = debugMe;
    }
//###############################################################


}
//################### END OF CLASS ##############################
