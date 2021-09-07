package main;

import analizadorLexico.AnalizadorLexico;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

@SuppressWarnings("unused")
public class ConsumidorTokens {

    public static void main(String[] args) throws IOException {
        //Logica de carga de archivo
        try {
            System.out.println("----------ANALIZADOR LÉXICO-----------");
            System.out.println("Ingrese la ruta del archivo a analizar:");
            Scanner scanner = new Scanner(System.in);
            String path = scanner.next();
            String entrada = Files.readString(Paths.get(path));

            AnalizadorLexico analizadorLexico = new AnalizadorLexico(entrada);
            /* DESCOMENTAR CUANDO EL ANALIZADOR LÉXICO ESTÉ LISTO

            while (analizadorLexico.isCodigoLeido() == false)
                analizadorLexico.yylex();
            Vector<Token> tokens = analizadorLexico.getListaTokens();
            if (tokens.isEmpty()) {
                System.out.println("----------------");
                System.out.println("No se detectaron tokens.");
            }
            for (Token token : tokens) {
                System.out.println("----------------");
                System.out.println("Tipo token: " + token.getTipo());
                System.out.println("Lexema: " + token.getLexema());
            }

            System.out.println("----------------");
            System.out.println("ERRORES");
            analizadorLexico.imprimirErrores();

            System.out.println("----------------");
            System.out.println("TABLA DE SÍMBOLOS");
            analizadorLexico.imprimirTablaSimbolos();

             */

        } catch(IOException e) {
            throw new IOException(e);
        }
    }

}
