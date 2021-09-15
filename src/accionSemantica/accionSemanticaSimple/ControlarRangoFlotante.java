package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class ControlarRangoFlotante extends AccionSemanticaSimple {

    public static final float MINIMO_FLOAT = 1.17549435E-38f;
    public static final float MAXIMO_FLOAT = 3.40282347E+38f;

    /**
     * Constructor de la clase.
     * @param analizadorLexico
     */
    public ControlarRangoFlotante(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    /**
     * Se hace un parse del string pasado por parámetro a un objeto Float.
     * @param cadena
     * @return
     */
    public static Float stringAFloat(String cadena) { return Float.parseFloat(cadena.replace('S', 'E')); }

    /**
     * Verifica si el equivalente float de 'cadena' se encuentra en el rango permitido.
     * @param cadena
     * @return
     */
    public static boolean enRango(String cadena) {
        float numero = stringAFloat(cadena);

        if (cadena.equals("0.0") || cadena.equals("0.") || cadena.equals(".0"))
            return true;

        return MINIMO_FLOAT < numero && numero < MAXIMO_FLOAT;
    }

    /**
     * Comprueba si el buffer contiene un flotante válido, arrojando un error léxico en caso contrario.
     * @param buffer
     * @param caracter
     * @return
     */
    @Override
    public boolean ejecutar(String buffer, char caracter) {
        if (!enRango(buffer)) {
            this.getAnalizadorLexico().addErrorLexico("ERROR LEXICO (Linea " + AnalizadorLexico.LINEA + "): la constante FLOAT está fuera de rango.");
            return false;
        }
        else {
            this.getAnalizadorLexico().agregarTokenATablaSimbolos(buffer, "FLOAT");
            this.getAnalizadorLexico().setTokenActual(this.getAnalizadorLexico().getIdToken("CTE"));
            return true;
        }
    }
    
}
