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

bloque_de_sentencias : BEGIN bloque END ';'
                     | sentencias
                     ;

bloque_sentencias_ejecutables_funcion : sentencias_ejecutables_func
                                      | bloque_sentencias_ejecutables_funcion sentencias_ejecutables_func
                                      ;

sentencias : sentencias_declarativas
           | sentencias_ejecutables
           ;

sentencias_declarativas : tipo lista_de_variables ';'
                        | declaracion_func
                        | FUNC lista_de_variables ';'
                        ;

lista_de_variables : ID
                   | lista_de_variables ',' ID
                   ;

encabezado_func : tipo FUNC ID '(' parametro ')'
                ;

declaracion_func : encabezado_func sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END ';'
                 ;

parametro : tipo ID
          ;

sentencias_ejecutables : asignacion
                       | salida
                       | invocacion_func
                       | sentencia_if
                       | sentencia_while
                       ;

sentencias_ejecutables_func : sentencias_ejecutables
                            | contrato
                            ;

contrato : CONTRACT ':' '(' condicion ')' ';'
         ;

condicion : expresion comparador expresion
          ;

asignacion : ID OPASIGNACION expresion ';'
           ;

salida : PRINT '(' CADENA ')' ';'
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