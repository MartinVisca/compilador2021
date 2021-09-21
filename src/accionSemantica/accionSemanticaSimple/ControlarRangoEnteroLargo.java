package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class ControlarRangoEnteroLargo extends AccionSemanticaSimple {

    public static final long MAXIMO_LONG = 2147483648L;

    /**
     * Constructor de la clase.
     * @param analizadorLexico
     */
    public ControlarRangoEnteroLargo(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    /**
     * Verifica si el entero largo está en el rango permitido.
     * @param buffer
     * @return
     */
    public static boolean enRango(String buffer) {
        Long numero = Long.parseLong(buffer);
        return (numero >= 0 && numero <= MAXIMO_LONG);
    }

    /**
     * Se comprueba si el buffer contiene un entero largo válido, arrojando un error léxico en el caso contrario.
     * @param buffer
     * @param caracter
     * @return
     */
    @Override
    public boolean ejecutar(String buffer, char caracter) {
        if (!enRango(buffer)) {
            this.getAnalizadorLexico().addErrorLexico("ERROR LÉXICO (Línea " + AnalizadorLexico.LINEA + "): la constante LONGINT está fuera de rango.");
            return false;
        }
        else {
            this.getAnalizadorLexico().agregarTokenATablaSimbolos(buffer, "LONG");
            this.getAnalizadorLexico().setTokenActual(this.getAnalizadorLexico().getIdToken("CTE"));
            return true;
        }

    }
    
}
