package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class DevolverTokenSimbolos extends AccionSemanticaSimple {

    /**
     * Constructor de la clase.
     * @param analizadorLexico
     */
    public DevolverTokenSimbolos(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    /**
     * Devuelve el id del token asociado al buffer, el cual se espera que contenga un símbolo.
     * @param buffer
     * @param caracter
     * @return
     */
    @Override
    public boolean ejecutar(String buffer, char caracter) {
        switch (buffer) {
            case "+":
                this.getAnalizadorLexico().setTokenActual(43);
                break;
            case "-":
                this.getAnalizadorLexico().setTokenActual(45);
                break;
            case "*":
                this.getAnalizadorLexico().setTokenActual(42);
                break;
            case "/":
                this.getAnalizadorLexico().setTokenActual(47);
                break;
            case ",":
                this.getAnalizadorLexico().setTokenActual(44);
                break;
            case ":":
                this.getAnalizadorLexico().setTokenActual(58);
                break;
            case ";":
                this.getAnalizadorLexico().setTokenActual(59);
                break;
            case "(":
                this.getAnalizadorLexico().setTokenActual(40);
                break;
            case ")":
                this.getAnalizadorLexico().setTokenActual(41);
                break;
            case ">":
                this.getAnalizadorLexico().setTokenActual(62);
                break;
            case "<":
                this.getAnalizadorLexico().setTokenActual(60);
                break;
            case "<=":
                this.getAnalizadorLexico().setTokenActual(274);
                break;
            case ">=":
                this.getAnalizadorLexico().setTokenActual(275);
                break;
            case "==":
                this.getAnalizadorLexico().setTokenActual(276);
                break;
            case "<>":
                this.getAnalizadorLexico().setTokenActual(277);
                break;
            case ":=":
                this.getAnalizadorLexico().setTokenActual(281);
                break;
            case "&&":
                this.getAnalizadorLexico().setTokenActual(282);
                break;
            case "||":
                this.getAnalizadorLexico().setTokenActual(283);
                break;
        }

        return true;
    }
}