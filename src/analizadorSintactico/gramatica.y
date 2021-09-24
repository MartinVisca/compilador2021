%{

package analizadorSintactico;

import java.util.Vector;

import analizadorLexico.AnalizadorLexico;
import analizadorSintactico.AnalizadorSintactico;
import analizadorLexico.RegistroSimbolo;

%}

%token ID CTE IF ELSE THEN END_IF PRINT FUNC RETURN BEGIN END BREAK CONTRACT TRY CATCH LONG SINGLE CADENA MENORIGUAL MAYORIGUAL IGUAL DISTINTO OPASIGNACION
%start programa

%%

programa : bloque
         ;

bloque : sentencias
       | bloque sentencias
       ;

bloque_sentencias : BEGIN bloque END
                  | sentencias
                  | bloque END error
                  | BEGIN bloque error
                  ;

sentencias : sentencias_declarativas
           | sentencias_ejecutables
           ;

sentencias_declarativas : tipo lista_de_variables ';'
                        | tipo lista_de_variables error
                        | tipo error
                        | ID ';' error
                        | declaracion_func
                        ;

lista_de_variables : ID
                   | lista_de_variables ',' ID
                   | lista_de_variables ID error
                   | tipo error
                   ;

sentencias_ejecutables : asignacion
                       | salida
                       | invocacion_func
                       | sentencia_if
                       | sentencia_while
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

tipo : LONG
     | SINGLE
     ;

%%