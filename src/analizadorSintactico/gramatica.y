%{

package analizadorSintactico;

import java.util.Vector;

import analizadorLexico.AnalizadorLexico;
import analizadorSintactico.AnalizadorSintactico;
import analizadorLexico.RegistroSimbolo;

%}

%token ID CTE IF THEN ELSE ENDIF PRINT FUNC RETURN BEGIN END BREAK LONG SINGLE WHILE DO CADENA MENORIGUAL MAYORIGUAL IGUAL DISTINTO CONTRACT TRY CATCH OPASIGNACION AND OR
%start programa

%%

programa : bloque
         ;

bloque : sentencias
       | bloque sentencias
       ;

bloque_de_sentencias : BEGIN bloque END ';'
                     | sentencias
                     | bloque END ';' error
                     | BEGIN bloque ';' error
                     | BEGIN bloque END error
                     ;

bloque_sentencias_while : sentencias_while
                        | bloque_sentencias_while sentencias_while

bloque_sentencias_ejecutables_funcion : sentencias_ejecutables_func
                                      | bloque_sentencias_ejecutables_funcion sentencias_ejecutables_func
                                      ;

bloque_sentencias_ejecutables : sentencias_ejecutables
                              | bloque_sentencias_ejecutables sentencias_ejecutables
                              ;

sentencias : sentencias_declarativas
           | sentencias_ejecutables
           ;

sentencias_declarativas : tipo lista_de_variables ';'
                        | tipo lista_de_variables error
                        | tipo error
                        | declaracion_func
                        | FUNC lista_de_variables ';'
                        | FUNC lista_de_variables error
                        ;

lista_de_variables : ID
                   | lista_de_variables ',' ID
                   | lista_de_variables ID error
                   ;

encabezado_func : tipo FUNC ID '(' parametro ')'
                | FUNC ID '(' parametro ')' error
                | tipo ID '(' parametro ')' error
                | tipo FUNC ID parametro error
                | tipo FUNC ID '(' ')' error
                | tipo FUNC ID '(' parametro error
                ;

declaracion_func : encabezado_func sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END ';'
                 | encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END ';'
                 | encabezado_func sentencias_declarativas bloque_sentencias_ejecutables_funcion error
                 | encabezado_func bloque_sentencias_ejecutables_funcion error
                 | encabezado_func sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion END error
                 | encabezado_func BEGIN bloque_sentencias_ejecutables_funcion END error
                 | encabezado_func sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN expresion error
                 | encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN expresion error
                 | encabezado_func sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' ')' error
                 | encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' ')' error
                 | encabezado_func sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ';' error
                 | encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ';' error
                 | encabezado_func sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' END error
                 | encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' END error
                 | encabezado_func sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' ';' error
                 | encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' ';' error
                 | encabezado_func sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END error
                 | encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END error
                 ;

parametro : tipo ID
          ;

sentencias_ejecutables : asignacion
                       | salida
                       | invocacion_func
                       | sentencia_if
                       | sentencia_while
                       | sentencia_try_catch
                       ;

asignacion : ID OPASIGNACION expresion ';'
           | ID OPASIGNACION expresion error
           | ID expresion error
           | ID OPASIGNACION error
           ;

salida : PRINT '(' CADENA ')' ';'
       | PRINT '(' CADENA ')' error
       | PRINT '(' CADENA error
       | PRINT CADENA error
       | '(' CADENA error
       | PRINT '(' ')' error
       ;

invocacion_func : ID '(' parametro ')' ';'
                | ID parametro error
                | ID '(' ')' error
                | ID '(' parametro ';' error
                | ID '(' parametro ')' error
                ;

sentencia_if : IF '(' condicion ')' THEN cuerpo_if END_IF ';'
             | IF '(' condicion ')' THEN cuerpo_if ELSE cuerpo_else END_IF ';' 
             | IF condicion error
             | IF '(' condicion  THEN error
             | IF '(' condicion ')' cuerpo_if error
             | IF '(' condicion ')' THEN cuerpo_if ';' error
             | IF '(' condicion ')' THEN cuerpo_if END_IF error
             | IF '(' condicion ')' THEN cuerpo_if ELSE cuerpo_else ';' error
             | IF '(' condicion ')' THEN cuerpo_if ELSE cuerpo_else END_IF error
             ;

cuerpo_if : bloque_de_sentencias
          ;

cuerpo_else : bloque_de_sentencias
            ;

sentencia_while : WHILE '(' condicion ')' DO BEGIN bloque_sentencias_while END ';'
                | WHILE condicion error
                | WHILE '(' condicion DO error
                | WHILE '(' condicion ')' BEGIN error
                | WHILE '(' condicion ')' DO BEGIN bloque_sentencias_while ';' error
                | WHILE '(' condicion ')' DO BEGIN bloque_sentencias_while END error
                ;

sentencias_ejecutables_sin_try_catch : asignacion
                                     | salida
                                     | invocacion_func
                                     | sentencia_if
                                     | sentencia_while
                                     | sentencia_try_catch error
                                     ;

sentencia_try_catch : TRY sentencias_ejecutables_sin_try_catch CATCH BEGIN bloque_sentencias_ejecutables END ';'
                    | TRY sentencias_ejecutables_sin_try_catch BEGIN error
                    | TRY sentencias_ejecutables_sin_try_catch CATCH bloque_sentencias_ejecutables error
                    | TRY sentencias_ejecutables_sin_try_catch CATCH BEGIN bloque_sentencias_ejecutables ';' error
                    | TRY sentencias_ejecutables_sin_try_catch CATCH BEGIN bloque_sentencias_ejecutables END error
                    ;

sentencias_ejecutables_func : sentencias_ejecutables
                            | contrato
                            ;

sentencias_while : sentencias_ejecutables
                 | sentencias_declarativas
                 | sentencia_break
                 ;

contrato : CONTRACT ':' '(' condicion ')' ';'
         | ':' error
         | CONTRACT '(' error
         | CONTRACT '(' ')' error
         | CONTRACT '(' condicion ';' error
         | CONTRACT '(' condicion ')' error
         ;

sentencia_break : BREAK ';'
                | BREAK error
                ;

condicion : expresion comparador expresion
          ;

expresion : expresion '+' termino
          | expresion '-' termino
          | termino
          ;

termino : termino '*' factor
        | termino '/' factor
        | factor
        ;

factor : ID
       | CTE
       | '-' CTE
       ;

comparador : '<'
           | '>'
           | MENORIGUAL
           | MAYORIGUAL
           | IGUAL
           | DISTINTO
           ;

tipo : LONG
     | SINGLE
     ;

%%

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