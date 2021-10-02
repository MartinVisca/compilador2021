package analizadorSintactico;

import accionSemantica.accionSemanticaSimple.ControlarRangoEnteroLargo;
import accionSemantica.accionSemanticaSimple.ControlarRangoFlotante;
import analizadorLexico.AnalizadorLexico;
import analizadorLexico.RegistroSimbolo;
import analizadorLexico.Token;

import java.util.Vector;

@SuppressWarnings("all")
public class AnalizadorSintactico {
    ///// ATRIBUTOS /////
    private AnalizadorLexico analizadorLexico;  // Se utiliza para obtener los tokens y poder verificar la sintaxis del código.
    private Parser parser;                    // Clase Parser generada por la herramienta YACC.
    Vector<String> analisisSintactico;          // Contiene las detecciones correctas de reglas de la gramática.
    Vector<String> listaErroresSintacticos;     // Estructura que guarda los errores sintácticos.
    Vector<RegistroSimbolo> tablaSimbolos;      // Estructura que representa a la tabla de símbolos.


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
        this.tablaSimbolos = new Vector<>();
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


    /// MÉTODOS --> Funcionales al compilador ///
    /**
     * Agrega un nuevo análisis sintáctico a la estructura que los almacena.
     * @param analisis
     */
    public void addAnalisis(String analisis) { this.analisisSintactico.add(analisis); }

    /**
     * Agrega un nuevo error a la estructura que los almacena.
     * @param error
     */
    public void addErrorSintactico(String error) { this.listaErroresSintacticos.add(error); }

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
     * Método para obtener un token de la tabla de símbolos dado su indice.
     * @param indice
     * @return
     */
    public RegistroSimbolo getRegistroFromTS(int indice) { return this.tablaSimbolos.get(indice); }

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
     * Método para imprimir la tabla de simbolos luego del análisis sintáctico
     */
    public void imprimirTablaSimbolos() {
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
        System.out.println("----------ANÁLIZADOR LÉXICO-----------");
        Vector<Token> tokens = this.analizadorLexico.getListaTokens();

        for (Token token : tokens) {
            System.out.println("----------------");
            System.out.println("Tipo token: " + token.getTipo());
            System.out.println("Lexema: " + token.getLexema());
        }
    }

    /**
     * Método start, necesario para la ejecución del Parser sobre la gramática definida.
     */
    public void start() {
        // parser.setLexico(this.lexico);
        // parser.setSintactico(this);
        if (parser.yyparse() == 0) {
            System.out.println("Ejecución del Parser finalizada.");
            imprimirAnalisisSintactico();
            imprimirTablaSimbolos();
        }
        else
            System.out.println("Ejecución del Parser no finalizada.");

        imprimirErroresSintacticos();
        analizadorLexico.setPosArchivo(0);
        analizadorLexico.setBuffer("");
    }

}
