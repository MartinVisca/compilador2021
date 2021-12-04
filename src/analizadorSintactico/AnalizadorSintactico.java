package analizadorSintactico;

import analizadorLexico.accionSemantica.accionSemanticaSimple.ControlarRangoEnteroLargo;
import analizadorLexico.accionSemantica.accionSemanticaSimple.ControlarRangoFlotante;
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
    private Stack<Integer> refFunciones;        // Pila que guarda las referencias a funciones para etiquetas en la polaca
    private boolean tieneBreak;                 // Sirve para controlar si un loop WHILE contiene una sentencia BREAK
    private int refInvocacion;                  // Guarda la referencia al ID de la función que se está invocando

    ///// BOOLEANS PARA CONTROLAR ERRORES /////
    private boolean errorFunc;                  // Sirve para controlar si hubo un error en alguna parte de la declaracion de la funcion
    private boolean errorInvocacion;            // Sirve para controlar si hubo un error en alguna parte de la invocacion a la funcion

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
        this.refFunciones = new Stack<>();
        this.ambito = "main";
        this.tipo = "";
        this.errorFunc = false;
        this.errorInvocacion = false;
        this.tieneBreak = false;
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

    public boolean huboErrorFunc() { return errorFunc;}

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

    public void setAmbito(String ambito) { this.ambito = ambito; }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setErrorFunc(boolean error) { this.errorFunc = error; }

    public boolean tieneSentBreak() { return tieneBreak; }

    public void setTieneSentBreak(boolean tieneBreak) { this.tieneBreak = tieneBreak; }

    public boolean huboErrorInvocacion() { return errorInvocacion; }

    public void setErrorInvocacion(boolean errorInvocacion) { this.errorInvocacion = errorInvocacion; }

    public int getRefInvocacion() { return refInvocacion; }

    public void setRefInvocacion(int refInvocacion) {  this.refInvocacion = refInvocacion; }

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
     * Agrega una nueva entrada a la tabla de símbolos
     * @param nuevo
     */
    public void agregarRegistroATS(RegistroSimbolo nuevo) { this.tablaSimbolos.add(nuevo); }

    /**
     * Elimina una entrada de la tabla de símbolos
     * @param referenciaATS
     */
    public void eliminarRegistroTS(int referenciaATS) { this.tablaSimbolos.remove(referenciaATS); }

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
    public int popElementoPila() { return this.pila.pop(); }

    /**
     * Agrega el índice de la función detectada por el Parser en la pila de referencias a funciones
     * @param indiceTS
     */
    public void agregarReferencia(int indiceTS) { this.refFunciones.push(indiceTS); }

    /**
     * Devuelve el tope de la pila de referencias a funciones
     * @return
     */
    public int obtenerReferencia() { return this.refFunciones.peek(); }

    /**
     * Devuelve el tope de la pila de referencias a funciones y se elimina (Usado cuando se llega al final de la generación de código de la función)
     * @return
     */
    public int eliminarReferencia() { return this.refFunciones.pop(); }

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
     * Devuelve el ambito de un token almacenado en la tabla de símbolos dado su índice
     * @param indice
     * @return
     */
    public String getAmbitoFromTS(int indice) { return this.tablaSimbolos.get(indice).getAmbito(); }

    /**
     * Devuelve el tipo de variable de un token almacenado en la tabla de símbolos dado su índice
     * @param indice
     * @return
     */
    public String getTipoVariableFromTS(int indice) { return this.tablaSimbolos.get(indice).getTipoVariable(); }

    /**
     * Devuelve la función a la que hace referencia el identificador
     * @param indice
     * @return
     */
    public String getFuncReferenciadaFromTS(int indice) { return this.tablaSimbolos.get(indice).getFuncionReferenciada(); }

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
     *
     * @param indice
     * @param nuevoTipo
     */
    public void setTipoVariableTablaSimb(int indice, String nuevoTipo) { this.tablaSimbolos.get(indice).setTipoVariable(nuevoTipo); }

    /**
     * Setea la función a la que hace referencia una función definida o una variable tipo FUNC
     * @param indice
     * @param ambitoFunc
     */
    public void setFuncReferenciadaTablaSimb(int indice, String ambitoFunc) { this.tablaSimbolos.get(indice).setFuncionReferenciada(ambitoFunc); }

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
                        addErrorSintactico("ERROR SEMÁNTICO (Línea " + analizadorLexico.LINEA + "): existe una variable declarada con ese nombre.");
                    else
                        addErrorSintactico("ERROR SEMÁNTICO (Línea " + analizadorLexico.LINEA + "): existe una función declarada con ese nombre.");
                } else {
                    if (registroEnIPos.getUso().equals("VARIABLE") || registroEnIPos.getUso().equals("PARAMETRO"))
                        addErrorSintactico("ERROR SEMÁNTICO (Línea " + analizadorLexico.LINEA + "): ya existe una variable con ese identificador.");
                    else
                        addErrorSintactico("ERROR SEMÁNTICO (Línea " + analizadorLexico.LINEA + "): ya existe una función con ese identificador.");
                }

                this.tablaSimbolos.remove(referenciaATS);
                return true;
            }
        }
        return false;
    }

    /**
     * Método que busca las referencias a las variables almacenadas en la tabla de símbolos para los identificadores que se usan en asignaciones
     * @param referenciaATS
     * @return
     */

    int referenciaCorrecta(int referenciaATS) {
        String ambitoIterador = this.ambito;
        String id = this.tablaSimbolos.get(referenciaATS).getLexema();
        // Se recorren todas las entradas de la tabla de símbolos buscando la referencia correcta
        for (int i = 0; i < this.tablaSimbolos.size(); i++) {
            // Si los identificadores son iguales
            if (this.tablaSimbolos.get(i).getLexema().equals(id))  // Habrá que agregar el uso = VARIABLE?
                while (ambitoIterador != "")
                    // Si tienen el mismo ámbito encontré la referencia y se retorna
                    if (this.tablaSimbolos.get(i).getAmbito().equals(id + "@" + ambitoIterador)) {
                        this.tablaSimbolos.remove(referenciaATS);
                        return i;
                    } else
                        // Recorto el ámbito para seguir buscando la referencia
                        if (ambitoIterador.contains("@"))
                            ambitoIterador = ambitoIterador.substring(0, ambitoIterador.lastIndexOf("@"));
                        else
                            ambitoIterador = "";
            ambitoIterador = this.ambito;
        }
        // No se encontró la referencia, entonces la variable está fuera de alcance
        this.tablaSimbolos.remove(referenciaATS);
        return -1;
    }

    /**
     * Método que busca la referencia a la variable de retorno auxiliar de una función
     * @param idFunc
     * @return
     */
    int getReferenciaReturn(String idFunc) {
        for (int i = 0; i < this.tablaSimbolos.size(); i++)
            // Si coincide el lexema nombreFunc_ret y el uso es VARIABLE RETORNO
            if (this.tablaSimbolos.get(i).getLexema().equals(idFunc + "_ret") && this.tablaSimbolos.get(i).getUso().equals("VARIABLE RETORNO"))
                return i;
        return -1;
    }

    /**
     * Devuelve el indice de la función a la que se hace referencia cuando se realiza una invocación
     * @param funcRef
     * @return
     */
    int getIndiceFuncRef(String funcRef) {
        for (int i = 0; i < this.tablaSimbolos.size(); i++)
            if (this.tablaSimbolos.get(i).getAmbito().equals(funcRef))
                return i;
        return -1;
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
                System.out.println("Tipo del símbolo: " + simbolo.getTipoToken() + " - Lexema: " + simbolo.getLexema() + " - Tipo Token: " + simbolo.getTipoToken() + " - Tipo Variable: " + simbolo.getTipoVariable() + " - Uso: " + simbolo.getUso() + " - Ambito: " + simbolo.getAmbito() + " - Func Ref: " + simbolo.getFuncionReferenciada());
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
        System.out.println();
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
            //imprimirAnalisisSintactico();
            imprimirTablaSimbolos();
        }
        else
            System.out.println("Ejecución del Parser no finalizada.");

        //analizadorLexico.imprimirErrores();
        //this.imprimirErroresSintacticos();
        //this.imprimirPolaca();
        analizadorLexico.setPosArchivo(0);
        analizadorLexico.setBuffer("");
    }

}
