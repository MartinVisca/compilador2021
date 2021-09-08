package analizadorLexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

@SuppressWarnings("all")
public class AnalizadorLexico {

    ///// ATRIBUTOS /////
    private String archivo;                 // Código del archivo.
    private String buffer = "";             // Buffer para leer el token actual.
    private int posArchivo = 0;             // Índice que indica la posición del archivo que se está leyendo.
    private int estadoActual = 0;           // Estado actual del autómata.
    private int tokenActual = -1;           // ID del token que se está procesando actualmente.
    private int refTablaSimbolos = -1;      // Número que indica el índice del token en la tabla de símbolos.
    private boolean codigoLeido = false;    // Variable que verifica si se terminó de leer el código.
    public static int linea = 1;            // Referencia a la línea de código.

    private Vector<RegistroSimbolo> tablaSimbolos;               // Tabla de símbolos.
    private Vector<Token> listaTokens;                           // Tokens resultantes del análisis léxico.
    private ArrayList<String> listaErrores;                         // Lista de errores léxicos.
    // private MatrizTransicionEstados matrizEstados;               // Matriz de transición de estados.
    // private MatrizAccionesSemanticas matrizAccionesSemanticas;   // Matriz de acciones semánticas.
    private HashMap<String, Integer> idTokens;                      // Contiene los id de los tokens definidos. Estructura <Token, [ID, Tipo]>.


    ///// MÉTODOS /////
    /**
     * Constructor de la clase.
     * @param archivo
     */
    public AnalizadorLexico(String archivo) {

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
        return linea;
    }

    public ArrayList<String> getListaErrores() {
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
        AnalizadorLexico.linea = linea;
    }

    public void setListaErrores(ArrayList<String> listaErrores) {
        this.listaErrores = listaErrores;
    }

    public void setIdTokens(HashMap<String, Integer> idTokens) {
        this.idTokens = idTokens;
    }

}
