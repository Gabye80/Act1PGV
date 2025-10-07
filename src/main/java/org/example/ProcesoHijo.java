package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Proceso hijo que procesa un fichero de entrada:
 * convierte las palabras a minúsculas, cuenta las vocales
 * (incluyendo acentuadas y diéresis) y guarda los resultados parciales.
 */
public class ProcesoHijo {
    public static void main(String[] args) {
        /**
         * Punto de entrada del proceso hijo.
         * Recibe como argumentos el ID del proceso y la ruta del archivo a procesar.
         * @param args argumentos: [0] ID del proceso, [1] ruta del archivo.
         */

        int id = Integer.parseInt(args[0]);
        File archivoEntrada = new File(args[1]);

        // Creación de los archivos
        File minusculasRes = new File("minusculas-" + id + ".res");
        File vocalesRes = new File("vocales-" + id + ".res");
        File palabrasRes = new File("palabras-" + id + ".res");

        int totalVocales = 0;
        int totalPalabras = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivoEntrada), StandardCharsets.UTF_8));
             // Minúsculas.
             BufferedWriter bwMinus = new BufferedWriter(
                     new OutputStreamWriter(new FileOutputStream(minusculasRes), StandardCharsets.UTF_8));
             // Vocales.
             BufferedWriter bwVoc = new BufferedWriter(
                     new OutputStreamWriter(new FileOutputStream(vocalesRes), StandardCharsets.UTF_8));
             // Palabras
             BufferedWriter bwPal = new BufferedWriter(
                     new OutputStreamWriter(new FileOutputStream(palabrasRes), StandardCharsets.UTF_8))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue; // Ignora líneas vacías

                String lower = Vocales.toLowerEsp(linea);
                bwMinus.write(lower);
                bwMinus.newLine();

                totalVocales += (int) Vocales.contarVocales(lower);
                totalPalabras++;
            }

            // Los valores de vocales y palkabras se escriben en el fichero.
            bwVoc.write(String.valueOf(totalVocales));
            bwPal.write(String.valueOf(totalPalabras));

            System.out.printf("Finalizado proceso hijo: " + id);

        } catch (IOException e) {
            System.exit(2);
        }
    }
}
