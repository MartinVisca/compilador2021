package analizadorLexico.accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class EliminarUltimoCaracter extends AccionSemanticaSimple{

    /**
     * Constructor de la clase.
     * @param analizadorLexico
     */
    public EliminarUltimoCaracter(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    /**
     * Elimina el último caracter agregado al buffer.
     * @param buffer
     * @param caracter
     * @return
     */
    @Override
    public boolean ejecutar(String buffer, char caracter) {
        buffer = buffer.substring(0, buffer.length() - 1);
        this.getAnalizadorLexico().setBuffer(buffer);
        return true;
    }
    
}
