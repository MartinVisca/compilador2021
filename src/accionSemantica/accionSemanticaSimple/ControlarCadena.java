package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

public class ControlarCadena extends AccionSemanticaSimple {

    public ControlarCadena(AnalizadorLexico analizadorLexico) { super(analizadorLexico); }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        // Agregamos el token a la tabla de símbolos o devolvemos su referencia si ya se encuentra guardado
        this.getAnalizadorLexico().agregarTokenATablaSimbolos(buffer, "CADENA");
        // Devolvemos el token
        this.getAnalizadorLexico().setTokenActual(this.getAnalizadorLexico().getIdToken("CADENA"));
        return true;
    }
    
}
