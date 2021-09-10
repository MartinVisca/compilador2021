package analizadorLexico;

import java.util.HashMap;
import java.util.Vector;

@SuppressWarnings("all")
public class AnalizadorLexico {
    ///// CONSTANTES /////
    public static int LINEA = 1;    // Referencia a la línea de código.


    ///// ATRIBUTOS /////
    private String archivo;         // Código del archivo.
    private String buffer;          // Buffer para leer el token actual.
    private int posArchivo;         // Índice que indica la posición del archivo que se está leyendo.
    private int estadoActual;       // Estado actual del autómata.
    private int tokenActual;        // ID del token que se está procesando actualmente.
    private int refTablaSimbolos;   // Número que indica el índice del token en la tabla de símbolos.
    private boolean codigoLeido;    // Variable que verifica si se terminó de leer el código.

    private Vector<RegistroSimbolo> tablaSimbolos;                  // Tabla de símbolos.
    private Vector<Token> listaTokens;                              // Tokens resultantes del análisis léxico.
    private Vector<String> listaErrores;                            // Lista de errores léxicos.
    // private MatrizTransicionEstados matrizEstados;               // Matriz de transición de estados.
    // private MatrizAccionesSemanticas matrizAccionesSemanticas;   // Matriz de acciones semánticas.
    private HashMap<String, Integer> idTokens;                      // Contiene los id de los tokens definidos. Estructura <Token, [ID, Tipo]>.


    ///// MÉTODOS /////
    /**
     * Constructor de la clase.
     * @param archivo
     */
    public AnalizadorLexico(String archivo) {
        //--- INICIALIZACIÓN DE ATRIBUTOS ---//
        this.archivo = archivo;
        this.buffer = "";
        this.posArchivo = 0;
        this.estadoActual = 0;
        this.tokenActual = -1;
        this.refTablaSimbolos = -1;
        this.codigoLeido = false;
        this.tablaSimbolos = new Vector<>();
        this.listaTokens = new Vector<>();
        this.listaTokens = new Vector<>();
        this.idTokens = new HashMap<>();


        //--- CARGA DE TOKENS ---//
        // Operadores aritméticos
        this.idTokens.put("+", (int) '+');
        this.idTokens.put("-", (int) '-');
        this.idTokens.put("*", (int) '*');
        this.idTokens.put("/", (int) '/');

        // Símbolos de puntuación
        this.idTokens.put("(", (int) '(');
        this.idTokens.put(")", (int) ')');
        this.idTokens.put("{", (int) '{');
        this.idTokens.put("}", (int) '}');
        this.idTokens.put(".", (int) '.');
        this.idTokens.put(",", (int) ',');
        this.idTokens.put(";", (int) ';');
        this.idTokens.put(":", (int) ':');

        // Comparadores
        this.idTokens.put(">", (int) '>');
        this.idTokens.put("<", (int) '<');
        this.idTokens.put("==", 277);
        this.idTokens.put("<>", 278);
        this.idTokens.put(">=", 276);
        this.idTokens.put("<=", 275);

        // Operadores lógicos
        this.idTokens.put("&&", 254);
        this.idTokens.put("||", 255);

        // Operador de asignación
        this.idTokens.put(":=", 256);

        //Identificador y constante
        this.idTokens.put("ID", 257);
        this.idTokens.put("CTE", 258);

        // Palabras reservadas
        this.idTokens.put("IF", 259);
        this.idTokens.put("THEN", 260);
        this.idTokens.put("ELSE", 261);
        this.idTokens.put("ENDIF", 262);
        this.idTokens.put("PRINT", 263);
        this.idTokens.put("FUNC", 264);
        this.idTokens.put("RETURN", 265);
        this.idTokens.put("BEGIN", 266);
        this.idTokens.put("END", 267);
        this.idTokens.put("BREAK", 268);
        this.idTokens.put("LONG", 269);
        this.idTokens.put("SINGLE", 270);
        this.idTokens.put("WHILE", 271);
        this.idTokens.put("DO", 272);

        // Cadena de caracteres
        this.idTokens.put("CADENA", 273);


        //--- ACCIONES SEMÁNTICAS ---//
    }

    /// MÉTODOS --> Getters & Setters ///
    public String getArchivo() {
        return archivo;
    }

    public String getBuffer() {
        return buffer;
    }

    public int getPosArchivo() {
        return posArchivo;
    }

    public int getEstadoActual() {
        return estadoActual;
    }

    public int getTokenActual() {
        return tokenActual;
    }

    public int getRefTablaSimbolos() {
        return refTablaSimbolos;
    }

    public boolean isCodigoLeido() {
        return codigoLeido;
    }

    public static int getLinea() {
        return LINEA;
    }

    public Vector<String> getListaErrores() {
        return listaErrores;
    }

    public HashMap<String, Integer> getIdTokens() {
        return idTokens;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

    public void setPosArchivo(int posArchivo) {
        this.posArchivo = posArchivo;
    }

    public void setEstadoActual(int estadoActual) {
        this.estadoActual = estadoActual;
    }

    public void setTokenActual(int tokenActual) {
        this.tokenActual = tokenActual;
    }

    public void setRefTablaSimbolos(int refTablaSimbolos) {
        this.refTablaSimbolos = refTablaSimbolos;
    }

    public void setCodigoLeido(boolean codigoLeido) {
        this.codigoLeido = codigoLeido;
    }

    public static void setLinea(int linea) {
        AnalizadorLexico.LINEA = linea;
    }

    public void setListaErrores(Vector<String> listaErrores) {
        this.listaErrores = listaErrores;
    }

    public void setIdTokens(HashMap<String, Integer> idTokens) {
        this.idTokens = idTokens;
    }


    /// MÉTODOS --> Funcionales al compilador ///
    /**
     * Devuelve el id del token a partir de su tipo.
     * @param tipoToken
     * @return
     */
    public int getIdToken(String tipoToken) {
        // TODO: 10/9/21
        return -1;
    }

    /**
     * Agrega el token a la tabla de símbolos.
     * Si el token ya existe en la tabla, se asigna la referencia a la misma.
     * @param lexema
     * @param tipo
     */
    public void agregarTokenATablaSimbolos(String lexema, String tipo) {
        // TODO: 10/9/21
    }

    /**
     * Devuelve el índice en la tabla de símbolos a partir de un lexema pasado por parámetro.
     * Su utilidad es la búsqueda de tokens.
     * @param lexema
     * @return
     */
    public int getIndiceEnTablaSimbolos(String lexema) {
        // TODO: 10/9/21
        return -1;
    }

    /**
     * Agrega un error léxico pasado por parámetro a la lista que los almacena.
     * @param error
     */
    public void addErrorLexico(String error) {
        // TODO: 10/9/21
    }

    /**
     * Agrega un token pasado por parámetro a la lista que los almacena.
     * @param nuevo
     */
    public void addToken(Token nuevo) {
        // TODO: 10/9/21
    }

    /**
     * En base a las palabras reservadas del compilador, se comprueba si el string pasado por parámetro (token) corresponde a alguna de ellas.
     * @param posiblePalabra
     * @return
     */
    public boolean esPalabraReservada(String posiblePalabra) {
        // TODO: 10/9/21
        return true;
    }

    /**
     * Determina si el token pasado por parámetro es un identificador, en base al análisis de sus caracteres.
     * @param stringToken
     * @return
     */
    public boolean esIdentificador(String stringToken) {
        // TODO: 10/9/21
        return true;
    }

    /**
     * En base al id de un token pasado por parámetro, determina su tipo.
     * @param idToken
     * @return
     */
    public String getTipoToken(int idToken) {
        // TODO: 10/9/21
        return "";
    }

    /**
     * Dado un caracter, devuelve su columna asociada en la matriz de transición de estados.
     * @param caracter
     * @return
     */
    public int getColumnaCaracter(Character caracter) {
        // TODO: 10/9/21
        return -1;
    }

    /**
     * Determina si la posición actual de análisis de un archivo corresponde a la final.
     * @return
     */
    public boolean esFinDeArchivo() {
        // TODO: 10/9/21
        return true;
    }

    /**
     * Sincroniza el análisis léxico del archivo cuando se llega a estados donde el buffer debe descartarse.
     * @param caracterActual
     */
    public void sincronizarAnalisis(char caracterActual) {
        // TODO: 10/9/21
    }

    /**
     * Método que devuelve un id de token en base al procesamiento de un archivo de entrada.
     * Es vitalmente importante para el trabajo conjunto con el analizador sintáctico.
     * @return
     */
    public int procesarYylex() {
        // TODO: 10/9/21
        return -1;
    }
}