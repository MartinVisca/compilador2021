package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class AvanzarEnCodigo extends AccionSemanticaSimple {

    /**
     * Constructor de la clase.
     * @param analizadorLexico
     */
    public AvanzarEnCodigo(AnalizadorLexico analizadorLexico) { super(analizadorLexico); }

    /**
     * Avanza el índice del código sin realizar ninguna acción particular.
     * @param buffer
     * @param caracter
     * @return
     */
    @Override
    public boolean ejecutar(String buffer, char caracter) {
        this.getAnalizadorLexico().setPosArchivo(this.getAnalizadorLexico().getPosArchivo() + 1);
        return true;
    }

}
