package analizadorLexico;

import java.util.Objects;

@SuppressWarnings("all")
public class RegistroSimbolo {

    ///// ATRIBUTOS /////
    private String lexema;      // Significado del símbolo; a qué equivale.
    private String tipoToken;   // Indica el tipo de token.


    ///// MÉTODOS /////
    /**
     * Constructor de la clase.
     * @param lexema
     * @param tipoToken
     */
    public RegistroSimbolo(String lexema, String tipoToken) {
        this.lexema = lexema;
        this.tipoToken = tipoToken;
    }

    /// MÉTODOS --> Getters & Setters ///
    public String getLexema() {
        return lexema;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public void setTipoToken(String tipoToken) {
        this.tipoToken = tipoToken;
    }

    /// MÉTODOS --> Overrides ///
    @Override
    public boolean equals(Object otroRegistroSimbolo) {
        RegistroSimbolo registro = (RegistroSimbolo) otroRegistroSimbolo;
        return (this.getLexema().equals(registro.getLexema()) && this.getTipoToken().equals(registro.getTipoToken()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(lexema, tipoToken);
    }

}
