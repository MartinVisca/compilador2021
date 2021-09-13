package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

public class AgregarCaracter extends AccionSemanticaSimple {

    public AgregarCaracter(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        // Agrego el caracter al buffer
        buffer += caracter;
        this.getAnalizadorLexico().setBuffer(buffer);
        return true;
    }
    
}
