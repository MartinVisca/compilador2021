package analizadorLexico.accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class AgregarCaracter extends AccionSemanticaSimple {

    /**
     * Constructor de la clase.
     * @param analizadorLexico
     */
    public AgregarCaracter(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    /**
     * Agrega el caracter al buffer.
     * @param buffer
     * @param caracter
     * @return
     */
    @Override
    public boolean ejecutar(String buffer, char caracter) {
        buffer += caracter;
        this.getAnalizadorLexico().setBuffer(buffer);
        return true;
    }
    
}
