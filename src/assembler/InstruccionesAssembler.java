package assembler;

@SuppressWarnings("all")
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
        codigo.append("MOV " + auxiliar + ", EAX\n");
        codigo.append("JO @ERROR_OVERFLOW\n");

        return codigo.toString();
    }

    /**
     * Suma de SINGLE.
     * @param a
     * @param b
     * @param auxiliar
     * @return
     */
    public String sumaSINGLE(String a, String b, String auxiliar, Boolean primerOperando) {
        StringBuffer codigo = new StringBuffer();

        if (primerOperando)
            codigo.append("FILD " + a + "\n");
        else
            codigo.append("FLD " + a + "\n");
        codigo.append("FADD " + b + "\n");
        codigo.append("FSTP " + auxiliar + "\n");
        codigo.append("FXAM\n");
        codigo.append("FSTSW aux_mem_2bytes\n");
        codigo.append("MOV EAX, aux_mem_2bytes\n");
        codigo.append("FWAIT\n");
        codigo.append("SAHF\n");
        codigo.append("JO @ERROR_OVERFLOW\n");

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
        codigo.append("MOV " + auxiliar + ", EAX\n");

        return codigo.toString();
    }

    /**
     * Resta SINGLE.
     * @param a
     * @param b
     * @param auxiliar
     * @return
     */
    public String restaSINGLE(String a, String b, String auxiliar, Boolean primerOperando) {
        StringBuffer codigo = new StringBuffer();

        if (primerOperando)
            codigo.append("FILD " + a + "\n");
        else
            codigo.append("FLD " + a + "\n");
        codigo.append("FSUB " + b + "\n");
        codigo.append("FSTP " + auxiliar + "\n");

        return codigo.toString();
    }

    /**
     * Multiplicación LONG.
     * @param a
     * @param b
     * @param auxiliar
     * @return
     */
    public String multiplicacionLONG(String a, String b, String auxiliar) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("MOV EAX, " + a + "\n");
        codigo.append("MOV EBX, " + b + "\n");
        codigo.append("IMUL EBX"); // IMUL por tener LONG valores negativos en su rango.
        codigo.append("MOV " + auxiliar + ", EAX\n");

        return codigo.toString();
    }

    /**
     * Multiplicación SINGLE.
     * @param a
     * @param b
     * @param auxiliar
     * @return
     */
    public String multiplicacionSINGLE(String a, String b, String auxiliar, Boolean primerOperando) {
        StringBuffer codigo = new StringBuffer();

        if (primerOperando)
            codigo.append("FILD " + a + "\n");
        else
            codigo.append("FLD " + a + "\n");       // Se carga el valor de a en el tope de la pila.
        codigo.append("FIMUL " + b + "\n");         // FIMUL por tener SINGLE valores negativos en su rango. Se utiliza el tope de la pila para hacer la operación.
        codigo.append("FSTP " + auxiliar + "\n");   // Guarda el tope de la pila en la var. auxiliar, desapilando el valor.

        return codigo.toString();
    }

    /**
     * División LONG.
     * @param a
     * @param b
     * @param auxiliar
     * @return
     */
    public String divisionLONG(String a, String b, String auxiliar) {
        StringBuffer codigo = new StringBuffer();

        // Control de división por cero.
        codigo.append("MOV EAX, " + b + "\n");
        codigo.append("CMP " + b + ", 0\n");
        codigo.append("JE @LABEL_DIVIDEZERO\n");        // Si el divisor es igual a cero, lanzo error.

        // Ejecución de la división.
        codigo.append("MOV aux_edx, EDX\n");            // Inicializo la variable auxiliar.
        codigo.append("MOV EAX, " + a + "\n");
        codigo.append("CDQ\n");                         // Extiendo el signo de EAX en EDX, necesario para efectuar la división contemplando valores negativos.
        codigo.append("MOV EBX, " + b + "\n");
        codigo.append("IDIV EBX\n");                    // IDIV por tener que contemplar valores negativos. La operación toma el valor en EAX y lo divide por lo alojado en EBX.
        codigo.append("MOV EDX, aux_edx\n");            // Guardo el resto de la división en EDX.
        codigo.append("MOV " + auxiliar + ", EAX/n");

        return codigo.toString();
    }

    /**
     * División SINGLE.
     * @param a
     * @param b
     * @param auxiliar
     * @return
     */
    public String divisionSINGLE(String a, String b, String auxiliar, Boolean flagConversion, Boolean primerOperando) {
        StringBuffer codigo = new StringBuffer();

        // Control de división por cero.
        if (flagConversion && !primerOperando)
            codigo.append("FILD " + b + "\n");
        else
            codigo.append("FLD " + b + "\n");
        codigo.append("FLDZ\n");                    // Carga el número 0 en el tope de la pila.
        codigo.append("FCOM\n");                    // Compara el tope de ST(0) = 0 con ST(1) = b, a fin de determinar si el divisor es igual a cero.
        codigo.append("FSTSW aux_mem_2bytes\n");    // Almacena la palabra de estado en memoria, es decir, el determinante de la comparación anterior.
        codigo.append("MOV EAX, aux_mem_2bytes\n"); // Copio el estado de la comparación en EAX.
        codigo.append("SAHF\n");                    // Almaceno en los 8 bits menos significativos del registro de indicadores el valor de AH, tomado de EAX (estado de la comparación).
        codigo.append("JE @ERROR_DIVIDEZERO\n");

        // Ejecución de la división.
        if (flagConversion && primerOperando)
            codigo.append("FILD " + a + "\n");
        else
            codigo.append("FLD " + a + "\n");
        codigo.append("FIDIV " + b + "\n");
        codigo.append("FSTP " + auxiliar + "\n");

        return codigo.toString();
    }

    /**
     * Asignación LONG.
     * @param a
     * @param b
     * @return
     */
    public String asignacionLONG(String a, String b) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("MOV EAX, " + b + "\n");
        codigo.append("MOV " + a + ", EAX\n");

        return codigo.toString();
    }

    /**
     * Asignación SINGLE.
     * @param a
     * @param b
     * @return
     */
    public String asignacionSINGLE(String a, String b, Boolean flagConversion) {
        StringBuffer codigo = new StringBuffer();

        if (!flagConversion)
            codigo.append("FLD " + b + "\n");
        else
            codigo.append("FILD " + b + "\n");
        codigo.append("FSTP " + a + "\n");

        return codigo.toString();
    }

    /**
     * Comparación entre LONG.
     * @param a
     * @param b
     * @return
     */
    public String comparadorLONG(String a, String b) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("MOV EAX, " + a + "\n");
        codigo.append("CMP EAX, " + b + ", EAX\n");

        return codigo.toString();
    }

    /**
     * Comparación entre SINGLE.
     * @param a
     * @param b
     * @return
     */
    public String comparadorSINGLE(String a, String b) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("FLD " + a + "\n");
        codigo.append("FLD " + b + "\n");
        codigo.append("FCOM\n");                    // Cargo los dos valores y realizo la comparación entre el tope y el segundo elemento de la pila ST.
        codigo.append("FSTSW aux_mem_2bytes\n");    // Al igual que la división, almaceno el estado de memoria o resultado de la comparación.
        codigo.append("MOV EAX, aux_mem_2bytes\n");
        codigo.append("SAHF\n");

        return codigo.toString();
    }

    /**
     * Operación AND.
     * @param a
     * @param b
     * @param auxiliar
     * @return
     */
    public String AND(String a, String b, String auxiliar) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("MOV EAX, " + a + "\n");
        codigo.append("AND EAX, " + b + "\n");
        codigo.append("MOV " + auxiliar + ", EAX\n");

        return codigo.toString();
    }

    /**
     * Operación OR.
     * @param a
     * @param b
     * @param auxiliar
     * @return
     */
    public String OR(String a, String b, String auxiliar) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("MOV EAX, " + a + "\n");
        codigo.append("MOV EBX, " + b + "\n");
        codigo.append("OR EAX, EBX\n");
        codigo.append("MOV " + auxiliar + ", EAX\n");

        return codigo.toString();
    }
}
