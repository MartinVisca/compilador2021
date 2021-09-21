package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class ControlarRangoFlotante extends AccionSemanticaSimple {

    public static final float MINIMO_FLOAT = (float) Math.pow(1.17549435, -38); // Equivalente a 1.17549435E-38f
    public static final float MAXIMO_FLOAT = (float) Math.pow(3.40282347, 38); // Equivalente a 3.40282347E+38f

    /**
     * Constructor de la clase.
     * @param analizadorLexico
     */
    public ControlarRangoFlotante(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    /**
     * Verifica si el equivalente float de 'cadena' se encuentra en el rango permitido.
     * @param cadena
     * @return
     */
    public static boolean enRango(String cadena) {
        if (cadena.equals("0.0") || cadena.equals("0.") || cadena.equals(".0"))
            return true;

        String[] parts = cadena.split("S");
        float numero = (float) Math.pow(Double.valueOf(parts[0]), Double.valueOf(parts[1]));

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
            this.getAnalizadorLexico().addErrorLexico("ERROR LEXICO (Línea " + AnalizadorLexico.LINEA + "): la constante FLOAT " + buffer + " está fuera de rango.");
            return false;
        } else {
            this.getAnalizadorLexico().agregarTokenATablaSimbolos(buffer, "FLOAT");
            this.getAnalizadorLexico().setTokenActual(this.getAnalizadorLexico().getIdToken("CTE"));
            return true;
        }
    }
    
}
