package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class ControlarCadena extends AccionSemanticaSimple {

    /**
     * Constructor de la clase
     * @param analizadorLexico
     */
    public ControlarCadena(AnalizadorLexico analizadorLexico) { super(analizadorLexico); }

    /**
     * Se agrega el token a la tabla de símbolos o se devuelve su referencia si ya se encuentra guardado.
     * @param buffer
     * @param caracter
     * @return
     */
    @Override
    public boolean ejecutar(String buffer, char caracter) {
        this.getAnalizadorLexico().agregarTokenATablaSimbolos(buffer, "CADENA");
        this.getAnalizadorLexico().setTokenActual(this.getAnalizadorLexico().getIdToken("CADENA"));
        return true;
    }
    
}
