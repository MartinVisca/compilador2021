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

programa : ID bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables END ';'        {
                                                                                                    sintactico.agregarAnalisis("Se reconoció un programa. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                    sintactico.setUsoTablaSimb($1.ival, "NOMBRE DE PROGRAMA");
                                                                                                }
         | ID BEGIN END ';'                                                                     {
                                                                                                    sintactico.agregarAnalisis("Se reconoció un programa. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                    sintactico.setUsoTablaSimb($1.ival, "NOMBRE DE PROGRAMA");
                                                                                                }
         | ID BEGIN bloque_sentencias_ejecutables END ';'                                       {
                                                                                                    sintactico.agregarAnalisis("Se reconoció un programa. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                    sintactico.setUsoTablaSimb($1.ival, "NOMBRE DE PROGRAMA");
                                                                                                }
         | ID bloque_sentencias_declarativas BEGIN END ';'                                      {
                                                                                                    sintactico.agregarAnalisis("Se reconoció un programa. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                    sintactico.setUsoTablaSimb($1.ival, "NOMBRE DE PROGRAMA");
                                                                                                }
         | ID bloque_sentencias_declarativas bloque_sentencias_ejecutables error                { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta BEGIN al abrir el bloque de sentencias ejecutables."); }
         | ID bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables ';' error      { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END al cerrar el bloque de sentencias ejecutables."); }
         | ID bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables END error      { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del END en el bloque de sentencias ejecutables."); }
         ;

bloque : sentencias
       | bloque sentencias
       ;

bloque_de_sentencias : BEGIN bloque END ';'
                     | BEGIN END ';'
                     | sentencias
                     | bloque END ';' error     { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta BEGIN al abrir el bloque."); }
                     | BEGIN bloque ';' error   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): bloque mal cerrado (falta 'END')."); }
                     | BEGIN bloque END error   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del bloque."); }
                     ;

bloque_sentencias_while : sentencias_while
                        | bloque_sentencias_while sentencias_while
                        ;

bloque_sentencias_ejecutables_funcion : sentencias_ejecutables_func
                                      | bloque_sentencias_ejecutables_funcion sentencias_ejecutables_func
                                      ;

bloque_sentencias_ejecutables : sentencias_ejecutables
                              | bloque_sentencias_ejecutables sentencias_ejecutables
                              ;

bloque_sentencias_declarativas : sentencias_declarativas
                               | bloque_sentencias_declarativas sentencias_declarativas
                               ;

sentencias : sentencias_declarativas
           | sentencias_ejecutables
           ;

sentencias_declarativas : tipo lista_de_variables ';'        { sintactico.agregarAnalisis("Se reconoció una declaración de variable. (Línea " + AnalizadorLexico.LINEA + ")"); }
                        | tipo lista_de_variables error      { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' al final de la declaración de variable."); }
                        | tipo error                         { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' al final de la declaración de variable."); }
                        | declaracion_func
                        | FUNC lista_de_variables ';'        { sintactico.agregarAnalisis("Se reconoció una declaración de variable de tipo función. (Línea " + AnalizadorLexico.LINEA + ")"); }
                        | FUNC lista_de_variables error ';'  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del bloque."); }
                        ;

lista_de_variables : ID                             {
                                                          sintactico.setAmbitoTablaSimb($1.ival);
                                                          sintactico.setUsoTablaSimb($1.ival, "VARIABLE");
                                                          if (!sintactico.variableFueDeclarada($1.ival))
                                                                sintactico.setTipoVariableTablaSimb($1.ival);
                                                    }
                   | lista_de_variables ',' ID      {
                                                          sintactico.setAmbitoTablaSimb($3.ival);
                                                          sintactico.setUsoTablaSimb($3.ival, "VARIABLE");
                                                          if (!sintactico.variableFueDeclarada($3.ival))
                                                                sintactico.setTipoVariableTablaSimb($3.ival);
                                                    }
                   | lista_de_variables ID error    { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta una ',' entre identificadores."); }
                   ;

encabezado_func : tipo FUNC ID '(' parametro ')'    {
                                                        sintactico.setUsoTablaSimb($3.ival, "NOMBRE DE FUNCIÓN");
                                                        sintactico.setAmbito($3.ival);
                                                    }
                | FUNC ID '(' parametro ')' error   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el tipo de la función."); }
                | tipo ID '(' parametro ')' error   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta FUNC en la definición de la función."); }
                | tipo FUNC ID parametro error      { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los parámetros deben ser colocados entre paréntesis."); }
                | tipo FUNC ID '(' ')' error        { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la función debe tener al menos un parámetro."); }
                | tipo FUNC ID '(' parametro error  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros."); }
                ;

declaracion_func : encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END ';'     { sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")"); }
                 | encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END ';'                                    { sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")"); }
                 | encabezado_func bloque_sentencias_declarativas BEGIN RETURN '(' expresion ')' ';' END ';'                                           { sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")"); }
                 | encabezado_func BEGIN RETURN '(' expresion ')' ';' END ';'                                                                          { sintactico.agregarAnalisis("Se reconoció una declaración de función. (Línea " + AnalizadorLexico.LINEA + ")"); }
                 | encabezado_func bloque_sentencias_declarativas bloque_sentencias_ejecutables_funcion error                                          { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el BEGIN antes del bloque de sentencias ejecutables."); }
                 | encabezado_func bloque_sentencias_ejecutables_funcion error                                                                         { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el BEGIN antes del bloque de sentencias ejecutables."); }
                 | encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion END error                                { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el RETURN de la función."); }
                 | encabezado_func BEGIN bloque_sentencias_ejecutables_funcion END error                                                               { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el RETURN de la función."); }
                 | encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN expresion error                   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la expresión de RETURN debe estar entre paréntesis."); }
                 | encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN expresion error                                                  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la expresión de RETURN debe estar entre paréntesis."); }
                 | encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' ')' error                     { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): RETURN debe tener al menos una expresión como parámetro."); }
                 | encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' ')' error                                                    { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): RETURN debe tener al menos una expresión como parámetro."); }
                 | encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ';' error           { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en los parámetros de RETURN."); }
                 | encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ';' error                                          { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en los parámetros de RETURN."); }
                 | encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' END error       { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de RETURN (expresión)."); }
                 | encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' END error                                      { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de RETURN (expresión)."); }
                 | encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' ';' error   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END luego de ';'."); }
                 | encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' ';' error                                  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END luego de ';'."); }
                 | encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END error   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
                 | encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END error                                  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
                 | encabezado_func bloque_sentencias_declarativas BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' bloque_sentencias_ejecutables_funcion error ';'    { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): no se permiten sentencias luego del RETURN en la función."); }
                 | encabezado_func BEGIN bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' bloque_sentencias_ejecutables_funcion error  ';'                                  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): no se permiten sentencias luego del RETURN en la función."); }
                 ;

parametro : tipo ID     {
                            sintactico.setUsoTablaSimb($2.ival, "NOMBRE DE PARÁMETRO");
                        }
          ;

sentencias_ejecutables : asignacion
                       | salida
                       | invocacion_func
                       | sentencia_if
                       | sentencia_while
                       | sentencia_try_catch
                       ;

asignacion : ID OPASIGNACION expresion ';'    { sintactico.agregarAnalisis("Se reconoció una operación de asignación. (Línea " + AnalizadorLexico.LINEA + ")"); }
           | ID OPASIGNACION expresion error  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' luego de la asignación."); }
           | ID expresion error               { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el operador de asignación."); }
           | ID OPASIGNACION error            { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' luego de la asignación."); }
           ;

salida : PRINT '(' CADENA ')' ';'     { sintactico.agregarAnalisis("Se reconoció una impresión por pantalla de una cadena. (Línea " + AnalizadorLexico.LINEA + ")"); }
       | PRINT '(' CADENA ')' error   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la impresión de cadena."); }
       | PRINT '(' CADENA error ';'   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): cierre erróneo de la lista de parámetros de PRINT."); }
       | PRINT CADENA error ';'       { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los parámetros de PRINT deben estar entre paréntesis."); }
       | '(' CADENA error             { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba PRINT, se encontró '('."); }
       | PRINT '(' ')' error ';'      { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta declarar una cadena para PRINT."); }
       ;

invocacion_func : ID '(' ID ')' ';'     { sintactico.agregarAnalisis("Se reconoció una invocación a una función (Línea " + AnalizadorLexico.LINEA + ")"); }
                | ID '(' CTE ')' ';'    { sintactico.agregarAnalisis("Se reconoció una invocación a una función (Línea " + AnalizadorLexico.LINEA + ")"); }
                | ID '(' ')' error ';'  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta especificar un parámetro al invocar a la función."); }
                | ID '(' ID ')' error   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la invocación a la función."); }
                | ID '(' CTE ')' error  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la invocación a la función."); }
                ;

sentencia_if : IF '(' condicion ')' THEN cuerpo_if ENDIF ';'                      { sintactico.agregarAnalisis("Se reconoció una sentencia IF. (Línea " + AnalizadorLexico.LINEA + ")"); }
             | IF '(' condicion ')' THEN cuerpo_if ELSE cuerpo_else ENDIF ';'     { sintactico.agregarAnalisis("Se reconoció una sentencia IF. (Línea " + AnalizadorLexico.LINEA + ")"); }
             | IF condicion error                                                 { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición de IF debe estar entre paréntesis."); }
             | IF '(' condicion  THEN error                                       { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros."); }
             | IF '(' condicion ')' cuerpo_if error                               { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta la declaración de THEN."); }
             | IF '(' condicion ')' THEN cuerpo_if error ';'                      { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta cierre de ENDIF."); }
             | IF '(' condicion ')' THEN cuerpo_if ENDIF error                    { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de ENDIF."); }
             | IF '(' condicion ')' THEN cuerpo_if ELSE cuerpo_else error ';'     { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta cierre de ENDIF."); }
             | IF '(' condicion ')' THEN cuerpo_if ELSE cuerpo_else ENDIF error   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de ENDIF."); }
             ;

cuerpo_if : bloque_de_sentencias
          ;

cuerpo_else : bloque_de_sentencias
            ;

sentencia_while : WHILE '(' condicion ')' DO BEGIN bloque_sentencias_while END ';'     {
                                                                                            sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                            while ($3.ival);
                                                                                       }
                | WHILE '(' condicion ')' DO BEGIN END ';'                             {
                                                                                            sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                            while ($3.ival);
                                                                                       }
                | WHILE '(' condicion ')' DO sentencias_while                          {
                                                                                            sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                            while ($3.ival);
                                                                                       }
                | WHILE condicion error                                                { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición de WHILE debe estar entre paréntesis."); }
                | WHILE '(' condicion DO error                                         { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta paréntesis de cierre en la lista de parámetros."); }
                | WHILE '(' condicion ')' BEGIN error                                  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba DO, se leyó BEGIN."); }
                | WHILE '(' condicion ')' DO BEGIN bloque_sentencias_while ';' error   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba END, se leyó ';'."); }
                | WHILE '(' condicion ')' DO BEGIN bloque_sentencias_while END error   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
                ;

sentencias_ejecutables_sin_try_catch : asignacion
                                     | salida
                                     | invocacion_func
                                     | sentencia_if
                                     | sentencia_while
                                     | sentencia_try_catch error  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los bloques TRY/CATCH no pueden anidarse"); }
                                     ;

sentencia_try_catch : TRY sentencias_ejecutables_sin_try_catch CATCH BEGIN bloque_sentencias_ejecutables END ';'     { sintactico.agregarAnalisis("Se reconoció un bloque TRY/CATCH. (Línea " + AnalizadorLexico.LINEA + ")"); }
                    | TRY sentencias_ejecutables_sin_try_catch BEGIN error                                           { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se leyó un BEGIN sin previo reconocimiento de CATCH."); }
                    | TRY sentencias_ejecutables_sin_try_catch CATCH bloque_sentencias_ejecutables error             { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): el cuerpo de CATCH no se inicializó con BEGIN."); }
                    | TRY sentencias_ejecutables_sin_try_catch CATCH BEGIN bloque_sentencias_ejecutables ';' error   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): cuerpo de CATCH mal cerrado; falta END."); }
                    | TRY sentencias_ejecutables_sin_try_catch CATCH BEGIN bloque_sentencias_ejecutables END error   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
                    ;

sentencias_ejecutables_func : sentencias_ejecutables
                            | contrato
                            ;

sentencias_while : sentencias_ejecutables
                 | sentencias_declarativas
                 | sentencia_break
                 ;

contrato : CONTRACT ':' '(' condicion ')' ';'    { sintactico.agregarAnalisis("Se reconoció una definición de contrato. (Línea " + AnalizadorLexico.LINEA + ")"); }
         | CONTRACT ':' condicion error          { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): la condición debe estar entre paréntesis."); }
         | CONTRACT ':' '(' ')' error            { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): CONTRACT debe tener al menos una condición como parámetro."); }
         | CONTRACT ':' '(' condicion ';' error  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el paréntesis de cierre para los parámetros de CONTRACT."); }
         | CONTRACT ':' '(' condicion ')' error  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de los parámetros de CONTRACT."); }
         ;

sentencia_break : BREAK ';'    { sintactico.agregarAnalisis("Se reconoció una sentencia de BREAK. (Línea " + AnalizadorLexico.LINEA + ")"); }
                | BREAK error  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de BREAK."); }
                ;

condicion : expresion
          | condicion comparador expresion
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
       | CTE {
                String tipo = sintactico.getTipoFromTS($1.ival);
                if (tipo.equals("LONG"))
                    sintactico.verificarRangoEnteroLargo($1.ival);
             }
       | '-' CTE {  sintactico.setNegativoTablaSimb($2.ival); }
       ;

comparador : '<'
           | '>'
           | MENORIGUAL
           | MAYORIGUAL
           | IGUAL
           | DISTINTO
           | AND
           | OR
           ;

tipo : LONG     {
                    sintactico.setTipo("LONG");
                    $$.sval = new String("LONG");
                }
     | SINGLE   {
                    sintactico.setTipo("SINGLE");
                    $$.sval = new String("SINGLE");
                }
     ;

%%

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