package analizadorSintactico;

import accionSemantica.accionSemanticaSimple.ControlarRangoEnteroLargo;
import accionSemantica.accionSemanticaSimple.ControlarRangoFlotante;
import analizadorLexico.AnalizadorLexico;
import analizadorLexico.RegistroSimbolo;
import analizadorLexico.Token;

import java.util.Stack;
import java.util.Vector;

@SuppressWarnings("all")
public class AnalizadorSintactico {
    ///// ATRIBUTOS /////
    private AnalizadorLexico analizadorLexico;  // Se utiliza para obtener los tokens y poder verificar la sintaxis del código.
    private Parser parser;                      // Clase Parser generada por la herramienta YACC.
    Vector<String> analisisSintactico;          // Contiene las detecciones correctas de reglas de la gramática.
    Vector<String> listaErroresSintacticos;     // Estructura que guarda los errores sintácticos.
    Vector<RegistroSimbolo> tablaSimbolos;      // Estructura que representa a la tabla de símbolos.
    private PolacaInversa polaca;               // Estructura de polaca inversa que servirá para poder generar el código assembler.
    private Stack<Integer> pila;                // Pila para guardar los estados de la polaca inversa.
    private String ambito;                      // Ámbito principal del programa.
    private String tipo;                        // Guarda el tipo para que cuando se haga una declaración se actualice el tipo del identificador en la TS


    ///// MÉTODOS /////
    /**
     * Constructor de la clase.
     * @param analizadorLexico
     */
    public AnalizadorSintactico(AnalizadorLexico analizadorLexico, Parser parser) {
        this.analizadorLexico = analizadorLexico;
        this.parser = parser;
        this.analisisSintactico = new Vector<>();
        this.listaErroresSintacticos = new Vector<>();
        this.tablaSimbolos = analizadorLexico.getTablaSimbolos();
        this.polaca = new PolacaInversa();
        this.pila = new Stack<>();
        this.ambito = "main";
        this.tipo = "";
    }


    /// MÉTODOS --> Getters & Setters ///
    public AnalizadorLexico getAnalizadorLexico() {
        return analizadorLexico;
    }

    public Parser getParser() {
        return parser;
    }

    public Vector<String> getAnalisisSintactico() {
        return analisisSintactico;
    }

    public Vector<String> getListaErroresSintacticos() {
        return listaErroresSintacticos;
    }

    public Vector<RegistroSimbolo> getTablaSimbolos() {
        return tablaSimbolos;
    }

    public PolacaInversa getPolaca() {
        return polaca;
    }

    public Stack<Integer> getPila() {
        return pila;
    }

    public String getAmbito() {
        return ambito;
    }

    public String getTipo() {
        return tipo;
    }

    public void setAnalizadorLexico(AnalizadorLexico analizadorLexico) {
        this.analizadorLexico = analizadorLexico;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    public void setAnalisisSintactico(Vector<String> analisisSintactico) {
        this.analisisSintactico = analisisSintactico;
    }

    public void setListaErroresSintacticos(Vector<String> listaErroresSintacticos) {
        this.listaErroresSintacticos = listaErroresSintacticos;
    }

    public void setTablaSimbolos(Vector<RegistroSimbolo> tablaSimbolos) {
        this.tablaSimbolos = tablaSimbolos;
    }

    public void setPolaca(PolacaInversa polaca) {
        this.polaca = polaca;
    }

    public void setPila(Stack<Integer> pila) {
        this.pila = pila;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    /// MÉTODOS --> Funcionales al compilador ///
    /**
     * Agrega un nuevo análisis sintáctico a la estructura que los almacena.
     * @param analisis
     */
    public void agregarAnalisis(String analisis) { this.analisisSintactico.add(analisis); }

    /**
     * Agrega un nuevo error a la estructura que los almacena.
     * @param error
     */
    public void addErrorSintactico(String error) { this.listaErroresSintacticos.add(error); }

    /**
     * Agrega un elemento al final de la polaca.
     * @param elemento
     */
    public void agregarAPolaca(String elemento) {
        this.polaca.addElemento(elemento);
    }

    /**
     * Agrega un elemento a la polaca en la posición indicada.
     * @param elemento
     * @param pos
     */
    public void agregarAPolacaEnPos(int pos, String elemento) {
        this.polaca.addElementoEnPosicion(elemento, pos);
    }

    /**
     * Retorna el tamaño de la polaca.
     * @return
     */
    public int getSizePolaca() {
        return this.polaca.getSize();
    }

    /**
     * Agrega un elemento a la pila.
     * @param valor
     */
    public void pushElementoPila(int valor) {
        this.pila.push(valor);
    }

    /**
     * Devuelve el tope de la pila, eliminándolo de la misma.
     * @return
     */
    public int popElementoPila() {
        return this.pila.pop();
    }

    /**
     * Método para obtener el lexema de un token almacenado en la tabla de símbolos, dado su índice.
     * @param indice
     * @return
     */
    public String getLexemaFromTS(int indice) { return this.tablaSimbolos.get(indice).getLexema(); }

    /**
     * Método para obtener el tipo de un token almacenado en la tabla de símbolos, dado su índice.
     * @param indice
     * @return
     */
    public String getTipoFromTS(int indice) { return this.tablaSimbolos.get(indice).getTipoToken(); }

    /**
     * Devuelve el uso dado para un token almacenado en la tabla de símbolos en el índice indicado.
     * @param indice
     * @return
     */
    public String getUsoFromTS(int indice) {
        return this.tablaSimbolos.get(indice).getUso();
    }

    /**
     * Método para obtener un token de la tabla de símbolos dado su indice.
     * @param indice
     * @return
     */
    public RegistroSimbolo getRegistroFromTS(int indice) { return this.tablaSimbolos.get(indice); }

    /**
     * Modifica el uso para un elemento de la tabla de símbolos.
     * @param indice
     * @param uso
     */
    public void setUsoTablaSimb(int indice, String uso) { this.tablaSimbolos.get(indice).setUso(uso); }

    /**
     * Modifica el ámbito de un elemento de la tabla de símbolos.
     * @param indice
     */
    public void setAmbitoTablaSimb(int indice) { this.tablaSimbolos.get(indice).setAmbito(this.tablaSimbolos.get(indice).getLexema() + "@" + this.ambito); }

    /**
     * Modifica el tipo de un elemento de la tabla de símbolos.
     * @param indice
     */
    public void setTipoVariableTablaSimb(int indice) { this.tablaSimbolos.get(indice).setTipoVariable(this.tipo); }

    /**
     * Método para agregar el signo '-' a una constante negativa
     * @param indice
     */
    public void setNegativoTablaSimb(int indice) { this.tablaSimbolos.get(indice).setLexema('-' + this.tablaSimbolos.get(indice).getLexema()); }

    /**
     * Método para verificar el rango de un LONG positivo.
     * @param indice
     */
    public void verificarRangoEnteroLargo(int indice) {
        String lexema = this.tablaSimbolos.get(indice).getLexema();
        Long numero = Long.parseLong(lexema);

        // Si el numero es positivo y es mayor que 2^31 - 1
        if (numero == ControlarRangoEnteroLargo.MAXIMO_LONG) {
            this.tablaSimbolos.remove(indice);    // Se elimina la entrada de la tabla de símbolos.
            this.addErrorSintactico("ERROR SINTÁCTICO (Línea " + this.analizadorLexico.getLinea() + "): la constante LONG está fuera de rango.");
        }
    }

    /**
     * Método para verificar el rango de un FLOAT negativo.
     * @param indice
     */
    public void verificarRangoFloat(int indice) {
        // Se inserta el signo negativo.
        String lexema = "-" + this.tablaSimbolos.get(indice).getLexema();
        Float numero = Float.parseFloat(lexema.replace('S', 'E'));

        // Si está en rango, modificamos la tabla de símbolos.
        if (numero > -ControlarRangoFlotante.MAXIMO_FLOAT && numero < -ControlarRangoFlotante.MINIMO_FLOAT)
            this.tablaSimbolos.get(indice).setLexema(lexema);
        else {
            this.tablaSimbolos.remove(indice);
            this.addErrorSintactico("ERROR SINTÁCTICO (Línea " + this.analizadorLexico.getLinea() + "): la constante SINGLE está fuera de rango.");
        }
    }

    /**
     * Determina si una variable ya fue declarada.
     * @param referenciaATS
     * @return
     */
    public boolean variableFueDeclarada(int referenciaATS) {
        RegistroSimbolo registroEnReferencia = this.tablaSimbolos.get(referenciaATS);

        for (int i = 0; i < this.tablaSimbolos.size(); i++) {
            RegistroSimbolo registroEnIPos = this.tablaSimbolos.get(i);

            if (registroEnIPos.equalsByLexema(registroEnReferencia) && i != referenciaATS && registroEnIPos.tienenMismoAmbito(registroEnReferencia)) {
                if (registroEnIPos.equalsByUso(registroEnReferencia)) {
                    if (registroEnIPos.getUso().equals("VARIABLE"))
                        addErrorSintactico("ERROR SINTÁCTICO (Línea " + analizadorLexico.LINEA + "): existe una variable declarada con ese nombre.");
                    else
                        addErrorSintactico("ERROR SINTÁCTICO (Línea " + analizadorLexico.LINEA + "): existe una función declarada con ese nombre.");
                } else {
                    if (registroEnIPos.getUso().equals("VARIABLE"))
                        addErrorSintactico("ERROR SINTÁCTICO (Línea " + analizadorLexico.LINEA + "): ya existe una función con ese identificador.");
                    else
                        addErrorSintactico("ERROR SINTÁCTICO (Línea " + analizadorLexico.LINEA + "): ya existe una variable con ese identificador.");
                }

                this.tablaSimbolos.remove(referenciaATS);
                return true;
            }
        }

        return false;
    }

    /**
     * Método para imprimir la tabla de simbolos luego del análisis sintáctico
     */
    public void imprimirTablaSimbolos() {
        System.out.println();
        System.out.println("----------TABLA DE SÍMBOLOS-----------");
        if (this.tablaSimbolos.isEmpty())
            System.out.println("Tabla de símbolos vacía.");
        else {
            for (RegistroSimbolo simbolo : this.tablaSimbolos)
                System.out.println("Tipo del símbolo: " + simbolo.getTipoToken() + " - Lexema: " + simbolo.getLexema());
        }
    }

    /**
     * Método para imprimir los errores sintácticos.
     */
    public void imprimirErroresSintacticos() {
        System.out.println();
        System.out.println("----------ERRORES SINTÁCTICOS-----------");
        if (this.listaErroresSintacticos.isEmpty())
            System.out.println("Ejecución sin errores.");
        else {
            for (int i = 0; i < this.listaErroresSintacticos.size(); i++)
                System.out.println(this.listaErroresSintacticos.get(i));
        }
    }

    /**
     * Método para imprimir el análisis sintáctico.
     */
    public void imprimirAnalisisSintactico() {
        System.out.println();
        System.out.println("----------ANÁLIZADOR SINTÁCTICO-----------");

        if (!this.analisisSintactico.isEmpty())
            for (String string : this.analisisSintactico)
                System.out.println(string);
        else
            System.out.println("Análisis sintáctico vacío.");
    }

    /**
     * Método para imprimir los errores léxicos.
     */
    public void imprimirErroresLexicos() {
        this.analizadorLexico.imprimirErrores();
    }

    /**
     * Método para imprimir el análisis léxico.
     */
    public void imprimirAnalisisLexico() {
        System.out.println();
        System.out.println("----------ANÁLIZADOR LÉXICO-----------");
        Vector<Token> tokens = this.analizadorLexico.getListaTokens();

        for (Token token : tokens) {
            System.out.println("----------------");
            System.out.println("Tipo token: " + token.getTipo());
            System.out.println("Lexema: " + token.getLexema());
        }
    }

    /**
     * Imprime la polaca por pantalla. En caso de esta vacía, se imprime un mensaje indicándolo.
     */
    public void imprimirPolaca() {
        System.out.println("----------POLACA INVERSA-----------");
        if (!polaca.esVacia())
            polaca.imprimirPolaca();
        else
            System.out.println("Polaca vacia");
    }

    /**
     * Método start, necesario para la ejecución del Parser sobre la gramática definida.
     */
    public void start() {
        parser.setLexico(this.analizadorLexico);
        parser.setSintactico(this);
        System.out.println();

        if (parser.yyparse() == 0) {
            System.out.println("Ejecución del Parser finalizada.");
            //imprimirAnalisisLexico();
            imprimirAnalisisSintactico();
            imprimirTablaSimbolos();
        }
        else
            System.out.println("Ejecución del Parser no finalizada.");

        //analizadorLexico.imprimirErrores();
        this.imprimirErroresSintacticos();
        this.imprimirPolaca();
        analizadorLexico.setPosArchivo(0);
        analizadorLexico.setBuffer("");
    }

}
