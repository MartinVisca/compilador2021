package assembler;

import analizadorLexico.RegistroSimbolo;
import analizadorSintactico.AnalizadorSintactico;
import analizadorSintactico.PolacaInversa;

import java.io.*;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

@SuppressWarnings("all")
public class GeneradorCodigoAssembler {
    ///// CONSTANTES /////
    private final static String USO_VARIABLE = "VARIABLE";
    private final static String USO_CONSTANTE = "CONSTANTE";
    private final static String USO_CADENA_CARACTERES = "CADENA DE CARACTERES";
    private final static String LABEL = "@LABEL_";


    ///// ATRIBUTOS /////
    private String assemblerGenerado;                                   // String con el código assembler ya generado
    private StringBuffer codigoAssembler;                               // String que va almacenando el código a medida que se va generando
    private static final Stack<RegistroSimbolo> pila = new Stack<>();   // Por cada entrada, la pila almacenará una 2-upla compuesta por el lexema y el tipo de la variable
    private AnalizadorSintactico analizadorSintactico;
    private Vector<RegistroSimbolo> tablaSimbolosAux;
    private InstruccionesAssembler traductorInstrucciones;
    private String proximoSalto;                                        // Determina el próximo tipo de salto a realizar
    private HashMap<String, String> cadenas;                            // Contiene las cadenas presentes en el código
    private HashMap<String, String> labels;                             // Estructura que contiene las labels que se van creando, a fin de poder identificarlas luego y colocarlas en sus lugares correspondientes
    private HashMap<String, String> funciones;

    // Contadores para las constantes, cadenas y variables auxiliares
    private int numeroConstante;
    private int numeroCadena;
    private int numeroVariableAuxiliar;
    private int numeroLabel;

    // Mensajes de error correspondientes a los chequeos semánticos asignados
    private final static String ERROR_OVERFLOW_SUMA = "ERROR: overflow en suma; el resultado está fuera del rango permitido para el tipo en cuestión.";
    private final static String ERROR_DIVISOR_IGUAL_A_CERO = "ERROR: el divisor de la operación es igual a cero.";
    private final static String ERROR_RECURSION_MUTUA = "ERROR: se encontró una recursión mutua en una invocación a una función.";

    // Vectores contenedores de operadores y comparadores
    private final Vector<String> operadoresBinarios;
    private final Vector<String> operadoresUnarios;
    private final Vector<String> operadoresLogicos;
    private final Vector<String> comparadores;

    // Estructura para identificar si un registro determinado está o no ocupado
    private final Vector<Boolean> registros;

    // Archivo que contiene el código generado
    File codigoGenerado;


    ///// MÉTODOS /////
    /**
     * Constructor de la clase.
     */
    public GeneradorCodigoAssembler(AnalizadorSintactico analizadorSintactico) {
        this.tablaSimbolosAux = new Vector<>();
        this.analizadorSintactico = analizadorSintactico;
        this.traductorInstrucciones = new InstruccionesAssembler();
        this.numeroCadena = 0;
        this.numeroConstante = 0;
        this.numeroVariableAuxiliar = 0;
        this.numeroLabel = 0;
        this.proximoSalto = "";
        this.cadenas = new HashMap<>();
        this.labels = new HashMap<>();
        this.funciones = new HashMap<>();

        // Inicialización de estructuras
        this.operadoresBinarios = new Vector<>();
        this.operadoresUnarios = new Vector<>();
        this.operadoresLogicos = new Vector<>();
        this.comparadores = new Vector<>();
        this.registros = new Vector<>();

        // Operadores binarios aritméticos
        this.operadoresBinarios.add("+");
        this.operadoresBinarios.add("-");
        this.operadoresBinarios.add("*");
        this.operadoresBinarios.add("/");
        this.operadoresBinarios.add(":=");

        // Operadores unarios
        this.operadoresUnarios.add("BI");       // Salto de la polaca
        this.operadoresUnarios.add("BF");       // Salto de la polaca
        this.operadoresUnarios.add("PRINT");
        this.operadoresUnarios.add("CALL");     // Call a la función

        // Comparadores
        this.comparadores.add("==");
        this.comparadores.add("<>");
        this.comparadores.add("<=");
        this.comparadores.add("<");
        this.comparadores.add(">=");
        this.comparadores.add(">");

        // Operadores lógicos
        this.operadoresLogicos.add("&&");
        this.operadoresLogicos.add("||");

        // Seteo de booleanos de la estructura identificadora de la ocupación de registros. Se agrega un 'false' en cuatro posiciones consecutivas, representado los registros A, B, C y D.
        this.registros.add(false);
        this.registros.add(false);
        this.registros.add(false);
        this.registros.add(false);

        // Inicialización de archivo
        this.codigoGenerado = new File("./codigos_generados.asm");
    }

    /**
     * Retorna el código generado por el traductor a Assembler.
     * @return
     */
    public File getCodigoAssembler() {
        try {
            // Creo el nuevo archivo y seteo el buffer para poder escribir en el mismo
            this.codigoGenerado.createNewFile();
            FileWriter writer = new FileWriter(this.codigoGenerado.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(writer);

            // Escribo el código generado en el archivo y lo cierro
            bw.write(this.generarAssembler());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return codigoGenerado;
    }

    /**
     * Método que genera el cuerpo del código Assembler.
     * @return
     */
    public String generarAssembler() {
        this.codigoAssembler = new StringBuffer();

        this.codigoAssembler.append("; \\masm32\\bin\\ml /c /Zd /coff \n");
        this.codigoAssembler.append("; \\masm32\\bin\\Link /SUBSYSTEM:CONSOLE \n");
        this.codigoAssembler.append(".386\n");
        this.codigoAssembler.append(".model flat, stdcall\n");
        this.codigoAssembler.append("option casemap :none\n\n");
        this.codigoAssembler.append(";------------ INCLUDES ------------\n");
        this.codigoAssembler.append("include \\masm32\\include\\windows.inc\n");
        this.codigoAssembler.append("include \\masm32\\macros\\macros.asm\n");
        this.codigoAssembler.append("include \\masm32\\include\\masm32.inc\n");
        this.codigoAssembler.append("include \\masm32\\include\\kernel32.inc\n");
        this.codigoAssembler.append("include \\masm32\\include\\user32.inc\n");
        this.codigoAssembler.append("include \\masm32\\include\\gdi32.inc\n\n");
        this.codigoAssembler.append(";------------ LIBRERÍAS ------------\n");
        this.codigoAssembler.append("includelib \\masm32\\lib\\masm32.lib\n");
        this.codigoAssembler.append("includelib \\masm32\\lib\\gdi32.lib\n");
        this.codigoAssembler.append("includelib \\masm32\\lib\\kernel32.lib\n");
        this.codigoAssembler.append("includelib \\masm32\\lib\\user32.lib\n");
        this.codigoAssembler.append("\n");

        this.codigoAssembler.append("\n.DATA\n");
        this.codigoAssembler.append(this.generarPuntoData());

        this.codigoAssembler.append("\n.CODE\n");
        this.codigoAssembler.append(this.generarPuntoCode());

        return this.codigoAssembler.toString();
    }

    /**
     * Generación de la sección .DATA del código Assembler.
     * @return
     */
    public String generarPuntoData() {
        StringBuffer puntoData = new StringBuffer();

        puntoData.append("overflowSuma db \"Error: El resultado de la suma ejecutada no está dentro del rango permitido\" , 0\n");
        puntoData.append("divisionPorCero db \"Error: La división por cero no es una operación válida\" , 0\n");
        puntoData.append("recursionMutua db \"Error: Se encontró una recursión mutua en una invocación a una función.\" , 0\n");
        puntoData.append("aux_mem_2bytes dw ?\n");

        this.codigoAssembler.append("\n;------------ VARIABLES ------------\n");

        if (!this.getVariablesDeclaradas().isEmpty())
            puntoData.append(this.getVariablesDeclaradas());

        puntoData.append("\n;------------ VARIABLES AUXILIARES ------------\n");

        if (!this.getVariablesAuxiliaresDeclaradas().isEmpty())
            puntoData.append(this.getVariablesAuxiliaresDeclaradas());

        return puntoData.toString();
    }

    /**
     * Generación de la sección .CODE del código Assembler.
     * @return
     */
    public String generarPuntoCode() {
        StringBuffer puntoCode = new StringBuffer();

        puntoCode.append("START:\n");
        puntoCode.append(this.generarStart());
        puntoCode.append("JMP @END_CODE\n\n");

        // Seteo de corte por error de overflow en la suma.
        puntoCode.append("@ERROR_OVERFLOW:");
        puntoCode.append("invoke MessageBox, NULL, addr overflowSuma, addr overflowSuma, MB_OK\n");
        puntoCode.append("JMP @END_CODE\n");

        // Seteo de corte por error de división por cero.
        puntoCode.append("@ERROR_DIVIDEZERO:");
        puntoCode.append("invoke MessageBox, NULL, addr divisionPorCero, addr divisionPorCero, MB_OK\n");
        puntoCode.append("JMP @END_CODE\n");

        // Seteo de corte por error de overflow en la suma.
        puntoCode.append("@ERROR_RECURSION_MUTUA:");
        puntoCode.append("invoke MessageBox, NULL, addr recursionMutua, addr recursionMutua, MB_OK\n");
        puntoCode.append("JMP @END_CODE\n");

        // Fin de programa.
        puntoCode.append("\n@END_CODE:");
        puntoCode.append("invoke ExitProcess, 0\n");
        puntoCode.append("END START");

        return puntoCode.toString();
    }

    /**
     * Convierte a Assembler las variables que esten declaradas en la tabla de símbolos propia del analizador sintáctico.
     * @return
     */
    private String getVariablesDeclaradas() {
        StringBuffer variables = new StringBuffer();
        Vector<RegistroSimbolo> tablaSimbolos = analizadorSintactico.getTablaSimbolos();

        for (RegistroSimbolo entrada : tablaSimbolos) {
            String usoEntrada = entrada.getUso();

            if (usoEntrada.equals(this.USO_VARIABLE)) { // Si es VARIABLE se agrega el lexema de la misma y el tamaño asignado (8 bytes para LONG, 4 para SINGLE).
                variables.append(entrada.getLexema());
                if (entrada.getTipoVariable().equals("LONG"))
                    variables.append(" dd ? \n");
                else if (entrada.getTipoVariable().equals("SINGLE"))
                    variables.append(" dw ? \n");
            } else if (usoEntrada.equals(this.USO_CONSTANTE)) { // Si es CONSTANTE se agrega CTE y luego el tamaño de la misma, los cuales coinciden con los asignados para las variables.
                if (entrada.getTipoVariable().equals("LONG")) {
                    variables.append("Constante" + this.numeroConstante);
                    variables.append("dd ? \n");
                    variables.append(entrada.getLexema() + "\n");
                    this.numeroConstante++;
                }
            } else if (usoEntrada.equals(this.USO_CADENA_CARACTERES)) { // Si es CADENA se agrega la cadena con un tamaño predefinido de 1 byte.
                variables.append("Cadena" + this.numeroCadena + " db ? \n");
                variables.append(entrada.getLexema() + ", 0 \n");
                this.cadenas.put(entrada.getLexema(), "Cadena" + this.numeroCadena);
                this.numeroCadena++;
            }
        }

        return variables.toString();
    }

    /**
     * Convierte a Assembler las variables auxiliares que esten declaradas en la tabla de símbolos propia del analizador sintáctico.
     * @return
     */
    private String getVariablesAuxiliaresDeclaradas() {
        StringBuffer variablesAuxiliares = new StringBuffer();
        variablesAuxiliares.append("aux_edx dw ?\n");

        for (RegistroSimbolo entrada : this.tablaSimbolosAux) {
            String usoEntrada = entrada.getUso();

            if (usoEntrada.equals(this.USO_VARIABLE)) { // Si es VARIABLE se agrega el lexema de la misma y el tamaño asignado (8 bytes para LONG, 4 para SINGLE).
                variablesAuxiliares.append(entrada.getLexema());
                if (entrada.getTipoVariable().equals("LONG"))
                    variablesAuxiliares.append(" dd ? \n");
                else if (entrada.getTipoVariable().equals("SINGLE"))
                    variablesAuxiliares.append(" dw ? \n");
            } else if (usoEntrada.equals(this.USO_CONSTANTE)) { // Si es CONSTANTE se agrega CTE y luego el tamaño de la misma, los cuales coinciden con los asignados para las variables.
                if (entrada.getTipoVariable().equals("LONG")) {
                    variablesAuxiliares.append("Constante" + this.numeroConstante);
                    variablesAuxiliares.append("dd ? \n");
                    variablesAuxiliares.append(entrada.getLexema() + "\n");
                    this.numeroConstante++;
                }
            } else if (usoEntrada.equals(this.USO_CADENA_CARACTERES)) { // Si es CADENA se agrega la cadena con un tamaño predefinido de 1 byte.
                variablesAuxiliares.append("Cadena" + this.numeroCadena + " db ? \n");
                variablesAuxiliares.append(entrada.getLexema() + ", 0 \n");
                this.numeroCadena++;
            }
        }

        return variablesAuxiliares.toString();
    }

    /**
     * Retorna el nombre a usar en la variable auxiliar a declarar, incrementando el atributo que controla su cantidad.
     * @return
     */
    public String getNombreAuxiliar() {
        String auxiliar = "@aux" + String.valueOf(this.numeroVariableAuxiliar);
        this.numeroVariableAuxiliar++;
        return auxiliar;
    }

    /**
     * Retorna el label a usar en las diferentes sentencias de control del código.
     * @return
     */
    public String getNombreLabel() {
        String label = this.LABEL + this.numeroLabel;
        this.numeroLabel++;
        return label;
    }

    /**
     * Chequea si la cadena es un número entero.
     * @param cadena
     * @return
     */
    public static boolean isNumber(String cadena) {
        if (cadena == null)
            return false;

        try {
            Integer number = Integer.parseInt(cadena);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    /**
     * Genera el cuerpo del START, perteneciente a .CODE.
     * @return
     */
    public String generarStart() {
        StringBuffer start = new StringBuffer();
        PolacaInversa polaca = this.analizadorSintactico.getPolaca();
        Vector<RegistroSimbolo> tablaSimbolos = analizadorSintactico.getTablaSimbolos();
        Boolean agregoAPila = false;

        // Cargo el hash con los labels y las direcciones de salto
        for (int i = 0; i < polaca.getSize(); i++) {
            String simboloPolaca = polaca.getElemento(i).toString();
            String nextSimbolo = "";

            if (i < polaca.getSize() - 1)
                 nextSimbolo = polaca.getElemento(i + 1).toString();

            if (this.isNumber(simboloPolaca)
                    && (nextSimbolo.equals("BF") || nextSimbolo.equals("BI"))
                    && !nextSimbolo.equals("")) {
                String label = this.getNombreLabel();
                this.labels.put(simboloPolaca, label);
            }

            if (simboloPolaca.contains("INIC")) {
                String nombreFuncion = simboloPolaca.substring(5, simboloPolaca.length()).replace("@", "_");

                this.labels.put(String.valueOf(i), "@" + nombreFuncion);
                this.funciones.put("@" + nombreFuncion, String.valueOf(i));
            }

            if (simboloPolaca.contains("FIN")) {
                String nombreFuncion = simboloPolaca.substring(4, simboloPolaca.length());

                for (int j = 0; j < polaca.getSize(); j++) {
                    if (polaca.getElemento(j).equals("CALL") && polaca.getElemento(j - 1).equals(nombreFuncion))
                        this.labels.put(String.valueOf(i), "@CALL_" + j);
                }
            }
        }

        for (int i = 0; i < polaca.getSize(); i++) {
            String simboloPolaca = polaca.getElemento(i).toString();

            for (RegistroSimbolo simboloTabla : tablaSimbolos) {
                if (simboloTabla.getAmbito().equals(simboloPolaca) || (this.isNumber(simboloPolaca) && simboloTabla.getLexema().equals(simboloPolaca))) {
                    pila.push(simboloTabla);
                    agregoAPila = true;
                    break;
                }
            }

            // Seteo la label en su posición
            if (this.labels.keySet().contains(String.valueOf(i))) {
                String label = this.labels.get(String.valueOf(i));
                if (label.contains("CALL"))
                    start.append("JMP " + label + "\n");
                else
                    start.append(label + ":\n");
            }

            if (!agregoAPila) {
                String variableAuxiliar = this.getNombreAuxiliar();
                RegistroSimbolo auxReg = new RegistroSimbolo(variableAuxiliar, "ID");

                if (this.operadoresBinarios.contains(simboloPolaca)) {
                    RegistroSimbolo operando2 = pila.pop();
                    RegistroSimbolo operando1 = pila.pop();     

                    switch(simboloPolaca) {
                        case "+":
                            if (operando1.getTipoVariable().equals("LONG") && operando2.getTipoVariable().equals("LONG")) { // Si los dos son LONG
                                start.append(traductorInstrucciones.sumaLONG(operando1.getLexema(), operando2.getLexema(), variableAuxiliar));
                                auxReg.setTipoVariable("LONG");
                            } else if (operando1.getTipoVariable().equals("SINGLE") && operando2.getTipoVariable().equals("SINGLE")) {
                                start.append(traductorInstrucciones.sumaSINGLE(operando1.getLexema(), operando2.getLexema(), variableAuxiliar, false));
                                auxReg.setTipoVariable("SINGLE");
                            } else if (operando2.getTipoVariable().equals("LONG")) {
                                start.append(traductorInstrucciones.sumaSINGLE(operando1.getLexema(), operando2.getLexema(), variableAuxiliar, false));
                                auxReg.setTipoVariable("SINGLE");
                            } else {
                                start.append(traductorInstrucciones.sumaSINGLE(operando1.getLexema(), operando2.getLexema(), variableAuxiliar, true));
                                auxReg.setTipoVariable("SINGLE");
                            }

                            auxReg.setTipoToken("ID");
                            auxReg.setLexema(variableAuxiliar);
                            pila.push(auxReg);
                            tablaSimbolosAux.add(auxReg);
                            break;

                        case "-":
                            if (operando1.getTipoVariable().equals("LONG") && operando2.getTipoVariable().equals("LONG")) {
                                start.append(traductorInstrucciones.restaLONG(operando1.getLexema(), operando2.getLexema(), variableAuxiliar));
                                auxReg.setTipoVariable("LONG");
                            } else if (operando1.getTipoVariable().equals("SINGLE") && operando2.getTipoVariable().equals("SINGLE")) {
                                start.append(traductorInstrucciones.restaSINGLE(operando1.getLexema(), operando2.getLexema(), variableAuxiliar, false));
                                auxReg.setTipoVariable("SINGLE");
                            } else if (operando2.getTipoVariable().equals("LONG")) {
                                start.append(traductorInstrucciones.restaSINGLE(operando1.getLexema(), operando2.getLexema(), variableAuxiliar, false));
                                auxReg.setTipoVariable("SINGLE");
                            } else {
                                start.append(traductorInstrucciones.restaSINGLE(operando1.getLexema(), operando2.getLexema(), variableAuxiliar, true));
                                auxReg.setTipoVariable("SINGLE");
                            }

                            auxReg.setTipoToken("ID");
                            auxReg.setLexema(variableAuxiliar);
                            pila.push(auxReg);
                            tablaSimbolosAux.add(auxReg);
                            break;

                        case "*":
                            if (operando1.getTipoVariable().equals("LONG") && operando2.getTipoVariable().equals("LONG")) {
                                start.append(traductorInstrucciones.multiplicacionLONG(operando1.getLexema(), operando2.getLexema(), variableAuxiliar));
                                auxReg.setTipoVariable("LONG");
                            } else if (operando1.getTipoVariable().equals("SINGLE") && operando2.getTipoVariable().equals("SINGLE")) {
                                start.append(traductorInstrucciones.multiplicacionSINGLE(operando1.getLexema(), operando2.getLexema(), variableAuxiliar, false));
                                auxReg.setTipoVariable("SINGLE");
                            } else if (operando2.getTipoVariable().equals("LONG")) {
                                start.append(traductorInstrucciones.multiplicacionSINGLE(operando1.getLexema(), operando2.getLexema(), variableAuxiliar, false));
                                auxReg.setTipoVariable("SINGLE");
                            } else {
                                start.append(traductorInstrucciones.multiplicacionSINGLE(operando1.getLexema(), operando2.getLexema(), variableAuxiliar, true));
                                auxReg.setTipoVariable("SINGLE");
                            }

                            auxReg.setTipoToken("ID");
                            auxReg.setLexema(variableAuxiliar);
                            pila.push(auxReg);
                            tablaSimbolosAux.add(auxReg);
                            break;

                        case "/":
                            if (operando1.getTipoVariable().equals("LONG") && operando2.getTipoVariable().equals("LONG")) {
                                start.append(traductorInstrucciones.divisionLONG(operando1.getLexema(), operando2.getLexema(), variableAuxiliar));
                                auxReg.setTipoVariable("LONG");
                            } else if (operando1.getTipoVariable().equals("SINGLE") && operando2.getTipoVariable().equals("SINGLE")) {
                                start.append(traductorInstrucciones.divisionSINGLE(operando1.getLexema(), operando2.getLexema(), variableAuxiliar, false, false));
                                auxReg.setTipoVariable("SINGLE");
                            } else if (operando2.getTipoVariable().equals("LONG")) {
                                start.append(traductorInstrucciones.divisionSINGLE(operando1.getLexema(), operando2.getLexema(), variableAuxiliar, true, false));
                                auxReg.setTipoVariable("SINGLE");
                            } else {
                                start.append(traductorInstrucciones.divisionSINGLE(operando1.getLexema(), operando2.getLexema(), variableAuxiliar, true, true));
                                auxReg.setTipoVariable("SINGLE");
                            }

                            auxReg.setTipoToken("ID");
                            auxReg.setLexema(variableAuxiliar);
                            pila.push(auxReg);
                            tablaSimbolosAux.add(auxReg);
                            break;

                        case ":=":
                            if (operando1.getTipoVariable().equals("LONG") && operando2.getTipoVariable().equals("LONG"))
                                start.append(traductorInstrucciones.asignacionLONG(operando2.getLexema(), operando1.getLexema()));
                            else if (operando1.getTipoVariable().equals("SINGLE") && operando2.getTipoVariable().equals("SINGLE"))
                                start.append(traductorInstrucciones.asignacionSINGLE(operando2.getLexema(), operando1.getLexema(), false));
                            else if (operando1.getTipoVariable().equals("LONG"))
                                start.append(traductorInstrucciones.asignacionSINGLE(operando1.getLexema(), operando2.getLexema(), true));
                            else
                                analizadorSintactico.addErrorSintactico("ERROR SEMÁNTICO: No es posible realizar la conversión en la asignación. Los operandos tienen distinto tipo.");

                            break;

                        case "||":
                            start.append(traductorInstrucciones.OR(operando1.getLexema(), operando2.getLexema(), variableAuxiliar));
                            break;

                        case "&&":
                            start.append(traductorInstrucciones.AND(operando1.getLexema(), operando2.getLexema(), variableAuxiliar));
                            break;
                    }
                } else if (this.operadoresUnarios.contains(simboloPolaca)) {
                    String numeroSalto = "";
                    String label = "";

                    switch (simboloPolaca) {
                        case "PRINT":
                            String nombreCadena = this.cadenas.get(pila.pop().getLexema());

                            start.append("invoke MessageBox, NULL, addr " + nombreCadena + ", addr " + nombreCadena + ", MB_OK\n");
                            start.append("invoke ExitProcess, 0\n");

                            break;

                        case "BF":
                            numeroSalto = polaca.getElemento(i - 1).toString();
                            label = this.labels.get(numeroSalto);
                            start.append(this.proximoSalto + label + "\n");

                            break;

                        case "BI":
                            numeroSalto = polaca.getElemento(i - 1).toString();
                            label = this.labels.get(numeroSalto);
                            start.append("JMP " + label + "\n");

                            break;

                        case "CALL":
                            numeroSalto = this.funciones.get("@" + polaca.getElemento(i - 1).toString().replace("@", "_"));
                            label = this.labels.get(numeroSalto);
                            start.append("JMP " + label + "\n");
                            start.append("@CALL_" + i + ":\n");

                            break;
                    }
                } else if (this.comparadores.contains(simboloPolaca)) {
                    RegistroSimbolo operando1 = pila.pop();
                    RegistroSimbolo operando2 = pila.pop();

                    if (operando1.getTipoVariable().equals("LONG") && operando2.getTipoVariable().equals("LONG"))
                        start.append(traductorInstrucciones.comparadorLONG(operando1.getLexema(), operando2.getLexema()));
                    else if (operando1.getTipoVariable().equals("SINGLE") && operando2.getTipoVariable().equals("SINGLE"))
                        start.append(traductorInstrucciones.comparadorSINGLE(operando1.getLexema(), operando2.getLexema()));
                    else
                        analizadorSintactico.addErrorSintactico("ERROR SEMÁNTICO: No es posible realizar comparaciones entre diferentes tipos.");

                    // Se setea el próximo salto en contra condición para poder traducir los saltos a la rama del else.
                    switch(simboloPolaca) {
                        case ">=":
                            this.proximoSalto = "JL ";
                            break;
                            
                        case "<=":
                            this.proximoSalto = "JG ";
                            break;
                            
                        case "<>":
                            this.proximoSalto = "JE ";
                            break;
                            
                        case "==":
                            this.proximoSalto = "JNE ";
                            break;
                            
                        case "<":
                            this.proximoSalto = "JGE ";
                            break;
                            
                        case ">":
                            this.proximoSalto = "JLE ";
                            break;
                    }
                }
            } else
                agregoAPila = false;
        }

        return start.toString();
    }
}
