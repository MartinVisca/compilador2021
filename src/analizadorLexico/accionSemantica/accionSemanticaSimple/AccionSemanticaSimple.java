package analizadorLexico.accionSemantica.accionSemanticaSimple;

import analizadorLexico.accionSemantica.AccionSemantica;
import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("unused")
public abstract class AccionSemanticaSimple implements AccionSemantica {

    private AnalizadorLexico analizadorLexico;

    public AccionSemanticaSimple(AnalizadorLexico analizadorLexico) {
        this.analizadorLexico = analizadorLexico;
    }

    public AnalizadorLexico getAnalizadorLexico() {
        return analizadorLexico;
    }

    public void setAnalizadorLexico(AnalizadorLexico analizadorLexico) {
        this.analizadorLexico = analizadorLexico;
    }

    @Override
    public abstract boolean ejecutar(String buffer, char caracter);

}


