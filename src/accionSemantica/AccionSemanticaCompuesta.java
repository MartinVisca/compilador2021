package accionSemantica;

import java.util.Vector;

@SuppressWarnings("unused")
public class AccionSemanticaCompuesta implements AccionSemantica {

    /***** Atributos *****/
    private Vector<AccionSemantica> accionSemanticas;


    /***** Métodos *****/

    // Constructor de la clase.
    public AccionSemanticaCompuesta() { this.accionSemanticas = new Vector<>(); }

    public Vector<AccionSemantica> getAccionSemanticas() {
        return accionSemanticas;
    }

    public void setAccionSemanticas(Vector<AccionSemantica> accionSemanticas) {
        this.accionSemanticas = accionSemanticas;
    }

    // Agrega una acción semántica al contenedor de las mismas.
    public void addAccionSemantica(AccionSemantica accionSemantica) {
        this.accionSemanticas.add(accionSemantica);
    }

    // Implementación del método heredado; define la ejecución propia para esta clase.
    @Override
    public boolean ejecutar(String buffer, char caracter) {
        return false;
    }

}
