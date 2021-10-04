package main;

import analizadorLexico.AnalizadorLexico;
import analizadorSintactico.AnalizadorSintactico;
import analizadorSintactico.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Ingrese la ruta del archivo a analizar:");
            Scanner scanner = new Scanner(System.in);
            String path = scanner.next();
            String entrada = Files.readString(Paths.get(path));

            Parser parser = new Parser();
            AnalizadorLexico lexico = new AnalizadorLexico(entrada);
            AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico, parser);
            sintactico.start();

        } catch(IOException e) {
            System.out.println("El archivo que se indica en la ruta ingresada no existe.");
        }
    }
}
