package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class ControlarIdentificador extends AccionSemanticaSimple {

    private static final int LONGITUD_MAXIMA = 22;

    /**
     * Constructor de la clase.
     * @param analizadorLexico
     */
    public ControlarIdentificador(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    /**
     * Comprueba si el buffer contiene un identificador válido. De lo contrario, agrega un error léxico a la lista que mantiene el analizador.
     * En caso de exceder la longitud permitida, el buffer se toma como identificador igualmente, pero se lo trunca y se lanza un warning.
     * @param buffer
     * @param caracter
     * @return
     */
    @Override
    public boolean ejecutar(String buffer, char caracter) {
        if (this.getAnalizadorLexico().esIdentificador(buffer)) {
            // Si el identificador excede la longitud máxima permitida, lo truncamos
            if (buffer.length() > LONGITUD_MAXIMA) {
                buffer = buffer.substring(0, LONGITUD_MAXIMA - 1);
                this.getAnalizadorLexico().addErrorLexico("WARNING (Línea " + AnalizadorLexico.LINEA + "): el identificador " + this.getAnalizadorLexico().getBuffer() + " ha sido truncado. Nuevo nombre: " + buffer);
                this.getAnalizadorLexico().setBuffer(buffer);
            }

            // Si no está en la tabla de símbolos lo agregamos; si está, se asigna la referencia a la tabla de símbolos
            this.getAnalizadorLexico().agregarTokenATablaSimbolos(buffer, "ID");

            this.getAnalizadorLexico().setTokenActual(this.getAnalizadorLexico().getIdToken("ID"));
            return true;
        } else {
            this.getAnalizadorLexico().addErrorLexico("ERROR LÉXICO (Línea " + AnalizadorLexico.LINEA + "): la cadena " + buffer + " no es un identificador, o una palabra reservada válida.");
            return false;
        }
    }
    
}
