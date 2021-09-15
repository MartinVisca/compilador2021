package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class ControlarPalabraReservada extends AccionSemanticaSimple {

    /**
     * Constructor de la clase.
     * @param analizadorLexico
     */
    public ControlarPalabraReservada(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    /**
     * Se comprueba si el buffer contiene una palabra reservada. De serlo, se busca el número de token y se lo asigna a la variable 'tokenActual' del analizador léxico.
     * @param buffer
     * @param caracter
     * @return
     */
    @Override
    public boolean ejecutar(String buffer, char caracter) {
        if (this.getAnalizadorLexico().esPalabraReservada(buffer)) {
            this.getAnalizadorLexico().setTokenActual(this.getAnalizadorLexico().getIdToken(buffer));
            return true;
        }
        else
            return false;
    }
    
}
