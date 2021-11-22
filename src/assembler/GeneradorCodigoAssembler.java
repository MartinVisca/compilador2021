package assembler;

import analizadorLexico.RegistroSimbolo;
import analizadorSintactico.AnalizadorSintactico;
import analizadorSintactico.PolacaInversa;

import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

public class GeneradorCodigoAssembler {
    ///// CONSTANTES /////
    private final static String USO_VARIABLE = "VARIABLE";
    private final static String USO_CONSTANTE = "CONSTANTE";
    private final static String USO_CADENA_CARACTERES = "CADENA DE CARACTERES";


    ///// ATRIBUTOS /////
    private String assemblerGenerado;                                   // String con el código assembler ya generado
    private StringBuffer codigoAssembler;                               // String que va almacenando el código a medida que se va generando
    private static final Stack<RegistroSimbolo> pila = new Stack<>();   // Por cada entrada, la pila almacenará una 2-upla compuesta por el lexema y el tipo de la variable
    private AnalizadorSintactico analizadorSintactico;
    private Vector<RegistroSimbolo> tablaSimbolosAux;
    private InstruccionesAssembler traductorInstrucciones;
    String proximoSalto;                                                // Determina el próximo tipo de salto a realizar
    HashMap<String, String> cadenas;                                    // Contiene las cadenas presentes en el código

    // Contadores para las constantes, cadenas y variables auxiliares
    private int numeroConstante;
    private int numeroCadena;
    private int numeroVariableAuxiliar;

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


    ///// MÉTODOS /////
    /**
     * Constructor de la clase.
     */
    public GeneradorCodigoAssembler() {
        this.tablaSimbolosAux = new Vector<>();
        this.analizadorSintactico = analizadorSintactico;
        this.traductorInstrucciones = new InstruccionesAssembler();
        this.numeroCadena = 0;
        this.numeroConstante = 0;
        this.numeroVariableAuxiliar = 0;
        this.proximoSalto = "";
        this.cadenas = new HashMap<>();

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
    }

    /**
     * Método que genera el cuerpo del código Assembler.
     * @return
     */
    public String generarAssembler() {
        this.codigoAssembler = new StringBuffer();

        this.codigoAssembler.append("; \\masm32\\bin\\ml /c /Zd /coff ");
        this.codigoAssembler.append("; \\masm32\\bin\\Link /SUBSYSTEM:CONSOLE ");
        this.codigoAssembler.append(".386");
        this.codigoAssembler.append(".model flat, stdcall");
        this.codigoAssembler.append("option casemap :none");
        this.codigoAssembler.append(";------------ INCLUDES ------------");
        this.codigoAssembler.append("include \\masm32\\include\\windows.inc");
        this.codigoAssembler.append("include \\masm32\\macros\\macros.asm");
        this.codigoAssembler.append("include \\masm32\\include\\masm32.inc");
        this.codigoAssembler.append("include \\masm32\\include\\kernel32.inc");
        this.codigoAssembler.append("include \\masm32\\include\\user32.inc");
        this.codigoAssembler.append("include \\masm32\\include\\gdi32.inc");
        this.codigoAssembler.append(";------------ LIBRERÍAS ------------");
        this.codigoAssembler.append("includelib \\masm32\\lib\\masm32.lib");
        this.codigoAssembler.append("includelib \\masm32\\lib\\gdi32.lib");
        this.codigoAssembler.append("includelib \\masm32\\lib\\kernel32.lib");
        this.codigoAssembler.append("includelib \\masm32\\lib\\user32.lib");
        this.codigoAssembler.append("\n");

        this.codigoAssembler.append(".DATA ");
        this.codigoAssembler.append(this.generarPuntoData());

        this.codigoAssembler.append(".CODE");
        this.codigoAssembler.append(this.generarPuntoCode());

        return this.codigoAssembler.toString();
    }

    /**
     * Generación de la sección .DATA del código Assembler.
     * @return
     */
    public String generarPuntoData() {
        StringBuffer puntoData = new StringBuffer();

        puntoData.append("overflowSuma db \"Error: El resultado de la suma ejecutada no está dentro del rango permitido\" , 0");
        puntoData.append("divisionPorCero db \"Error: La división por cero no es una operación válida\" , 0");
        puntoData.append("recursionMutua db \"Error: Se encontró una recursión mutua en una invocación a una función.\" , 0");
        puntoData.append("aux_mem_2bytes dw ?\n");

        this.codigoAssembler.append(";------------ VARIABLES ------------");

        if (!this.getVariablesDeclaradas().isEmpty())
            puntoData.append(this.getVariablesDeclaradas());

        puntoData.append(";------------ VARIABLES AUXILIARES ------------");

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

        // Seteo de corte por error de overflow en la suma.
        puntoCode.append("JMP @END_CODE");
        puntoCode.append("@ERROR_OVERFLOW:");
        puntoCode.append("invoke MessageBox, NULL, addr overflowSuma, addr overflowSuma, MB_OK\n");

        // Seteo de corte por error de división por cero.
        puntoCode.append("JMP @END_CODE");
        puntoCode.append("@ERROR_DIVIDEZERO:");
        puntoCode.append("invoke MessageBox, NULL, addr divisionPorCero, addr divisionPorCero, MB_OK\n");

        // Seteo de corte por error de overflow en la suma.
        puntoCode.append("JMP @END_CODE");
        puntoCode.append("@ERROR_RECURSION_MUTUA:");
        puntoCode.append("invoke MessageBox, NULL, addr recursionMutua, addr recursionMutua, MB_OK\n");

        // Fin de programa.
        puntoCode.append("@END_CODE:");
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
                if (entrada.getTipoToken().equals("LONG"))
                    variables.append(" dq ? \n");
                else if (entrada.getTipoToken().equals("SINGLE"))
                    variables.append(" dw ? \n");
            } else if (usoEntrada.equals(this.USO_CONSTANTE)) { // Si es CONSTANTE se agrega CTE y luego el tamaño de la misma, los cuales coinciden con los asignados para las variables.
                if (entrada.getTipoToken().equals("LONG")) {
                    variables.append("Constante" + this.numeroConstante);
                    variables.append("dq ? \n");
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
     *
     * @return
     */
    private String getVariablesAuxiliaresDeclaradas() {
        StringBuffer variablesAuxiliares = new StringBuffer();

        variablesAuxiliares.append("aux_edx\n");

        // Ver uso de siguiente variable
        variablesAuxiliares.append("@0 dw 0\n");
        //

        for (RegistroSimbolo entrada : this.tablaSimbolosAux) {
            String usoEntrada = entrada.getUso();

            if (usoEntrada.equals(this.USO_VARIABLE)) { // Si es VARIABLE se agrega el lexema de la misma y el tamaño asignado (8 bytes para LONG, 4 para SINGLE).
                variablesAuxiliares.append(entrada.getLexema());
                if (entrada.getTipoToken().equals("LONG"))
                    variablesAuxiliares.append(" dq ? \n");
                else if (entrada.getTipoToken().equals("SINGLE"))
                    variablesAuxiliares.append(" dw ? \n");
            } else if (usoEntrada.equals(this.USO_CONSTANTE)) { // Si es CONSTANTE se agrega CTE y luego el tamaño de la misma, los cuales coinciden con los asignados para las variables.
                if (entrada.getTipoToken().equals("LONG")) {
                    variablesAuxiliares.append("Constante" + this.numeroConstante);
                    variablesAuxiliares.append("dq ? \n");
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
     * Genera el cuerpo del START, perteneciente a .CODE.
     * @return
     */
    public String generarStart() {
        StringBuffer start = new StringBuffer();
        PolacaInversa polaca = this.analizadorSintactico.getPolaca();
        Vector<RegistroSimbolo> tablaSimbolos = analizadorSintactico.getTablaSimbolos();
        Boolean agregoAPila = false;

        for (int i = 0; i < polaca.getSize(); i++) {
            String simboloPolaca = polaca.getElemento(i).toString();

            // To refactor
            for (RegistroSimbolo simboloTabla : tablaSimbolos) {
                if (simboloTabla.getLexema().equals(simboloPolaca)) {
                    pila.push(simboloTabla);
                    agregoAPila = true;
                    break;
                }
            }

            if (!agregoAPila) {
                String variableAuxiliar = "";
                variableAuxiliar = this.getNombreAuxiliar();
                RegistroSimbolo auxReg = new RegistroSimbolo(variableAuxiliar, String.valueOf(257));

                if (this.operadoresBinarios.contains(simboloPolaca)) {
                    RegistroSimbolo operando1 = pila.pop();
                    RegistroSimbolo operando2 = pila.pop();

                    switch(simboloPolaca) {
                        case "+":
                            if (operando1.getTipoToken().equals("LONG") && operando2.getTipoToken().equals("LONG")) { // Si los dos son LONG
                                start.append(traductorInstrucciones.sumaLONG(operando1.getLexema(), operando2.getLexema(), variableAuxiliar));
                                auxReg.setTipoToken("LONG");
                            } else { // Todos los demás casos se realizando con suma en SINGLE.
                                start.append(traductorInstrucciones.sumaSINGLE(operando1.getLexema(), operando2.getLexema(), variableAuxiliar));
                                auxReg.setTipoToken("SINGLE");
                            }

                            tablaSimbolosAux.add(auxReg);
                            break;

                        case "-":
                            if (operando1.getTipoToken().equals("LONG") && operando2.getTipoToken().equals("LONG")) {
                                start.append(traductorInstrucciones.restaLONG(operando1.getLexema(), operando2.getLexema(), variableAuxiliar));
                                auxReg.setTipoToken("LONG");
                            } else {
                                start.append(traductorInstrucciones.restaSINGLE(operando1.getLexema(), operando2.getLexema(), variableAuxiliar));
                                auxReg.setTipoToken("SINGLE");
                            }

                            tablaSimbolosAux.add(auxReg);
                            break;

                        case "*":
                            if (operando1.getTipoToken().equals("LONG") && operando2.getTipoToken().equals("LONG")) {
                                start.append(traductorInstrucciones.multiplicacionLONG(operando1.getLexema(), operando2.getLexema(), variableAuxiliar));
                                auxReg.setTipoToken("LONG");
                            } else {
                                start.append(traductorInstrucciones.multiplicacionSINGLE(operando1.getLexema(), operando2.getLexema(), variableAuxiliar));
                                auxReg.setTipoToken("SINGLE");
                            }

                            tablaSimbolosAux.add(auxReg);
                            break;

                        case "/":
                            if (operando1.getTipoToken().equals("LONG") && operando2.getTipoToken().equals("LONG")) {
                                start.append(traductorInstrucciones.divisionLONG(operando1.getLexema(), operando2.getLexema(), variableAuxiliar));
                                auxReg.setTipoToken("LONG");
                            } else {
                                start.append(traductorInstrucciones.divisionSINGLE(operando1.getLexema(), operando2.getLexema(), variableAuxiliar));
                                auxReg.setTipoToken("SINGLE");
                            }

                            tablaSimbolosAux.add(auxReg);
                            break;

                        case ":=":
                            if (operando1.getTipoToken().equals("LONG") && operando2.getTipoToken().equals("LONG"))
                                start.append(traductorInstrucciones.asignacionLONG(operando1.getLexema(), operando2.getLexema()));
                            else
                                start.append(traductorInstrucciones.asignacionSINGLE(operando1.getLexema(), operando2.getLexema()));

                            break;

                        case "||":
                            start.append(traductorInstrucciones.OR(operando1.getLexema(), operando2.getLexema(), variableAuxiliar));
                            break;

                        case "&&":
                            start.append(traductorInstrucciones.AND(operando1.getLexema(), operando2.getLexema(), variableAuxiliar));
                            break;
                    }
                } else if (this.operadoresUnarios.contains(simboloPolaca)) {
                    if (simboloPolaca.equals("PRINT")) {
                        String nombreCadena = this.cadenas.get(pila.pop().getLexema());

                        start.append("invoke MessageBox, NULL, addr " + nombreCadena + ", addr " + nombreCadena + ", MB_OK\n");
                        start.append("invoke ExitProcess, 0\n");
                    }
                } else if (this.comparadores.contains(simboloPolaca)) {
                    RegistroSimbolo operando1 = pila.pop();
                    RegistroSimbolo operando2 = pila.pop();

                    switch(simboloPolaca) {
                        case ">=":
                            if (operando1.getTipoToken().equals("LONG") && operando2.getTipoToken().equals("LONG"))
                                start.append(traductorInstrucciones.comparadorLONG(operando1.getLexema(), operando2.getLexema()));
                            else
                                start.append(traductorInstrucciones.comparadorSINGLE(operando1.getLexema(), operando2.getLexema()));

                            this.proximoSalto = "JGE";  // Salto para mayor igual
                            break;
                            
                        case "<=":
                            if (operando1.getTipoToken().equals("LONG") && operando2.getTipoToken().equals("LONG"))
                                start.append(traductorInstrucciones.comparadorLONG(operando1.getLexema(), operando2.getLexema()));
                            else
                                start.append(traductorInstrucciones.comparadorSINGLE(operando1.getLexema(), operando2.getLexema()));

                            this.proximoSalto = "JLE";  // Salto para menor igual

                            break;
                            
                        case "<>":
                            if (operando1.getTipoToken().equals("LONG") && operando2.getTipoToken().equals("LONG"))
                                start.append(traductorInstrucciones.comparadorLONG(operando1.getLexema(), operando2.getLexema()));
                            else
                                start.append(traductorInstrucciones.comparadorSINGLE(operando1.getLexema(), operando2.getLexema()));

                            this.proximoSalto = "JNE";  // Salto para distinto

                            break;
                            
                        case "==":
                            if (operando1.getTipoToken().equals("LONG") && operando2.getTipoToken().equals("LONG"))
                                start.append(traductorInstrucciones.comparadorLONG(operando1.getLexema(), operando2.getLexema()));
                            else
                                start.append(traductorInstrucciones.comparadorSINGLE(operando1.getLexema(), operando2.getLexema()));

                            this.proximoSalto = "JE";  // Salto para igual

                            break;
                            
                        case "<":
                            if (operando1.getTipoToken().equals("LONG") && operando2.getTipoToken().equals("LONG"))
                                start.append(traductorInstrucciones.comparadorLONG(operando1.getLexema(), operando2.getLexema()));
                            else
                                start.append(traductorInstrucciones.comparadorSINGLE(operando1.getLexema(), operando2.getLexema()));

                            this.proximoSalto = "JL";  // Salto para menor

                            break;
                            
                        case ">":
                            if (operando1.getTipoToken().equals("LONG") && operando2.getTipoToken().equals("LONG"))
                                start.append(traductorInstrucciones.comparadorLONG(operando1.getLexema(), operando2.getLexema()));
                            else
                                start.append(traductorInstrucciones.comparadorSINGLE(operando1.getLexema(), operando2.getLexema()));

                            this.proximoSalto = "JG";  // Salto para mayor

                            break;
                    }
                }
            } else
                agregoAPila = false;
        }

        return start.toString();
    }
}
