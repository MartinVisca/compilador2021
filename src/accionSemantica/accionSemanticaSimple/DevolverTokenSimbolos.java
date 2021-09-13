package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

public class DevolverTokenSimbolos extends AccionSemanticaSimple {

    public DevolverTokenSimbolos(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

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
            // TODO: check the ID token generated in class Parser and change if needed for cases below this line
            case "<=":
                this.getAnalizadorLexico().setTokenActual(275);
                break;
            case ">=":
                this.getAnalizadorLexico().setTokenActual(276);
                break;
            case "==":
                this.getAnalizadorLexico().setTokenActual(277);
                break;
            case "<>":
                this.getAnalizadorLexico().setTokenActual(278);
                break;
            case ":=":
                this.getAnalizadorLexico().setTokenActual(279);
                break;
            case "&&":
                this.getAnalizadorLexico().setTokenActual(280);
                break;
            case "||":
                this.getAnalizadorLexico().setTokenActual(281);
                break;
        }

        return true;

    }
}