package main;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Vector;

@SuppressWarnings("all")
public class ConsumidorTokens {

    public static void main(String[] args) throws IOException {
        try {
            System.out.println("----------ANALIZADOR LÉXICO-----------");
            System.out.println("Ingrese la ruta del archivo a analizar:");
            Scanner scanner = new Scanner(System.in);
            String path = scanner.next();
            String entrada = Files.readString(Paths.get(path));

            AnalizadorLexico analizadorLexico = new AnalizadorLexico(entrada);

            while (analizadorLexico.isCodigoLeido() == false)
                analizadorLexico.procesarYylex();
            Vector<Token> tokens = analizadorLexico.getListaTokens();

            if (tokens.isEmpty()) {
                System.out.println("----------------");
                System.out.println("TOKENS");
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

        } catch(IOException e) {
            System.out.println("El archivo que se indica en la ruta ingresada no existe.");
        }
    }

}
