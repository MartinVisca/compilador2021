package assembler;

public class InstruccionesAssembler {
    ///// MÉTODOS /////
    /**
     * Constructor de la clase;
     */
    public InstruccionesAssembler() {}

    /**
     * Suma de LONG.
     * @param a
     * @param b
     * @param auxiliar
     * @return
     */
    public String sumaLONG(String a, String b, String auxiliar) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("MOV EAX, " + b + "\n");
        codigo.append("ADD EAX, " + a + "\n");
        codigo.append("MOV " + auxiliar + ", EAX \n");
        codigo.append("JO @ERROR_OVERFLOW \n");

        return codigo.toString();
    }

    /**
     * Suma de SINGLE.
     * @param a
     * @param b
     * @param auxiliar
     * @return
     */
    public String sumaSINGLE(String a, String b, String auxiliar) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("FLD " + a + "\n");
        codigo.append("FADD " + b + "\n");
        codigo.append("FSTP " + auxiliar + "\n");

        return codigo.toString();
    }

    /**
     * Resta de LONG.
     * @param a
     * @param b
     * @param auxiliar
     * @return
     */
    public String restaLONG(String a, String b, String auxiliar) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("MOV EAX, " + a + "\n");
        codigo.append("SUB EAX, " + b + "\n");
        codigo.append("MOV " + auxiliar + ", EAX \n");
        codigo.append("JO @ERROR_OVERFLOW \n");

        return codigo.toString();
    }

    /**
     * Resta SINGLE.
     * @param a
     * @param b
     * @param auxiliar
     * @return
     */
    public String restaSINGLE(String a, String b, String auxiliar) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("FLD " + a + "\n");
        codigo.append("FSUB " + b + "\n");
        codigo.append("FSTP " + auxiliar + "\n");

        return codigo.toString();
    }

    public String multiplicacionLONG(String a, String b, String auxiliar) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("MOV EAX, " + a + "\n");
        codigo.append("MOV EBX, " + b + "\n");
        codigo.append("IMUL EBX"); // IMUL por tener LONG valores negativos en su rango.
        codigo.append("MOV " + auxiliar + ", EAX \n");

        return codigo.toString();
    }

    public String multiplicacionSINGLE(String a, String b, String auxiliar) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("FLD " + a + "\n");           // Se carga el valor de a en el tope de la pila.
        codigo.append("FIMUL " + b + "\n");         // FIMUL por tener SINGLE valores negativos en su rango. Se utiliza el tope de la pila para hacer la operación.
        codigo.append("FSTP " + auxiliar + "\n");   // Guarda el tope de la pila en la var. auxiliar, desapilando el valor.

        return codigo.toString();
    }

    public String divisionLONG() {
        StringBuffer codigo = new StringBuffer();
        // TODO: Traducción a ASSEMBLER
        return codigo.toString();
    }

    public String divisionSINGLE() {
        StringBuffer codigo = new StringBuffer();
        // TODO: Traducción a ASSEMBLER
        return codigo.toString();
    }

    public String comparadorLONG() {
        StringBuffer codigo = new StringBuffer();
        // TODO: Traducción a ASSEMBLER
        return codigo.toString();
    }

    public String comparadorSINGLE() {
        StringBuffer codigo = new StringBuffer();
        // TODO: Traducción a ASSEMBLER
        return codigo.toString();
    }
}
