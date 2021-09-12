package analizadorLexico.matrices;

import accionSemantica.AccionSemantica;

@SuppressWarnings("all")
public class MatrizAccionesSemanticas {

    private AccionSemantica[][] matriz;

    /**
     * Constructor de la clase.
     * @param estadosOrigen
     * @param simbolos
     */
    public MatrizAccionesSemanticas(int estadosOrigen, int simbolos) {
        this.matriz = new AccionSemantica[estadosOrigen][simbolos];
    }

    /// MÉTODOS --> Getter & Setter ///
    public AccionSemantica get(int estadoOrigen, int simbolo) {
        return matriz[estadoOrigen][simbolo];
    }

    public void set(int estadoOrigen, int simbolo, AccionSemantica accionSemantica) {
        matriz[estadoOrigen][simbolo] = accionSemantica;
    }
}
