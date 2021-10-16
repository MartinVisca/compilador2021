package analizadorSintactico;

import java.util.Vector;

@SuppressWarnings("all")
public class PolacaInversa {

    private Vector<String> estructuraPolaca;

    /**
     * Constructor de la clase.
     */
    public PolacaInversa() {
        this.estructuraPolaca = new Vector<>();
    }

    /**
     * Retorna un elemento en la posición indicada.
     * @param posicion
     * @return
     */
    public String getElemento(int posicion) {
        return this.estructuraPolaca.elementAt(posicion);
    }

    /**
     * Agrega un elemento al final de la estructura.
     * @param elemento
     */
    public void addElemento(String elemento) {
        this.estructuraPolaca.add(elemento);
    }

    /**
     * Agrega un elemento a la estructura en la posición indicada.
     * @param elemento
     * @param posicion
     */
    public void addElementoEnPosicion(String elemento, int posicion) {
        this.estructuraPolaca.set(posicion, elemento);
    }

    /**
     * Vacía la estructura.
     */
    public void vaciarPolaca() {
        this.estructuraPolaca.removeAllElements();
    }

    /**
     * Retorna el tamaño de la estructura.
     * @return
     */
    public int getSize() { return this.estructuraPolaca.size(); }

    /**
     * Retorna un booleano indicando si la estructura contiene o no elementos.
     * @return
     */
    public boolean esVacia() { return this.estructuraPolaca.isEmpty(); }

    /**
     * Imprime la estructura por pantalla.
     */
    public void imprimirPolaca () {
        for (int i = 0; i < this.getSize() ; i++)
            System.out.println(i + " " + this.getElemento(i));
    }

}