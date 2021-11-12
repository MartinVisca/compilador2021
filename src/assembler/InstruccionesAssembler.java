package assembler;

public class InstruccionesAssembler {
    ///// ATRIBUTOS /////
    String format;


    ///// MÉTODOS /////
    /**
     * Constructor de la clase;
     */
    public InstruccionesAssembler() {
        this.format = "%-25s%s%n";
    }

    /**
     * Suma de LONG.
     * @param a
     * @param b
     * @param auxiliar
     * @return
     */
    public String sumaLONG(String a, String b, String auxiliar) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("MOV AX, " + b + "\n");
        codigo.append("ADD AX, " + a + "\n");
        codigo.append("MOV " + auxiliar + ", AX \n");
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

        codigo.append("MOV AH, " + b + "\n");
        codigo.append("ADD AH, " + a + "\n");
        codigo.append("MOV " + auxiliar + ", AH \n");

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

        codigo.append("MOV AX, " + a + "\n");
        codigo.append("SUB AX, " + b + "\n");
        codigo.append("MOV " + auxiliar + ", AX \n");
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

        codigo.append("MOV AH, " + a + "\n");
        codigo.append("SUB AH, " + b + "\n");
        codigo.append("MOV " + auxiliar + ", AH \n");

        return codigo.toString();
    }

    public String multiplicacionLONG() {
        StringBuffer codigo = new StringBuffer();
        // TODO: Traducción a ASSEMBLER
        return codigo.toString();
    }

    public String multiplicacionSINGLE() {
        StringBuffer codigo = new StringBuffer();
        // TODO: Traducción a ASSEMBLER
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
