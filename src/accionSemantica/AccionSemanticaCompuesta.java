package accionSemantica;

import java.util.Vector;

@SuppressWarnings("unused")
public class AccionSemanticaCompuesta implements AccionSemantica {

    ///// ATRIBUTOS /////
    private Vector<AccionSemantica> accionSemanticas;


    ///// MÉTODOS /////

    /**
     * Constructor de la clase.
     */
    public AccionSemanticaCompuesta() { this.accionSemanticas = new Vector<>(); }

    /**
     * Getter de accionesSemanticas.
     * @return
     */
    public Vector<AccionSemantica> getAccionSemanticas() {
        return accionSemanticas;
    }

    /**
     * Setter de accionesSemanticas.
     * @param accionSemanticas
     */
    public void setAccionSemanticas(Vector<AccionSemantica> accionSemanticas) {
        this.accionSemanticas = accionSemanticas;
    }

    /**
     * Agrega una acción semántica al contenedor de las mismas.
     * @param accionSemantica
     */
    public void addAccionSemantica(AccionSemantica accionSemantica) {
        this.accionSemanticas.add(accionSemantica);
    }

    /**
     * Implementación del método heredado; define la ejecución propia para esta clase.
     * @param buffer
     * @param caracter
     * @return
     */
    @Override
    public boolean ejecutar(String buffer, char caracter) {
        for (AccionSemantica accion : this.accionSemanticas)
            if (accion.ejecutar(buffer, caracter))
                return true;
        return false;
    }

}
