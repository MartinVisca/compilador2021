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

encabezado_programa : ID bloque_sentencias_declarativas BEGIN       { sintactico.agregarAPolaca("START"); }
                    | ID BEGIN                                      { sintactico.agregarAPolaca("START"); }
                    | ID bloque_sentencias_declarativas error       { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta BEGIN al abrir el bloque de sentencias ejecutables."); }
                    | ID error                                      { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta BEGIN al abrir el bloque de sentencias ejecutables."); }
                    ;

programa : encabezado_programa bloque_sentencias_ejecutables END ';'                            {
                                                                                                    sintactico.agregarAnalisis("Se reconoció un programa. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                    sintactico.setUsoTablaSimb($1.ival, "NOMBRE DE PROGRAMA");
                                                                                                    sintactico.agregarAPolaca("END START");
                                                                                                }
         | encabezado_programa END ';'                                                                     {
                                                                                                    sintactico.agregarAnalisis("Se reconoció un programa. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                    sintactico.setUsoTablaSimb($1.ival, "NOMBRE DE PROGRAMA");
                                                                                                    sintactico.agregarAPolaca("END START");
                                                                                                }
         | encabezado_programa bloque_sentencias_ejecutables ';' error      { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END al cerrar el bloque de sentencias ejecutables."); }
         | encabezado_programa bloque_sentencias_ejecutables END error      { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del END en el bloque de sentencias ejecutables."); }
         | encabezado_programa ';' error                                    { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta END al cerrar el bloque de sentencias ejecutables."); }
         | encabezado_programa END error                                    { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del END en el bloque de sentencias ejecutables."); }
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

tipo_func : FUNC    {
                        sintactico.setTipo("FUNC");
                        $$.sval = new String("FUNC");
                    }
          ;

sentencias_declarativas : tipo lista_de_variables ';'        { sintactico.agregarAnalisis("Se reconoció una declaración de variable. (Línea " + AnalizadorLexico.LINEA + ")"); }
                        | tipo lista_de_variables error      { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' al final de la declaración de variable."); }
                        | tipo error                         { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' al final de la declaración de variable."); }
                        | declaracion_func
                        | tipo_func lista_de_variables ';'        { sintactico.agregarAnalisis("Se reconoció una declaración de variable de tipo función. (Línea " + AnalizadorLexico.LINEA + ")"); }
                        | tipo_func lista_de_variables error ';'  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego del bloque."); }
                        ;

lista_de_variables : ID                             {
                                                          sintactico.setAmbitoTablaSimb($1.ival);
                                                          if(sintactico.getTipo().equals("FUNC")) {
                                                                if(!sintactico.variableFueDeclarada($1.ival))
                                                                    sintactico.setUsoTablaSimb($1.ival, "FUNC");
                                                          }
                                                          else {
                                                                sintactico.setUsoTablaSimb($1.ival, "VARIABLE");
                                                                if (!sintactico.variableFueDeclarada($1.ival))
                                                                    sintactico.setTipoVariableTablaSimb($1.ival);
                                                          }
                                                    }
                   | lista_de_variables ',' ID      {
                                                        sintactico.setAmbitoTablaSimb($3.ival);
                                                        if(sintactico.getTipo().equals("FUNC")) {
                                                            if(!sintactico.variableFueDeclarada($3.ival))
                                                                sintactico.setUsoTablaSimb($3.ival, "FUNC");
                                                        }
                                                        else {
                                                            sintactico.setUsoTablaSimb($3.ival, "VARIABLE");
                                                            if (!sintactico.variableFueDeclarada($3.ival))
                                                            sintactico.setTipoVariableTablaSimb($3.ival);
                                                        }
                                                    }
                   | lista_de_variables ID error    { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta una ',' entre identificadores."); }
                   ;

encabezado_func : tipo FUNC ID '('  {
                                        sintactico.setUsoTablaSimb($3.ival, "FUNC");
                                        sintactico.setAmbitoTablaSimb($3.ival);
                                        if (sintactico.variableFueDeclarada($3.ival))
                                            sintactico.setErrorFunc(true);
                                        else {
                                            sintactico.setTipoVariableTablaSimb($3.ival);
                                            sintactico.agregarReferencia($3.ival);
                                            sintactico.setFuncReferenciadaTablaSimb($3.ival, sintactico.getAmbitoFromTS($3.ival));
                                            sintactico.setAmbito(sintactico.getAmbito() + "@" + sintactico.getLexemaFromTS($3.ival));
                                        }
                                    }
                | tipo ID '(' error   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta FUNC en la definición de la función."); }
                ;

parametro : tipo ID ')' BEGIN       {
                                        if(!sintactico.huboErrorFunc()) {
                                            sintactico.setAmbitoTablaSimb($2.ival);
                                            sintactico.setTipoVariableTablaSimb($2.ival);
                                            sintactico.setUsoTablaSimb($2.ival, "PARAMETRO");
                                            sintactico.agregarAPolaca("INIC_" + sintactico.getAmbitoFromTS(sintactico.obtenerReferencia()));
                                        }
                                        else
                                            sintactico.eliminarRegistroTS($2.ival);
                                    }
          | tipo ID ')' {
                            if(!sintactico.huboErrorFunc()) {
                                sintactico.setAmbitoTablaSimb($2.ival);
                                sintactico.setTipoVariableTablaSimb($2.ival);
                                sintactico.setUsoTablaSimb($2.ival, "PARAMETRO");
                                sintactico.agregarAPolaca("INIC_" + sintactico.getAmbitoFromTS(sintactico.obtenerReferencia()));
                            }
                            else
                                sintactico.eliminarRegistroTS($2.ival);
                       } bloque_sentencias_declarativas BEGIN
          ;

declaracion_func : encabezado_func parametro bloque_sentencias_ejecutables_funcion RETURN '(' expresion ')' ';' END ';' {
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
                 | encabezado_func parametro RETURN '(' expresion ')' ';' END ';'                                       {
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
                 ;

sentencias_ejecutables : asignacion
                       | salida
                       | sentencia_if
                       | sentencia_while
                       | sentencia_try_catch
                       ;

op_asignacion : OPASIGNACION    { $$.sval = new String(":="); }
              ;

asignacion : ID op_asignacion expresion ';'      {
                                                     int referencia = sintactico.referenciaCorrecta($1.ival);
                                                     if (referencia == -1)
                                                         sintactico.addErrorSintactico("ERROR SEMÁNTICO (Línea " + (AnalizadorLexico.LINEA) + "): error en la asignación, la variable a asignar se encuentra fuera de alcance.");
                                                     else {
                                                         if (!sintactico.huboErrorInvocacion()) {
                                                             if(sintactico.getUsoFromTS(referencia).equals("FUNC") && sintactico.getTipoVariableFromTS(referencia).equals(""))
                                                                 if(sintactico.getUsoFromTS(sintactico.getRefInvocacion()).equals("FUNC")) {
                                                                     sintactico.agregarAnalisis("Se reconoció una operación de asignación. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                     if(!sintactico.getFuncReferenciadaFromTS(sintactico.getRefInvocacion()).equals("")) {
                                                                         sintactico.setTipoVariableTablaSimb(referencia, sintactico.getTipoVariableFromTS(sintactico.getRefInvocacion()));
                                                                         sintactico.setFuncReferenciadaTablaSimb(referencia, sintactico.getFuncReferenciadaFromTS(sintactico.getRefInvocacion()));
                                                                         sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(referencia));
                                                                         sintactico.agregarAPolaca($2.sval);
                                                                     }
                                                                     else {
                                                                         sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(referencia));
                                                                         sintactico.agregarAPolaca($2.sval);
                                                                     }

                                                                 }
                                                                 else
                                                                     sintactico.addErrorSintactico("ERROR SEMÁNTICO (Línea " + (AnalizadorLexico.LINEA) + "): error en la asignación, no se puede asignar a un tipo FUNC una variable de distinto tipo.");
                                                             else {
                                                                 sintactico.agregarAnalisis("Se reconoció una operación de asignación. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                 sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(referencia));
                                                                 sintactico.agregarAPolaca($2.sval);
                                                             }
                                                         }
                                                         else {
                                                             sintactico.addErrorSintactico("ERROR SEMÁNTICO (Línea " + (AnalizadorLexico.LINEA) + "): error en la asignación, se produjo error en la invocación a función.");
                                                             sintactico.setErrorInvocacion(false);
                                                         }
                                                     }
                                                 }
           | ID op_asignacion expresion error  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' luego de la asignación."); }
           | ID expresion error                { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta el operador de asignación."); }
           | ID op_asignacion error            { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + (AnalizadorLexico.LINEA - 1) + "): falta ';' luego de la asignación."); }
           ;

salida : PRINT '(' CADENA ')' ';'     {
                                            sintactico.agregarAnalisis("Se reconoció una impresión por pantalla de una cadena. (Línea " + AnalizadorLexico.LINEA + ")");
                                            sintactico.setUsoTablaSimb($3.ival, "CADENA DE CARACTERES");
                                            sintactico.agregarAPolaca(sintactico.getLexemaFromTS($3.ival));
                                            sintactico.agregarAPolaca("PRINT");
                                      }
       | PRINT '(' CADENA ')' error   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de la impresión de cadena."); }
       | PRINT '(' CADENA error ';'   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): cierre erróneo de la lista de parámetros de PRINT."); }
       | PRINT CADENA error ';'       { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los parámetros de PRINT deben estar entre paréntesis."); }
       | '(' CADENA error             { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se esperaba PRINT, se encontró '('."); }
       | PRINT '(' ')' error ';'      { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta declarar una cadena para PRINT."); }
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

cuerpo_if : bloque_de_sentencias    {
                                        sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca() + 2));   // Desapila dirección incompleta y completa el destino de BF
                                        sintactico.agregarAPolaca(" ");                               // Crea paso incompleto
                                        sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);  // Apila el nro de paso incompleto
                                        sintactico.agregarAPolaca("BI");                              // Se crea el paso BI
                                    }
          ;

cuerpo_else : bloque_de_sentencias  { sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca())); }
            ;

encabezado_while : WHILE '('    { sintactico.pushElementoPila(sintactico.getSizePolaca()); }
                 ;

sentencia_while : encabezado_while condicion ')' DO BEGIN bloque_sentencias_while END ';'   {
                                                                                                sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                if (sintactico.tieneSentBreak()) {
                                                                                                        sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca() + 2)); // Desapila dirección incompleta y completa el destino de BI del BREAK
                                                                                                        sintactico.setTieneSentBreak(false);
                                                                                                }
                                                                                                sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca() + 2)); // Desapila dirección incompleta y completa el destino de BF
                                                                                                sintactico.agregarAPolaca(String.valueOf(sintactico.popElementoPila()));    // Desapilar paso de inicio
                                                                                                sintactico.agregarAPolaca("BI");
                                                                                            }
                | encabezado_while condicion ')' DO BEGIN END ';'                           {
                                                                                                sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca() + 2)); // Desapila dirección incompleta y completa el destino de BF
                                                                                                sintactico.agregarAPolaca(String.valueOf(sintactico.popElementoPila()));    // Desapilar paso de inicio
                                                                                                sintactico.agregarAPolaca("BI");
                                                                                            }
                | encabezado_while condicion ')' DO sentencias_while                        {
                                                                                                sintactico.agregarAnalisis("Se reconoció una declaración de loop while. (Línea " + AnalizadorLexico.LINEA + ")");
                                                                                                sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca() + 2)); // Desapila dirección incompleta y completa el destino de BF
                                                                                                sintactico.agregarAPolaca(String.valueOf(sintactico.popElementoPila()));    // Desapilar paso de inicio
                                                                                                sintactico.agregarAPolaca("BI");
                                                                                            }
                ;

sentencias_ejecutables_sin_try_catch : asignacion
                                     | salida
                                     | sentencia_if
                                     | sentencia_while
                                     | sentencia_try_catch error  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): los bloques TRY/CATCH no pueden anidarse"); }
                                     ;

encabezado_try_catch: TRY sentencias_ejecutables_sin_try_catch CATCH            {
                                                                                    // Desapila dirección incompleta y completa el destino de BF
                                                                                    sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), String.valueOf(sintactico.getSizePolaca()));
                                                                                }
                    | TRY sentencias_ejecutables_sin_try_catch BEGIN error      { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): se leyó un BEGIN sin previo reconocimiento de CATCH."); }
                    ;

sentencia_try_catch : encabezado_try_catch BEGIN bloque_sentencias_ejecutables END ';'     { sintactico.agregarAnalisis("Se reconoció un bloque TRY/CATCH. (Línea " + AnalizadorLexico.LINEA + ")"); }
                    | encabezado_try_catch bloque_sentencias_ejecutables error             { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): el cuerpo de CATCH no se inicializó con BEGIN."); }
                    | encabezado_try_catch BEGIN bloque_sentencias_ejecutables ';' error   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): cuerpo de CATCH mal cerrado; falta END."); }
                    | encabezado_try_catch BEGIN bloque_sentencias_ejecutables END error   { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de END."); }
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

sentencia_break : BREAK ';'    {
                                    sintactico.agregarAnalisis("Se reconoció una sentencia de BREAK. (Línea " + AnalizadorLexico.LINEA + ")");
                                    sintactico.agregarAPolaca(" ");                               // Crea paso incompleto
                                    sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);  // Apila el nro de paso incompleto
                                    sintactico.agregarAPolaca("BI");                              // Se crea el paso BI
                                    sintactico.setTieneSentBreak(true);
                               }
                | BREAK error  { sintactico.addErrorSintactico("ERROR SINTÁCTICO (Línea " + AnalizadorLexico.LINEA + "): falta ';' luego de BREAK."); }
                ;

condicion : expresion_or    {
                                sintactico.agregarAPolaca(" ");
                                sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);
                                sintactico.agregarAPolaca("BF");
                            }
          ;

expresion_or : expresion_and
             | expresion_or comparador_or expresion_and    { sintactico.agregarAPolaca($2.sval); }
             ;

expresion_and : expresion_relacional
              | expresion_and comparador_and expresion_relacional  { sintactico.agregarAPolaca($2.sval); }
              ;

expresion_relacional : expresion
                     | expresion_relacional comparador expresion    { sintactico.agregarAPolaca($2.sval); }
                     ;

expresion : expresion '+' termino           {
                                                sintactico.agregarAnalisis("Se reconoció una suma. (Línea " + AnalizadorLexico.LINEA + ")");
                                                sintactico.agregarAPolaca("+");
                                            }
          | expresion '-' termino           {
                                                sintactico.agregarAnalisis("Se reconoció una resta. (Línea " + AnalizadorLexico.LINEA + ")");
                                                sintactico.agregarAPolaca("-");
                                            }
          | termino
          ;

termino : termino '*' factor                {
                                                sintactico.agregarAnalisis("Se reconoció una multiplicación. (Línea " + AnalizadorLexico.LINEA + ")");
                                                sintactico.agregarAPolaca("*");
                                            }
        | termino '/' factor                {
                                                sintactico.agregarAnalisis("Se reconoció una división. (Línea " + AnalizadorLexico.LINEA + ")");
                                                sintactico.agregarAPolaca("/");
                                            }
        | factor
        ;

factor : ID         {
                        int ref = sintactico.referenciaCorrecta($1.ival);
                        if (ref == -1)
                            sintactico.addErrorSintactico("ERROR SEMÁNTICO (Línea " + (AnalizadorLexico.LINEA) + "): la variable no fue declarada o se encuentra fuera de alcance.");
                        else {
                            sintactico.setRefInvocacion(ref);
                            sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(ref));
                        }
                    }
       | CTE        {
                        sintactico.setTipo(sintactico.getTipoFromTS($1.ival));
                        if (sintactico.getTipo().equals("LONG"))
                            sintactico.verificarRangoEnteroLargo($1.ival);

                        sintactico.setTipoVariableTablaSimb($1.ival);
                        sintactico.setAmbitoTablaSimb($1.ival, sintactico.getLexemaFromTS($1.ival));
                        sintactico.agregarAPolaca(sintactico.getLexemaFromTS($1.ival));
                    }
       | '-' CTE    {
                        sintactico.setTipo(sintactico.getTipoFromTS($2.ival));
                        sintactico.setTipoVariableTablaSimb($2.ival);
                        sintactico.setAmbitoTablaSimb($2.ival, sintactico.getLexemaFromTS($2.ival));
                        sintactico.agregarAPolaca(sintactico.getLexemaFromTS($2.ival));
                        sintactico.setNegativoTablaSimb($2.ival);
                        sintactico.agregarAPolaca("-");
                    }
       | ID '(' ID ')'     {
                               int referencia = sintactico.referenciaCorrecta($1.ival);
                               if (referencia == -1) {
                                   sintactico.addErrorSintactico("ERROR SEMÁNTICO(Línea " + AnalizadorLexico.LINEA + "): Error en la invocación. Variable de invocación " + sintactico.getLexemaFromTS($1.ival) + " no declarada.");
                                   sintactico.setErrorInvocacion(true);
                               }
                               else {
                                   String funcionReferenciada = sintactico.getFuncReferenciadaFromTS(referencia);
                                   if (!funcionReferenciada.equals("")) {
                                       if (sintactico.getAmbitoFromTS(referencia).equals(funcionReferenciada))
                                           sintactico.setRefInvocacion(referencia);
                                       else {
                                           int indiceFuncRef = sintactico.getIndiceFuncRef(funcionReferenciada);
                                           sintactico.setRefInvocacion(indiceFuncRef);
                                       }
                                       int refParamReal = sintactico.referenciaCorrecta($3.ival - 1);
                                       if (refParamReal == -1) {
                                           sintactico.addErrorSintactico("ERROR SEMÁNTICO(Línea " + AnalizadorLexico.LINEA + "): Error en la invocación. Parámetro de invocación " + sintactico.getLexemaFromTS($1.ival) + " no declarado.");
                                           sintactico.setErrorInvocacion(true);
                                       }
                                       else {
                                       // Comparo tipos de parámetro formal con real
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
                                   else {
                                       sintactico.addErrorSintactico("ERROR SEMÁNTICO(Línea " + AnalizadorLexico.LINEA + "): error en la invocación. La variable de invocación " + sintactico.getLexemaFromTS($1.ival) + " no hace referencia a ninguna función.");
                                       sintactico.setErrorInvocacion(true);
                                   }

                               }
                           }
       | ID '(' CTE ')'    {
                               int referencia = sintactico.referenciaCorrecta($1.ival);
                               if (referencia == -1) {
                                   sintactico.addErrorSintactico("ERROR SEMÁNTICO(Línea " + AnalizadorLexico.LINEA + "): Error en la invocación. Variable de invocación " + sintactico.getLexemaFromTS($1.ival) + " no declarada.");
                                   sintactico.setErrorInvocacion(true);
                               }
                               else {
                                   String funcionReferenciada = sintactico.getFuncReferenciadaFromTS(referencia);
                                   if (!funcionReferenciada.equals("")) {
                                       if (sintactico.getAmbitoFromTS(referencia).equals(funcionReferenciada))
                                           sintactico.setRefInvocacion(referencia);
                                       else {
                                           int indiceFuncRef = sintactico.getIndiceFuncRef(funcionReferenciada);
                                           sintactico.setRefInvocacion(indiceFuncRef);
                                       }
                                       // Comparo tipos de parámetro formal con real
                                       sintactico.setTipoVariableTablaSimb($3.ival - 1, sintactico.getTipoFromTS($3.ival -1));
                                       sintactico.setAmbitoTablaSimb($3.ival - 1, sintactico.getLexemaFromTS($3.ival - 1));
                                       int refParamFormal = sintactico.getRefInvocacion() + 1;
                                       if (!sintactico.getTipoVariableFromTS(refParamFormal).equals(sintactico.getTipoVariableFromTS($3.ival - 1))) {
                                           sintactico.addErrorSintactico("ERROR SEMÁNTICO(Línea " + AnalizadorLexico.LINEA + "): Error en la invocación. El tipo del parámetro real no coincide con el tipo del parámetro formal.");
                                           sintactico.setErrorInvocacion(true);
                                       }
                                       else {
                                           sintactico.agregarAPolaca(sintactico.getLexemaFromTS($3.ival - 1));
                                           sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(refParamFormal));
                                           sintactico.agregarAPolaca(":=");
                                           sintactico.agregarAnalisis("Se reconoció una invocación a una función (Línea " + AnalizadorLexico.LINEA + ")");
                                           sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(sintactico.getRefInvocacion()));
                                           sintactico.agregarAPolaca("CALL");
                                           int referenciaReturn = sintactico.getReferenciaReturn(sintactico.getLexemaFromTS(sintactico.getRefInvocacion()));
                                           sintactico.agregarAPolaca(sintactico.getAmbitoFromTS(referenciaReturn));
                                       }
                                   }
                                   else {
                                       sintactico.addErrorSintactico("ERROR SEMÁNTICO(Línea " + AnalizadorLexico.LINEA + "): error en la invocación. La variable de invocación " + sintactico.getLexemaFromTS($1.ival) + " no hace referencia a ninguna función.");
                                       sintactico.setErrorInvocacion(true);
                                   }
                               }
                           }
       ;

comparador : '<'            { $$.sval = new String("<"); }
           | '>'            { $$.sval = new String(">"); }
           | MENORIGUAL     { $$.sval = new String("<="); }
           | MAYORIGUAL     { $$.sval = new String(">="); }
           | IGUAL          { $$.sval = new String("=="); }
           | DISTINTO       { $$.sval = new String("<>"); }
           ;

comparador_and : AND   { $$.sval = new String("&&"); }
                        ;

comparador_or : OR    { $$.sval = new String("||"); }
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