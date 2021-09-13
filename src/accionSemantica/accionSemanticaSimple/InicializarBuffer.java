package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

public class InicializarBuffer extends AccionSemanticaSimple {
    
    public InicializarBuffer(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }
    
    @Override
    public boolean ejecutar(String buffer, char caracter) {
        // Inicializo el buffer y lo seteo en el Analizador Léxico
        buffer = "";
        this.getAnalizadorLexico().setBuffer(buffer);
        return true;
    }

}
