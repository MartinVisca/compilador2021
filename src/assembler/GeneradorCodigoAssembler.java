package assembler;

import analizadorLexico.RegistroSimbolo;
import analizadorSintactico.AnalizadorSintactico;
import analizadorSintactico.PolacaInversa;

import java.util.Stack;
import java.util.Vector;

public class GeneradorCodigoAssembler {
    ///// CONSTANTES /////
    private final static String USO_VARIABLE = "VARIABLE";
    private final static String USO_CONSTANTE = "CONSTANTE";
    private final static String USO_CADENA_CARACTERES = "CADENA DE CARACTERES";


    ///// ATRIBUTOS /////
    private String assemblerGenerado;                           // String con el código assembler ya generado
    private StringBuffer codigoAssembler;                       // String que va almacenando el código a medida que se va generando
    private static final Stack<String> pila = new Stack<>();    // Por cada entrada, la pila almacenará una 2-upla compuesta por el lexema y el tipo de la variable
    private AnalizadorSintactico analizadorSintactico;

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
        this.analizadorSintactico = analizadorSintactico;
        this.numeroCadena = 0;
        this.numeroConstante = 0;
        this.numeroVariableAuxiliar = 0;

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
        this.operadoresUnarios.add("PRINT");    // Debería ser OUT o PRINT? PRINT PAPU

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
        this.codigoAssembler.append(".DATA ");
        this.codigoAssembler.append(this.generarPuntoData());
        this.codigoAssembler.append(".CODE");
        //codigoAssembler.append("START:");
        this.codigoAssembler.append(this.generarPuntoCode());
        //codigoAssembler.append("END START");

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

        if (!this.getVariablesDeclaradas().isEmpty())
            puntoData.append(this.getVariablesDeclaradas());

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
        // Llamado y seteo de los label de los errores
        // @ERROR_OVERFLOW es el error del overflow de la suma
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

            if (usoEntrada.equals(this.USO_VARIABLE)) { // Si es VARIABLE se agrega el lexema de la misma y el tamaño asignado (4 bytes para LONGINT, 2 para FLOAT).
                variables.append(entrada.getLexema());
                if (entrada.getTipoToken().equals("LONG"))
                    variables.append(" DD ? \n");
                else if (entrada.getTipoToken().equals("SINGLE"))
                    variables.append(" DW ? \n");
            } else if (usoEntrada.equals(this.USO_CONSTANTE)) { // Si es CONSTANTE se agrega CTE y luego el tamaño de la misma, los cuales coinciden con los asignados para las variables.
                if (entrada.getTipoToken().equals("LONG")) {
                    variables.append("Constante" + this.numeroConstante);
                    variables.append("DD ");
                    variables.append(entrada.getLexema() + "\n");
                    this.numeroConstante++;
                }
            } else if (usoEntrada.equals(this.USO_CADENA_CARACTERES)) { // Si es CADENA se agrega la cadena con un tamaño predefinido de 1 byte.
                variables.append("Cadena" + this.numeroCadena + " DB ");
                variables.append(entrada.getLexema() + ", 0 \n");
                this.numeroCadena++;
            }
        }

        return variables.toString();
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
            String simboloPolaca = polaca.getElemento(i);

            for (RegistroSimbolo simboloTabla : tablaSimbolos) {
                if (simboloTabla.getLexema().equals(simboloPolaca)) {
                    pila.push(simboloPolaca);
                    agregoAPila = true;
                    break;
                }
            }

            if (!agregoAPila) {
                String variableAuxiliar = "";

                if (this.operadoresBinarios.contains(simboloPolaca)) {
                    if (simboloPolaca.equals("+")) {
                        variableAuxiliar = "@auxiliar" + String.valueOf(this.numeroVariableAuxiliar);


                    } else if (simboloPolaca.equals("-")) {
                        //Resta
                    } else if (simboloPolaca.equals("*")) {
                        //Multiplicación
                    } else if (simboloPolaca.equals("/")) {
                        //División
                    } else if (simboloPolaca.equals(":=")) {
                        //Asignación
                    }
                } else if (this.operadoresUnarios.contains(simboloPolaca)) {
                    //Evaluar si sólo consideramos las cadenas (PRINT) o también el resto de los operadores unarios.
                } else if (this.comparadores.contains(simboloPolaca)) {
                    if (simboloPolaca.equals(">=")) {

                    } else if (simboloPolaca.equals("<=")) {

                    } else if (simboloPolaca.equals("<>")) {

                    } else if (simboloPolaca.equals("==")) {

                    } else if (simboloPolaca.equals("<")) {

                    } else if (simboloPolaca.equals(">")) {

                    }
                }
            } else
                agregoAPila = false;
        }

        return start.toString();
    }
}
