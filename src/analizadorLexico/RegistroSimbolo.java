package analizadorLexico;

import java.util.Objects;

@SuppressWarnings("all")
public class RegistroSimbolo {

    ///// ATRIBUTOS /////
    private String lexema;          // Significado del símbolo; a qué equivale.
    private String tipoToken;       // Indica el tipo de token.
    private String tipoVariable;    // Tipo de la variable (FLOAT o LONGINT).
    private String uso;             // Uso que se le da a la variable o símbolo.
    private String ambito;          // Lugar de definición de la variable, siguiendo Name Mangling.
    private String tipoPasaje;      // Sólo para parámetros; indica si es pasado por referencia o por copia-valor.
    private boolean esParametro;    // Indica si el símbolo es un parámetro.


    ///// CONSTANTES /////
    private static final String PASAJE_PARAMETRO_POR_DEFECTO = "COPIA VALOR";


    ///// MÉTODOS /////
    /**
     * Constructor de la clase.
     * @param lexema
     * @param tipoToken
     */
    public RegistroSimbolo(String lexema, String tipoToken) {
        this.lexema = lexema;
        this.tipoToken = tipoToken;
        this.tipoVariable = "";
        this.uso = "";
        this.ambito = "";
        this.tipoPasaje = "";
        this.esParametro = false;
    }


    /// MÉTODOS --> Getters & Setters ///
    public String getLexema() {
        return lexema;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public String getTipoVariable() {
        return tipoVariable;
    }

    public String getUso() {
        return uso;
    }

    public String getAmbito() {
        return ambito;
    }

    public String getTipoPasaje() {
        return tipoPasaje;
    }

    public boolean getEsParametro() {
        return esParametro;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public void setTipoToken(String tipoToken) {
        this.tipoToken = tipoToken;
    }

    public void setTipoVariable(String tipoVariable) {
        this.tipoVariable = tipoVariable;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public void setTipoPasaje(String tipoPasaje) {
        this.tipoPasaje = tipoPasaje;
    }

    public void setEsParametro(boolean esParametro) {
        this.esParametro = esParametro;
        this.tipoPasaje = this.PASAJE_PARAMETRO_POR_DEFECTO;
    }


    /// MÉTODOS --> Funcionales al compilador ///
    /**
     * Determina si dos instancias de la clase son iguales en una comparativa por lexema.
     * @param otroRegistroSimbolo
     * @return
     */
    public boolean equalsByLexema(RegistroSimbolo otroRegistroSimbolo) {
        return (this.getLexema().equals(otroRegistroSimbolo.getLexema()));
    }

    /**
     * Determina si dos instancias de la clase son iguales en una comparativa por uso.
     * @param otroRegistroSimbolo
     * @return
     */
    public boolean equalsByUso(RegistroSimbolo otroRegistroSimbolo) {
        return (this.getUso().equals(otroRegistroSimbolo.getUso()));
    }

    /**
     * Determina si dos instancias de la clase son iguales en una comparativa por ámbito.
     * @param otroRegistroSimbolo
     * @return
     */
    public boolean tienenMismoAmbito(RegistroSimbolo otroRegistroSimbolo) {
        return (this.getAmbito().equals(otroRegistroSimbolo.getAmbito()));
    }


    /// MÉTODOS --> Overrides ///
    @Override
    public boolean equals(Object otroRegistroSimbolo) {
        RegistroSimbolo registro = (RegistroSimbolo) otroRegistroSimbolo;
        return (this.getLexema().equals(registro.getLexema())
                && this.getTipoToken().equals(registro.getTipoToken())
                && this.getUso().equals((registro.getUso()))
                && this.getAmbito().equals(registro.getAmbito()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(lexema, tipoToken);
    }

}
