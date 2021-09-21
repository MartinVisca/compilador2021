package analizadorLexico;

import accionSemantica.AccionSemantica;
import accionSemantica.AccionSemanticaCompuesta;
import accionSemantica.accionSemanticaSimple.*;
import analizadorLexico.matrices.MatrizAccionesSemanticas;
import analizadorLexico.matrices.MatrizEstados;

import java.util.HashMap;
import java.util.Vector;

@SuppressWarnings("all")
public class AnalizadorLexico {
    ///// CONSTANTES /////
    public static int LINEA = 1;                // Referencia a la línea de código.
    public static final int ESTADOS = 18;       // Cantidad de estados del autómata.
    public static final int SIMBOLOS = 23;      // Cantidad de símbolos aceptados por el compilador.
    public static final int NO_ENCONTRADO = -1; // Indicador de falta de símbolo en la tabla que los almacena.


    ///// ATRIBUTOS /////
    private String archivo;         // Código del archivo.
    private String buffer;          // Buffer para leer el token actual.
    private int posArchivo;         // Índice que indica la posición del archivo que se está leyendo.
    private int estadoActual;       // Estado actual del autómata.
    private int tokenActual;        // ID del token que se está procesando actualmente.
    private int refTablaSimbolos;   // Número que indica el índice del token en la tabla de símbolos.
    private boolean codigoLeido;    // Variable que verifica si se terminó de leer el código.

    private Vector<RegistroSimbolo> tablaSimbolos;              // Tabla de símbolos.
    private Vector<Token> listaTokens;                          // Tokens resultantes del análisis léxico.
    private Vector<String> listaErrores;                        // Lista de errores léxicos.
    private MatrizEstados matrizEstados;                        // Matriz de transición de estados.
    private MatrizAccionesSemanticas matrizAccionesSemanticas;  // Matriz de acciones semánticas.
    private HashMap<String, Integer> idTokens;                  // Contiene los id de los tokens definidos. Estructura <Token, [ID, Tipo]>.


    ///// MÉTODOS /////
    /**
     * Constructor de la clase.
     * @param archivo
     */
    public AnalizadorLexico(String archivo) {
        //--- INICIALIZACIÓN DE ATRIBUTOS ---//
        this.archivo = archivo + '$';
        this.buffer = "";
        this.posArchivo = 0;
        this.estadoActual = 0;
        this.tokenActual = -1;
        this.refTablaSimbolos = -1;
        this.codigoLeido = false;
        this.tablaSimbolos = new Vector<>();
        this.listaTokens = new Vector<>();
        this.listaErrores = new Vector<>();
        this.idTokens = new HashMap<>();
        this.matrizEstados = new MatrizEstados();
        this.matrizAccionesSemanticas = new MatrizAccionesSemanticas(ESTADOS, SIMBOLOS);


        //--- CARGA DE TOKENS ---//
        // Operadores aritméticos
        this.idTokens.put("+", (int) '+');
        this.idTokens.put("-", (int) '-');
        this.idTokens.put("*", (int) '*');
        this.idTokens.put("/", (int) '/');

        // Símbolos de puntuación
        this.idTokens.put("(", (int) '(');
        this.idTokens.put(")", (int) ')');
        this.idTokens.put(".", (int) '.');
        this.idTokens.put(",", (int) ',');
        this.idTokens.put(";", (int) ';');
        this.idTokens.put(":", (int) ':');

        // Operadores lógicos
        this.idTokens.put("&&", 254);
        this.idTokens.put("||", 255);

        // Operador de asignación
        this.idTokens.put(":=", 256);

        // Identificador y constante
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

        // Comparadores
        this.idTokens.put(">", (int) '>');
        this.idTokens.put("<", (int) '<');
        this.idTokens.put("<=", 274);
        this.idTokens.put(">=", 275);
        this.idTokens.put("==", 276);
        this.idTokens.put("<>", 277);


        //--- ACCIONES SEMÁNTICAS ---//

        //--------- SIMPLES ---------//

        // AS4 -> Controla el rango de los enteros largos. Si está en rango, lo agrega a la tabla de símbolos, sino, devuelve error
        AccionSemanticaSimple AS4 = new ControlarRangoEnteroLargo(this);

        // AS5 -> Controla el rango de los float. Si está en rango, lo agrega a la tabla de símbolos, sino, devuelve error
        AccionSemanticaSimple AS5 = new ControlarRangoFlotante(this);

        // AS7 -> Devolver token de todos los símbolos (op. aritméticos, comparadores, etc.)
        AccionSemanticaSimple AS7 = new DevolverTokenSimbolos(this);

        //AS9 -> Aumenta el valor de posArchivo para avanzar ignorando el caracter
        AccionSemanticaSimple AS9 = new AvanzarEnCodigo(this);

        // AS12 -> Controlar si es identificador. Si es identificador y se excede la longitud máxima, se trunca
        AccionSemanticaSimple AS12 = new ControlarIdentificador(this);

        // AS13 -> Controlar si es palabra reservada
        AccionSemanticaSimple AS13 = new ControlarPalabraReservada(this);

        // AS14 -> Descarta el buffer (lo pone en vacio)
        AccionSemanticaSimple AS14 = new DescartarBuffer(this);

        // AS15 -> Inicializa el buffer en vacío
        AccionSemanticaSimple AS15 = new InicializarBuffer(this);

        // AS16 -> Agrega el token de tipo cadena de caracteres a la tabla de símbolos y devuelve su identificador
        AccionSemanticaSimple AS16 = new DevolverCadena(this);

        // AS17 -> Agrega un caracter al buffer
        AccionSemanticaSimple AS17 = new AgregarCaracter(this);

        // AS19 -> Elimina el último caracter agregado al buffer
        AccionSemanticaSimple AS19 = new EliminarUltimoCaracter(this);


        //------- COMPUESTAS ------- //

        // AS1 -> Inicializar buffer, agregar caracter al buffer y avanzar en código
        AccionSemanticaCompuesta AS1 = new AccionSemanticaCompuesta(this);
        AS1.addAccionSemantica(AS15);
        AS1.addAccionSemantica(AS17);
        AS1.addAccionSemantica(AS9);

        // AS2 -> Agregar caracter al buffer y avanzar en código
        AccionSemanticaCompuesta AS2 = new AccionSemanticaCompuesta(this);
        AS2.addAccionSemantica(AS17);
        AS2.addAccionSemantica(AS9);

        // AS3 -> Inicializa el buffer, agrega el caracter, devuelve el ID token del literal y avanza en código
        AccionSemanticaCompuesta AS3 = new AccionSemanticaCompuesta(this);
        AS3.addAccionSemantica(AS15);
        AS3.addAccionSemantica(AS17);
        AS3.addAccionSemantica(AS7);
        AS3.addAccionSemantica(AS9);

        // AS6 -> Agrega el caracter al buffer, devuelve el ID token del simbolo compuesto y avanza en código
        AccionSemanticaCompuesta AS6 = new AccionSemanticaCompuesta(this);
        AS6.addAccionSemantica(AS17);
        AS6.addAccionSemantica(AS7);
        AS6.addAccionSemantica(AS9);

        // AS8 -> Descartar buffer y avanzar en código
        AccionSemanticaCompuesta AS8 = new AccionSemanticaCompuesta(this);
        AS8.addAccionSemantica(AS14);
        AS8.addAccionSemantica(AS9);

        // AS10 -> Controlar cadena multilínea y avanzar en código
        AccionSemanticaCompuesta AS10 = new AccionSemanticaCompuesta(this);
        AS10.addAccionSemantica(AS16);
        AS10.addAccionSemantica(AS9);

        // AS11 -> Controla si es palabra reservada o si es identificador. Sino devuelve error
        AccionSemanticaCompuesta AS11 = new AccionSemanticaCompuesta(this);
        AS11.addAccionSemantica(AS13);
        AS11.addAccionSemantica(AS12);

        // AS18 -> Inicializa el buffer en vacio y avanza en el código (se usa en la primer transición de las cadenas multilineas)
        AccionSemanticaCompuesta AS18 = new AccionSemanticaCompuesta(this);
        AS18.addAccionSemantica(AS15);
        AS18.addAccionSemantica(AS9);

        // AS20 -> Elimina el último caracter agregado al buffer y avanza en el código
        AccionSemanticaCompuesta AS20 = new AccionSemanticaCompuesta(this);
        AS20.addAccionSemantica(AS19);
        AS20.addAccionSemantica(AS9);
        

        // ----- MATRIZ DE ACCIONES SEMÁNTICAS ------ //

        AccionSemantica [][] matriz = {
        /*            L       d     .     _     S     <     >     =     &     |     +     -     *     /     %    \n     ;     :     ,     (     )   otro  Bl,Tab  $                  
        /*0*/        {AS1 ,  AS1,  AS1,  AS1,  AS1,  AS1,  AS1,  AS1,  AS1,  AS1,  AS3,  AS3,  AS1,  AS3, AS18,  AS9,  AS3,  AS1,  AS3,  AS3,  AS3, null,  AS9,  AS9 },
        /*1*/        {AS4 ,  AS2,  AS2,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4 },
        /*2*/        {null,  AS2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,  null},
        /*3*/        {AS2 ,  AS2,  AS5,  AS5,  AS2,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5 },
        /*4*/        {null,  AS2, null, null, null, null, null, null, null, null,  AS2,  AS2, null, null, null, null, null, null, null, null, null, null, null,  null},
        /*5*/        {AS5 ,  AS2,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5,  AS5 },
        /*6*/        {null,  AS2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,  null},
        /*7*/        {AS7 ,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS6,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7 },
        /*8*/        {AS7 ,  AS7,  AS7,  AS7,  AS7,  AS7,  AS6,  AS6,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7 },
        /*9*/        {null, null, null, null, null, null, null, null,  AS6, null, null, null, null, null, null, null, null, null, null, null, null, null, null,  null},
        /*10*/       {null, null, null, null, null, null, null, null, null,  AS6, null, null, null, null, null, null, null, null, null, null, null, null, null,  null},
        /*11*/       {null, null, null, null, null, null, null,  AS6, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,  null},
        /*12*/       {AS7 ,  AS8,  AS9, AS10,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS8,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS7 },
        /*13*/       {AS9 ,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9 },
        /*14*/       {AS2 ,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2, AS10, null,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2 },
        /*15*/       {AS2 ,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2, AS10, AS20,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2 },
        /*16*/       {null, null, null, null, null, null, null, null, null, null,  AS9, null, null, null, null, null, null, null, null, null, null, null,  AS9,  null},
        /*17*/       {AS2 ,  AS2, AS11,  AS2,  AS2, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11,  AS11},
        /*18*/       {null, null, null, null, null, null, null,  AS7, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,  null},   
        };
        this.matrizAccionesSemanticas.set(matriz);

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

    public Vector<RegistroSimbolo> getTablaSimbolos() {
        return tablaSimbolos;
    }

    public Vector<Token> getListaTokens() {
        return listaTokens;
    }

    public Vector<String> getListaErrores() {
        return listaErrores;
    }

    public MatrizEstados getMatrizEstados() {
        return matrizEstados;
    }

    public MatrizAccionesSemanticas getMatrizAccionesSemanticas() {
        return matrizAccionesSemanticas;
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

    public void setTablaSimbolos(Vector<RegistroSimbolo> tablaSimbolos) {
        this.tablaSimbolos = tablaSimbolos;
    }

    public void setListaTokens(Vector<Token> listaTokens) {
        this.listaTokens = listaTokens;
    }

    public void setListaErrores(Vector<String> listaErrores) {
        this.listaErrores = listaErrores;
    }

    public void setMatrizEstados(MatrizEstados matrizEstados) {
        this.matrizEstados = matrizEstados;
    }

    public void setMatrizAccionesSemanticas(MatrizAccionesSemanticas matrizAccionesSemanticas) {
        this.matrizAccionesSemanticas = matrizAccionesSemanticas;
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
        return this.idTokens.get(tipoToken);
    }

    /**
     * Agrega el token a la tabla de símbolos.
     * Si el token ya existe en la tabla, se pasa la referencia en la tabla de símbolos al índice en cuestión.
     * Si el token no existe, se agrega al final de la tabla.
     * @param lexema
     * @param tipo
     */
    public void agregarTokenATablaSimbolos(String lexema, String tipo) {
        int indiceEnTS = this.getIndiceEnTablaSimbolos(lexema);

        if (indiceEnTS != -1)
            this.refTablaSimbolos = indiceEnTS;
        else {
            RegistroSimbolo nuevo = new RegistroSimbolo(lexema, tipo);
            this.tablaSimbolos.add(nuevo);
            this.refTablaSimbolos = this.tablaSimbolos.size() - 1;
        }
    }

    /**
     * Devuelve el índice en la tabla de símbolos a partir de un lexema pasado por parámetro.
     * Su utilidad es la búsqueda de tokens.
     * @param lexema
     * @return
     */
    public int getIndiceEnTablaSimbolos(String lexema) {
        for (int i = 0; i < this.tablaSimbolos.size(); i++) {
            if (lexema.equals(this.tablaSimbolos.get(i).getLexema()))
                return i;
        }

        return this.NO_ENCONTRADO;
    }

    /**
     * Agrega un error léxico pasado por parámetro a la lista que los almacena.
     * @param error
     */
    public void addErrorLexico(String error) {
        this.listaErrores.add(error);
    }

    /**
     * Agrega un token pasado por parámetro a la lista que los almacena.
     * @param nuevo
     */
    public void addToken(Token nuevo) {
        this.listaTokens.add(nuevo);
    }

    /**
     * En base a las palabras reservadas del compilador, se comprueba si el string pasado por parámetro (token) corresponde a alguna de ellas.
     * @param posiblePalabra
     * @return
     */
    public boolean esPalabraReservada(String posiblePalabra) {
        switch (posiblePalabra) {
            case "IF":
            case "THEN":
            case "ELSE":
            case "ENDIF":
            case "PRINT":
            case "FUNC":
            case "RETURN":
            case "BEGIN":
            case "END":
            case "BREAK":
            case "LONG":
            case "SINGLE":
            case "WHILE":
            case "DO":
                return true;
            default:
                return false;
        }
    }

    /**
     * Determina si el token pasado por parámetro es un identificador, en base al análisis de sus caracteres.
     * @param stringToken
     * @return
     */
    public boolean esIdentificador(String stringToken) {
        if (!Character.isLetter(stringToken.charAt(0)))
            return false;

        for (int i = 0; i < stringToken.length(); i++) {
            char c = stringToken.charAt(i);

            if (!(c == '_' || Character.isDigit(c) || (Character.isLetter(c) && Character.isLowerCase(c))))
                return false;
        }

        return true;
    }

    /**
     * En base al id de un token pasado por parámetro, determina su tipo.
     * @param idToken
     * @return
     */
    public String getTipoToken(int idToken) {
        String tipo = "";

        switch (idToken) {
            case (int) '+':
            case (int) '-':
            case (int) '*':
            case (int) '/':
                tipo = "OPERADOR ARITMETICO";
                break;
            case (int) '(':
            case (int) ')':
            case (int) '.':
            case (int) ',':
            case (int) ';':
            case (int) ':':
                tipo = "LITERAL";
                break;
            case 256:
                tipo = "ASIGNACION";
                break;
            case 257:
                tipo = "IDENTIFICADOR";
                break;
            case 258:
                tipo = "CONSTANTE";
                break;
            case 259:
            case 260:
            case 261:
            case 262:
            case 263:
            case 264:
            case 265:
            case 266:
            case 267:
            case 268:
            case 269:
            case 270:
            case 271:
            case 272:
                tipo = "PALABRA RESERVADA";
                break;
            case 273:
                tipo = "CADENA DE CARACTERES";
                break;
            case (int) '<':
            case (int) '>':
            case 274:
            case 275:
            case 276:
            case 277:
                tipo = "COMPARADOR";
                break;
            default:
                break;
        }

        return tipo;
    }

    /**
     * Dado un caracter, devuelve su columna asociada en la matriz de transición de estados.
     * @param caracter
     * @return
     */
    public int getColumnaCaracter(Character caracter) {
        if (caracter == null)
            return -1;

        if (caracter == 'S')
            return 4;

        if (Character.isLetter(caracter))
            return 0;

        if (Character.isDigit(caracter))
            return 1;

        switch (caracter) {
            case '.': return 2;
            case '_': return 3;
            case '<': return 5;
            case '>': return 6;
            case '=': return 7;
            case '&': return 8;
            case '|': return 9;
            case '+': return 10;
            case '-': return 11;
            case '*': return 12;
            case '/': return 13;
            case '%': return 14;
            case '\n': return 15;
            case ';': return 16;
            case ':' : return 17;
            case ',': return 18;
            case '(': return 19;
            case ')': return 20;
            case 9: return 22;      // Tabulación
            case 32: return 22;     // Espacio en blanco
            case '$': return 23;
        }

        return 21; //Otros
    }

    /**
     * Determina si la posición actual de análisis de un archivo corresponde a la final.
     * @return
     */
    public boolean esFinDeArchivo() {
        if (this.estadoActual == 0) {
            if (this.posArchivo == (this.archivo.length() - 1)) {
                this.tokenActual = 0;
                this.posArchivo = 0;
                this.LINEA = 1;
                this.codigoLeido = true;
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }

    /**
     * Sincroniza el análisis léxico del archivo cuando se llega a estados donde el buffer debe descartarse.
     * @param caracterActual
     */
    public void sincronizarAnalisis(char caracterActual) {
        String aux = this.buffer;  // Variable para imprimir el error
        this.buffer = "";
        while (this.archivo.charAt(this.posArchivo) != ';'
                && this.archivo.charAt(this.posArchivo) != '\n'
                && this.archivo.charAt(this.posArchivo) != 9
                && this.archivo.charAt(this.posArchivo) != 32
                && this.archivo.charAt(this.posArchivo) != '$'
                && this.posArchivo < this.archivo.length()) {
            aux += this.archivo.charAt(this.posArchivo);
            this.posArchivo++;
        }
        this.estadoActual = 0;
        this.addErrorLexico("ERROR LEXICO (Linea " + this.LINEA + "): \'" + aux + "\' es un token invalido");
    }

    /**
     * Método que devuelve un id de token en base al procesamiento de un archivo de entrada.
     * Es vitalmente importante para el trabajo conjunto con el analizador sintáctico.
     * @return
     */
    public int procesarYylex() {
        this.tokenActual = -1;
        this.estadoActual = 0;
        this.buffer = "";
        this.refTablaSimbolos = -1;
        char caracterActual;
        int columnaCaracter = -1;
        AccionSemantica accion;

        while (this.posArchivo < this.archivo.length() && this.tokenActual != 0 && this.tokenActual == -1) {
            caracterActual = this.archivo.charAt(this.posArchivo);
            columnaCaracter = this.getColumnaCaracter(caracterActual);
            accion = this.matrizAccionesSemanticas.get(this.estadoActual, columnaCaracter);

            if (accion != null)
                accion.ejecutar(this.buffer, caracterActual);

            if (caracterActual == '\n' && this.estadoActual != 6)
                // Si es un salto de línea y no estoy dentro de la cadena
                this.LINEA++;

            if (caracterActual == '$') {
                if (esFinDeArchivo())
                    break;
                else {
                    // Si no cerró la cadena o el comentario, y venía el EOF
                    if (this.posArchivo == this.archivo.length())
                        if (this.estadoActual == 13 || this.estadoActual == 14) {
                            this.addErrorLexico("ERROR LÉXICO (Línea " + this.LINEA + "): cadena o comentario mal cerrados");
                            this.tokenActual = 0;
                            this.posArchivo = 0;
                            this.LINEA = 1;
                            this.codigoLeido = true;
                            break;
                        }
                }
            }

            // FIXME: a esto hay que sacarlo (porque sino se agregan 2 errores, este y el de sincronizarAnalisis) o hay que agregar una variable estadoAnterior y agregar el error dentro de sincronizarAnalisis
            // Si estoy controlando la cadena multilinea y viene un caracter != a '+', blanco y tab
            if (this.estadoActual == 16 && caracterActual != '+' && caracterActual != 9 && caracterActual != 32) {
                this.addErrorLexico("ERROR LÉXICO (Línea " + this.LINEA + "): cadena con formato erróneo; luego del salto de línea debe existir un '+'");
            }

            this.estadoActual = this.matrizEstados.get(this.estadoActual, columnaCaracter);

            if (this.estadoActual == -1)
                this.sincronizarAnalisis(caracterActual);
        }

        if (this.tokenActual != 0 && this.tokenActual != -1) {
            String tipo = getTipoToken(this.tokenActual);
            Token nuevo = new Token(this.tokenActual, this.buffer, this.LINEA, tipo);
            this.addToken(nuevo);
        }

        if (this.posArchivo == this.archivo.length())
            this.codigoLeido = true;

        return this.tokenActual;
    }

    /**
     * Imprime el contenido de la tabla de símbolos o indica que la misma está vacía.
     */
    public void imprimirTablaSimbolos() {
        if (this.tablaSimbolos.isEmpty())
            System.out.println("Tabla de símbolos vacía.");
        else {
            for (RegistroSimbolo simbolo : this.tablaSimbolos)
                System.out.println("Tipo del simbolo: " + simbolo.getTipoToken() + " - Lexema: " + simbolo.getLexema());
        }
    }

    /**
     * Imprime los errores léxicos del compilador o "Ejecución sin errores" en caso de no existir ninguno.
     */
    public void imprimirErrores() {
        if (this.listaErrores.isEmpty())
            System.out.println("Ejecución sin errores.");
        else {
            for (int i = 0; i < this.listaErrores.size(); i++)
                System.out.println(this.listaErrores.get(i));
        }
    }
}
