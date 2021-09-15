package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class DescartarBuffer extends AccionSemanticaSimple {

    /**
     * Constructor de la clase.
     * @param analizadorLexico
     */
    public DescartarBuffer(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    /**
     * Se descarta el buffer asignándole un nuevo valor vacío, para luego retornar un false que permite realizar la próxima acción.
     * @param buffer
     * @param caracter
     * @return
     */
    @Override
    public boolean ejecutar(String buffer, char caracter) {
        this.getAnalizadorLexico().setBuffer("");
        return false;
    }
    
}
