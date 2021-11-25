package analizadorLexico.accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class InicializarBuffer extends AccionSemanticaSimple {

    /**
     * Constructor de la clase.
     * @param analizadorLexico
     */
    public InicializarBuffer(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    /**
     * Se inicializa el buffer del analizador léxico en vacío.
     * @param buffer
     * @param caracter
     * @return
     */
    @Override
    public boolean ejecutar(String buffer, char caracter) {
        buffer = "";
        this.getAnalizadorLexico().setBuffer(buffer);
        return true;
    }

}
