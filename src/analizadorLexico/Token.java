package analizadorLexico;

@SuppressWarnings("all")
public class Token {

    ///// ATRIBUTOS /////
    private int id;                     // Id del token.
    private String lexema;              // Significado del token.
    private int nroLinea;               // Número de línea en la que se encuentra el token.
    private String tipo;                // Tipo de token.
    private RegistroSimbolo registro;   // RegistroSimbolo asociado al token.


    ///// MÉTODOS /////
    /**
     * Constructor de la clase.
     * @param id
     * @param lexema
     * @param nroLinea
     * @param tipo
     * @param registro
     */
    public Token(int id, String lexema, int nroLinea, String tipo, RegistroSimbolo registro) {
        this.id = id;
        this.lexema = lexema;
        this.nroLinea = nroLinea;
        this.tipo = tipo;
        this.registro = registro;
    }

    /// MÉTODOS --> Getters & Setters ///
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public int getNroLinea() {
        return nroLinea;
    }

    public void setNroLinea(int nroLinea) {
        this.nroLinea = nroLinea;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public RegistroSimbolo getRegistro() {
        return registro;
    }

    public void setRegistro(RegistroSimbolo registro) {
        this.registro = registro;
    }

}
